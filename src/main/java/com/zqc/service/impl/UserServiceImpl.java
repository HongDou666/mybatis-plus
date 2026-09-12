package com.zqc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
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
     * Wrapper 拼 id = ?，复用同一条自定义扣减 SQL
     */
    @Override
    public void deductBalanceById(Long id, int money) {
        var wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getId, id);
        getBaseMapper().deductBalance(wrapper, money);
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
