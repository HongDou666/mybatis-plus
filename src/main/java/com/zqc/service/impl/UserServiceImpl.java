package com.zqc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.zqc.common.ResultCode;
import com.zqc.common.exception.BizException;
import com.zqc.domain.dto.UserFormDTO;
import com.zqc.domain.po.User;
import com.zqc.domain.vo.UserVO;
import com.zqc.mapper.UserMapper;
import com.zqc.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * getById 查 PO，再拷贝为 UserVO（过滤 password 等字段）
     */
    @Override
    public UserVO queryUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            return null;
        }
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    /**
     * listByIds 一次查出，再用 Hutool 批量转为 VO 列表
     */
    @Override
    public List<UserVO> queryUserByIds(List<Long> ids) {
        List<User> users = listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
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
     * 根据 id 扣减余额：校验参数 → 校验用户存在 → WHERE 带余额保护 → 校验影响行数。
     * 失败时抛 {@link com.zqc.common.exception.BizException}，由全局异常处理器转为 R。
     */
    @Override
    public void deductBalanceById(Long id, int money) {
        // 1. 参数校验
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "用户 id 不能为空");
        }
        if (money <= 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "扣减金额必须大于 0");
        }
        // 2. 用户存在性：与「余额不足」区分开
        User user = getById(id);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND, "用户不存在，id=" + id);
        }
        // 3. 余额保护：WHERE id=? AND balance>=money，避免扣成负数
        var wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getId, id)
                .ge(User::getBalance, money);
        int rows = getBaseMapper().deductBalance(wrapper, money);
        // 4. 影响行数为 0：余额不足或并发下已不够扣
        if (rows == 0) {
            throw new BizException(ResultCode.BALANCE_NOT_ENOUGH,
                    "余额不足，扣减失败，id=" + id + ", money=" + money);
        }
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
}
