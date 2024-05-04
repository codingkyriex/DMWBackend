package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.LoginDto;
import com.example.dmwbackend.dto.RegisterDto;
import com.example.dmwbackend.dto.UserUpdateDto;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 16:53
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //用户注册
    @PostMapping("/register")
    ResponseResult<Object> register(@RequestBody RegisterDto dto) {
        return userService.register(dto);
    }

    //用户登录
    @PostMapping("/login")
    ResponseResult<Object> login(@RequestBody LoginDto dto) {
        return userService.login(dto);
    }

    //获取当前登录用户的信息
    @GetMapping("/getInfo")
    ResponseResult<UserVo> getUserInfo(HttpServletRequest request) {
        return userService.getUserInfo(request);
    }

    //更新用户信息
    @PostMapping("/updateInfo")
    ResponseResult<Object> updateInfo(@RequestBody UserUpdateDto updateDto, HttpServletRequest request) {
        return userService.updateInfo(updateDto, request);
    }

    //获取用户喜欢的文章
    @GetMapping("/likes")
    ResponseResult<Article> getLikeArticles(HttpServletRequest request) {
        return userService.getLikeArticles(request);
    }

    //获取用户发表的文章
    @GetMapping("/articles")
    ResponseResult<Article> getArticles(HttpServletRequest request) {
        return userService.getArticles(request);
    }

}
