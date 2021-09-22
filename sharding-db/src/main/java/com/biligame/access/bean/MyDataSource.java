package com.biligame.access.bean;

import javax.sql.DataSource;

/**
 * @author cyy
 * @date 2021/8/30 17:54
 * @desc 自己包装的一层数据源
 */
public class MyDataSource {

    private int id;
    private DataSource dataSource;

    public MyDataSource(){}

    public MyDataSource(int id, DataSource dataSource) {
        this.id = id;
        this.dataSource = dataSource;
    }

    public int getId() {
        return id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
