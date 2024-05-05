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

    public static String getSingleTestPrompt(String word){
        return "请你根据以下单词：" + word + "编写一个英语测试题目，题目给出这个单词的部分解释或者用法，让回答者选择一个单词来匹配，回答直接保留英语例题，不要有其他的内容，格式类似如下：q：a fruit. option:A. B. C. D.";
    }


}
