package com.example.dmwbackend.pojo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 16:22
 */

@Data
@TableName("word")
public class Word implements Serializable {

    @TableId(value = "word_id", type = IdType.AUTO)
    private Integer wordId;

    @TableField(value = "english", fill = FieldFill.INSERT)
    private String english;

    @TableField(value = "chinese", fill = FieldFill.INSERT)
    private String chinese;

    @TableField(value = "property", fill = FieldFill.INSERT)
    private String property;

    @TableField(value = "example")
    private String example;

}
