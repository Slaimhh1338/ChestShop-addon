package com.chestshopaddon.services;

import com.chestshopaddon.ChestShopAddon;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class ShopLimitService {
    private final ChestShopAddon plugin;

    public ShopLimitService(ChestShopAddon plugin) {
        this.plugin = plugin;
    }

    public int getPlayerShopLimit(Player player) {
        // Check for direct limit permission first
        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            String permission = perm.getPermission();
            if (permission.startsWith("chestshop.limit.")) {
                try {
                    return Integer.parseInt(permission.substring("chestshop.limit.".length()));
                } catch (NumberFormatException ignored) {}
            }
        }

        // If no direct permission, check group limits from config
        return plugin.getConfigManager().getShopLimit(getPlayerGroup(player));
    }

    private String getPlayerGroup(Player player) {
        // Check primary group permission
        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            String permission = perm.getPermission();
            if (permission.startsWith("group.")) {
                return permission.substring("group.".length());
            }
        }
        return "default";
    }
}
