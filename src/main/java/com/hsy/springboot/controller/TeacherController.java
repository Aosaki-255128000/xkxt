package com.hsy.springboot.controller;

import com.hsy.springboot.entity.Teacher;
import com.hsy.springboot.mapper.TeacherMapper;
import com.hsy.springboot.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherService teacherService;

    // 新增和修改
    @PostMapping
    public Integer save(@RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }

    // 查询所有数据
    @GetMapping
    public List<Teacher> findAll() {
        List<Teacher> all = teacherMapper.findAll();
        return all;
    }

    // 分页查询
    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String username,
                                        @RequestParam(defaultValue = "") String name,
                                        @RequestParam(defaultValue = "") String sex,
                                        @RequestParam(defaultValue = "") String title,
                                        @RequestParam(required = false) Integer departmentId,
                                        @RequestParam(defaultValue = "") String role) {

        pageNum = (pageNum - 1) * pageSize;

        username = "%" + username + "%";
        name = "%" + name + "%";
        sex = "%" + sex + "%";
        title = "%" + title + "%";
        role = "%" + role + "%";

        List<Teacher> data = teacherMapper.selectPage(pageNum, pageSize, username, name, sex, title, role, departmentId);
        Integer total = teacherMapper.selectTotal(username, name, sex, title, role, departmentId);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

    // 删除
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) { return teacherMapper.deleteById(id); }

}