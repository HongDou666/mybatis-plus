package com.zqc.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 收货地址表 address
 */
@Data
@TableName("address")
public class Address {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户 ID */
    private Long userId;

    private String province;

    private String city;

    private String town;

    private String mobile;

    private String street;

    private String contact;

    /** 是否默认地址：1 默认 0 否 */
    private Boolean isDefault;

    private String notes;

    /** 逻辑删除：1 已删 0 未删 */
    @TableLogic
    private Boolean deleted;
}
