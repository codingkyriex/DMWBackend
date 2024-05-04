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
        Map<String, String> test = LLMGenerator.getResponse(PromptGenerator.getSingleTestPrompt("purchase"), "glm-4");
        System.out.println(LLMGenerator.convertResponse(test));
    }


}
