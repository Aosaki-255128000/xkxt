package com.hsy.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Student {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String sex;
    private String code;
    private Integer collegeId;
    private String phone;
    private LocalDate birthDate;
    private String hometown;

    private Department department;  // 新增属性，用于存储院系信息
}

