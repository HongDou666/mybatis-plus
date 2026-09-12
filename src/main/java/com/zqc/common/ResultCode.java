package com.zqc.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 业务错误码枚举。
 * 约定：接口 HTTP 状态仍可为 200，业务成功/失败看响应体中的 {@code code}。
 */
@Getter
@RequiredArgsConstructor
public enum ResultCode {

    /** 成功 */
    SUCCESS(200, "OK"),
    /** 请求参数不合法 */
    BAD_REQUEST(400, "请求参数错误"),
    /** 用户不存在（业务码，非 HTTP 404） */
    USER_NOT_FOUND(40401, "用户不存在"),
    /** 余额不足以完成本次扣减 */
    BALANCE_NOT_ENOUGH(40001, "余额不足"),
    /** 未预期的系统异常 */
    SYSTEM_ERROR(500, "系统异常");

    /** 业务状态码，写入统一响应 R.code */
    private final int code;
    /** 默认提示文案，可被 BizException 自定义 msg 覆盖 */
    private final String msg;
}
