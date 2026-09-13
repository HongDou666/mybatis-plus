package com.zqc.controller;

import com.zqc.common.R;
import com.zqc.domain.dto.UserFormDTO;
import com.zqc.domain.query.UserQuery;
import com.zqc.domain.vo.UserVO;
import com.zqc.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户管理", description = "用户增删查、扣减余额、按地址查询")
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
    @Operation(summary = "新增用户")
    @PostMapping
    public R<Void> saveUser(@RequestBody UserFormDTO userFormDTO) {
        userService.saveUser(userFormDTO);
        return R.ok();
    }

    /**
     * 根据 id 删除用户
     * DELETE /users/{id}
     */
    @Operation(summary = "根据 id 删除用户")
    @DeleteMapping("/{id}")
    public R<Void> deleteUser(
            @Parameter(description = "用户 id") @PathVariable Long id) {
        userService.deleteUser(id);
        return R.ok();
    }

    /**
     * 根据 id 查询用户，返回 UserVO
     * GET /users/{id}
     */
    @Operation(summary = "根据 id 查询用户（含收货地址列表）")
    @GetMapping("/{id}")
    public R<UserVO> queryUserById(
            @Parameter(description = "用户 id") @PathVariable Long id) {
        return R.ok(userService.queryUserById(id));
    }

    /**
     * 根据 id 列表批量查询用户，例如 /users?ids=1,2,4
     * GET /users?ids=
     */
    @Operation(summary = "根据 id 列表批量查询用户（含收货地址列表）")
    @GetMapping
    public R<List<UserVO>> queryUserByIds(
            @Parameter(description = "用户 id 列表") @RequestParam List<Long> ids) {
        return R.ok(userService.queryUserByIds(ids));
    }

    /**
     * 批量扣减余额（自定义 SQL）
     * PUT /users/balance/deduct?ids=1,2,4&amount=200
     */
    @Operation(summary = "批量扣减余额")
    @PutMapping("/balance/deduct")
    public R<Void> deductBalance(
            @Parameter(description = "用户 id 列表") @RequestParam List<Long> ids,
            @Parameter(description = "扣减金额") @RequestParam int amount) {
        userService.deductBalance(ids, amount);
        return R.ok();
    }

    /**
     * 根据 id 扣减余额
     * PUT /users/{id}/deduction/{money}
     */
    @Operation(summary = "根据 id 扣减余额")
    @PutMapping("/{id}/deduction/{money}")
    public R<Void> deductBalanceById(
            @Parameter(description = "用户 id") @PathVariable("id") Long id,
            @Parameter(description = "扣减金额") @PathVariable("money") Integer money) {
        userService.deductBalanceById(id, money);
        return R.ok();
    }

    /**
     * 按收货地址城市 + 用户 id 关联查询（自定义 SQL：user JOIN address）
     * GET /users/by-address?city=北京&ids=1,2,4
     */
    @Operation(summary = "按收货地址城市 + 用户 id 关联查询")
    @GetMapping("/by-address")
    public R<List<UserVO>> queryUsersByAddress(
            @Parameter(description = "用户 id 列表") @RequestParam List<Long> ids,
            @Parameter(description = "收货地址城市") @RequestParam String city) {
        return R.ok(userService.queryUsersByAddress(ids, city));
    }

    /**
     * 复杂条件查询用户（后台筛选，条件均可为空）
     * GET /users/list?name=Jack&status=1&minBalance=1000&maxBalance=20000
     */
    @Operation(summary = "复杂条件查询用户")
    @GetMapping("/list")
    public R<List<UserVO>> queryUsers(UserQuery query) {
        return R.ok(userService.queryUsers(query));
    }
}
