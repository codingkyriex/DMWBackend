package com.example.dmwbackend.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/5 11:01
 */
public class SortUtil {
    public static <T> T selectRandomWithBias(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        // 计算总权重
        double totalWeight = list.size();

        // 生成一个[0, totalWeight)之间的随机数
        Random random = new Random();
        double randomValue = random.nextDouble() * totalWeight;

        // 计算当前权重并选择元素
        double currentWeight = 0;
        for (T element : list) {
            currentWeight += 1; // 每个元素的权重为1，因此索引越小，权重越大
            if (randomValue < currentWeight) {
                return element;
            }
        }

        // 如果由于浮点数精度问题没有返回，返回列表中的最后一个元素
        return list.get(list.size() - 1);
    }

    public static <T> T selectRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        // 创建一个列表的副本，以避免修改原始列表
        List<T> shuffledList = new ArrayList<>(list);

        // 打乱列表
        Collections.shuffle(shuffledList);

        // 返回打乱后列表的第一个元素
        return shuffledList.get(0);
    }
}
