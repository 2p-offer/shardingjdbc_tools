package com.biligame.access.utils;

import com.biligame.access.property.DataSourceProperty;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author cyy
 * @date 2021/8/27 11:12
 * @desc 数据库工具
 */
public class DBUtil {

    private static DBUtil instance = new DBUtil();

    private DBUtil() {
    }

    public static DBUtil getInstance() {
        return instance;
    }

    /**
     * 构建shard的数据源
     *
     * @param dataSourceProperty
     * @return
     */
    public DataSource buildShardDataSource(DataSourceProperty dataSourceProperty) {
        return ShardingSphereUtil.getInstance().buildDataSource(dataSourceProperty);
    }

    /**
     * 构建loop的数据源
     *
     * @param dataSourceProperty
     */
    public void buildLoopDataSource(DataSourceProperty dataSourceProperty, Properties properties) {
        LoopQueryUtil.getInstance().buildDataSource(dataSourceProperty, properties);
    }

    /**
     * 构建loop的数据源
     *
     * @param dataSourceProperty
     */
    public void buildLoopDataSource(DataSourceProperty dataSourceProperty) {
        LoopQueryUtil.getInstance().buildDataSource(dataSourceProperty, null);
    }

    /**
     * 取得可以执行的sql，以及对应的数据源
     *
     * @param sql 原始sql
     * @return
     */
    public Map<DataSource, List<String>> getExecuteData(String sql) {
        return LoopQueryUtil.getInstance().getExecuteData(sql);
    }
}
