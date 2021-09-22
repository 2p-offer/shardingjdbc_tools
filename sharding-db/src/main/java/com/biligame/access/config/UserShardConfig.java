package com.biligame.access.config;

/**
 * @author cyy
 * @date 2021/8/27 14:55
 * @desc 用户表-分库分表的配置
 */
public class UserShardConfig extends BaseShardConfig {

    public UserShardConfig() {}

    public UserShardConfig(int shardTableNum, String shardColumn, String showSql) {
        super(shardTableNum, shardColumn, showSql);
    }

    @Override
    public String toString() {
        return "UserShardProperty{" +
                ", shardTableNum=" + shardTableNum +
                ", shardColumn='" + shardColumn + '\'' +
                ", showSql='" + showSql + '\'' +
                '}';
    }
}
