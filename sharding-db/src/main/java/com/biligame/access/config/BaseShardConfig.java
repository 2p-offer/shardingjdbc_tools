package com.biligame.access.config;

/**
 * @author cyy
 * @date 2021/8/27 14:44
 * @desc 分库分表的配置
 */
public class BaseShardConfig {

    /** 分表数量 */
    protected int shardTableNum = 100;
    /** 分片使用的表列名 */
    protected String shardColumn;
    /** 是否打印sql */
    protected String showSql;
    /** 逻辑库名 */
    protected String logicSchema;
    /** 分库数量 */
    protected int shardSchemaNum;

    public BaseShardConfig() {}

    public BaseShardConfig(int shardTableNum, String shardColumn, String showSql) {
        this.shardTableNum = shardTableNum;
        this.shardColumn = shardColumn;
        this.showSql = showSql;
    }

    public int getShardTableNum() {
        return shardTableNum;
    }

    public String getShardColumn() {
        return shardColumn;
    }

    public String getShowSql() {
        return showSql;
    }

    public String getLogicSchema() {
        return logicSchema;
    }

    public void setLogicSchema(String logicSchema) {
        this.logicSchema = logicSchema;
    }

    public int getShardSchemaNum() {
        return shardSchemaNum;
    }

    public void setShardSchemaNum(int shardSchemaNum) {
        this.shardSchemaNum = shardSchemaNum;
    }
}
