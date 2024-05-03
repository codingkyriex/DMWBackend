package com.example.dmwbackend.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 16:29
 */

@Data
public class LoginDto implements Serializable {
    String username;
    String password;
}
