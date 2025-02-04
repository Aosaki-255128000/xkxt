package com.hsy.springboot.entity;

import lombok.Data;

@Data
public class CourseSelection {
    private Integer id;
    private String studentId;
    private String semester;
    private String courseId;
    private String jobNumber;
    private Integer usualPerformance;
    private Integer testScore;
    private Integer totalScore;

    private String courseName;
    private String studentName;
    private String teacherName;

    private Student student;
    private OpenCourse openCourse;
}
