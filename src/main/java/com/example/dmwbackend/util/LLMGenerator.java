package com.example.dmwbackend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 20:40
 */
public class LLMGenerator {
    private static final String apiKey = "85fab10ae2f22a18fd6cd4fa05646369.bJQOKq3K6v5QByMX";
    private static final String AUTHORIZATION_HEADER = "Bearer " + apiKey;
    private static final String MODEL_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";


    public static Map<String, String> getResponse(String prompt, String model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", AUTHORIZATION_HEADER);

        // 创建消息对象
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        // 将消息对象放入列表中，以匹配服务器期望的ArrayList结构
        List<Map<String, Object>> messagesList = new ArrayList<>();
        messagesList.add(message);

        // 创建请求体的Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messagesList);

        // 使用Jackson库将Map转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(MODEL_URL, entity, String.class);

            HashMap<String, String> res = new HashMap<>();
            res.put("status", response.getStatusCode().toString());
            res.put("body", response.getBody());
            return res;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            HashMap<String, String> errorRes = new HashMap<>();
            errorRes.put("status", "error");
            errorRes.put("body", e.getMessage());
            return errorRes;
        }
    }

    public static String convertResponse(Map<String,String> res){
        String nres;
        String body = res.get("body");
        String status = res.get("status");
        if(!status.equals("200 OK")){
            nres = "error";
        }else{
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(body);
                nres = jsonNode.get("choices").get(0).get("message").get("content").asText();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return nres;
    }

}
