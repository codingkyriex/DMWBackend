package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.ArticleCreateDto;
import com.example.dmwbackend.mapper.ArticleMapper;
import com.example.dmwbackend.mapper.ArticleUrlMapper;
import com.example.dmwbackend.mapper.FavoritesArticleMapper;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.pojo.ArticleUrl;
import com.example.dmwbackend.pojo.FavoritesArticle;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.service.UserService;
import com.example.dmwbackend.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Autowired
    FavoritesArticleMapper favoritesArticleMapper;

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
        if(favoritesArticleMapper.judgeLikeById(user.getUserId())==null){
            res.put("like",false);
        }else{
            res.put("like",true);
        }

        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult<Object> likeArticle(Integer id,Integer u) {
        Article article = articleMapper.selectById(id);
        if(userMapper.selectById(u)==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        FavoritesArticle favoritesArticle = new FavoritesArticle();
        favoritesArticle.setArticleId(article.getArticleId());
        favoritesArticle.setUserId(u);
        favoritesArticle.setFavoriteTime(new Date());
        favoritesArticleMapper.insert(favoritesArticle);
        if(article==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_ITEM);
        }
        article.setNumOfLikes(article.getNumOfLikes()+1);
        updateById(article);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<Object> createArticle(ArticleCreateDto dto,Integer u) {
        if(userMapper.selectById(u)==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        Article article = new Article();
        article.setUserId(u);
        article.setContent(dto.getContent());
        article.setCreateTime(new Date());
        // 初步测试都为已接收
        article.setReviewStatus("approved");
        article.setTitle(dto.getTitle());
        String strings = dto.getContent().length() > 40
                ? dto.getContent().substring(0, 40)
                : dto.getContent();
        article.setSummary(strings);
        article.setNumOfLikes(0);
        articleMapper.insert(article);
        for(String url : dto.getPictures()){
            ArticleUrl articleUrl = new ArticleUrl();
            articleUrl.setArticleId(article.getArticleId());
            articleUrl.setUrl(url);
            articleUrlMapper.insert(articleUrl);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<Object> getBestArticleUrl() {
        List<Article> articles = articleMapper.getTopThreeArticlesByLikes();
        HashMap<String, Object> res = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        List<ArticleUrl> articleUrls = articleUrlMapper.selectList(null);
        for(Article article:articles){
            List<ArticleUrl> articleUrl1 = articleUrlMapper.getArticleUrl(article.getArticleId());
            ArticleUrl articleUrl;
            if(articleUrl1.size()==0){
                Collections.shuffle(articleUrls);
                articleUrl = articleUrls.get(0);
            }else {
                articleUrl = articleUrl1.get(0);
            }
            list.add(articleUrl.getUrl());
        }
        res.put("url",list);
        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult<Object> searchArticleByTitle(String title){
        try {
            title = URLDecoder.decode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(title);
        List<Article> article = articleMapper.getArticleByTitle(title);
        ArrayList<ArticleVo> articleVos = new ArrayList<>();
        for(Article a:article){
            User user = userMapper.selectById(a.getUserId());
            if(user==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             articleVos.add(ArticleVo.builder()
                     .articleId(a.getArticleId())
                     .title(a.getTitle())
                     .createTime(sdf.format(a.getCreateTime()))
                     .summary(a.getSummary())
                     .numOfLikes(a.getNumOfLikes())
                     .userName(user.getUsername()).build());

        }
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult<Object> modifyArticle(ArticleCreateDto dto, Integer u) {

        return null;
    }


}
