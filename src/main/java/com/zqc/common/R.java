package com.zqc.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一 API 响应体：{ code, msg, data }
 */
@Data
@Schema(description = "统一响应")
public class R<T> {

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "提示信息")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    /** 成功（无 data） */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /** 成功（带 data） */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(ResultCode.SUCCESS.getCode()); // 成功码
        r.setMsg(ResultCode.SUCCESS.getMsg()); // 成功文案
        r.setData(data);
        return r;
    }

    /** 失败：默认系统错误码 + 自定义文案 */
    public static <T> R<T> fail(String msg) {
        return fail(ResultCode.SYSTEM_ERROR.getCode(), msg);
    }

    /** 失败：使用错误码枚举的 code / 默认文案 */
    public static <T> R<T> fail(ResultCode resultCode) {
        return fail(resultCode.getCode(), resultCode.getMsg());
    }

    /** 失败：显式指定 code 与 msg（全局异常处理器常用） */
    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }
}
