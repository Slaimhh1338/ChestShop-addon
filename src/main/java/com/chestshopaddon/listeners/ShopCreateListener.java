package com.chestshopaddon.listeners;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.chestshopaddon.ChestShopAddon;
import com.chestshopaddon.services.ShopLimitService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ShopCreateListener implements Listener {
    private final ChestShopAddon plugin;
    private final ShopLimitService shopLimitService;

    public ShopCreateListener(ChestShopAddon plugin) {
        this.plugin = plugin;
        this.shopLimitService = plugin.getShopLimitService();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreShopCreation(PreShopCreationEvent event) {
        Player player = event.getPlayer();
        
        // Skip if player has admin permission
        if (player.hasPermission("chestshop.addon.admin")) {
            return;
        }

        int currentShops = getCurrentShopCount(player);
        int shopLimit = shopLimitService.getPlayerShopLimit(player);

        // -1 means unlimited shops
        if (shopLimit != -1 && currentShops >= shopLimit) {
            event.setCancelled(true);
            player.sendMessage(plugin.getConfig().getString("messages.shop-limit-reached")
                    .replace("%limit%", String.valueOf(shopLimit))
                    .replace("&", "§"));
        }
    }

    private int getCurrentShopCount(Player player) {
        // TODO: Implement shop counting logic using ChestShop API
        return 0; // Placeholder
    }
}
