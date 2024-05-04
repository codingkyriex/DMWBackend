package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.ArticleCreateDto;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/detail/like/{id}")
    public ResponseResult<Object> likeArticle(@PathVariable("id") Integer id, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);

        return articleService.likeArticle(id,userId);
    }

    @PostMapping ("/create")
    public ResponseResult<Object> createArticle(@RequestBody ArticleCreateDto dto,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);

        return articleService.createArticle(dto,userId);
    }
}
