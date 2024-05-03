package com.example.dmwbackend.pojo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @description: 文章
 * @author: eric
 * @time: 2024/5/2 16:15
 */


@Data
@TableName(value = "articles")
public class Article {

    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    @TableField(value = "title", fill = FieldFill.INSERT)
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Integer userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "review_status", fill = FieldFill.INSERT)
    private String reviewStatus;

    @TableField(value = "summary", fill = FieldFill.INSERT)
    private String summary;

    @TableField(value = "num_of_likes", fill = FieldFill.INSERT)
    private String numOfLikes;




}