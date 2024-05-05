package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult<Object> getAiSentence(@PathVariable("id") Integer id){
        return wordService.getAiSentence(id);
    }

    @GetMapping("/test")
    public ResponseResult<Object> getAiTest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Integer userId = TokenUtils.getUserIdFromToken(token);
        return wordService.getAiTest(userId);
    }


}
