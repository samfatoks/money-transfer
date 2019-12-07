package com.demo.moneytransfer.config;

import java.util.Map;


public final class Configuration {
    private JettyConfig server;
    private DatabaseConfig database;
    private Map< String, String > url;

    public JettyConfig getServer() {
        return server;
    }

    public void setServer(JettyConfig server) {
        this.server = server;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }

    public Map<String, String> getUrl() {
        return url;
    }

    public void setUrl(Map<String, String> url) {
        this.url = url;
    }
}