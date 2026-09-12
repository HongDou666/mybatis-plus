package com.zqc.controller;

import com.zqc.common.R;
import com.zqc.domain.dto.UserFormDTO;
import com.zqc.domain.vo.UserVO;
import com.zqc.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 REST 接口
 *
 * 依赖注入对比：
 * 1）当前：@RequiredArgsConstructor + final 字段 → 构造器注入（推荐）
 * 2）下方注释：@Autowired 字段注入 → 可取消注释对比（同时需去掉 @RequiredArgsConstructor 与 final）
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /** 构造器注入：配合类上 @RequiredArgsConstructor，由 Spring 传入 IUserService */
    private final IUserService userService;

    // ---------- @Autowired 字段注入写法（对比用，默认注释）----------
    // import org.springframework.beans.factory.annotation.Autowired;
    // @Autowired
    // private IUserService userService;
    // ---------------------------------------------------------------

    /**
     * 新增用户：接收 UserFormDTO，委托 Service，统一返回 R
     * POST /users
     */
    @PostMapping
    public R<Void> saveUser(@RequestBody UserFormDTO userFormDTO) {
        userService.saveUser(userFormDTO);
        return R.ok();
    }

    /**
     * 根据 id 删除用户
     * DELETE /users/{id}
     */
    @DeleteMapping("/{id}")
    public R<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return R.ok();
    }

    /**
     * 根据 id 查询用户，返回 UserVO
     * GET /users/{id}
     */
    @GetMapping("/{id}")
    public R<UserVO> queryUserById(@PathVariable Long id) {
        return R.ok(userService.queryUserById(id));
    }

    /**
     * 根据 id 列表批量查询用户，例如 /users?ids=1,2,4
     * GET /users?ids=
     */
    @GetMapping
    public R<List<UserVO>> queryUserByIds(@RequestParam List<Long> ids) {
        return R.ok(userService.queryUserByIds(ids));
    }

    /**
     * 批量扣减余额（自定义 SQL）
     * PUT /users/balance/deduct?ids=1,2,4&amount=200
     */
    @PutMapping("/balance/deduct")
    public R<Void> deductBalance(@RequestParam List<Long> ids, @RequestParam int amount) {
        userService.deductBalance(ids, amount);
        return R.ok();
    }

    /**
     * 根据 id 扣减余额
     * PUT /users/{id}/deduction/{money}
     */
    @PutMapping("/{id}/deduction/{money}")
    public R<Void> deductBalanceById(@PathVariable("id") Long id,
                                     @PathVariable("money") Integer money) {
        userService.deductBalanceById(id, money);
        return R.ok();
    }

    /**
     * 按收货地址城市 + 用户 id 关联查询（自定义 SQL：user JOIN address）
     * GET /users/by-address?city=北京&ids=1,2,4
     */
    @GetMapping("/by-address")
    public R<List<UserVO>> queryUsersByAddress(@RequestParam List<Long> ids,
                                               @RequestParam String city) {
        return R.ok(userService.queryUsersByAddress(ids, city));
    }
}
