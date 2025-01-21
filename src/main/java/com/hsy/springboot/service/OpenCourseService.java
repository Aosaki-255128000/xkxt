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

}
