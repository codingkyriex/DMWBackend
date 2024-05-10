package com.example.dmwbackend.dto;

import lombok.Data;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/10 16:03
 */

@Data
public class AiContactDto {
    String LLMReply;
    String userReply;
    Boolean isFirst;
}
