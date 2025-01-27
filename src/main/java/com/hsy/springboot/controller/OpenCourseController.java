package com.hsy.springboot.controller;


import com.hsy.springboot.entity.OpenCourse;
import com.hsy.springboot.mapper.OpenCourseMapper;
import com.hsy.springboot.service.OpenCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/openCourse")
public class OpenCourseController {

    @Autowired
    private OpenCourseMapper openCourseMapper;

    @Autowired
    private OpenCourseService openCourseService;

    @PostMapping
    public Integer save(@RequestBody OpenCourse opencourse) { return openCourseService.save(opencourse); }

    @GetMapping
    public List<OpenCourse> findAll() {
        List<OpenCourse> all = openCourseMapper.findAll();
        return all;
    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String semester,
                                        @RequestParam(defaultValue = "") String courseId,
                                        @RequestParam(defaultValue = "") String jobNumber,
                                        @RequestParam(defaultValue = "") String classTime) {
        pageNum = (pageNum - 1) * pageSize;

        semester = "%" + semester + "%";
        courseId = "%" + courseId + "%";
        jobNumber = "%" + jobNumber + "%";
        classTime = "%" + classTime + "%";

        List<OpenCourse> data = openCourseMapper.selectPage(pageNum, pageSize, semester, courseId, jobNumber, classTime);
        Integer total = openCourseMapper.selectTotal(semester, courseId, jobNumber, classTime);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) { return openCourseMapper.deleteById(id); }


}
