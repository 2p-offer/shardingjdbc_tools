package com.biligame.access.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.biligame.access.bean.DBInfo;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author cyy
 * @date 2021/8/30 15:56
 * @desc 循环查询的工具类 主要用来应对gm工具的复杂查询，比如：join
 */
public class BaseDataSourceUtil {

    /** 配置文件 */
    protected static Properties shardProperties;

    protected DataSource createDataSource(DBInfo dataBaseInfo) {
        DruidDataSource result = new DruidDataSource();
        result.setDriverClassName("com.mysql.jdbc.Driver");
        result.setDbType("com.alibaba.druid.pool.DruidDataSource");
        result.setUrl(dataBaseInfo.getUrl());
        result.setUsername(dataBaseInfo.getUsername());
        result.setPassword(dataBaseInfo.getPassword());
        String defaultAutoCommit = shardProperties.getProperty("defaultAutoCommit");
        result.setDefaultAutoCommit(StringUtils.isEmpty(defaultAutoCommit) || Boolean.parseBoolean(defaultAutoCommit));
        String initialSize = shardProperties.getProperty("initialSize");
        result.setInitialSize(!StringUtils.isEmpty(initialSize) ? Integer.parseInt(initialSize) : 3);
        String maxActive = shardProperties.getProperty("maxActive");
        result.setMaxActive(!StringUtils.isEmpty(maxActive) ? Integer.parseInt(maxActive) : 3);
//        result.setMaxIdle(120);
        String minIdle = shardProperties.getProperty("minIdle");
        result.setMinIdle(!StringUtils.isEmpty(minIdle) ? Integer.parseInt(minIdle) : 3);
//        result.setMaxWaitMillis(10000);
        result.setValidationQuery("SELECT 1");
        String validationQueryTimeout = shardProperties.getProperty("validationQueryTimeout");
        result.setValidationQueryTimeout(!StringUtils.isEmpty(validationQueryTimeout) ? Integer.parseInt(validationQueryTimeout) : 3);
        String testOnBorrow = shardProperties.getProperty("testOnBorrow");
        result.setTestOnBorrow(StringUtils.isEmpty(testOnBorrow) || Boolean.parseBoolean(testOnBorrow));
        String testWhileIdle = shardProperties.getProperty("testWhileIdle");
        result.setTestWhileIdle(StringUtils.isEmpty(testWhileIdle) || Boolean.parseBoolean(testWhileIdle));
        String timeBetweenEvictionRunsMillis = shardProperties.getProperty("timeBetweenEvictionRunsMillis");
        result.setTimeBetweenEvictionRunsMillis(!StringUtils.isEmpty(timeBetweenEvictionRunsMillis) ? Integer.parseInt(timeBetweenEvictionRunsMillis) : 10000);
        //result.setNumTestsPerEvictionRun(10);
        String minEvictableIdleTimeMillis = shardProperties.getProperty("minEvictableIdleTimeMillis");
        result.setMinEvictableIdleTimeMillis(!StringUtils.isEmpty(minEvictableIdleTimeMillis) ? Integer.parseInt(minEvictableIdleTimeMillis) : 120000);
        //result.setRemoveAbandonedOnBorrow(true);
        String removeAbandonedTimeout = shardProperties.getProperty("removeAbandonedTimeout");
        result.setRemoveAbandonedTimeout(!StringUtils.isEmpty(removeAbandonedTimeout) ? Integer.parseInt(removeAbandonedTimeout) : 120);
        String poolPreparedStatements = shardProperties.getProperty("poolPreparedStatements");
        result.setPoolPreparedStatements(StringUtils.isEmpty(poolPreparedStatements) || Boolean.parseBoolean(poolPreparedStatements));
        return result;
    }
}
