/*
 * Copyright (c) 2024 Slaimhh1337
 * Licensed under Creative Commons Attribution-NoDerivatives 4.0 International
 * See LICENSE.md for full license text
 */

package com.chestshopaddon;

import com.chestshopaddon.commands.ShopsCommand;
import com.chestshopaddon.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShopAddon extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Initialize config
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Register commands
        getCommand("shops").setExecutor(new ShopsCommand(this));
        
        // Check if ChestShop is present
        if (getServer().getPluginManager().getPlugin("ChestShop") == null) {
            getLogger().severe("ChestShop not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        getLogger().info("ChestShop Addon has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestShop Addon has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
