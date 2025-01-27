package com.hsy.springboot.mapper;
import com.hsy.springboot.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {
    @Select("select jobNumber, name from teacher")
    List<Teacher> findAll();

    @Insert("INSERT into teacher(username, password, name, sex, title, jobNumber, departmentId, role, birthDate) VALUES (#{username}, #{password}, #{name}, #{sex}, #{title}, #{jobNumber}, #{departmentId}, #{role}, #{birthDate})")
    int insert(Teacher teacher);

    int update(Teacher teacher);

    @Delete("delete from teacher where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    // 查询所有教师及其院系信息
    @Select({
            "<script>",
            "SELECT t.*, d.id AS departmentId, d.name AS departmentName FROM teacher t",
            "LEFT JOIN department d ON t.departmentId = d.id",
            "WHERE t.username LIKE #{username}",
            "AND t.name LIKE #{name}",
            "AND t.sex LIKE #{sex}",
            "AND t.title LIKE #{title}",
            "AND t.jobNumber LIKE #{jobNumber}",
            "AND t.role LIKE #{role}",
            "<if test='departmentId != null'> AND t.departmentId = #{departmentId}</if>",
            "LIMIT #{pageNum}, #{pageSize}",
            "</script>"
    })
    List<Teacher> selectPage(Integer pageNum, Integer pageSize, String username, String name, String sex, String title, String jobNumber, String role, Integer departmentId);


    @Select({
            "<script>",
            "SELECT COUNT(*) FROM teacher t",
            "LEFT JOIN department d ON t.departmentId = d.id",
            "WHERE t.username LIKE #{username}",
            "AND t.name LIKE #{name}",
            "AND t.sex LIKE #{sex}",
            "AND t.title LIKE #{title}",
            "AND t.jobNumber LIKE #{jobNumber}",
            "AND t.role LIKE #{role}",
            "<if test='departmentId != null'> AND t.departmentId = #{departmentId}</if>",
            "</script>"
    })
    Integer selectTotal(String username, String name, String sex, String title, String jobNumber, String role, Integer departmentId);

    // 关联查询：获取所有课程及对应的院系信息
    @Select("SELECT t.id, t.username, t.name, t.sex, t.title, t.jobNumber, t.departmentId, t.role, t.birthDate " +
            "d.id AS department_id, d.name AS department_name, d.address AS department_address, d.phone AS department_phone " +
            "FROM teaher t " +
            "LEFT JOIN department d ON t.departmentId = d.id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "name", column = "name"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "title", column = "title"),
            @Result(property = "jobNumber", column = "JobNumber"),
            @Result(property = "departmentId", column = "departmentId"),
            @Result(property = "role", column = "role"),
            @Result(property = "birthDate", column = "birthDate"),

            // 嵌套的 Department 对象映射
            @Result(property = "department.id", column = "department_id"),
            @Result(property = "department.name", column = "department_name"),
            @Result(property = "department.address", column = "department_address"),
            @Result(property = "department.phone", column = "department_phone")
    })
    List<Teacher> findAllTeachersWithDepartment();

    @Select("select * from teacher where departmentId = #{departmentId}")
    List<Teacher> findByDepartmentId(Integer departmentId);

    @Select("select * from teacher where username = #{username} and role = #{role}")
    Teacher findByTeachernameAndRole(@Param("username") String username, @Param("role") String role);

}
