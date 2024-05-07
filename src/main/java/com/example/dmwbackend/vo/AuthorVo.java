package com.example.dmwbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tavis
 * @date 2024-05-07
 * @desc 文章作者信息封装类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorVo {

    private Integer userId;

    private String username;

    private String avatar;

}
