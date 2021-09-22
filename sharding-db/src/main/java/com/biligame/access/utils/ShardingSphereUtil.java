package com.biligame.access.utils;

import com.biligame.access.algorithm.SchemaShardingAlgorithm;
import com.biligame.access.algorithm.TableShardingAlgorithm;
import com.biligame.access.bean.DBInfo;
import com.biligame.access.config.UserShardConfig;
import com.biligame.access.property.DataSourceProperty;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 分表分库的工具类
 *
 * @author chaiyueying
 * @Date: 2019/10/15 18:45
 */

public class ShardingSphereUtil extends BaseDataSourceUtil {

    /** 用户表-分库分表的配置 */
    private UserShardConfig userShardConfig;

    private static ShardingSphereUtil instance = new ShardingSphereUtil();
    /** 分片的user表集合 */
    private List<String> userShardTables = new ArrayList<>();

    private ShardingSphereUtil() {
        init();
    }

    public static ShardingSphereUtil getInstance() {
        return instance;
    }

    private void init() {
        shardProperties = CommonUtil.loadProperties();
        userShardConfig = new UserShardConfig(Integer.parseInt(shardProperties.getProperty("shardTableNum")),
                shardProperties.getProperty("shardColumn"), shardProperties.getProperty("showSql"));
    }

    public Map<String, DataSource> getDataSourceMap(DataSourceProperty dataSourceProperty) {
        Map<String, DataSource> dataSourceMap = new HashMap<>(10);
        // 添加数据库到map里
        HashMap<String, DBInfo> dbInfoMap = dataSourceProperty.getDbInfoMap();
        for (Map.Entry<String, DBInfo> dbInfo : dbInfoMap.entrySet()) {
            dataSourceMap.put(dbInfo.getKey(), createDataSource(dbInfo.getValue()));
        }
        userShardTables = dataSourceProperty.getUserShardTables();
        userShardConfig.setShardSchemaNum(dbInfoMap.size());
        userShardConfig.setLogicSchema(dbInfoMap.keySet().iterator().next().split("_")[0]);
        return dataSourceMap;
    }

    public DataSource buildDataSource(DataSourceProperty dataSourceProperty) {
        int shardSchemaNum = dataSourceProperty.getDbInfoMap().size();
        if(shardSchemaNum == 1){
            return createDataSource(dataSourceProperty.getDbInfoMap().values().iterator().next());
        }
        if( userShardConfig.getShardTableNum() % shardSchemaNum != 0 || (userShardConfig.getShardTableNum() / shardSchemaNum) % 2 != 0){
            throw new IllegalArgumentException("分表数量("+userShardConfig.getShardTableNum()+") ➗ 分库数量("+shardSchemaNum+") 必须是偶数！！！");
        }
        // 设置分库映射
        Map<String, DataSource> dataSourceMap = this.getDataSourceMap(dataSourceProperty);
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        // 配置默认数据库分片策略
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(userShardConfig.getShardColumn(), new SchemaShardingAlgorithm()));
        // 分别设置每个数据表的分表策略
        addSingleTableShading(shardingRuleConfig);
        Properties props = new Properties();
        props.put("sql.show", userShardConfig.getShowSql());
        try {
            return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addSingleTableShading(ShardingRuleConfiguration shardingRuleConfig) {
//        for (int i = 0; i < userShardTables.size(); i++) {
//            //默认表名
//            String logicTable = userShardTables.get(i);
//            //实际存储节点  ,号分割的两次拼接
//            String actualDataNodes = userShardConfig.getShardSchemaName() + "_${0..9}." + logicTable + "_${0..99}";
//            shardingRuleConfig.getTableRuleConfigs().add(userTableShardRule(logicTable, actualDataNodes));
//        }

        // 遍历全部需要分表分库的数据表
        for(int i=0; i < userShardTables.size(); i++){
            String logicTable = userShardTables.get(i);
            StringBuilder actualDataNodes = new StringBuilder();
            // 默认表名
            // 生成节点规则表达式
            for (int tableIndex = 0; tableIndex < userShardConfig.getShardTableNum(); tableIndex++){
                int dbIndex = tableIndex % userShardConfig.getShardSchemaNum();
                if(i < 10){
                    actualDataNodes.append(userShardConfig.getLogicSchema()).append("_0");
                }else{
                    actualDataNodes.append(userShardConfig.getLogicSchema()).append("_");
                }
                actualDataNodes.append(dbIndex).append(".");
                if(i < 10){
                    actualDataNodes.append(logicTable).append("_0");
                }else{
                    actualDataNodes.append(logicTable).append("_");
                }
                actualDataNodes.append(tableIndex);
                if(tableIndex < userShardConfig.getShardTableNum()){
                    actualDataNodes.append(",");
                }
            }
            shardingRuleConfig.getTableRuleConfigs().add(userTableShardRule(logicTable, actualDataNodes.toString()));
        }
    }

    private TableRuleConfiguration userTableShardRule(String logicTable, String actualDataNodes) {
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration(logicTable, actualDataNodes);
        //设置分片策略
        StandardShardingStrategyConfiguration standardShardingStrategyConfiguration = new StandardShardingStrategyConfiguration(userShardConfig.getShardColumn(), new TableShardingAlgorithm());
        tableRuleConfiguration.setTableShardingStrategyConfig(standardShardingStrategyConfiguration);
        return tableRuleConfiguration;
    }

    public List<String> getUserShardTables() {
        return userShardTables;
    }

    public UserShardConfig getUserShardConfig() {
        return userShardConfig;
    }
}
