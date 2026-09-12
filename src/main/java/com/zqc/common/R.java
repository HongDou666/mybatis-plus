package com.zqc.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "统一响应")
public class R<T> {

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "提示信息")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("OK");
        r.setData(null);
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("OK");
        r.setData(data);
        return r;
    }

    public static <T> R<T> fail(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }
}
