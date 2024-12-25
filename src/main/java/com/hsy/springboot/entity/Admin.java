package com.hsy.springboot.entity;

import lombok.Data;

@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String avatar;
}