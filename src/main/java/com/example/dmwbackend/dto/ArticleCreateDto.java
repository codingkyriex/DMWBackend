package com.example.dmwbackend.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/4 12:25
 */

@Data
public class ArticleCreateDto {
    String title;
    String content;
    private List<String> pictures;

}
