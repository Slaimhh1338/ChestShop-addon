package com.chestshopaddon.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final Plugin plugin;
    private final Map<String, Integer> shopLimits = new HashMap<>();
    
    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        
        // Load shop limits
        if (config.contains("shop-limits")) {
            for (String group : config.getConfigurationSection("shop-limits").getKeys(false)) {
                shopLimits.put(group, config.getInt("shop-limits." + group));
            }
        }
    }
    
    public int getShopLimit(String group) {
        return shopLimits.getOrDefault(group, shopLimits.getOrDefault("default", 5));
    }
}
