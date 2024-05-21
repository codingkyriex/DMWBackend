package com.example.dmwbackend.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @description: 用户
 * @author: eric
 * @time: 2024/5/2 16:18
 */


@Data
@TableName("user")
public class User {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField(value = "username", fill = FieldFill.INSERT)
    private String username;

    @TableField(value = "password", fill = FieldFill.INSERT)
    private String password;

    @TableField(value = "points", fill = FieldFill.INSERT)
    private Integer points;

    @TableField(value = "avatar")
    private String avatar;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("vip_status")
    private String vipStatus;

    @TableField(value = "vip_level")
    private Integer vipLevel;

    @TableField(value = "progress")
    private Integer progress;

    @TableField(value = "state")
    private String state;

    @TableField(value = "book")
    private Integer book;

}
