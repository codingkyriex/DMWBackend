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
        String te="从现在起你是一个雅思口语的考官，名字叫Lee,全程使用英语，与你对话的人会回答你的英语问题，你可以在各个角度与对话人进行对话，现在开始第一句对话";
        String next="I think is cultural Landmarks,The historical sites such as the Great Wall, the Forbidden City, and the Summer Palace are not only visually stunning but also serve as a window into China's rich past.";
//        String s1 = LLMGenerator.convertResponse(LLMGenerator.getResponse(te));
        String o1 = "Beijing, my hometown, is a dynamic modern city steeped in rich history and culture. It boasts magnificent historical sites like the Great Wall and the Forbidden City, as well as bustling business districts and high-tech zones. The unique charm of Peking Duck and Hutongs (narrow alleys) is part of its allure. With distinct seasons, each brings its own beauty and activities. As China's capital, it's also the epicenter of politics, economy, and culture, always buzzing with opportunities and challenges.";
        String n1="从现在起你是一个雅思口语的考官，全程使用英语，与你对话的人会回答你的英语问题，你可以在各个角度与对话人进行对话，我会给你提供前面一轮的对话："+"Hello Eric, it's nice to meet you. So, coming from China, can you tell me a bit about your hometown? What's special about the place you come from?"+o1;
        String s = LLMGenerator.convertResponse(LLMGenerator.getResponse(te));
        System.out.println(s);
    }


}
