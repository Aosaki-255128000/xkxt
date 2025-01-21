package com.hsy.springboot.mapper;

import com.hsy.springboot.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("select * from department")
    List<Department> findAll();

    @Insert("insert into department(name, address, phone) VALUES (#{name}, #{address}, #{phone})")
    int insert(Department department);

    @Update("update department set name = #{department.name} where id = #{department.id}")
    int update(Department department);

    @Delete("delete from department where id =#{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("select * from department where name like #{name} and address like #{address} and phone like #{phone} limit #{pageNum}, #{pageSize}")
    List<Department> selectPage(Integer pageNum, Integer pageSize, String name, String address, String phone);

    @Select("select count(*) from department where name like #{name} and address like #{address} and phone like #{phone}")
    Integer selectTotal(String name, String address, String phone);

}
