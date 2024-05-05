package com.example.dmwbackend;

import com.example.dmwbackend.util.LLMGenerator;
import com.example.dmwbackend.util.PromptGenerator;
import com.example.dmwbackend.util.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dmwbackend.util.HashUtil.getHash;
import static com.example.dmwbackend.util.TokenGenerator.generateToken;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 12:33
 */
public class testLLM {

    public static void main(String[] args) {
        String input = "Q: A term used when acquiring goods or services by exchanging money or its equivalent.\n" +
                "Options:\n" +
                "A. Sell\n" +
                "B. Trade\n" +
                "C. Barter\n" +
                "D. Purchase";

        Map<String, Object> dictionary = new HashMap<>();
        ArrayList<String> choices = new ArrayList<>();
        int answerIndex = -1; // 默认答案索引为-1，表示未找到答案

        // 使用正则表达式匹配问题
        String[] questionMatch = input.split("\\r?\\n")[0].split("Q: ");
        String question = questionMatch.length > 1 ? questionMatch[1].trim() : "";

        // 使用正则表达式匹配选项
        String[] options = input.split("\\r?\\n");
        for (int i = 1; i < options.length; i++) {
            String option = options[i].trim();
            String[] parts = option.split("\\. ");
            if (parts.length == 2) {
                choices.add(parts[1]); // 添加选项值到choices数组

                // 检查这个选项的值是否是要找的答案
                if (parts[1].equals("Trade")) {
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
        // 输出字典信息
        System.out.println("Dictionary contents:");
        dictionary.forEach((key, value) -> System.out.println(key + ": " + value));
    }


}
