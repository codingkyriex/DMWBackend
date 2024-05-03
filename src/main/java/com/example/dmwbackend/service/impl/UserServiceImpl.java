package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.LoginDto;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.util.HashUtil;
import com.example.dmwbackend.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 21:24
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public ResponseResult<Object> login(LoginDto dto) {
        User user = userMapper.getUserByAccount(dto.getUsername());
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        // 密码不正确
        if(!HashUtil.getHash(dto.getPassword()).equals(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        HashMap<String, String> res = new HashMap<>();
        res.put("token",TokenUtils.createToken(user.getUserId()));
        return ResponseResult.okResult(res);
    }
}
