package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.pojo.ArticleUrl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-03 11:11
 **/

@Mapper
public interface ArticleUrlMapper extends BaseMapper<ArticleUrl> {

    @Select("select * from article_urls where article_id = #{id}")
    List<ArticleUrl> getArticleUrl(@Param("id") Integer id);
}
