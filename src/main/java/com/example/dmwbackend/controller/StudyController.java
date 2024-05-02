package com.example.dmwbackend.controller;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/ai/{id}")
    public ResponseResult<Object> getAiSentence(@PathVariable("id") Integer id){
        return wordService.getAiSentence(id);
    }


}
