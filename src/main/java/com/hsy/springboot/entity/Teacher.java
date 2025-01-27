package com.hsy.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Teacher {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String sex;
    private String title;
    private String jobNumber;
    private Integer departmentId;
    private String role;
    private LocalDate birthDate;

    private Department department;
}