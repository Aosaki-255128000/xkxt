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

    public List<Student> list() { return studentMapper.findAll(); }

    public Student findByUsernameAndRole(String username, String role) {
        return studentMapper.findByStudentnameAndRole(username, role);
    }

    public Student login(Student student) {
        Student dbStudent = studentMapper.findByUsername(student.getUsername());
        if(dbStudent != null && dbStudent.getPassword().equals(student.getPassword())) {
            return dbStudent;
        }
        return null;
    }

}
