package com.example.dmwbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dmwbackend.pojo.Vocabulary;
import com.example.dmwbackend.pojo.Word;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

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

    // 根据wordId和userId查询favoriites_word表中是否存在该单词
    @Select("SELECT COUNT(*) FROM favorites_word WHERE user_id = #{userId} AND word_id = #{wordId}")
    Integer isLiked(Integer userId, Integer wordId);

    // 向favorites_word表中插入一条记录（收藏单词）
    @Insert("INSERT INTO favorites_word (user_id, word_id, favorite_time) VALUES (#{userId}, #{wordId}, #{date})")
    Integer likeWord(Integer userId, Integer wordId, Date date);

    // 从user_word_progress表中获取所有学习状态为'forget'的单词
    @Select("SELECT * FROM word WHERE word_id IN (SELECT word_id FROM user_word_progress WHERE user_id = #{userId} AND status = 'forget')")
    List<Integer> getReviewWords(Integer userId);

    // 从word表中随机获取一个单词
    @Select("SELECT * FROM word ORDER BY RAND() LIMIT 1")
    Word getRandomWord();

    // 获取全部单词书
    @Select("SELECT * FROM vocabulary")
    List<Vocabulary> getBooks();
}
