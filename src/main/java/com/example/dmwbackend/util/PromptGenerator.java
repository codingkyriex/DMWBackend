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

    public static String getCEGuessTest(String word){
        return "请你根据以下单词：" + word + "编写一个英语测试题目，题目给出这个单词的中文释义或者用法，让回答者选择一个单词来匹配，回答直接保留英语例题，不要有其他的内容，格式类似如下：q：苹果. option:A. B. C. D.";
    }

    public static String getDialoguePrompt(String lastDialog, boolean isFirst){
        if(isFirst){
            return "从现在起你是一个雅思口语的考官，名字叫Lee,全程使用英语，与你对话的人会回答你的英语问题，你可以在各个角度与对话人进行对话，除了对话内容不要有其他的额外内容，不需要回复我你可以做到等类似信息，现在开始第一句对话";
        }
        else {
            return "从现在起你是一个雅思口语的考官，全程使用英语，与你对话的人会回答你的英语问题，你可以在各个角度与对话人进行对话，除了对话内容不要有其他的额外内容，不需要回复我你可以做到等类似信息，我会给你提供前面一轮的对话，其中考官的话在前，回答者的话在后："+lastDialog;
        }
    }


}
