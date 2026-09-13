package com.zqc.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zqc.domain.po.User;
import com.zqc.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
        System.out.println("id = " + user.getId());
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(1L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setUsername("toDelete");
        user.setPassword("123");
        user.setPhone("18688990099");
        user.setBalance(0);
        user.setInfo(UserInfo.of(0, "temp", "male"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
        userMapper.deleteById(user.getId());
    }

    @Test
    void testQueryByNameLikeAndBalanceGe() {
        var query = Wrappers.<User>lambdaQuery()
                .like(User::getUsername, "o") // 指定查询条件
                .ge(User::getBalance, 1000); // 指定查询条件
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateBalanceByUsername() {
        var update = Wrappers.<User>lambdaUpdate()
                .eq(User::getUsername, "Jack") // 指定更新条件
                .set(User::getBalance, 2000); // 指定更新字段
        userMapper.update(null, update);
    }

    @Test
    void testDeductBalanceByIds() {
        // 一条 SQL 批量扣减，避免循环逐条 update
        var update = Wrappers.<User>lambdaUpdate()
                .in(User::getId, List.of(1L, 2L, 4L))
                .setSql("balance = balance - 200");
        userMapper.update(null, update);
    }

    /**
     * 自定义 SQL：SET 写在 Mapper，WHERE 仍用 Wrapper 拼装（避免业务层写 setSql）
     */
    @Test
    void testDeductBalanceByIdsCustomSql() {
        List<Long> ids = List.of(1L, 2L, 4L);
        var query = Wrappers.<User>lambdaQuery()
                .in(User::getId, ids);
        userMapper.deductBalance(query, 100);
    }

    /**
     * 自定义 SQL：查询收货地址在北京、且用户 id 在 1/2/4 中的用户
     */
    @Test
    void testQueryUsersByBeijingAddressAndIds() {
        List<Long> ids = List.of(1L, 2L, 4L);
        // Wrapper 只拼 id；city 交给 Mapper 参数（写在 XML）
        var query = Wrappers.<User>query()
                .in("u.id", ids);
        List<User> users = userMapper.queryUsersByAddress(query, "北京");
        users.forEach(System.out::println);
    }
}
