package com.hsy.springboot.controller;


import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.mapper.AdminMapper;
import com.hsy.springboot.service.AdminService;
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
    public Integer delete(@PathVariable Integer id) {return adminMapper.deleteById(id); }

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

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Admin admin) {
        Map<String, Object> result = new HashMap<>();
        // 模拟数据库校验逻辑
        if ("admin".equals(admin.getUsername()) && "password123".equals(admin.getPassword())) {
            result.put("code", 200);
            result.put("message", "登录成功");
        } else {
            result.put("code", 401);
            result.put("message", "用户名或密码错误");
        }
        System.out.println("接收到的登录数据: " + admin);
        return result;

    }



}
