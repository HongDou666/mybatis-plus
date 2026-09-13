package com.zqc.domain.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页与排序参数")
public class PageQuery {

    @Schema(description = "页码，从 1 开始，默认 1")
    private Integer pageNo = 1;

    @Schema(description = "每页条数，默认 5")
    private Integer pageSize = 5;

    @Schema(description = "排序字段（表列名，如 create_time），可为空")
    private String sortBy;

    @Schema(description = "是否升序；未传排序字段时忽略")
    private Boolean isAsc;

    public <T>  Page<T> toMpPage(OrderItem ... orders){
        // 1.分页条件
        Page<T> p = Page.of(pageNo, pageSize);
        // 2.排序条件
        // 2.1.先看前端有没有传排序字段
        if (sortBy != null) {
            // 2.1.1. 如果前端传了排序字段，则根据排序字段和排序方式进行排序（升序或降序）
            p.addOrder(Boolean.TRUE.equals(isAsc) ? OrderItem.asc(sortBy) : OrderItem.desc(sortBy));
            return p;
        }
        // 2.2.再看有没有手动指定排序字段
        if(orders != null){
            p.addOrder(orders); // 2.2.1. 如果手动指定排序字段，则根据排序字段和排序方式进行排序（升序或降序）
        }
        return p;
    }

    public <T> Page<T> toMpPage(String defaultSortBy, boolean isAsc){
        return this.toMpPage(isAsc ? OrderItem.asc(defaultSortBy) : OrderItem.desc(defaultSortBy));
    }

    public <T> Page<T> toMpPageDefaultSortByCreateTimeDesc() {
        return toMpPage("create_time", false);
    }

    public <T> Page<T> toMpPageDefaultSortByUpdateTimeDesc() {
        return toMpPage("update_time", false);
    }
}
