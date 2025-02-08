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

}
