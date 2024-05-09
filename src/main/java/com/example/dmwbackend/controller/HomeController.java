package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.vo.WordVo;
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
    public ResponseResult<WordVo> getDailyWord() {
        return wordService.getDailyWord();

    }

    //获取每日一句
    @GetMapping("/dailysentence")
    public ResponseResult<Object> getDailySentence() {
        return wordService.getDailySentence();
    }

    //获取首页轮播图
    @GetMapping("/pictures")
    public ResponseResult<Object> getPicture() {
        return articleService.getBestArticleUrl();
    }

}
