package com.hsy.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.hsy.springboot.entity.User;
import com.hsy.springboot.mapper.UserMapper;
import com.hsy.springboot.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    //新增和修改
    @PostMapping
    public Integer save(@RequestBody User user) {
        // 新增或者更新
        return userService.save(user);
    }

    //查询所有数据
    @GetMapping
    public List<User> findAll() {
        List<User> all = userMapper.findAll();
        return all;
    }

    //删除
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return userMapper.deleteById(id);
    }

    //批量删除
    @PostMapping("del/batch")
    public Integer deleteBatch(@RequestBody List<Integer> ids) {
        return userService.deleteBatch(ids);
    }

    //分页查询
    //@RequestParam接收pageNum=1 & pageSize=10
    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String username,
                                        @RequestParam(defaultValue = "") String email,
                                        @RequestParam(defaultValue = "") String address) {
        pageNum = (pageNum - 1) * pageSize;

        username = "%" + username + "%";
        email = "%" + email + "%";
        address = "%" + address + "%";

        List<User> data = userMapper.selectPage(pageNum, pageSize, username, email, address);
        Integer total = userMapper.selectTotal(username, email, address);

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库中查询所有的数据
        List<User> list = userService.list();
        //通过工具类创建writer写出到磁盘路径
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");
        //一次性写出list内的对象到excel
        writer.write(list, true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; ，filemname=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件不能为空");
        }
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        List<User> list = reader.read(0,1,User.class);
        for(User user : list) {
            if(user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("用户名不能为空");
            }
        }
        userService.saveBatch(list);
        return true;
    }
}