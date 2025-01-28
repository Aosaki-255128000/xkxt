package com.hsy.springboot.controller;

import com.hsy.springboot.entity.Teacher;
import com.hsy.springboot.mapper.TeacherMapper;
import com.hsy.springboot.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public Map<String, Object> login(@RequestBody Teacher teacher, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // 【修正点1】仅根据用户名查询（不要带role）
        Teacher dbTeacher = teacherService.findByUsername(teacher.getUsername());

        if (dbTeacher == null) {
            result.put("code", 404);
            result.put("message", "用户名不存在");
            return result;
        }

        // 【修正点2】校验角色类型
        if (!"teacher".equals(dbTeacher.getRole())) { // 确保数据库中的角色字段是"teacher"
            result.put("code", 403);
            result.put("message", "非教师账号禁止登录");
            return result;
        }

        // 【修正点3】密码验证
        if (!dbTeacher.getPassword().equals(teacher.getPassword())) {
            result.put("code", 401);
            result.put("message", "密码错误");
            return result;
        }

        // 存储Session（关键步骤）
        HttpSession session = request.getSession();
        session.setAttribute("teacher", dbTeacher);
        System.out.println("登录成功，SessionID: " + session.getId()); // 调试输出

        result.put("code", 200);
        result.put("message", "登录成功");
        return result;
    }

}