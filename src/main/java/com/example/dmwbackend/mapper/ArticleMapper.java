package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 16:29
 **/


@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
