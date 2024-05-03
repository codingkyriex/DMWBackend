package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.Article;
import com.example.dmwbackend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 20:29
 **/


@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where user_id = #{id}")
    User getUserByArticleId(@Param("id") Integer id);

    @Select("select * from user where account = #{account}")
    User getUserByAccount(@Param("account") String account);
}
