package com.hsy.springboot.mapper;
import com.hsy.springboot.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {
    @Select("select * from teacher")
    List<Teacher> findAll();

    @Insert("INSERT into teacher(username, password, name, sex, title, specialtyId, role) VALUES (#{username}, #{password}, #{name}, #{sex}, #{title}, #{specialtyId}, #{role})")
    int insert(Teacher teacher);

    int update(Teacher teacher);

    @Select("select * from teacher where username like  #{username} and name like #{name} and sex like #{sex} and title like #{title} and specialtyId like #{specialtyId} and role like #{role} limit #{pageNum}, #{pageSize}")
    List<Teacher> selectPage(Integer pageNum, Integer pageSize, String username, String name, String sex, String title, String specialtyId, String role);

    @Select("select count(*) from teacher where username like  #{username} and name like #{name} and sex like #{sex} and title like #{title} and specialtyId like #{specialtyId} and role like #{role} ")
    Integer selectTotal(String username, String name, String sex, String title, String specialtyId, String role);

    @Delete("delete from teacher where id = #{id}")
    Integer deleteById(@Param("id") Integer id);
}
