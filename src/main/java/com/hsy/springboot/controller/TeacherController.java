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
                                        @RequestParam(defaultValue = "") String jobNumber,
                                        @RequestParam(required = false) Integer departmentId,
                                        @RequestParam(defaultValue = "") String role) {

        pageNum = (pageNum - 1) * pageSize;

        username = "%" + username + "%";
        name = "%" + name + "%";
        sex = "%" + sex + "%";
        title = "%" + title + "%";
        jobNumber = "%" + jobNumber + "%";
        role = "%" + role + "%";

        List<Teacher> data = teacherMapper.selectPage(pageNum, pageSize, username, name, sex, title, jobNumber, role, departmentId);
        Integer total = teacherMapper.selectTotal(username, name, sex, title, jobNumber, role, departmentId);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

    // 删除
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) { return teacherMapper.deleteById(id); }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Teacher teacher) {
        Map<String, Object> result = new HashMap<>();
        Teacher dbTeacher = teacherMapper.findByTeachernameAndRole(teacher.getUsername(), teacher.getRole());

        if(dbTeacher != null) {
            System.out.println("DB Password: " + dbTeacher.getPassword());
            System.out.println("Input Password: " + teacher.getPassword());

            if (dbTeacher.getPassword().equals(teacher.getPassword())) {
                result.put("code", 200);
                result.put("message", "登陆成功");
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