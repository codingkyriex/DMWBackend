package com.example.dmwbackend.pojo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 16:26
 */


@Data
@TableName("favorites_word")
public class FavoritesWord implements Serializable {

    @TableId(value = "favorite_id", type = IdType.AUTO)
    private Integer favoriteId;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "word_id")
    private Integer wordId;

    @TableField(value = "favorite_time")
    private Date favoriteTime;

}