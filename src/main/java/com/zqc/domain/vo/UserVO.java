package com.zqc.domain.vo;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String info;

    private Integer status;

    private Integer balance;
}
