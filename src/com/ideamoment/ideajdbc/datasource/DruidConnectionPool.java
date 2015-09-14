/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;

import com.alibaba.druid.pool.DruidDataSource;
import com.ideamoment.ideajdbc.configuration.DataSourceConfig;


/**
 * @author Chinakite
 *
 */
public class DruidConnectionPool implements ConnectionPool {

    private String name;    //Channel名称
    private DruidDataSource druidDataSource;
    
    /**
     * 构造函数。在构造函数中根据DataSourceConfig中的信息设置Proxool连接池的对应配置。
     * 
     * @param channelName Channel名称
     * @param config 数据源配置信息
     */
    public DruidConnectionPool(String channelName, DataSourceConfig config) {
        name = channelName;
        try {
            Class.forName(config.getDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        Properties info = new Properties();
//        info.setProperty("proxool.maximum-connection-count", String.valueOf(config.getMaxConnections()));
//        info.setProperty("proxool.minimum-connection-count", String.valueOf(config.getMinConnections()));
//        info.setProperty("proxool.house-keeping-test-sql", config.getHeartbeatSql());
//        info.setProperty("proxool.house-keeping-sleep-time", String.valueOf(config.getHeartbeatSleepTime()));
//        info.setProperty("proxool.simultaneous-build-throttle", String.valueOf(config.getMaxConnections()));
//        info.setProperty("proxool.maximum-active-time", String.valueOf(config.getMaxActiveTime()));
//        info.setProperty("proxool.maximum-connection-lifetime", String.valueOf(config.getMaxConnectionLifetime()));
//        info.setProperty("user", config.getUsername());
//        info.setProperty("password", config.getPassword());
//        String alias = name;
//        proxoolUrl = "proxool." + alias + ":" + config.getDriver() + ":" + config.getUrl();
//        try {
//            ProxoolFacade.registerConnectionPool(proxoolUrl, info);
//        } catch (ProxoolException e) {
//            e.printStackTrace();
//        }
        druidDataSource = new DruidDataSource(); 
        druidDataSource.setDriverClassName(config.getDriver()); 
        druidDataSource.setUsername(config.getUsername()); 
        druidDataSource.setPassword(config.getPassword()); 
        druidDataSource.setUrl(config.getUrl()); 
        druidDataSource.setInitialSize(config.getMinConnections()); 
        druidDataSource.setMinIdle(config.getMinConnections()); 
        druidDataSource.setMaxActive(config.getMaxConnections()); // 启用监控统计功能  
        druidDataSource.setValidationQuery(config.getHeartbeatSql());
        druidDataSource.setMinEvictableIdleTimeMillis(config.getMaxConnectionLifetime());
        druidDataSource.setPoolPreparedStatements(false);
        try {
            druidDataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /* (non-Javadoc)
     * @see com.ideamoment.rainbow.datasource.ConnectionPool#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }

    /* (non-Javadoc)
     * @see com.ideamoment.rainbow.datasource.ConnectionPool#getConnection()
     */
    public Connection getConnection(String username, String password)
            throws SQLException {
        return druidDataSource.getConnection(username, password);
    }

}
