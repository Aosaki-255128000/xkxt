package com.hsy.springboot.service;


import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public int save(Admin admin) {
        if(admin.getId() == null) {
            return adminMapper.insert(admin);
        } else {
            return adminMapper.update(admin);
        }
    }

    public List<Admin> list() { return adminMapper.findAll(); }

    public Admin findByUsernameAndRole(String username, String role) {
        return adminMapper.findByUsernameAndRole(username, role);
    }

}
