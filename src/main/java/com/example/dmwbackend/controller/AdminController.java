package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.ArticleModifyDto;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/7 9:47
 */

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;

    @PostMapping("/modify")
    public ResponseResult<Object> modifyArticle(@RequestBody ArticleModifyDto dto){
        return articleService.modifyArticle(dto);
    }

    @GetMapping("/detail/{id}")
    public ResponseResult<Object> getDetailArticle(@PathVariable("id") Integer id) {
        return articleService.getArticleDetail(id);
    }

    @GetMapping("/search/title/{name}")
    public ResponseResult<Object> searchArticleByName(@PathVariable("name") String name){
        try {
            return articleService.searchArticleByTitle(name);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllUser")
    public ResponseResult<Object> getUsersByPage(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        return userService.getUsers(pageNum,pageSize);
    }

}
