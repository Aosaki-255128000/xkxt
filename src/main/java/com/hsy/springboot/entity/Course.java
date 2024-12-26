package com.hsy.springboot.entity;

import lombok.Data;

@Data
public class Course {
    private Integer id;
    private String courseId;
    private String name;
    private Integer credit;
    private Integer creditHour;
    private Integer dptId;   // 外键，关联到 Department 里面的 id
    private Department department;  // 新增一个 Department 属性来保存院系信息
}
