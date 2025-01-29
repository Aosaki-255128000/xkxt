package com.hsy.springboot.controller;

import com.hsy.springboot.common.Result;
import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        log.info("管理员登录：（）", admin);
        Admin admin1 = adminService.login(admin);
        return admin != null ? Result.success() : Result.error();
    }
}
