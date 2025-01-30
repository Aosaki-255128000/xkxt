package com.hsy.springboot.controller;

import com.hsy.springboot.common.Result;
import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.service.AdminService;
import com.hsy.springboot.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result loginAdmin(@RequestBody Admin admin) {
        log.info("管理员登录：（）", admin);
        Admin admin1 = adminService.login(admin);

        // 如果登录成功，生成并下发令牌
        if (admin1 != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", admin1.getId());
            claims.put("name", admin1.getUsername());

            String jwt = JWTUtils.generateToken(claims);
            return Result.success();
        }

        // 如果登录失败，返回错误信息
        return Result.error();
    }
}
