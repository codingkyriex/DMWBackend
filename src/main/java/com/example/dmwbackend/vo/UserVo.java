package com.example.dmwbackend.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Tavis
 * @date 2024-05-03
 * @desc 用户信息结果类
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserVo {

    private Integer userId;

    private String username;

    private String account;

    private Integer points;

    private String avatar;

    private String reviewStatus;

    private Date createTime;

    private String vipStatus;

    private Integer vipLevel;

}
