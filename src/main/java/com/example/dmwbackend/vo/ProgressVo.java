package com.example.dmwbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tavis
 * @date 2024-05-21
 * @desc 获取当前学习进度接口结果类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressVo {
    private Integer progress;

    private Integer book;
}
