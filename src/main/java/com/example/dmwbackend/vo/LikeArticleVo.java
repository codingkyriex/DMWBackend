package com.example.dmwbackend.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Tavis
 * @date 2024-05-07
 * @desc 用户喜欢的文章信息封装类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(pattern = "yyyy-mm-dd”, timezone = “GMT+8")
public class LikeArticleVo {
    private Integer articleId;

    private String title;

    private String summary;

    private String[] pictures;

    private String createTime;

    private AuthorVo author;
}
