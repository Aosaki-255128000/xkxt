package com.hsy.springboot.entity;

import lombok.Data;

@Data
public class EnrollRequest {
    private String semester;
    private String courseId;
    private String jobNumber;
}
