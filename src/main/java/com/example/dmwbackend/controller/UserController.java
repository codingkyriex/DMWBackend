package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.LoginDto;
import com.example.dmwbackend.dto.UserUpdateDto;
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
}
