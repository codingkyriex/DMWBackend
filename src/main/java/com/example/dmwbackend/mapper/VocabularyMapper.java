package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.Vocabulary;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tavis
 * @date 2024-05-25
 * @desc 单词表映射接口
 */
@Mapper
public interface VocabularyMapper extends BaseMapper<Vocabulary> {
}
