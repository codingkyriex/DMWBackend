package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.*;
import com.example.dmwbackend.mapper.ArticleMapper;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.mapper.VocabularyMapper;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.pojo.Vocabulary;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.util.HashUtil;
import com.example.dmwbackend.util.LLMGenerator;
import com.example.dmwbackend.util.PromptGenerator;
import com.example.dmwbackend.util.TokenUtils;
import com.example.dmwbackend.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Int;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    VocabularyMapper vocabularyMapper;

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
    public ResponseResult<UserVo> getUserInfo(Integer id) {
        User user = userMapper.selectById(id);
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
        List<Integer> articleId = articleMapper.getLikeArticle(userId);
        //根据文章id列表获取文章列表
        List<Article> articles = articleMapper.selectBatchIds(articleId);
        List<LikeArticleVo> res = new ArrayList<>();
        //获取文章的作者
        for (Article article : articles) {
            //将文章封装为LikeArticleVo
            LikeArticleVo articleVo = new LikeArticleVo();
            //将article的属性拷贝到articleVo
            BeanUtils.copyProperties(article, articleVo);
            User user = userMapper.selectById(article.getUserId());
            AuthorVo author = new AuthorVo();
            //获取文章作者的id，名字和头像信息
            author.setUserId(user.getUserId());
            author.setUsername(user.getUsername());
            author.setAvatar(user.getAvatar());
            articleVo.setAuthor(author);
            String[] pictures = articleMapper.getPictureByArticleId(article.getArticleId());
            articleVo.setPictures(pictures);
            //处理时间格式为yyyy-MM-dd
            String createTime = article.getCreateTime().toString();
            articleVo.setCreateTime(createTime);
            res.add(articleVo);
        }
        return ResponseResult.okResult(res);
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
        newUser.setState("readAndWrite");
        newUser.setAvatar("http://49.233.255.219/images/profile.jpg");
        newUser.setVipLevel(0);
        newUser.setVipStatus("non_vip");
        newUser.setProgress(0);
        newUser.setPoints(0);
        newUser.setCreateTime(new Date());
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

    @Override
    public ResponseResult<Object> modifyUserVIP(UserVipDto dto) {
        User user1 = userMapper.selectById(dto.getUser());
        if (user1 == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        user1.setVipStatus(dto.getVip() == 0 ? "non_vip" : "vip");
        user1.setVipLevel(dto.getVipLevel());
        userMapper.updateById(user1);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<Object> getRejectedArticles(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        List<Article> articles = articleMapper.getRejectedArticle(id);
        return ResponseResult.okResult(articles);
    }


    public ResponseResult<ProgressVo> getProgress(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        ProgressVo progressVo = new ProgressVo();
        Integer progress = user.getProgress();
        progressVo.setProgress(progress);
        Integer bookId = user.getBook();
        Vocabulary book = vocabularyMapper.selectById(bookId);
        progressVo.setBookName(book.getName());
        return ResponseResult.okResult(progressVo);
    }

    @Override
    public ResponseResult<Object> changeUserState(Integer user, Integer state) {
        User user1 = userMapper.selectById(user);
        if (user1 == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        user1.setState(state == 0 ? "read" : "readAndWrite");
        updateById(user1);
        return ResponseResult.okResult(null);
    }

    @Override
    public ResponseResult<Object> getAiContactSentence(AiContactDto dto) {
        String s = LLMGenerator.convertResponse(LLMGenerator.getResponse(PromptGenerator.getDialoguePrompt(dto.getLLMReply() + " " + dto.getUserReply(), dto.getIsFirst())));
        HashMap<String, String> res = new HashMap<>();
        res.put("content", s);
        res.put("content-type", "text");
        return ResponseResult.okResult(res);
    }

}
