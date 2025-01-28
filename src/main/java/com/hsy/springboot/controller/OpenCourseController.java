package com.hsy.springboot.controller;


import com.hsy.springboot.entity.OpenCourse;
import com.hsy.springboot.entity.Teacher;
import com.hsy.springboot.mapper.OpenCourseMapper;
import com.hsy.springboot.service.OpenCourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String semester,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(defaultValue = "") String classTime,
            HttpServletRequest request) { // 通过请求对象获取Session

        // 从Session中获取当前登录教师信息（需确保登录时已存入Session）
        HttpSession session = request.getSession(false);

        if (session == null) {
            System.err.println("ERROR: 请求未携带有效Session");
            throw new RuntimeException("请先登录");
        }

        Teacher teacher = (Teacher) session.getAttribute("teacher");
        System.out.println("当前SessionID: " + session.getId());
        System.out.println("Session中的教师信息: " + teacher);

        // 提取教师工号（需确保登录逻辑已正确设置Session）
        String jobNumber = teacher.getJobNumber();

        // 分页参数处理
        pageNum = (pageNum - 1) * pageSize;
        semester = "%" + semester + "%";
        courseId = "%" + courseId + "%";
        classTime = "%" + classTime + "%";

        // 使用精确匹配教师工号查询
        List<OpenCourse> data = openCourseMapper.selectTeacherPage(
                pageNum, pageSize, semester, courseId, jobNumber, classTime
        );
        Integer total = openCourseMapper.selectTeacherTotal(
                semester, courseId, jobNumber, classTime
        );

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }


}
