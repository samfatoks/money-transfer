package com.demo.moneytransfer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class ConfigManagerImpl implements ConfigManager {

    private Configuration configuration;
    private static Logger logger = LoggerFactory.getLogger(ConfigManagerImpl.class);

    public ConfigManagerImpl() {
        logger.info("ConfigManager initialized");

        InputStream yamlIS = ClassLoader.getSystemClassLoader().getResourceAsStream("config.yml");
        Yaml yaml = new Yaml();
        try {
            this.configuration = yaml.loadAs(yamlIS, Configuration.class );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
