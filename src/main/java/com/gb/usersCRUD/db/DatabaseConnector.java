package com.gb.usersCRUD.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public DatabaseConnector(String propertiesFile) {
        Properties properties = loadProperties(propertiesFile);
        this.dbUrl = properties.getProperty("db.url");
        this.dbUser = properties.getProperty("db.user");
        this.dbPassword = properties.getProperty("db.password");
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private Properties loadProperties(String propertiesFile) {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (inputStream == null) {
                throw new IOException("Properties file not found.");
            }

            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties: " + e.getMessage(), e);
        }

        return properties;
    }
}
