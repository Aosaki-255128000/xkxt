package com.hsy.springboot.controller;

import com.hsy.springboot.entity.Course;
import com.hsy.springboot.mapper.CourseMapper;
import com.hsy.springboot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseContoller {

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
    public Integer delete(@PathVariable Integer id) { return courseMapper.deleteById(id); }

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
