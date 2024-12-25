package com.hsy.springboot.service;

import com.hsy.springboot.entity.Student;
import com.hsy.springboot.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public int save(Student student) {
        if(student.getId() == null) {
            return studentMapper.insert(student);
        } else {
            return studentMapper.update(student);
        }
    }

    // 查询所有教师信息
    public List<Student> list() { return studentMapper.findAll(); }

}
