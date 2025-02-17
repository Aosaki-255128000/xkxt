package com.hsy.springboot.entity;

import lombok.Data;

@Data
public class OpenCourse {
    private Integer id;
    private String semester;
    private String courseId;
    private String jobNumber;
    private String classTime;
    private Double averageScore;

    private String courseName;

    private Teacher teacher;
    private Course course;

}
