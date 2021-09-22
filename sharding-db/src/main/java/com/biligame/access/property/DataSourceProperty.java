package com.biligame.access.property;

import com.biligame.access.bean.DBInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author chaiyueying
 * @Date: 2019/10/15 14:50
 * @Description: 数据源配置信息
 */
public class DataSourceProperty {

    /** 分片的user表集合 */
    List<String> userShardTables = new ArrayList<>();
    /**
     * 数据库的配置信息
     */
    private HashMap<String, DBInfo> dbInfoMap = new HashMap<>();

    public DataSourceProperty() {}

    public DataSourceProperty(HashMap<String, DBInfo> dbInfoMap, List<String> userShardTables) {
        this.dbInfoMap = dbInfoMap;
        this.userShardTables = userShardTables;
    }

    public HashMap<String, DBInfo> getDbInfoMap() {
        return dbInfoMap;
    }

    public List<String> getUserShardTables() {
        return userShardTables;
    }

}
