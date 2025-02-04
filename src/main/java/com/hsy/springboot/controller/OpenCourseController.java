package com.hsy.springboot.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.hsy.springboot.entity.OpenCourse;
import com.hsy.springboot.mapper.OpenCourseMapper;
import com.hsy.springboot.service.OpenCourseService;
import com.hsy.springboot.utils.JWTUtils;
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

    @GetMapping("/teacherPage")
    public Map<String, Object> findTeacherPage(
            @RequestHeader(value = "token") String token,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String semester,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(defaultValue = "") String classTime) {

        pageNum = (pageNum - 1) * pageSize;

        // 解析 token 获取教师工号
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
        String jobNumber = decodedJWT.getClaim("jobNumber").asString();

        // 查询该教师的课程信息
        Map<String, Object> result = new HashMap<>();
        List<OpenCourse> courses = openCourseService.findCoursesByJobNumber(jobNumber, pageNum, pageSize, semester, courseId, classTime);
        int total = openCourseService.countCoursesByJobNumber(jobNumber, semester, courseId, classTime);

        result.put("data", courses);
        result.put("total", total);
        return result;
    }

    // 学生选课端
    @GetMapping("/studentPage")
    public Map<String, Object> selectCoursesWithDetails(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String semester,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(defaultValue = "") String courseName,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) Integer creditHour,
            @RequestParam(defaultValue = "") String jobNumber,
            @RequestParam(defaultValue = "") String teacherName,
            @RequestParam(defaultValue = "") String classTime) {

        // 分页参数计算
        int offset = (pageNum - 1) * pageSize;

        Map<String, Object> result = new HashMap<>();
        List<OpenCourse> courses = openCourseService.listDetails(
                semester, courseId, courseName,
                credit, creditHour, jobNumber,
                teacherName, classTime, offset, pageSize
        );

        int total = openCourseMapper.selectTotal(semester, courseId, jobNumber, classTime);

        result.put("data", courses);
        result.put("total", total);
        return result;
    }
}
