package com.zqc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户状态枚举。
 * {@link EnumValue}：写入/读取数据库时与 int 自动转换；
 * {@link JsonValue}：接口 JSON 序列化为对应数字（1/2）。
 */
@Getter
@RequiredArgsConstructor
public enum UserStatus {

    /** 正常 */
    NORMAL(1, "正常"),
    /** 冻结 */
    FROZEN(2, "冻结");

    /** 存入数据库的值 */
    @EnumValue // 指定存入数据库的值
    @JsonValue // 指定接口 JSON 序列化为对应数字（1/2）
    private final int value;

    private final String desc;

    /**
     * 按库中 int 值反查枚举（查询条件等场景）
     */
    public static UserStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (UserStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知用户状态: " + value);
    }
}
