package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.LoginDto;
import com.example.dmwbackend.dto.UserUpdateDto;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.util.HashUtil;
import com.example.dmwbackend.util.TokenUtils;
import com.example.dmwbackend.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = userMapper.getUserByUsername(dto.getUsername());
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        // 密码不正确
        if (!HashUtil.getHash(dto.getPassword()).equals(user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        HashMap<String, String> res = new HashMap<>();
        res.put("token", TokenUtils.createToken(user.getUserId()));
        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult<UserVo> getUserInfo(HttpServletRequest request) {
        //根据token获取用户信息
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        } else {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            return ResponseResult.okResult(userVo);
        }
    }

    @Override
    public ResponseResult<Object> updateInfo(UserUpdateDto updateDto, HttpServletRequest request) {
        //根据token获取当前用户
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        } else {
            //根据updateDto更新用户信息
            user.setUsername(updateDto.getUsername());
            user.setAvatar(updateDto.getAvatar());
            userMapper.updateById(user);
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
    }


}
