package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: eric
 * @createTime: 2024-05-02 21:26
 **/

@Mapper
public interface WordMapper extends BaseMapper<Word> {

    //获取下一个要学习的单词（包含在word表中，但是不在user_word_progress表中的单词），使用差集查询
    @Select("SELECT * FROM word WHERE word_id NOT IN (SELECT word_id FROM user_word_progress WHERE user_id = #{userId}) ORDER BY RAND() LIMIT 1")
    Word getNextWord(Integer userId);

    // 从数据库中随机获取三个错误的选项（不能和原单词的翻译重复）
    @Select("SELECT chinese FROM word WHERE word_id != #{wordId} ORDER BY RAND() LIMIT 3")
    String[] getWrongChoices(Integer wordId);

}
