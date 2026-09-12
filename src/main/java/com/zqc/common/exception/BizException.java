package com.zqc.common.exception;

import com.zqc.common.ResultCode;
import lombok.Getter;

/**
 * 业务异常：携带错误码，由 {@link GlobalExceptionHandler} 统一转为 {@link com.zqc.common.R}。
 * Service 层抛出本异常，Controller 无需 try-catch。
 */
@Getter
public class BizException extends RuntimeException {

    /** 业务错误码，对应 ResultCode.code / R.code */
    private final int code;

    /**
     * 使用错误码枚举的默认文案
     */
    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    /**
     * 使用错误码枚举的 code，文案可自定义（便于带上 id 等上下文）
     */
    public BizException(ResultCode resultCode, String msg) {
        super(msg);
        this.code = resultCode.getCode();
    }

    /**
     * 直接指定 code 与 msg（少用，优先走 ResultCode）
     */
    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
