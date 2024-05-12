package com.example.dmwbackend.mapper;

import com.example.dmwbackend.pojo.FavoritesWord;
import com.example.dmwbackend.pojo.UserWordProgress;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-12 16:13
 **/

@Mapper
public interface FavoritesWordMapper {

    @Select("select * from favorites_word where user_id=#{id}")
    List<FavoritesWord> getLikedWords(@Param("id") Integer id);
}
