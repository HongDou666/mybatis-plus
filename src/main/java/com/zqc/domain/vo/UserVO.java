package com.zqc.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户视图")
public class UserVO {

    @Schema(description = "用户 id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "额外信息")
    private String info;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "余额")
    private Integer balance;
}
