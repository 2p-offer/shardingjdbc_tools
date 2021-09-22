package com.biligame.access.utils;

import com.biligame.access.bean.MyDataSource;
import com.biligame.access.bean.DBInfo;
import com.biligame.access.property.DataSourceProperty;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cyy
 * @date 2021/8/30 15:56
 * @desc 循环查询的工具类 主要用来应对gm工具的复杂查询，比如：join
 */
public class LoopQueryUtil extends BaseDataSourceUtil {

    /**
     * 数据源集合
     */
    private List<MyDataSource> dataSourceList = new ArrayList(10);

    private static LoopQueryUtil instance = new LoopQueryUtil();

    public static LoopQueryUtil getInstance() {
        return instance;
    }

    public Map<DataSource, List<String>> getExecuteData(String sql) {
        Map<DataSource, List<String>> executeData = new ConcurrentHashMap<>(10);
        dataSourceList.parallelStream().forEach(myDataSource -> {
            List<String> result = new ArrayList<>();
            // 把table替换为分库分表后的表名
            String tableSql = sql.split("from")[1];
            String beforeTable = tableSql.split(" ")[1];
            if (sql.contains("JOIN")) {
                // 替换表连接后一个表名 表连接需要用大写JOIN 表名要后要跟空格加as 赋值别名
                String containTable = sql.split("JOIN")[1];
                String joinTable = containTable.split(" as")[0];
                Map<String, String[]> tableMap = getTables(beforeTable, joinTable, myDataSource.getId());
                String[] beforeTables = tableMap.get(beforeTable);
                String[] joinTables = tableMap.get(joinTable);
                for (int i = 0; i < beforeTables.length; i++) {
                    result.add(sql.replaceFirst(beforeTable, beforeTables[i]).replace(joinTable, joinTables[i]));
                }
            } else {
                String[] tables = getTables(beforeTable, myDataSource.getId());
                for (String afterTable : tables) {
                    result.add(sql.replace(beforeTable, afterTable));
                }
            }
            try {
                executeData.put(myDataSource.getDataSource(), result);
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        });
        return executeData;
    }

    public void buildDataSource(DataSourceProperty dataSourceProperty, Properties properties) {
        if (properties == null) {
            shardProperties = CommonUtil.loadProperties();
        } else {
            shardProperties = properties;
        }
        dataSourceList.clear();
        for (Map.Entry<String, DBInfo> dbInfo : dataSourceProperty.getDbInfoMap().entrySet()) {
            String dbInfoId = dbInfo.getKey().split("_")[1];
            if (!CommonUtil.isInteger(dbInfoId)) {
                continue;
            }
            dataSourceList.add(new MyDataSource(Integer.parseInt(dbInfoId), createDataSource(dbInfo.getValue())));
        }
    }

    /**
     * 单个查询拼接sql
     *
     * @param table
     * @param db
     * @return
     */
    public String[] getTables(String table, int db) {
        String[] tables = new String[0];
        int tableCount = Integer.parseInt(shardProperties.getProperty("shardTableNum"));
        if (dataSourceList.size() == 1) {
            tables = new String[1];
            tables[0] = table;
            return tables;
        } else if (dataSourceList.size() > 1) {
            Set<String> set = new HashSet<>();
            for (int i = 0; i < tableCount; i++) {
                if (i % dataSourceList.size() == db) {
                    int tableNum = i % tableCount;
                    set.add(table + "_" + (tableNum < 10 ? "0" + tableNum : String.valueOf(tableNum)));
                }
            }
            tables = new String[set.size()];
            set.toArray(tables);
        }
        return tables;
    }

    /**
     * 有子查询时拼接sql
     *
     * @param table
     * @param joinTable
     * @param db
     * @return
     */
    public Map<String, String[]> getTables(String table, String joinTable, int db) {
        String[] joinTables = new String[1];
        Map<String, String[]> result = new HashMap<>();
        String[] tables = new String[1];
        int tableCount = Integer.parseInt(shardProperties.getProperty("shardTableNum"));
        if (dataSourceList.size() == 1) {
            tables[0] = table;
            joinTables[0] = joinTable;
        } else if (dataSourceList.size() > 1) {
            List<String> set = new ArrayList<>();
            List<String> joinSet = new ArrayList<>();
            for (int i = 0; i < tableCount; i++) {
                if (i % dataSourceList.size() == db) {
                    int tableNum = i % tableCount;
                    set.add(table + "_" + (tableNum < 10 ? "0" + tableNum : String.valueOf(tableNum)));
                    joinSet.add(joinTable + "_" + (tableNum < 10 ? "0" + tableNum : String.valueOf(tableNum)));
                }
            }
            tables = new String[set.size()];
            joinTables = new String[joinSet.size()];
            set.toArray(tables);
            joinSet.toArray(joinTables);
        }
        result.put(table, tables);
        result.put(joinTable, joinTables);
        return result;
    }
}
