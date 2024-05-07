package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.FavoritesArticle;
import com.example.dmwbackend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-03 21:55
 **/

@Mapper
public interface FavoritesArticleMapper extends BaseMapper<FavoritesArticle> {

    @Select("select * from favorites_article where user_id=#{id}")
    FavoritesArticle judgeLikeById(@Param("id") Integer id);

    @Select("select * from favorites_article where user_id=#{id} and article_id=#{aid}")
    FavoritesArticle judgeLikeByUserIdAndArticle(@Param("id") Integer id,@Param("aid") Integer aid);
}
