package com.hsy.springboot.mapper;

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

    // 关联查询：获取所有课程及对应的院系信息
//    @Select("SELECT s.id, s.username, s.name, s.role, s.sex, s.code, s.collegeId, s.phone, s.birthDate, s.hometown " +
//            "d.id AS department_id, d.name AS department_name, d.address AS department_address, d.phone AS department_phone " +
//            "FROM student s " +
//            "LEFT JOIN department d ON s.collegeId = d.id")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "username", column = "username"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "role", column = "role"),
//            @Result(property = "sex", column = "sex"),
//            @Result(property = "code", column = "code"),
//            @Result(property = "collegeId", column = "collegeId"),
//            @Result(property = "phone", column = "phone"),
//            @Result(property = "birthDate", column = "birthDate"),
//            @Result(property = "hometown", column = "hometown"),
//            // 嵌套的 Department 对象映射
//            @Result(property = "department.id", column = "department_id"),
//            @Result(property = "department.name", column = "department_name"),
//            @Result(property = "department.address", column = "department_address"),
//            @Result(property = "department.phone", column = "department_phone")
//    })
//    List<Student> findAllStudentsWithDepartment();

    @Select("select * from student where collegeId = #{collegeId}")
    List<Student> findByDepartmentId(Integer departmentId);
}

