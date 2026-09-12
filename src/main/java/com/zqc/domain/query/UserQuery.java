package com.zqc.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户后台筛选条件（字段均可为空，未传则不参与查询）
 */
@Data
@Schema(description = "用户复杂条件查询")
public class UserQuery {

    @Schema(description = "用户名关键字，模糊匹配，可为空")
    private String name;

    @Schema(description = "用户状态（1正常 2冻结），可为空")
    private Integer status;

    @Schema(description = "最小余额，可为空")
    private Integer minBalance;

    @Schema(description = "最大余额，可为空")
    private Integer maxBalance;
}
