package com.hsy.springboot.mapper;

import com.hsy.springboot.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("select courseId, name from course")
    List<Course> findAll();

    @Insert("insert into course(courseId, name, credit, creditHour, departmentId) VALUES (#{courseId}, #{name}, #{credit}, #{creditHour}, #{dptId})")
    int insert(Course course);

    @Update("update course set courseId = #{courseId}, name = #{name}, credit = #{credit}, creditHour = #{creditHour}, dptId = #{dptId} where id = #{id}")
    int update(@Param("course") Course course);

    @Delete("delete from course where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

//    @Select("select * from course where courseId like #{courseId} and name like #{name} and credit like #{credit} and creditHour like #{creditHour} and dptId like #{dptId} limit #{pageNum}, #{pageSize}")
//    List<Course> selectPage(Integer pageNum, Integer pageSize, String courseId, String name, Integer credit, Integer creditHour, Integer dptId);

    @Select({
            "<script>",
            "SELECT * FROM course",
            "WHERE 1=1",
            "<if test='courseId != null and courseId != &quot;&quot;'>",
            "  AND courseId LIKE #{courseId}",
            "</if>",
            "<if test='name != null and name != &quot;&quot;'>",
            "  AND name LIKE #{name}",
            "</if>",
            "<if test='credit != null'>",
            "  AND credit = #{credit}",
            "</if>",
            "<if test='creditHour != null'>",
            "  AND creditHour = #{creditHour}",
            "</if>",
            "<if test='dptId != null'>",
            "  AND dptId = #{dptId}",
            "</if>",
            "LIMIT #{pageNum}, #{pageSize}",
            "</script>"
    })
    List<Course> selectPage(Integer pageNum, Integer pageSize, String courseId, String name, Integer credit, Integer creditHour, Integer dptId);


//    @Select("select count(*) from course where courseId like #{courseId} and name like #{name} and credit like #{credit} and creditHour like #{creditHour} and dptId like #{dptId}")
//    Integer selectTotal(String courseId, String name, Integer credit, Integer creditHour, Integer dptId);

    @Select({
            "<script>",
            "SELECT count(*) FROM course",
            "WHERE 1=1",
            "<if test='courseId != null and courseId != &quot;&quot;'>",
            "  AND courseId LIKE #{courseId}",
            "</if>",
            "<if test='name != null and name != &quot;&quot;'>",
            "  AND name LIKE #{name}",
            "</if>",
            "<if test='credit != null'>",
            "  AND credit = #{credit}",
            "</if>",
            "<if test='creditHour != null'>",
            "  AND creditHour = #{creditHour}",
            "</if>",
            "<if test='dptId != null'>",
            "  AND dptId = #{dptId}",
            "</if>",
            "</script>"
    })
    Integer selectTotal(String courseId, String name, Integer credit, Integer creditHour, Integer dptId);


    // 关联查询：获取所有课程及对应的院系信息
    @Select("SELECT c.id, c.course_id, c.name, c.credit, c.credit_hour, c.dpt_id, " +
            "d.id AS department_id, d.name AS department_name, d.address AS department_address, d.phone AS department_phone " +
            "FROM course c " +
            "LEFT JOIN department d ON c.dpt_id = d.id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "courseId", column = "course_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "credit", column = "credit"),
            @Result(property = "creditHour", column = "credit_hour"),
            @Result(property = "dptId", column = "dpt_id"),
            // 嵌套的 Department 对象映射
            @Result(property = "department.id", column = "department_id"),
            @Result(property = "department.name", column = "department_name"),
            @Result(property = "department.address", column = "department_address"),
            @Result(property = "department.phone", column = "department_phone")
    })
    List<Course> findAllCoursesWithDepartment();

    @Select("select * from course where dptId = #{dptId}")
    List<Course> findByDepartmentId(Integer departmentId);

    @Select("select credit from course where courseId=#{courseId}")
    Course getCourseById(String courseId);
}
