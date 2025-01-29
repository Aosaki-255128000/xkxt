package com.hsy.springboot.service;


import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public Admin login(Admin admin) {
        return adminMapper.findByUsernameAndPassword(admin);
    }

}
