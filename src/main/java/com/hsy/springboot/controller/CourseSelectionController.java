package com.hsy.springboot.controller;
import com.hsy.springboot.entity.Course;
import com.hsy.springboot.entity.CourseSelection;
import com.hsy.springboot.mapper.CourseSelectionMapper;
import com.hsy.springboot.service.CourseSelectionService;
import org.apache.catalina.startup.ContextRuleSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courseSelection")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Autowired
    private CourseSelectionService courseSelectionService;

    @PostMapping
    public Integer save(@RequestBody CourseSelection courseSelection) { return courseSelectionService.save(courseSelection); }

    @GetMapping
    public List<CourseSelection> findAll() {
        List<CourseSelection> all = courseSelectionMapper.findAll();
        return all;
    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String studentId,
                                        @RequestParam(defaultValue = "") String semester,
                                        @RequestParam(defaultValue = "") String courseId,
                                        @RequestParam(defaultValue = "") String jobNumber,
                                        @RequestParam(required = false) Integer usualPerformance,
                                        @RequestParam(required = false) Integer testScore,
                                        @RequestParam(required = false) Integer totalScore) {
        pageNum = (pageNum - 1) * pageSize;

        semester = "%" + semester + "%";
        courseId = "%" + courseId + "%";
        jobNumber = "%" + jobNumber + "%";

        List<CourseSelection> data = courseSelectionMapper.selectPage(pageNum, pageSize, studentId, semester, courseId, jobNumber, usualPerformance, testScore, totalScore);
        Integer total = courseSelectionMapper.selectTotal(studentId, semester, courseId, jobNumber, usualPerformance, testScore, totalScore);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) { return courseSelectionMapper.deleteById(id); }

}
