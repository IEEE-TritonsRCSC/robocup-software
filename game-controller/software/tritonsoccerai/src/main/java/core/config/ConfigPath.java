package main.java.core.config;

public enum ConfigPath {

    NETWORK_CONFIG("network_config.yaml", NetworkConfig.class),
    OBJECT_CONFIG("object_config.yaml", ObjectConfig.class),
    GAME_CONFIG("game_config.yaml", GameConfig.class),
    DISPLAY_CONFIG("display_config.yaml", DisplayConfig.class),
    AI_CONFIG("ai_config.yaml", AIConfig.class),
    ;

    private final String configPath;
    private final Class configClass;

    ConfigPath(String configPath, Class configClass) {
        this.configPath = "../../config/" + configPath;
        this.configClass = configClass;
    }

    public String getConfigPath() {
        return configPath;
    }

    public Class getConfigClass() {
        return configClass;
    }

}