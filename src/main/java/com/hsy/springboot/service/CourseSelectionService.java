package com.hsy.springboot.service;

import com.hsy.springboot.entity.CourseSelection;
import com.hsy.springboot.mapper.CourseSelectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseSelectionService {
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    public int save(CourseSelection courseSelection) {
        if(courseSelection.getId() == null) {
            return courseSelectionMapper.insert(courseSelection);
        } else {
            return courseSelectionMapper.update(courseSelection);
        }
    }

    public List<CourseSelection> list() { return courseSelectionMapper.findAll(); }

}
