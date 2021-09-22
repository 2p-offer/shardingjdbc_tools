package com.biligame.access.bean;

/**
 * @author chaiyueying
 * @Date: 2019/10/15 14:50
 * @Description: 数据库配置
 */
public class DBInfo {

    private int id;
    /** 数据源别名 */
    private String dbName;
    /** url */
    private String url;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;

    public DBInfo() {
    }

    public DBInfo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
