package com.example.dmwbackend.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tavis
 * @date 2024-05-04
 * @desc
 */

@Data
public class RegisterDto implements Serializable {
    String username;
    String password;
}