package com.hsy.springboot.controller;


import com.hsy.springboot.common.Result;
import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.mapper.AdminMapper;
import com.hsy.springboot.service.AdminService;
import com.hsy.springboot.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @PostMapping
    public Integer save(@RequestBody Admin admin) {
        return adminService.save(admin);
    }

    @GetMapping
    public List<Admin> findAll() {
        List<Admin> all = adminMapper.findAll();
        return all;
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) { return adminMapper.deleteById(id); }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String username,
                                        @RequestParam(defaultValue = "") String role) {
        pageNum = (pageNum - 1) * pageSize;
        username = "%" + username + "%";
        role = "%" + role + "%";

        List<Admin> data = adminMapper.selectPage(pageNum, pageSize, username, role);
        Integer total = adminMapper.selectTotal(username, role);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

//    @PostMapping("/login")
//    public Result login(@RequestBody Admin admin) {
//        // 查询数据库
//        Admin dbAdmin = adminService.login(admin);
//
//        if (dbAdmin != null) {
//            // 生成JWT令牌
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("id", dbAdmin.getId());
//            claims.put("username", dbAdmin.getUsername());
//            claims.put("role", "admin"); // 角色信息
//
//            String token = JWTUtils.generateToken(claims);
//
//            // 打印 JWT
//            System.out.println("Generated JWT: " + token);
//
//            // 返回登录成功信息 & Token
//            return Result.success()
//                    .message("登录成功")
//                    .data("token", token);
//        }
//
//        // 失败返回
//        return Result.error().message("用户名或密码错误");
//    }

}
