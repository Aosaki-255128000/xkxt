package com.hsy.springboot.mapper;

import com.hsy.springboot.entity.CourseSelection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseSelectionMapper {
    @Select("select * from courseselection")
    List<CourseSelection> findAll();

    @Insert("insert into courseselection(studentId, semester, courseId, jobNumber, usualPerformance, testScore, totalScore) values (#{studentId}, #{semester}, #{courseId}, #{jobNumber},#{usualPerformance}, #{testScore}, #{totalScore})")
    int insert(CourseSelection courseSelection);

    int update(CourseSelection courseSelection);

    @Delete("delete from courseselection where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select({"<script>" +
            "SELECT * FROM courseselection " +
            "WHERE studentId LIKE CONCAT('%', #{studentId}, '%') " +
            "AND semester LIKE CONCAT('%', #{semester}, '%') " +
            "AND courseId LIKE CONCAT('%', #{courseId}, '%') " +
            "AND jobNumber LIKE CONCAT('%', #{jobNumber}, '%')" +
            "<if test='usualPerformance != null'> AND usualPerformance = #{usualPerformance}</if>" +
            "<if test='testScore != null'> AND testScore = #{testScore}</if>" +
            "<if test='totalScore != null'> AND totalScore = #{totalScore}</if>" +
            "LIMIT #{pageNum}, #{pageSize}",
            "</script>"})
    List<CourseSelection> selectPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                     @Param("studentId") String studentId, @Param("semester") String semester,
                                     @Param("courseId") String courseId, @Param("jobNumber") String jobNumber,
                                     @Param("usualPerformance") Integer usualPerformance,
                                     @Param("testScore") Integer testScore, @Param("totalScore") Integer totalScore);

    @Select({"<script>" +
            "SELECT COUNT(*) FROM courseselection " +
            "WHERE studentId LIKE CONCAT('%', #{studentId}, '%') " +
            "AND semester LIKE CONCAT('%', #{semester}, '%') " +
            "AND courseId LIKE CONCAT('%', #{courseId}, '%') " +
            "AND jobNumber LIKE CONCAT('%', #{jobNumber}, '%')" +
            "<if test='usualPerformance != null'> AND usualPerformance = #{usualPerformance}</if>" +
            "<if test='testScore != null'> AND testScore = #{testScore}</if>" +
            "<if test='totalScore != null'> AND totalScore = #{totalScore}</if>" +
            "</script>"})
    Integer selectTotal(@Param("studentId") String studentId, @Param("semester") String semester,
                        @Param("courseId") String courseId, @Param("jobNumber") String jobNumber,
                        @Param("usualPerformance") Integer usualPerformance,
                        @Param("testScore") Integer testScore, @Param("totalScore") Integer totalScore);
}
