package com.example.dmwbackend.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 21:37
 */
public class PromptGenerator {
    public static String getAiSentencePrompt(String word){
        return "请你根据以下单词：" + word + "编写一个英语例句（不需要翻译成中文），要求例句生动有意义，回答直接保留英语例句，不要有其他的内容";
    }
}
