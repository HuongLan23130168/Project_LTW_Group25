package com.example.project_ltw_25.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;

public abstract class BaseDao {

    Jdbi jdbi;
    protected Jdbi get() {
        if(jdbi == null) {
            connect();
        }
        return jdbi;
    }
    private void connect() {
        MysqlDataSource dataSource = new MysqlDataSource();
        System.out.println("jdbc:mysql://"+DBProperties.host+":"+DBProperties.port+"/"+DBProperties.dbname);
        dataSource.setURL("jdbc:mysql://"+DBProperties.host+":"+DBProperties.port+"/"+DBProperties.dbname);
        dataSource.setUser(DBProperties.username);
        dataSource.setPassword(DBProperties.pass);
        try{
            dataSource.setUseCompression(true);
            dataSource.setAutoReconnect(true);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jdbi = Jdbi.create(dataSource);
    }


}
