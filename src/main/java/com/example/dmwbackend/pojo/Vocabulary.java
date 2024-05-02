package com.example.dmwbackend.pojo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 16:23
 */

@Data
@TableName("vocabulary")
public class Vocabulary implements Serializable {

    @TableId(value = "book_id", type = IdType.AUTO)
    private Integer bookId;

    @TableField(value = "name", fill = FieldFill.INSERT)
    private String name;

    @TableField(value = "description")
    private String description;

    @TableField(value = "num_of_readers")
    private Integer numOfReaders;

}
