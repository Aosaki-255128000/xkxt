package com.hsy.springboot.controller;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hsy.springboot.entity.Course;
import com.hsy.springboot.entity.CourseSelection;
import com.hsy.springboot.mapper.CourseSelectionMapper;
import com.hsy.springboot.service.CourseSelectionService;
import com.hsy.springboot.utils.JWTUtils;
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


    @GetMapping("/studentResult")
    public Map<String, Object> findStudentResult(
            @RequestHeader(value = "token") String token,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String studentId,
            @RequestParam(defaultValue = "") String semester,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(required = false) Integer usualPerformance,
            @RequestParam(required = false) Integer testScore,
            @RequestParam(required = false) Integer totalScore){

        pageNum = (pageNum - 1) * pageSize;

        // 解析 token 获取教师工号
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String jobNumber = decodedJWT.getClaim("jobNumber").asString();

        // 查询该教师的选课信息
        Map<String, Object> result = new HashMap<>();
        List<CourseSelection> courseSelections = courseSelectionService.findSelectionsByJobNumber(jobNumber, pageNum, pageSize, studentId, semester, courseId, usualPerformance, testScore, totalScore);
        int total = courseSelectionService.countSelectionsByJobNumber(jobNumber, studentId, semester, courseId, usualPerformance, testScore, totalScore);

        result.put("data", courseSelections);
        result.put("total", total);
        return result;
    }


}
