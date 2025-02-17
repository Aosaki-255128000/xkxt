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

    List<CourseSelection> selectSelectionsByJobNumber(
            @Param("jobNumber") String jobNumber,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize,
            @Param("studentId") String studentId,
            @Param("semester") String semester,
            @Param("courseId") String courseId,
            @Param("usualPerformance") Integer usualPerformance,
            @Param("testScore") Integer testScore,
            @Param("totalScore") Integer totalScore);

    List<CourseSelection> selectSelectionsByStudentId(
            @Param("studentId") String studentId,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize,
            @Param("jobNumber") String jobNumber,
            @Param("semester") String semester,
            @Param("courseId") String courseId,
            @Param("usualPerformance") Integer usualPerformance,
            @Param("testScore") Integer testScore,
            @Param("totalScore") Integer totalScore);


    int countSelectionsTeacher(String jobNumber, String studentId, String semester, String courseId, Integer usualPerformance, Integer testScore, Integer totalScore);

    int countSelectionsStudent(String studentId, String jobNumber, String semester, String courseId, Integer usualPerformance, Integer testScore, Integer totalScore);

    List<CourseSelection> selectTimetableByStudent(String studentId, String semester);

    @Select("select * from courseselection where id = #{id}")
    CourseSelection selectById(Integer id);

    @Select("SELECT * FROM courseselection WHERE courseId = #{courseId} AND studentId = #{studentId}")
    CourseSelection selectByCourseIdAndStudentId(String courseId, String studentId);


    Double selectAverageTotalScore(String semester, String courseId, String jobNumber);
}
