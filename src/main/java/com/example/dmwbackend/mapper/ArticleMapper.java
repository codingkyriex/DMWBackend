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

    @Select("SELECT * FROM articles WHERE review_status = 'approved' ORDER BY num_of_likes DESC LIMIT 3")
    List<Article> getTopThreeArticlesByLikes();

    //根据userId获取用户喜欢的文章
    @Select("SELECT * FROM favorites_article WHERE user_id = #{userId} ")
    List<Integer> getArticleIdByUserId(int userId);

    //根据userId获取用户发表的文章
    @Select("SELECT * FROM articles WHERE user_id = #{userId} ")
    List<Article> getArticleByUserId(int userId);
}
