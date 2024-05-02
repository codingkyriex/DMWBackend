package com.example.dmwbackend.service;

import com.example.dmwbackend.config.ResponseResult;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 11:28
 **/

public interface WordService {
    ResponseResult<Object> getAiSentence(@RequestParam("id") Integer id);
}
