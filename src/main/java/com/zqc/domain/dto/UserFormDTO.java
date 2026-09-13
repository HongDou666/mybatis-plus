package com.zqc.domain.dto;

import com.zqc.domain.po.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户表单")
public class UserFormDTO {

    @Schema(description = "用户 id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "额外信息（JSON 对象）")
    private UserInfo info;

    @Schema(description = "余额")
    private Integer balance;
}
