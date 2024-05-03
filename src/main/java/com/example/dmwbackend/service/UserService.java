package com.example.dmwbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.LoginDto;
import com.example.dmwbackend.dto.UserUpdateDto;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 20:30
 **/

public interface UserService extends IService<User> {
    ResponseResult<Object> login(LoginDto dto);

    ResponseResult<UserVo> getUserInfo(HttpServletRequest request);

    ResponseResult<Object> updateInfo(UserUpdateDto updateDto, HttpServletRequest request);

}
