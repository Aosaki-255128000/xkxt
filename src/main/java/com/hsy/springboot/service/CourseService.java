package com.hsy.springboot.service;

import com.hsy.springboot.entity.Course;
import com.hsy.springboot.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    public int save(Course course) {
        if(course.getId() == null) {
            return courseMapper.insert(course);
        } else {
            return courseMapper.update(course);
        }
    }

    public List<Course> list() { return courseMapper.findAll(); }

    // 获取包含院系信息的课程列表
    public List<Course> findAllCoursesWithDepartment() {
        return courseMapper.findAllCoursesWithDepartment();
    }

    public List<Course> findByDepartmentId(Integer departmentId) {
        return courseMapper.findByDepartmentId(departmentId);
    }
}
