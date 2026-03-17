package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private final Properties properties = new Properties();

    public ConfigReader(String env) {
        String fileName = "config-" + env + ".properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find config file: " + fileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config file: " + fileName, e);
        }
    }

    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public String getUsername() {
        return properties.getProperty("username");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public String getInvalidUsername() {
        return properties.getProperty("invalid.username");
    }

    public String getInvalidPassword() {
        return properties.getProperty("invalid.password");
    }

    public String getLockedUsername() {
        return properties.getProperty("locked.username");
    }

    public String getBlankValue() {
        return properties.getProperty("blank.value", "");
    }
}