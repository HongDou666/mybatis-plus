package com.zqc.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户后台筛选条件（字段均可为空，未传则不参与查询）。
 * 继承 {@link PageQuery}，分页接口可同时传 pageNo/pageSize/sortBy 与筛选条件。
 */
@Data
@EqualsAndHashCode(callSuper = true) // 调用父类构造器 可继承父类的构造器
@Schema(description = "用户复杂条件查询（可含分页参数）")
public class UserQuery extends PageQuery {

    @Schema(description = "用户名关键字，模糊匹配，可为空")
    private String name;

    @Schema(description = "用户状态（1正常 2冻结），可为空")
    private Integer status;

    @Schema(description = "最小余额，可为空")
    private Integer minBalance;

    @Schema(description = "最大余额，可为空")
    private Integer maxBalance;
}
