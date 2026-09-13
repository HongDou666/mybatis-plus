package com.zqc.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.zqc.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user", autoResultMap = true) // autoResultMap：查询结果才能走 typeHandler
public class User {

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID) // 指定主键类型为雪花算法
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册手机号
     */
    private String phone;

    /**
     * 详细信息（库中为 JSON，由 JacksonTypeHandler 与 UserInfo 互转）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserInfo info;

    /**
     * 使用状态（枚举，库中为 int，由 @EnumValue 自动转换）
     */
    private UserStatus status;

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
