package com.biligame.access.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author cyy
 * @date 2021/8/30 19:33
 * @desc 工具类
 */
public class CommonUtil {

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 加载配置文件
     */
    public static Properties loadProperties() {

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("shard/shardConfig.properties");
        Properties shardProperties = new Properties();
        try {
            shardProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shardProperties;
    }
}
