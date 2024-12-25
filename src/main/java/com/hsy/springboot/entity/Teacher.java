package com.hsy.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Teacher {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String name;
    private String sex;
    private String title;
    private String specialtyId;
    private String role;
    private String avatar;
}