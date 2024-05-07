package com.example.dmwbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/6 8:57
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVo {
    private Integer articleId;

    private String title;

    private String userName;

    private String createTime;

    private String summary;

    private Integer numOfLikes;

}
