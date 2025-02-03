package com.hsy.springboot.mapper;

import com.hsy.springboot.entity.Admin;
import com.hsy.springboot.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Select("SELECT * FROM student")
    List<Student> findAll();

    @Insert("INSERT INTO student(username, password, name, role, sex, code, collegeId, phone, birthDate, hometown) VALUES (#{username}, #{password}, #{name}, #{role}, #{sex}, #{code}, #{collegeId}, #{phone}, #{birthDate}, #{hometown})")
    int insert(Student student);

    int update(Student student);

    @Delete("DELETE FROM student WHERE id=#{id}")
    Integer deleteById(@Param("id") Integer id);

    // 查询所有学生及其院系信息
    @Select({
            "<script>",
            "SELECT s.*, d.id AS departmentId, d.name AS departmentName FROM student s",
            "LEFT JOIN department d ON s.collegeId = d.id",
            "WHERE s.username LIKE #{username}",
            "AND s.name LIKE #{name}",
            "AND s.role LIKE #{role}",
            "AND s.sex LIKE #{sex}",
            "AND s.code LIKE #{code}",
            "<if test='collegeId != null'> AND s.collegeId = #{collegeId}</if>",
            "LIMIT #{pageNum}, #{pageSize}",
            "</script>"
    })
    List<Student> selectPage(Integer pageNum, Integer pageSize, String username, String name, String role, String sex, String code, Integer collegeId);


    @Select({
            "<script>",
            "SELECT COUNT(*) FROM student s",
            "LEFT JOIN department d ON s.collegeId = d.id",
            "WHERE s.username LIKE #{username}",
            "AND s.name LIKE #{name}",
            "AND s.role LIKE #{role}",
            "AND s.sex LIKE #{sex}",
            "AND s.code LIKE #{code}",
            "<if test='collegeId != null'> AND s.collegeId = #{collegeId}</if>",
            "</script>"
    })
    Integer selectTotal(String username, String name, String role, String sex, String code, Integer collegeId);

    @Select("select * from student where collegeId = #{collegeId}")
    List<Student> findByDepartmentId(Integer departmentId);

    @Select("SELECT * FROM student WHERE username = #{username} AND role = #{role}")
    Student findByStudentnameAndRole(@Param("username") String username, @Param("role") String role);

    @Select("select * from student where username = #{username}")
    Student findByUsername(String username);
}

