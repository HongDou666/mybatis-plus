package com.zqc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.zqc.common.ResultCode;
import com.zqc.common.exception.BizException;
import com.zqc.domain.dto.UserFormDTO;
import com.zqc.domain.po.Address;
import com.zqc.domain.po.User;
import com.zqc.domain.query.UserQuery;
import com.zqc.domain.vo.AddressVO;
import com.zqc.domain.vo.UserVO;
import com.zqc.enums.UserStatus;
import com.zqc.mapper.UserMapper;
import com.zqc.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户业务实现。
 * 继承 {@link ServiceImpl}，自动具备 IService 中 save / removeById / getById / listByIds 等实现，
 * 无需自己再写一遍通用 CRUD；自定义 SQL 通过 getBaseMapper() 调用 UserMapper。
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * DTO 转 PO 后调用 IService.save 插入；主键由雪花策略 ASSIGN_ID 生成
     */
    @Override
    public void saveUser(UserFormDTO userFormDTO) {
        // 1. 将 UserFormDTO 转换为 User 对象（DTO -> PO）
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        // 2. 调用 IService.save 插入
        save(user);
    }

    /**
     * 调用 IService.removeById 按主键删除
     */
    @Override
    public void deleteUser(Long id) {
        removeById(id);
    }

    /**
     * 根据 id 查询用户，并附带收货地址列表。
     * 地址查询使用 {@link Db} 静态工具，避免注入 AddressService 引发循环依赖。
     * 用户不存在时抛 {@link BizException}（USER_NOT_FOUND）。
     */
    @Override
    public UserVO queryUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "用户不存在，id=" + id);
        }
        UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
        // 通过 Db 按 userId 查地址（不注入其他 Service）
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();
        vo.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        return vo;
    }

    /**
     * 批量按 id 查询用户，并附带各自的收货地址列表。
     * 用户 listByIds 一次查；地址用 Db.in(userId) 一次查，再内存分组，避免循环查库（N+1）。
     */
    @Override
    public List<UserVO> queryUserByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<User> users = listByIds(ids);
        if (users.isEmpty()) {
            return List.of();
        }
        List<UserVO> vos = BeanUtil.copyToList(users, UserVO.class);
        // 一次查出全部相关地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, ids)
                .list();
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        Map<Long, List<AddressVO>> addressMap = addressVOList.stream()
                .collect(Collectors.groupingBy(AddressVO::getUserId));
        for (UserVO vo : vos) {
            vo.setAddresses(addressMap.getOrDefault(vo.getId(), List.of()));
        }
        return vos;
    }

    /**
     * Wrapper 只拼 id IN (...)，扣减语句在 UserMapper.xml 的 deductBalance
     */
    @Override
    public void deductBalance(List<Long> ids, int amount) {
        var wrapper = Wrappers.<User>lambdaQuery()
                .in(User::getId, ids);
        getBaseMapper().deductBalance(wrapper, amount);
    }

    /**
     * Wrapper 只拼 u.id IN (...)；city 作为 Mapper 参数写在 XML JOIN 条件中
     */
    @Override
    public List<UserVO> queryUsersByAddress(List<Long> ids, String city) {
        var query = Wrappers.<User>query()
                .in("u.id", ids);
        List<User> users = getBaseMapper().queryUsersByAddress(query, city);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    /**
     * 根据 id 扣减余额：
     * 1）校验用户状态（须正常，冻结不可扣）
     * 2）校验用户余额（须 >= 扣减金额）
     * 3）扣减成功后若余额为 0，则将 status 置为 FROZEN
     * 使用 IService.lambdaUpdate()，无需自定义 deductBalance SQL。
     * 失败时抛 {@link com.zqc.common.exception.BizException}，由全局异常处理器转为 R。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductBalanceById(Long id, int money) {
        // 1. 参数校验
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "用户 id 不能为空");
        }
        if (money <= 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "扣减金额必须大于 0");
        }
        // 2. 用户存在性
        User user = getById(id);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "用户不存在，id=" + id);
        }
        // 3. 校验用户状态：仅 NORMAL 可扣减
        if (user.getStatus() != UserStatus.NORMAL) {
            throw new BizException(ResultCode.USER_FROZEN, "用户状态异常或已冻结，无法扣减余额，id=" + id);
        }
        // 4. 校验用户余额：余额必须足够
        Integer balance = user.getBalance();
        if (balance == null || balance < money) {
            throw new BizException(ResultCode.BALANCE_NOT_ENOUGH,
                    "余额不足，扣减失败，id=" + id + ", balance=" + balance + ", money=" + money);
        }
        // 5. Lambda 更新：扣减余额；若扣完为 0 则同一次更新里冻结
        //    eq(旧余额) 作乐观锁：仅当库中余额仍等于查询时的值才更新，避免并发覆盖
        int remain = balance - money;
        boolean success = lambdaUpdate()
                .set(User::getBalance, remain) // 设置用户余额为剩余金额
                .set(remain == 0, User::getStatus, UserStatus.FROZEN) // 若扣完为 0 则将用户状态设置为冻结
                .eq(User::getId, id) // 条件1：用户 id
                .eq(User::getStatus, UserStatus.NORMAL) // 条件2：用户状态为正常
                .eq(User::getBalance, balance) // 条件3：用户余额等于查询时的值 乐观锁
                .update(); // 执行更新
        if (!success) {
            throw new BizException(ResultCode.BALANCE_NOT_ENOUGH,
                    "扣减失败（并发下余额或状态已变更），id=" + id + ", money=" + money);
        }
    }
   
    /**
     * 使用 IService.lambdaQuery() 动态拼接条件：第一个布尔参数为 true 时才加入该条件
     */
    @Override
    public List<UserVO> queryUsers(UserQuery query) {
        if (query == null) {
            query = new UserQuery();
        }
        // 查询条件里的 status 仍是 Integer，转成枚举再比较（由 @EnumValue 映射库字段）
        UserStatus status = UserStatus.of(query.getStatus());
        List<User> users = lambdaQuery()
                // 用户名关键字：非空才模糊查询
                .like(StrUtil.isNotBlank(query.getName()), User::getUsername, query.getName())
                // 状态：非空才等值匹配
                .eq(status != null, User::getStatus, status)
                // 最小余额
                .ge(query.getMinBalance() != null, User::getBalance, query.getMinBalance())
                // 最大余额
                .le(query.getMaxBalance() != null, User::getBalance, query.getMaxBalance())
                .list(); // 执行查询
        return BeanUtil.copyToList(users, UserVO.class);
    }
}
