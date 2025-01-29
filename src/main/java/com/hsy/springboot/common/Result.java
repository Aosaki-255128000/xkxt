package com.hsy.springboot.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private Integer code;   // 状态码（200成功，500失败）
    private String message; // 返回信息
    private Map<String, Object> data = new HashMap<>(); // 返回数据

    // 成功静态方法
    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    public static Result ok() {
        Result result = new Result();
        result.code = 200;
        return result;
    }

    // 失败静态方法
    public static Result error() {
        Result result = new Result();
        result.setCode(500);
        result.setMessage("用户名或密码错误");
        return result;
    }

    // 链式调用方法
    public Result code(Integer code) {
        this.code = code;
        return this;
    }

    public Result message(String message) {
        this.message = message;
        return this;
    }

    public Result data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}