package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.mapper.ArticleMapper;
import com.example.dmwbackend.mapper.ArticleUrlMapper;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.pojo.ArticleUrl;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 10:50
 */

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleUrlMapper articleUrlMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public ResponseResult<Object> getValidArticles() {
        List<Article> articles = articleMapper.getValidArticle();
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        for(Article article:articles){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",article.getArticleId());
            map.put("title",article.getTitle());
            map.put("summary",article.getSummary());
            List<ArticleUrl> articleUrl = articleUrlMapper.getArticleUrl(article.getArticleId());
            ArrayList<String> urls = new ArrayList<>();
            for(ArticleUrl url : articleUrl){
                urls.add(url.getUrl());
            }
            map.put("pictures",urls);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            map.put("create_time",sdf.format(article.getCreateTime()));
            User user = userMapper.getUserByArticleId(article.getUserId());
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("id",user.getUserId());
            map1.put("name",user.getUsername());
            map1.put("avatar",user.getAvatar());
            map.put("author",map1);
            res.add(map);
        }
        return ResponseResult.okResult(res);
    }


    @Override
    public ResponseResult<Object> getArticleDetail(Integer id) {
        Article article = articleMapper.selectById(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("id",article.getArticleId());
        res.put("title",article.getTitle());
        res.put("content",article.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        res.put("create_time",sdf.format(article.getCreateTime()));
        HashMap<String, Object> map1 = new HashMap<>();
        User user = userMapper.getUserByArticleId(article.getUserId());
        map1.put("id",user.getUserId());
        map1.put("name",user.getUsername());
        map1.put("avatar",user.getAvatar());
        res.put("author",map1);

        return ResponseResult.okResult(res);
    }
}
