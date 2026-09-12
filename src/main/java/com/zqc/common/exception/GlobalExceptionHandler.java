package com.zqc.common.exception;

import com.zqc.common.R;
import com.zqc.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：将各类异常统一包装为 {@link R}，避免把堆栈直接暴露给前端。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常（参数错误、用户不存在、余额不足等）
     */
    @ExceptionHandler(BizException.class)
    public R<Void> handleBizException(BizException e) {
        log.warn("业务异常 code={}, msg={}", e.getCode(), e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 兜底参数异常（兼容未改成 BizException 的旧代码）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数异常 msg={}", e.getMessage());
        return R.fail(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 未知系统异常：只返回通用文案，详情打日志
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail(ResultCode.SYSTEM_ERROR);
    }
}
