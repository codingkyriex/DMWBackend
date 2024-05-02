package com.example.dmwbackend.pojo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 单词书本映射表
 * @author: eric
 * @time: 2024/5/2 16:24
 */


@Data
@TableName("word_vocabulary")
public class WordVocabulary implements Serializable {

    @TableId(value = "word_id")
    private Integer wordId;

    @TableId(value = "book_id")
    private Integer bookId;

}
