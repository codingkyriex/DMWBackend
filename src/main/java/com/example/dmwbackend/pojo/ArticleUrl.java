package com.example.dmwbackend.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 11:08
 */
@Data
@TableName(value = "article_urls")
public class ArticleUrl {

    @TableId(value = "url_id", type = IdType.AUTO)
    private Integer urlId;

    @TableField(value = "article_id", fill = FieldFill.INSERT)
    private Integer articleId;

    @TableField(value = "url", fill = FieldFill.INSERT)
    private String url;
}