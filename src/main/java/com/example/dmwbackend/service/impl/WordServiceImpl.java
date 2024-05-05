package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.mapper.UserWordProgressMapper;
import com.example.dmwbackend.mapper.WordMapper;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.pojo.UserWordProgress;
import com.example.dmwbackend.pojo.Word;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.util.LLMGenerator;
import com.example.dmwbackend.util.PromptGenerator;
import com.example.dmwbackend.util.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 21:26
 */

@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserWordProgressMapper userWordProgressMapper;

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

    @Override
    public ResponseResult<Object> getAiTest(Integer u) {
        User user = userMapper.selectById(u);
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        List<UserWordProgress> progress = userWordProgressMapper.getProgressById(u);
        // 使用Stream API进行排序和截取操作
        List<UserWordProgress> sortedList = progress.stream()
                .sorted((a, b) -> b.getDay().compareTo(a.getDay()))
                .limit(10)
                .collect(Collectors.toList());

        // 如果列表不足十个，则返回全部
        if (sortedList.size() < 10) {
            sortedList = progress.stream()
                    .sorted((a, b) -> b.getDay().compareTo(a.getDay()))
                    .collect(Collectors.toList());
        }
        UserWordProgress p = SortUtil.selectRandomWithBias(sortedList);
        Map<String,Object> res = null;
        Word word = wordMapper.selectById(p.getWordId());
        res = getSingleTest(word.getEnglish());
        return ResponseResult.okResult(res);
    }

    private Map<String,Object> getSingleTest(String word){
        String response = LLMGenerator.convertResponse(LLMGenerator.getResponse(PromptGenerator.getSingleTestPrompt(word), "glm-4"));
        Map<String, Object> dictionary = new HashMap<>();
        ArrayList<String> choices = new ArrayList<>();
        int answerIndex = -1; // 默认答案索引为-1，表示未找到答案

        // 使用正则表达式匹配问题
        String[] questionMatch = response.split("\\r?\\n")[0].split("Q: ");
        String question = questionMatch.length > 1 ? questionMatch[1].trim() : "";

        // 使用正则表达式匹配选项
        String[] options = response.split("\\r?\\n");
        for (int i = 1; i < options.length; i++) {
            String option = options[i].trim();
            String[] parts = option.split("\\. ");
            if (parts.length == 2) {
                choices.add(parts[1]); // 添加选项值到choices数组

                // 检查这个选项的值是否是要找的答案
                if (parts[1].equalsIgnoreCase(word)) {
                    answerIndex = choices.size() - 1; // 设置答案索引
                }
            }
        }

        // 将问题、选项和答案索引存储到字典中
        dictionary.put("question", question);
        dictionary.put("choices", choices);
        if (answerIndex != -1) {
            dictionary.put("answer", answerIndex); // 如果找到了答案，添加答案索引
        } else {
            dictionary.put("answer", null); // 如果没有找到答案，设置为null
        }
        return dictionary;
    }


}
