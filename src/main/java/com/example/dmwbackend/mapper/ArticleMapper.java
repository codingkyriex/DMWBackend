package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 16:29
 **/


@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select * from articles where review_status = 'approved'")
    List<Article> getValidArticle();



}
