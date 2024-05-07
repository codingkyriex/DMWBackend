package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.ArticleCreateDto;
import com.example.dmwbackend.dto.ArticleModifyDto;
import com.example.dmwbackend.dto.ImageUrlDto;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

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
    public ResponseResult<Object> getArticleList() {
        return articleService.getValidArticles();
    }

    @GetMapping("/detail/{id}")
    public ResponseResult<Object> getDetailArticle(@PathVariable("id") Integer id) {
        return articleService.getArticleDetail(id);
    }

    @GetMapping("/detail/like/{id}")
    public ResponseResult<Object> likeArticle(@PathVariable("id") Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);

        return articleService.likeArticle(id, userId);
    }

    @PostMapping("/create")
    public ResponseResult<Object> createArticle(@RequestBody ArticleCreateDto dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);

        return articleService.createArticle(dto, userId);
    }

    @GetMapping("/pictures")
    public ResponseResult<Object> getTopArticleUrls() {
        return articleService.getBestArticleUrl();
    }

    @GetMapping("/search/title/{name}")
    public ResponseResult<Object> searchArticleByName(@PathVariable("name") String name){
        try {
            return articleService.searchArticleByTitle(name);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/modify")
    public ResponseResult<Object> modifyArticle(@RequestBody ArticleModifyDto dto,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        return articleService.modifyArticle(dto,userId);
    }

    @PostMapping("/upload")
    public ResponseResult<Object> uploadImage(@RequestBody ImageUrlDto dto){
        return articleService.uploadImage(dto.getData());
    }
}
