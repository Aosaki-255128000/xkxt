package com.hsy.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Student {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String name;
    private String role;
    private String sex;
    private String code;
    private Integer collegeId;
    private Integer score;
    private String avatar;
}
