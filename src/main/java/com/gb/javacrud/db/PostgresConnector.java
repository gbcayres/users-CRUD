package com.gb.javacrud.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector implements IDatabaseConnector {
    private final DatabaseConfig config;

    public PostgresConnector(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
    }
}
