package com.zqc.controller;

import com.zqc.common.R;
import com.zqc.domain.dto.AddressFormDTO;
import com.zqc.domain.vo.AddressVO;
import com.zqc.service.IAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址 REST 接口（基本增删改查）
 */
@Tag(name = "收货地址", description = "地址增删改查")
@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    /**
     * 新增地址
     * POST /addresses
     */
    @Operation(summary = "新增收货地址")
    @PostMapping
    public R<Void> saveAddress(@RequestBody AddressFormDTO formDTO) {
        addressService.saveAddress(formDTO);
        return R.ok();
    }

    /**
     * 根据 id 删除地址（逻辑删除）
     * DELETE /addresses/{id}
     */
    @Operation(summary = "根据 id 删除收货地址")
    @DeleteMapping("/{id}")
    public R<Void> deleteAddress(
            @Parameter(description = "地址 id") @PathVariable Long id) {
        addressService.deleteAddress(id);
        return R.ok();
    }

    /**
     * 根据 id 修改地址
     * PUT /addresses
     */
    @Operation(summary = "修改收货地址")
    @PutMapping
    public R<Void> updateAddress(@RequestBody AddressFormDTO formDTO) {
        addressService.updateAddress(formDTO);
        return R.ok();
    }

    /**
     * 根据 id 查询地址
     * GET /addresses/{id}
     */
    @Operation(summary = "根据 id 查询收货地址")
    @GetMapping("/{id}")
    public R<AddressVO> queryAddressById(
            @Parameter(description = "地址 id") @PathVariable Long id) {
        return R.ok(addressService.queryAddressById(id));
    }

    /**
     * 根据用户 id 查询地址列表
     * GET /addresses?userId=1
     */
    @Operation(summary = "根据用户 id 查询收货地址列表")
    @GetMapping
    public R<List<AddressVO>> queryAddressByUserId(
            @Parameter(description = "用户 id") @RequestParam Long userId) {
        return R.ok(addressService.queryAddressByUserId(userId));
    }
}
