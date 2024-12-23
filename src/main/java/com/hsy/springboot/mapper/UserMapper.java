package com.hsy.springboot.mapper;
import com.hsy.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from sys_user")
    List<User> findAll();


    @Insert("INSERT into sys_user(username, password, nickname, email, phone, address) VALUES (#{username}, #{password}, #{nickname}, #{email}, #{phone}, #{address})")
    int insert(User user);

    int update(User user);

    @Delete("delete from sys_user where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Delete("<script>" +
            "DELETE FROM sys_user WHERE id IN " +
            "<foreach collection='list' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    Integer deleteBatchIds(@Param("list") List<Integer> ids);

    @Select("select * from sys_user where username like  #{username} and email like #{email} and address like #{address} limit #{pageNum}, #{pageSize}")
    List<User> selectPage(Integer pageNum, Integer pageSize, String username, String email, String address);

    @Select("select count(*) from sys_user where username like #{username} and email like #{email} and address like #{address}")
    Integer selectTotal(String username, String email, String address);

    @Insert(
            "<script>" +
                    "INSERT INTO sys_user (username, password, nickname, email, phone, address) VALUES " +
                    "<foreach collection='list' item='user' separator=','>" +
                    "(#{user.username}, #{user.password}, #{user.nickname}, #{user.email}, #{user.phone}, #{user.address})" +
                    "</foreach>" +
                    "</script>")
    void insertBatch(List<User> list);
}