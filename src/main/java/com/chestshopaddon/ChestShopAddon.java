/*
 * Copyright (c) 2025 Slaimhh1337
 * Licensed under Creative Commons Attribution-NoDerivatives 4.0 International
 * See LICENSE for full license text
 */

package com.chestshopaddon;

import org.bukkit.plugin.java.JavaPlugin;

import com.chestshopaddon.commands.SetLimitCommand;
import com.chestshopaddon.commands.ShopsCommand;
import com.chestshopaddon.config.ConfigManager;
import com.chestshopaddon.database.DatabaseManager;
import com.chestshopaddon.services.CustomItemService;
import com.chestshopaddon.services.ShopLimitService;

public class ChestShopAddon extends JavaPlugin {
    private ConfigManager configManager;
    private ShopLimitService shopLimitService;
    private DatabaseManager databaseManager;
    private CustomItemService customItemService;

    @Override
    public void onEnable() {
        // Initialize config
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Initialize services
        this.shopLimitService = new ShopLimitService(this);
        this.databaseManager = new DatabaseManager(this);
        this.customItemService = new CustomItemService();
        
        // Register commands
        getCommand("shops").setExecutor(new ShopsCommand(this));
        getCommand("shop").setExecutor(new SetLimitCommand(this));
        
        // Check dependencies
        if (getServer().getPluginManager().getPlugin("ChestShop") == null) {
            getLogger().severe("ChestShop not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        getLogger().info("ChestShop Addon has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("ChestShop Addon has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ShopLimitService getShopLimitService() {
        return shopLimitService;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public CustomItemService getCustomItemService() {
        return customItemService;
    }
}
