package com.example.dmwbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/22 20:20
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWordVo {
    Integer userId;
    List<Integer> forgetWord;
    List<Integer> knowWord;
    int total;
    int know;
}
