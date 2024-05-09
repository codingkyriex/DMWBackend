package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.pojo.Word;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.util.TokenUtils;
import com.example.dmwbackend.vo.WordVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 22:39
 */

@RestController
@RequestMapping("/study")
public class StudyController {

    @Autowired
    WordService wordService;

    @GetMapping("/ai/{id}")
    public ResponseResult<Object> getAiSentence(@PathVariable("id") Integer id) {
        return wordService.getAiSentence(id);
    }

    @GetMapping("/test")
    public ResponseResult<Object> getAiTest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        String tokenError = (String) request.getAttribute("tokenError");
        if(tokenError!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.valueOf(tokenError));
        }
        return wordService.getAiTest(userId);
    }

    //获取下一个要学习的单词
    @GetMapping("/next")
    public ResponseResult<WordVo> getNextWord(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        String tokenError = (String) request.getAttribute("tokenError");
        if(tokenError!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.valueOf(tokenError));
        }
        return wordService.getNextWord(userId);
    }

    //获取单词详情
    @GetMapping("/detail/{id}")
    public ResponseResult<Word> getWordDetail(@PathVariable("id") Integer id) {
        return wordService.getWordDetail(id);
    }

    //收藏单词
    @PostMapping("/like/{id}")
    public ResponseResult<Object> likeWord(@PathVariable("id") Integer wordId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        String tokenError = (String) request.getAttribute("tokenError");
        if(tokenError!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.valueOf(tokenError));
        }
        return wordService.likeWord(userId, wordId);
    }

    //获取复习单词列表
    @GetMapping("/review")
    public ResponseResult<List<WordVo>> getReviewWords(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        String tokenError = (String) request.getAttribute("tokenError");
        if(tokenError!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.valueOf(tokenError));
        }
        return wordService.getReviewWords(userId);
    }
}
