package com.hsy.springboot.controller;

import com.hsy.springboot.entity.Student;
import com.hsy.springboot.mapper.StudentMapper;
import com.hsy.springboot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            int result = studentMapper.deleteById(id);
            if(result > 0) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("删除失败，可能存在关联数据");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无法删除该学生，存在关联的选课记录");
        }
    }

}
