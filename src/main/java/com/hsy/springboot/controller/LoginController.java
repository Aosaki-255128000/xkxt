package com.hsy.springboot.controller;

import com.hsy.springboot.common.Result;
import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.entity.Teacher;
import com.hsy.springboot.service.AdminService;
import com.hsy.springboot.service.TeacherService;
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

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/admin/login")
    public Result loginAdmin(@RequestBody Admin admin) {
        log.info("管理员登录：{}", admin); // 修正日志占位符
        Admin admin1 = adminService.login(admin);

        if (admin1 != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", admin1.getId());
            claims.put("username", admin1.getUsername()); // 使用 "username" 而非 "name"
            claims.put("role", "admin"); // 添加角色信息

            String jwt = JWTUtils.generateToken(claims);
            System.out.println("Generated JWT: " + jwt );
            return Result.success()
                    .data("token", jwt); // 返回 Token
        }

        return Result.error();
    }





    @PostMapping("/teacher/login")
    public Result loginTeacher(@RequestBody Teacher teacher) {
        log.info("教师登录：{}", teacher); // 修正日志占位符
        Teacher teacher1 = teacherService.login(teacher);

        if (teacher1 != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", teacher1.getId());
            claims.put("username", teacher1.getUsername());
            claims.put("role", "teacher"); // 添加角色信息
            claims.put("jobNumber", teacher1.getJobNumber());
            claims.put("name", teacher1.getName());


            String jwt = JWTUtils.generateToken(claims);
            System.out.println("Generated JWT: " + jwt );
            return Result.success()
                    .data("token", jwt) // 返回 Token
                    .data("name", teacher1.getName());
        }

        return Result.error();
    }
}
