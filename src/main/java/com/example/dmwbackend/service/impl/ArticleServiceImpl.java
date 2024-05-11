package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.ArticleCreateDto;
import com.example.dmwbackend.dto.ArticleModifyDto;
import com.example.dmwbackend.mapper.ArticleMapper;
import com.example.dmwbackend.mapper.ArticleUrlMapper;
import com.example.dmwbackend.mapper.FavoritesArticleMapper;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.pojo.ArticleUrl;
import com.example.dmwbackend.pojo.FavoritesArticle;
import com.example.dmwbackend.pojo.User;
import com.example.dmwbackend.service.ArticleService;
import com.example.dmwbackend.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
        for (Article article : articles) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", article.getArticleId());
            map.put("title", article.getTitle());
            map.put("summary", article.getSummary());
            List<ArticleUrl> articleUrl = articleUrlMapper.getArticleUrl(article.getArticleId());
            ArrayList<String> urls = new ArrayList<>();
            for (ArticleUrl url : articleUrl) {
                urls.add(url.getUrl());
            }
            map.put("pictures", urls);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            map.put("create_time", sdf.format(article.getCreateTime()));
            User user = userMapper.getUserByArticleId(article.getUserId());
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("id", user.getUserId());
            map1.put("name", user.getUsername());
            map1.put("avatar", user.getAvatar());
            map.put("author", map1);
            res.add(map);
        }
        return ResponseResult.okResult(res);
    }


    @Override
    public ResponseResult<Object> getArticleDetail(Integer id) {
        Article article = articleMapper.selectById(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("id", article.getArticleId());
        res.put("title", article.getTitle());
        res.put("content", article.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        res.put("create_time", sdf.format(article.getCreateTime()));
        HashMap<String, Object> map1 = new HashMap<>();
        User user = userMapper.getUserByArticleId(article.getUserId());
        map1.put("id", user.getUserId());
        map1.put("name", user.getUsername());
        map1.put("avatar", user.getAvatar());
        res.put("author", map1);
        if (favoritesArticleMapper.judgeLikeByUserIdAndArticle(id,user.getUserId()) == null) {
            res.put("like", false);
        } else {
            res.put("like", true);
        }

        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult<Object> likeArticle(Integer id, Integer u) {
        Article article = articleMapper.selectById(id);
        if (userMapper.selectById(u) == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        FavoritesArticle like = favoritesArticleMapper.judgeLikeByUserIdAndArticle(id, u);
        if (like == null) {
            FavoritesArticle favoritesArticle = new FavoritesArticle();
            favoritesArticle.setArticleId(article.getArticleId());
            favoritesArticle.setUserId(u);
            favoritesArticle.setFavoriteTime(new Date());
            favoritesArticleMapper.insert(favoritesArticle);
        }
        if (article == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_ITEM);
        }
        article.setNumOfLikes(article.getNumOfLikes() + 1);
        updateById(article);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<Object> createArticle(ArticleCreateDto dto, Integer u) {
        User user = userMapper.selectById(u);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        if("read".equals(user.getState())){
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        Article article = new Article();
        article.setUserId(u);
        article.setContent(dto.getContent());
        article.setCreateTime(new Date());
        // 初步测试都为已接收
        article.setReviewStatus("pending");
        article.setTitle(dto.getTitle());
        String strings = dto.getContent().length() > 40
                ? dto.getContent().substring(0, 40)
                : dto.getContent();
        article.setSummary(strings);
        article.setNumOfLikes(0);
        articleMapper.insert(article);
        for (String url : dto.getPictures()) {
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
        for (Article article : articles) {
            List<ArticleUrl> articleUrl1 = articleUrlMapper.getArticleUrl(article.getArticleId());
            ArticleUrl articleUrl;
            if (articleUrl1.size() == 0) {
                Collections.shuffle(articleUrls);
                articleUrl = articleUrls.get(0);
            } else {
                articleUrl = articleUrl1.get(0);
            }
            list.add(articleUrl.getUrl());
        }
        res.put("url", list);
        return ResponseResult.okResult(res);
    }


    @Override
    public ResponseResult<Object> searchArticleByTitle(String title) {
        try {
            title = URLDecoder.decode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(title);
        List<Article> article = articleMapper.getArticleByTitle(title);
        ArrayList<ArticleVo> articleVos = new ArrayList<>();
        for (Article a : article) {
            User user = userMapper.selectById(a.getUserId());
            if (user == null) {
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
    public ResponseResult<Object> modifyArticle(ArticleModifyDto dto) {
        Article article = articleMapper.selectById(dto.getId());
        if (article == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_ITEM);
        }
        article.setContent(dto.getContent());
        article.setTitle(dto.getTitle());
        String strings = dto.getContent().length() > 40
                ? dto.getContent().substring(0, 40)
                : dto.getContent();
        article.setSummary(strings);
        articleMapper.updateById(article);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<Object> modifyArticle(ArticleModifyDto dto, Integer userId) {
        Article article = articleMapper.getArticleByTitleAndUser(dto.getTitle(), userId);
        if (article == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_ITEM);
        }
        User user = userMapper.selectById(userId);
        if("read".equals(user.getState())){
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        article.setContent(dto.getContent());
        article.setTitle(dto.getTitle());
        String strings = dto.getContent().length() > 40
                ? dto.getContent().substring(0, 40)
                : dto.getContent();
        article.setSummary(strings);
        articleMapper.updateById(article);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<Object> uploadImage(String base64String) {
        try {
            // 解码base64字符串
            byte[] imageBytes = Base64.getDecoder().decode(base64String.split(",")[1]);
            // 创建图片存储目录
            File imagesDir = new File("/home/ubuntu/swe/images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            // 创建图片文件
            String imageFileName = "image_" + System.currentTimeMillis() + ".png";
            File imageFile = new File(imagesDir, imageFileName);
            try (FileOutputStream out = new FileOutputStream(imageFile)) {
                out.write(imageBytes);
            }

            // 构建图片URL
            String imageUrl = "http://49.233.255.219" + "/images/" + imageFileName;
            HashMap<String, String> res = new HashMap<>();
            res.put("url", imageUrl);
            return ResponseResult.okResult(res);
        } catch (IOException e) {
            // 返回错误响应
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
    }

    @Override
    public ResponseResult<Object> getPagedArticles(Integer pageNum, Integer pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        List<Article> records = articleMapper.selectPage(page, null).getRecords();
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        for (Article article : records) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", article.getArticleId());
            map.put("title", article.getTitle());
            map.put("summary", article.getSummary());
            List<ArticleUrl> articleUrl = articleUrlMapper.getArticleUrl(article.getArticleId());
            ArrayList<String> urls = new ArrayList<>();
            for (ArticleUrl url : articleUrl) {
                urls.add(url.getUrl());
            }
            map.put("pictures", urls);
            map.put("status", article.getReviewStatus());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            map.put("createTime", sdf.format(article.getCreateTime()));
            User user = userMapper.getUserByArticleId(article.getUserId());
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("articleId", user.getUserId());
            map1.put("name", user.getUsername());
            map1.put("avatar", user.getAvatar());
            map.put("author", map1);
            res.add(map);
        }
        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult<Object> getPendingArticles(Integer pageNum,Integer pageSize) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("review_status", "pending"); // 假设"status"是您表中状态的字段名
        Page<Article> page = new Page<>(pageNum, pageSize);
        List<Article> records = articleMapper.selectPage(page, queryWrapper).getRecords();
        ArrayList<ArticleVo> res = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Article a:records){
            User user = userMapper.selectById(a.getUserId());
            res.add(ArticleVo.builder().articleId(a.getArticleId()).userName(user.getUsername()).summary(a.getSummary()).title(a.getTitle()).createTime(sdf.format(a.getCreateTime())).numOfLikes(a.getNumOfLikes()).build());
        }
        return ResponseResult.okResult(records);
    }

    @Override
    public ResponseResult<Object> reviewArticle(Integer id, Integer status) {
        Article article = articleMapper.selectById(id);
        if(article==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_ITEM);
        }
        article.setReviewStatus(status==0?"rejected":"approved");
        updateById(article);
        return ResponseResult.okResult(article.getArticleId());
    }

    @Override
    public ResponseResult<Object> getArticlesById(Integer id) {
        List<Article> article = articleMapper.getArticleByUserId(id);
        if(article==null){
            ResponseResult.errorResult(AppHttpCodeEnum.MISS_ITEM);
        }
        User user = userMapper.selectById(id);
        ArrayList<ArticleVo> articleVos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Article a:article){
            articleVos.add(ArticleVo.builder().articleId(a.getArticleId()).userName(user.getUsername()).summary(a.getSummary()).title(a.getTitle()).createTime(sdf.format(a.getCreateTime())).numOfLikes(a.getNumOfLikes()).build());
        }
        return ResponseResult.okResult(articleVos);
    }


}
