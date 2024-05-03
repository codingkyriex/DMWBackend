package com.example.dmwbackend.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 16:48
 */
public class HashUtil {
    public static String toHexString(byte[] bytes) {
        BigInteger number = new BigInteger(1, bytes);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, "0");
        }
        return hexString.toString();
    }

    public static String getHash(String input) {
        try {
            // 创建MessageDigest实例，指定SHA-256哈希函数
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 对输入数据进行哈希处理
            byte[] messageDigest = md.digest(input.getBytes());

            // 生成并返回十六进制字符串
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
