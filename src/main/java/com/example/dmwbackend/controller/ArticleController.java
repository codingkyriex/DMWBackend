package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 11:49
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping("/list")
    public ResponseResult<Object> getArticleList(){
        return articleService.getValidArticles();
    }

    @GetMapping("/detail/{id}")
    public ResponseResult<Object> getDetailArticle(@PathVariable("id") Integer id){
        return articleService.getArticleDetail(id);
    }
}
