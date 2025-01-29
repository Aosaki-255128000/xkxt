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

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Admin admin) {
        Map<String, Object> result = new HashMap<>();

        // 从数据库中查询用户名对应的记录
        Admin dbAdmin = adminService.login(admin);

        if (dbAdmin != null) {

            System.out.println("DB Password: " + dbAdmin.getPassword());
            System.out.println("Input Password: " + admin.getPassword());
            // 验证密码是否正确
            if (dbAdmin.getPassword().equals(admin.getPassword())) {
                result.put("code", 200);
                result.put("message", "登录成功");
            } else {
                result.put("code", 401);
                result.put("message", "密码错误");
            }
        } else {
            result.put("code", 404);
            result.put("message", "用户名不存在");
        }

        return result;
    }
}
