package com.example.dmwbackend.pojo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 16:25
 */


@Data
@TableName("user_word_progress")
public class UserWordProgress implements Serializable {

    @TableField("word_id")
    private Integer wordId;

    @TableField("user_id")
    private Integer userId;

    @TableField("day")
    private Date day;

}
