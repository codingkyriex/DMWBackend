package com.example.dmwbackend.config;

/**
 * @description: 常用返回值
 * @author: eric
 * @time: 2024/5/2 11:25
 */
public enum AppHttpCodeEnum {

    // 成功段固定为200
    SUCCESS(200, "操作成功"),
    // 找不到用户
    MISS_USER(400, "找不到用户"),
    MISS_ITEM(401, "找不到商品"),
    MISS_FAVOLIST(402, "找不到收藏夹"),
    // 登录段1~50
    NEED_LOGIN(1, "新用户登录"),
    LOGIN_PASSWORD_ERROR(2, "密码错误"),
    DUPLICATE_USER_NAME(3, "用户名已被占用"),
    // 基础功能50~100
    INSERT_ERROR(50, "插入错误"),
    UPDATE_ERROR(51, "更新错误"),
    // 逻辑错误 100~120
    LACK_OF_MONEY(100, "余额不足"),
    IS_FAVORITED(101, "已经收藏"),
    LACK_OF_ITEMS(102, "购买物品数超过库存"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500, "缺少参数"),
    PARAM_INVALID(501, "无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502, "图片格式有误"),
    SERVER_ERROR(503, "服务器内部错误"),
    // 数据错误 1000~2000
    DATA_EXIST(1000, "数据已经存在"),
    AP_USER_DATA_NOT_EXIST(1001, "ApUser数据不存在"),
    DATA_NOT_EXIST(1002, "数据不存在"),
    // 数据错误 3000~3500
    NO_OPERATOR_AUTH(3000, "无权限操作"),
    NEED_ADMIND(3001, "需要管理员权限"),

    // 不存在未学习单词
    NO_UNLEARNED_WORD(4000, "不存在未学习的单词");

    int code;
    String message;

    AppHttpCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

