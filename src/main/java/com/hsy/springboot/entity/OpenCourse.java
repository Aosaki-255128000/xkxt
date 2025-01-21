package com.hsy.springboot.entity;

import lombok.Data;

@Data
public class OpenCourse {
    private Integer id;
    private String semester;
    private String courseId;
    private String jobNumber;
    private String classTime;

    private Teacher teacher;
    private Course course;
}
