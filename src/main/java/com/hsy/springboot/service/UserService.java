package com.hsy.springboot.service;
import com.hsy.springboot.entity.User;
import com.hsy.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int save(User user) {
        if(user.getId() == null) { // user没有id，新增
            return userMapper.insert(user);
        } else { // 否则更新
            return userMapper.update(user);
        }
    }

    // 批量删除方法
    public int deleteBatch(List<Integer> ids) {
        return userMapper.deleteBatchIds(ids);
    }

    // 查询所有用户方法
    public List<User> list() {
        return userMapper.findAll();
    }


    public void saveBatch(List<User> list) {
        userMapper.insertBatch(list);
    }
}
