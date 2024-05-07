package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.util.TokenUtils;
import com.example.dmwbackend.vo.WordVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tavis
 * @date 2024-05-06
 * @desc 首页控制器
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    WordService wordService;

    @Autowired
    ArticleService articleService;

    //获取每日单词
    @GetMapping("/dailyword")
    public ResponseResult<WordVo> getDailyWord(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        return wordService.getDailyWord(userId);
    }

    //获取每日一句
    @GetMapping("/dailysentence")
    public ResponseResult<Object> getDailySentence(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        return wordService.getDailySentence(userId);
    }

    //获取首页轮播图
    @GetMapping("/pictures")
    public ResponseResult<Object> getPicture() {
        return articleService.getBestArticleUrl();
    }

}
