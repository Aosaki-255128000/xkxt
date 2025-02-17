package com.hsy.springboot.controller;

import com.hsy.springboot.entity.Course;
import com.hsy.springboot.mapper.CourseMapper;
import com.hsy.springboot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @PostMapping
    public Integer save(@RequestBody Course course) { return courseService.save(course); }

    @GetMapping
    public List<Course> findAll() {
        List<Course> all = courseMapper.findAll();
        return all;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            int result = courseMapper.deleteById(id);
            if (result > 0) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("删除失败，可能存在关联数据");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无法删除该课程，存在关联的开课记录");
        }
    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String courseId,
                                        @RequestParam(defaultValue = "") String name,
                                        @RequestParam(required = false) Integer credit,
                                        @RequestParam(required = false) Integer creditHour,
                                        @RequestParam(required = false) Integer dptId) {
        pageNum = (pageNum - 1) * pageSize;

        courseId = "%" + courseId + "%";
        name = "%" + name + "%";

        List<Course> data = courseMapper.selectPage(pageNum, pageSize, courseId, name, credit, creditHour, dptId);
        Integer total = courseMapper.selectTotal(courseId, name, credit, creditHour, dptId);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

}
