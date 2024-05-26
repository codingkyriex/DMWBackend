package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.ArticleModifyDto;
import com.example.dmwbackend.dto.UserVipDto;
import com.example.dmwbackend.dto.WordDto;
import com.example.dmwbackend.pojo.Word;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.util.TokenUtils;
import com.example.dmwbackend.vo.UserVo;
import com.example.dmwbackend.vo.UserWordVo;
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

    @Autowired
    WordService wordService;

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
    public ResponseResult<Object> getUsersByPage(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10000") int pageSize){
        return userService.getUsers(pageNum,pageSize);
    }

    @PostMapping("/modifyUser")
    public ResponseResult<Object> modifyUser(@RequestBody UserVipDto dto){
        return userService.modifyUserVIP(dto);
    }

    @GetMapping("/getAllArticle")
    public  ResponseResult<Object> getPagedArticles(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10000") int pageSize) {
        return articleService.getPagedArticles(pageNum, pageSize);
    }

    @GetMapping("/getPendingArticle")
    public  ResponseResult<Object> getPedingArticles(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10000") int pageSize){
        return articleService.getPendingArticles(pageNum,pageSize);
    }

    @GetMapping("/reviewArticle/{id}/{status}")
    public ResponseResult<Object> reviewArticle(@PathVariable("id")Integer id,@PathVariable("status") Integer status){
        return articleService.reviewArticle(id,status);
    }

    @GetMapping("/ban/{id}/{status}")
    public ResponseResult<Object> banUser(@PathVariable("id")Integer id,@PathVariable("status") Integer status){
        return userService.changeUserState(id,status);
    }

    @GetMapping("/getArticleById/{id}")
    public ResponseResult<Object> getArticleById(@PathVariable("id")Integer id){
        return articleService.getArticlesById(id);
    }

    @GetMapping("/getUserInfo/{id}")
    public ResponseResult<UserVo> getDetailUser(@PathVariable("id") Integer id){
        return userService.getUserInfo(id);
    }

    @GetMapping("/getUserWords")
    public ResponseResult<Object> getUserWords(){
        return wordService.getUsersWords();
    }

    @GetMapping("/getAllWords")
    public ResponseResult<Object> getAllWords(@RequestParam(name = "word", defaultValue = "") String word,@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10000") int pageSize){
        return wordService.getAllWords(word,pageNum,pageSize);
    }

    @PostMapping("/updateWord/{id}")
    public ResponseResult<Object> updateWord(@PathVariable("id") Integer id,@RequestBody WordDto dto){
        return wordService.updateWord(id,dto);
    }

    @GetMapping("/getWord/{id}")
    public ResponseResult<Word> getWord(@PathVariable("id") Integer id){
        return wordService.getWordDetail(id);
    }
}
