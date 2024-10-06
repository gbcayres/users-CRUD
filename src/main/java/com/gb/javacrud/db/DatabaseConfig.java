package com.gb.javacrud.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public DatabaseConfig(String propertiesFile) {
        loadProperties(propertiesFile);
    }

    private void loadProperties(String propertiesFile) {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (inputStream != null) {
                properties.load(inputStream);
                this.dbUrl = properties.getProperty("db.url");
                this.dbUser = properties.getProperty("db.user");
                this.dbPassword = properties.getProperty("db.password");
            } else {
                throw new IOException("Properties file not found.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties: " + e.getMessage(), e);
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
