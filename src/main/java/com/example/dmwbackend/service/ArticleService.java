package com.example.dmwbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.ArticleCreateDto;
import com.example.dmwbackend.dto.ArticleModifyDto;
import com.example.dmwbackend.pojo.Article;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 20:31
 **/

@Service
public interface ArticleService extends IService<Article> {
    ResponseResult<Object> getValidArticles();
    ResponseResult<Object> getArticleDetail(Integer id);

    ResponseResult<Object> likeArticle(Integer id,Integer u);

    ResponseResult<Object> createArticle(ArticleCreateDto dto,Integer u);

    ResponseResult<Object> getBestArticleUrl();

    ResponseResult<Object> searchArticleByTitle(String title) throws UnsupportedEncodingException;

    ResponseResult<Object> modifyArticle(ArticleModifyDto dto);

    ResponseResult<Object> modifyArticle(ArticleModifyDto dto,Integer userId);

    ResponseResult<Object> uploadImage(String base);

    ResponseResult<Object> getPagedArticles(Integer pageNum,Integer pageSize);

}
