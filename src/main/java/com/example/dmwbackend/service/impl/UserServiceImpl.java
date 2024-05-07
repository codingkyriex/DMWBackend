package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.LoginDto;
import com.example.dmwbackend.dto.RegisterDto;
import com.example.dmwbackend.dto.UserUpdateDto;
import com.example.dmwbackend.mapper.ArticleMapper;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.util.HashUtil;
import com.example.dmwbackend.util.TokenUtils;
import com.example.dmwbackend.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 21:24
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ArticleMapper articleMapper;

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

    @Override
    public ResponseResult<Article> getLikeArticles(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        //获取用户喜欢的文章id列表
        List<Integer> articleId = articleMapper.getArticleIdByUserId(userId);
        //根据文章id列表获取文章列表
        List<Article> articles = articleMapper.selectBatchIds(articleId);
        return ResponseResult.okResult(articles);
    }

    @Override
    public ResponseResult<Article> getArticles(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        //获取用户发表的文章
        List<Article> articles = articleMapper.getArticleByUserId(userId);
        return ResponseResult.okResult(articles);
    }

    @Override
    public ResponseResult<Object> register(RegisterDto dto) {
        //判断用户名是否已存在
        User user = userMapper.getUserByUsername(dto.getUsername());
        if (user != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DUPLICATE_USER_NAME);
        }
        //创建新用户
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(HashUtil.getHash(dto.getPassword()));
        userMapper.insert(newUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<Object> getUsers(Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<User> records = userMapper.selectPage(page, null).getRecords();
        return ResponseResult.okResult(records);
    }


}
