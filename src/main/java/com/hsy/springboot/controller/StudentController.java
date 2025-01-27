package com.hsy.springboot.controller;

import com.hsy.springboot.entity.Student;
import com.hsy.springboot.mapper.StudentMapper;
import com.hsy.springboot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;

    @PostMapping
    public Integer save(@RequestBody Student student) { return studentService.save(student); }

    @GetMapping
    public List<Student> findAll() {
        List<Student> all = studentMapper.findAll();
        return all;
    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String username,
                                        @RequestParam(defaultValue = "") String name,
                                        @RequestParam(defaultValue = "") String role,
                                        @RequestParam(defaultValue = "") String sex,
                                        @RequestParam(defaultValue = "") String code,
                                        @RequestParam(required = false) Integer collegeId) {

        pageNum = (pageNum - 1) * pageSize;

        username = "%" + username + "%";
        name = "%" + name + "%";
        role = "%" + role + "%";
        sex = "%" + sex + "%";
        code = "%" + code + "%";

        List<Student> data = studentMapper.selectPage(pageNum, pageSize, username, name, role, sex, code, collegeId);
        Integer total = studentMapper.selectTotal(username, name, role, sex, code, collegeId);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) { return studentMapper.deleteById(id); }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Student student) {
        Map<String, Object> result = new HashMap<>();
        Student dbStudent = studentMapper.findByStudentnameAndRole(student.getUsername(), student.getRole());

        if(dbStudent != null) {
            // Debugging: Check the passwords
            System.out.println("DB Password: " + dbStudent.getPassword());
            System.out.println("Input Password: " + student.getPassword());

            if(dbStudent.getPassword().equals(student.getPassword())) {
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
