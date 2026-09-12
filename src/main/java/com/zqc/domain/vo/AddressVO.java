package com.zqc.domain.vo;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author 虎哥
 * @since 2023-07-01
 */
@Data
public class AddressVO{

    private Long id;

    private Long userId;

    private String province;

    private String city;

    private String town;

    private String mobile;

    private String street;

    private String contact;

    private Boolean isDefault;

    private String notes;
}
