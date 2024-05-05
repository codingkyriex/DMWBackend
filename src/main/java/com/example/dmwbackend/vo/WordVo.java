package com.example.dmwbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tavis
 * @date 2024-05-05
 * @desc 下一个学习单词的封装类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordVo {

    private Integer id;

    private String word;

    private int progress;

    private String pronunciation;

    private String[] choice;

    private int answer;

}
