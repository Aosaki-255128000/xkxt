package com.hsy.springboot.mapper;

import com.hsy.springboot.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("select * from admin")
    List<Admin> findAll();

    @Insert("INSERT into admin(username, password, role) VALUES (#{username}, #{password}, #{role})")
    int insert(Admin admin);

    @Update("UPDATE admin SET username = #{admin.username} WHERE id = #{admin.id}")
    int update(@Param("admin") Admin admin);

    @Delete("delete from admin where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("select * from admin where username like #{username} and role like #{role}")
    List<Admin> selectPage(Integer pageNum, Integer pageSize, String username, String role);

    @Select("select count(*) from admin where username like #{username} and role like #{role}")
    Integer selectTotal(String username, String role);

    @Select("SELECT * FROM admin WHERE username = #{username} AND password = #{password}")
    Admin findByUsernameAndPassword(Admin admin);


}
