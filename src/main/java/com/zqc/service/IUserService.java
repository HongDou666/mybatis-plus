package com.zqc.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.zqc.domain.dto.UserFormDTO;
import com.zqc.domain.po.User;
import com.zqc.domain.query.UserQuery;
import com.zqc.domain.vo.UserVO;

import java.util.List;

/**
 * 用户业务接口。
 * 继承 MyBatis-Plus {@link IService}，复用通用 CRUD；
 * 再声明与业务相关的自定义方法（入参/出参使用 DTO、VO，而不是直接暴露 PO）。
 */
public interface IUserService extends IService<User> {

    /**
     * 新增用户（表单 DTO → PO → 落库）
     */
    void saveUser(UserFormDTO userFormDTO);

    /**
     * 根据 id 删除用户
     */
    void deleteUser(Long id);

    /**
     * 根据 id 查询用户，返回 VO（无密码等敏感字段）；不存在时返回 null
     */
    UserVO queryUserById(Long id);

    /**
     * 根据 id 列表批量查询用户（一次 listByIds，避免循环查库）
     */
    List<UserVO> queryUserByIds(List<Long> ids);

    /**
     * 批量扣减余额（自定义 SQL：XML 写 SET，Wrapper 拼 WHERE）
     *
     * @param ids    用户 id 列表
     * @param amount 扣减金额
     */
    void deductBalance(List<Long> ids, int amount);

    /**
     * 根据单个用户 id 扣减余额（校验状态/余额；扣完为 0 则自动冻结）
     *
     * @param id    用户 id
     * @param money 扣减金额（须大于 0）
     */
    void deductBalanceById(Long id, int money);

    /**
     * 按收货地址城市 + 用户 id 列表关联查询（自定义 SQL：user JOIN address）
     *
     * @param ids  用户 id 列表
     * @param city 城市，如「北京」
     */
    List<UserVO> queryUsersByAddress(List<Long> ids, String city);

    /**
     * 复杂条件查询用户（Lambda 条件构造，条件均可为空）
     *
     * @param query name / status / minBalance / maxBalance，未传则不拼进 WHERE
     */
    List<UserVO> queryUsers(UserQuery query);
}
