package com.chestshopaddon.services;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.chestshopaddon.ChestShopAddon;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;

/**
 * Service for managing shop limits with LuckPerms integration.
 * @since 1.0
 * @requires Java 16+
 */
public class ShopLimitService {
    private final ChestShopAddon plugin;
    private LuckPerms luckPerms;

    public ShopLimitService(ChestShopAddon plugin) {
        this.plugin = plugin;
        setupLuckPerms();
    }

    private void setupLuckPerms() {
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            RegisteredServiceProvider<LuckPerms> provider = plugin.getServer().getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                luckPerms = provider.getProvider();
                plugin.getLogger().info("Successfully hooked into LuckPerms!");
            }
        }
    }

    /**
     * Gets the shop limit for a player based on:
     * 1. Custom limit from config
     * 2. Direct permission (chestshop.limit.X)
     * 3. LuckPerms group limit
     * 4. Default limit
     *
     * @param player The player to check limits for
     * @return The maximum number of shops the player can have
     */
    public int getPlayerShopLimit(Player player) {
        // Check custom limit first
        String customLimitPath = "custom-limits." + player.getUniqueId().toString();
        if (plugin.getConfig().contains(customLimitPath)) {
            return plugin.getConfig().getInt(customLimitPath);
        }

        // Check for direct limit permission
        for (String permission : player.getEffectivePermissions().stream()
                .map(p -> p.getPermission())
                .filter(p -> p.startsWith("chestshop.limit."))
                .toList()) {
            try {
                return Integer.parseInt(permission.substring("chestshop.limit.".length()));
            } catch (NumberFormatException ignored) {}
        }

        // If LuckPerms is available, get the player's primary group
        if (luckPerms != null) {
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                String primaryGroup = user.getPrimaryGroup();
                return plugin.getConfigManager().getShopLimit(primaryGroup);
            }
        }

        // Fallback to default group limit
        return plugin.getConfigManager().getShopLimit("default");
    }

    public boolean hasLuckPerms() {
        return luckPerms != null;
    }
}
