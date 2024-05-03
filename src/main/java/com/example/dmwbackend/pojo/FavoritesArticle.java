package com.example.dmwbackend.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 16:27
 */


@Data
@TableName("favorites_article")
public class FavoritesArticle implements Serializable {

    @TableId(value = "favorite_id", type = IdType.AUTO)
    private Integer favoriteId;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "article_id")
    private Integer articleId;

    @TableField(value = "favorite_time")
    private Date favoriteTime;

}
