package com.zqc.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 收货地址视图对象
 */
@Data
@Schema(description = "收货地址视图")
public class AddressVO {

    @Schema(description = "地址 id")
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
