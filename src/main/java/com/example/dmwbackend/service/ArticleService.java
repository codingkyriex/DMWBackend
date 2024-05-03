package com.example.dmwbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.pojo.Article;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 20:31
 **/

@Service
public interface ArticleService extends IService<Article> {
    ResponseResult<Object> getValidArticles();
}
