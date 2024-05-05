package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.UserWordProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-05 10:21
 **/

@Mapper
public interface UserWordProgressMapper extends BaseMapper<UserWordProgress> {

    @Select("select * from user_word_progress where user_id=#{id}")
    List<UserWordProgress> getProgressById(@Param("id") Integer id);
}
