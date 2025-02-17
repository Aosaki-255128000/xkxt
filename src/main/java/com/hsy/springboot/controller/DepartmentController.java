package com.hsy.springboot.controller;

import com.hsy.springboot.entity.Department;
import com.hsy.springboot.mapper.DepartmentMapper;
import com.hsy.springboot.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public Integer save(@RequestBody Department department) { return departmentService.save(department); }

    @GetMapping
    public List<Department> findAll() {
        List<Department> all = departmentMapper.findAll();
        return all;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            int result = departmentMapper.deleteById(id);
            if (result > 0) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("删除失败，可能存在关联数据");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无法删除该部门，存在关联的教师记录");
        }
    }


    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String name,
                                        @RequestParam(defaultValue = "") String address,
                                        @RequestParam(defaultValue = "") String phone){
        pageNum = (pageNum - 1) * pageSize;
        name = "%" + name + "%";
        address = "%" + address + "%";
        phone = "%" + phone + "%";

        List<Department> data = departmentMapper.selectPage(pageNum, pageSize, name, address, phone);
        Integer total = departmentMapper.selectTotal(name, address, phone);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

}
