package com.example.dmwbackend.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 11:08
 */
@Data
public class ArticleUrl {

    @TableId(value = "url_id", type = IdType.AUTO)
    private Integer urlId;

    @TableField(value = "article_id", fill = FieldFill.INSERT)
    private Integer articleId;

    @TableField(value = "url", fill = FieldFill.INSERT)
    private String url;
}