package com.neotech.timeprinter.database.impl;

import com.neotech.timeprinter.configuration.DataBaseProperties;
import com.neotech.timeprinter.database.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

@Service
public class ConnectionPoolImpl implements ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionPoolImpl.class);

    private final DataBaseProperties dataBaseProperties;
    private Connection connection;

    public ConnectionPoolImpl(DataBaseProperties dataBaseProperties) {
        this.dataBaseProperties = dataBaseProperties;
    }

    @Override
    public Connection getConnection() {
        while (true) {
            try {
                if (connection != null && connection.isValid(dataBaseProperties.getTimeout())) {
                    return connection;
                }
                tryToClose(connection);
                connection = DriverManager.getConnection(dataBaseProperties.getUrl(), dataBaseProperties.getUserName(), dataBaseProperties.getPassword());
                LOGGER.info("Connection established");
                return connection;
            } catch (SQLTimeoutException e) {
                LOGGER.error("Connection timeout exceeded. Try to reconnect", e);
                waitForTimeout();
            } catch (SQLException e) {
                LOGGER.error("Database is not reachable at the moment. Try to reconnect", e);
                waitForTimeout();
            }
        }
    }

    private void waitForTimeout() {
        try {
            Thread.sleep(dataBaseProperties.getReconnectionTime() * 1000);
        } catch (InterruptedException e) {
            LOGGER.error("Waiting interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private void tryToClose(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            connection = null;
            LOGGER.error("Failed to close connection", e);
        }
    }
}
