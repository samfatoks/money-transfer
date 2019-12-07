package com.demo.moneytransfer.dao;

import com.demo.moneytransfer.config.Configuration;
import com.demo.moneytransfer.config.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

public class DBHelper {

    private static Logger logger = LoggerFactory.getLogger(DBHelper.class);
    private HikariDataSource ds;
    @Inject
    public DBHelper(Configuration config) {
        DatabaseConfig dbConfig = config.getDatabase();
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(dbConfig.getUrl());
        hikariConfig.setUsername(dbConfig.getUser());
        hikariConfig.setPassword(dbConfig.getPassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(hikariConfig);
        setup();
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private void setup() {
        logger.info("Setting up table and populating with test data");
        Connection conn = null;
        try {
            conn = getConnection();
            RunScript.execute(conn, new FileReader("src/main/resources/test.sql"));
        } catch (SQLException e) {
            logger.error("Error populating test data: ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            logger.error("Error finding test script file ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
