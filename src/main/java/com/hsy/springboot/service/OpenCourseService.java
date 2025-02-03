package com.hsy.springboot.service;


import com.hsy.springboot.entity.OpenCourse;
import com.hsy.springboot.mapper.OpenCourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenCourseService {

    @Autowired
    private OpenCourseMapper openCourseMapper;

    public int save(OpenCourse opencourse) {
        if(opencourse.getId() == null) {
            return openCourseMapper.insert(opencourse);
        } else {
            return openCourseMapper.update(opencourse);
        }
    }

    public List<OpenCourse> list() { return openCourseMapper.findAll(); }

    public List<OpenCourse> findCoursesByJobNumber(String jobNumber, Integer pageNum, Integer pageSize, String semester, String courseId, String classTime) {
        return openCourseMapper.selectCoursesByJobNumber(jobNumber, pageNum, pageSize, semester, courseId, classTime);
    }

    public int countCoursesByJobNumber(String jobNumber, String semester, String courseId, String classTime) {
        return openCourseMapper.countCoursesTeacher(jobNumber, semester, courseId, classTime);
    }
}
