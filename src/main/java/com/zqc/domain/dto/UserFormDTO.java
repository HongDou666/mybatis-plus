package com.zqc.domain.dto;

import lombok.Data;

@Data
public class UserFormDTO {

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String info;

    private Integer balance;
}
