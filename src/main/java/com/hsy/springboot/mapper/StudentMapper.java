package com.hsy.springboot.mapper;
import com.hsy.springboot.entity.Student;
import com.hsy.springboot.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Select("select * from student")
    List<Student> findAll();

    @Insert("INSERT into student(username, password, name, role, sex, code, collegeId, score) VALUES (#{username}, #{password}, #{name}, #{role}, #{sex}, #{code}, #{collegeId}, #{score})")
    int insert(Student student);

    @Select({
            "<script>",
            "SELECT * FROM student",
            "WHERE username LIKE #{username}",
            "AND name LIKE #{name}",
            "AND role LIKE #{role}",
            "AND sex LIKE #{sex}",
            "AND code LIKE #{code}",
            "<if test='collegeId != null'> AND collegeId = #{collegeId}</if>",
            "<if test='score != null'> AND score = #{score}</if>",
            "LIMIT #{pageNum}, #{pageSize}",
            "</script>"
    })
    List<Student> selectPage(Integer pageNum, Integer pageSize, String username, String name, String role, String sex, String code, Integer collegeId, Integer score);

    @Select({
            "<script>",
            "SELECT COUNT(*) FROM student",
            "WHERE username LIKE #{username}",
            "AND name LIKE #{name}",
            "AND role LIKE #{role}",
            "AND sex LIKE #{sex}",
            "AND code LIKE #{code}",
            "<if test='collegeId != null'> AND collegeId = #{collegeId}</if>",
            "<if test='score != null'> AND score = #{score}</if>",
            "</script>"
    })
    Integer selectTotal(String username, String name, String role, String sex, String code, Integer collegeId, Integer score);


    int update(Student student);

    @Delete("delete from student where id = #{id}")
    Integer deleteById(@Param("id") Integer id);
}
