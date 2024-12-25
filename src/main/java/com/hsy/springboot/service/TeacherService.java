package com.hsy.springboot.service;
import com.hsy.springboot.entity.Teacher;
import com.hsy.springboot.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    public int save(Teacher teacher) {
        if(teacher.getId() == null) {
            return teacherMapper.insert(teacher);
        } else {
            return teacherMapper.update(teacher);
        }
    }

    // 查询所有教师信息
    public List<Teacher> list() { return teacherMapper.findAll(); }

}
