package com.example.dmwbackend.service;

import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.pojo.Word;
import com.example.dmwbackend.vo.WordVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 11:28
 **/

public interface WordService {
    ResponseResult<Object> getAiSentence(@RequestParam("id") Integer id);

    ResponseResult<Object> getAiTest(Integer u);

    ResponseResult<WordVo> getNextWord(Integer userId);

    ResponseResult<Word> getWordDetail(Integer id);

    ResponseResult<Object> likeWord(Integer userId, Integer wordId);

    ResponseResult<List<WordVo>> getReviewWords(Integer userId);

    ResponseResult<Object> getDailySentence(Integer userId);

    ResponseResult<WordVo> getDailyWord(Integer userId);
}
