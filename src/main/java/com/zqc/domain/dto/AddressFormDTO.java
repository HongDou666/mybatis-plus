package com.zqc.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 收货地址表单（新增 / 修改）
 */
@Data
@Schema(description = "收货地址表单")
public class AddressFormDTO {

    @Schema(description = "地址 id（修改时必填）")
    private Long id;

    @Schema(description = "用户 id")
    private Long userId;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "县/区")
    private String town;

    @Schema(description = "手机")
    private String mobile;

    @Schema(description = "详细地址")
    private String street;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "是否默认地址")
    private Boolean isDefault;

    @Schema(description = "备注")
    private String notes;
}
