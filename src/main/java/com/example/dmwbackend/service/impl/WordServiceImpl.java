package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.mapper.WordMapper;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.pojo.Word;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.util.LLMGenerator;
import com.example.dmwbackend.util.PromptGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 21:26
 */

@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Override
    public ResponseResult<Object> getAiSentence(Integer id) {
        Word word = wordMapper.selectById(id);
        String prompt = PromptGenerator.getAiSentencePrompt(word.getEnglish());
        Map<String, String> response = LLMGenerator.getResponse(prompt, "glm-4");
        String res = LLMGenerator.convertResponse(response);
        HashMap<String, String> nres = new HashMap<>();
        if(res.equals("error")){
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        nres.put("sentence",res);
        return ResponseResult.okResult(nres);
    }
}
