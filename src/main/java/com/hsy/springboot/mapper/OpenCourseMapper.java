package com.hsy.springboot.mapper;
import com.hsy.springboot.entity.OpenCourse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OpenCourseMapper {
    @Select("select * from opencourse")
    List<OpenCourse> findAll();

    @Insert("insert into opencourse(semester, courseId, jobNumber, classTime) values (#{semester}, #{courseId}, #{jobNumber}, #{classTime})")
    int insert(OpenCourse opencourse);

    int update(OpenCourse opencourse);

    @Delete("delete from opencourse where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select({"select * from opencourse where semester like #{semester} and courseId like #{courseId} and jobNumber like #{jobNumber} and classTime like #{classTime}"})
    List<OpenCourse> selectPage(Integer pageNum, Integer pageSize, String semester, String courseId, String jobNumber, String classTime);


    @Select({"select count(*) from opencourse where semester like #{semester} and courseId like #{courseId} and jobNumber like #{jobNumber} and classTime like #{classTime}"})
    Integer selectTotal(String semester, String courseId, String jobNumber, String classTime);


}
