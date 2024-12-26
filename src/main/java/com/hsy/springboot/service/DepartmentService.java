package com.hsy.springboot.service;


import com.hsy.springboot.entity.Department;
import com.hsy.springboot.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public int save(Department department) {
        if(department.getId() == null) {
            return departmentMapper.insert(department);
        } else {
            return departmentMapper.update(department);
        }
    }

    public List<Department> list() { return departmentMapper.findAll(); }

}
