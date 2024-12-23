package com.hsy.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createTime;
    private String avatarUrl;
}
