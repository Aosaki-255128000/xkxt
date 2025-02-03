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

    @Select({
            "SELECT * FROM opencourse",
            "WHERE semester LIKE #{semester}",
            "AND courseId LIKE #{courseId}",
            "AND jobNumber = #{jobNumber}", // 精确匹配
            "AND classTime LIKE #{classTime}",
            "LIMIT #{pageNum}, #{pageSize}"
    })
    List<OpenCourse> selectTeacherPage(
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize,
            @Param("semester") String semester,
            @Param("courseId") String courseId,
            @Param("jobNumber") String jobNumber,
            @Param("classTime") String classTime
    );

    @Select({
            "SELECT COUNT(*) FROM opencourse",
            "WHERE semester LIKE #{semester}",
            "AND courseId LIKE #{courseId}",
            "AND jobNumber = #{jobNumber}",
            "AND classTime LIKE #{classTime}"
    })
    Integer selectTeacherTotal(
            @Param("semester") String semester,
            @Param("courseId") String courseId,
            @Param("jobNumber") String jobNumber,
            @Param("classTime") String classTime
    );


    List<OpenCourse> selectCoursesByJobNumber(@Param("jobNumber") String jobNumber,
                                              @Param("pageNum") int pageNum,
                                              @Param("pageSize") int pageSize,
                                              @Param("semester") String semester,
                                              @Param("courseId") String courseId,
                                              @Param("classTime") String classTime);

    int countCoursesTeacher(@Param("jobNumber") String jobNumber,
                     @Param("semester") String semester,
                     @Param("courseId") String courseId,
                     @Param("classTime") String classTime);





}
