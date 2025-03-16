package com.chestshopaddon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.chestshopaddon.ChestShopAddon;

public class ShopListener implements Listener {
    private final ChestShopAddon plugin;

    public ShopListener(ChestShopAddon plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreShopCreate(PreShopCreationEvent event) {
        // Check shop limit
        int currentShops = plugin.getDatabaseManager().getPlayerShopCount(event.getPlayer().getName());
        int shopLimit = plugin.getShopLimitService().getPlayerShopLimit(event.getPlayer());

        if (shopLimit != -1 && currentShops >= shopLimit) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(plugin.getConfig().getString("messages.limit-reached")
                    .replace("%limit%", String.valueOf(shopLimit))
                    .replace("&", "§"));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onShopCreate(ShopCreatedEvent event) {
        // Convert item name to ItemStack
        String itemName = event.getSign().getLine(3);
        ItemStack item = plugin.getCustomItemService().parseItemString(itemName);

        // Add shop to database
        plugin.getDatabaseManager().addShop(
            event.getPlayer().getName(),
            event.getSign().getLocation(),
            item
        );

        // Create hologram
        plugin.getHologramService().createShopHologram(
            event.getSign().getLocation(),
            event.getPlayer().getName(),
            item
        );
    }

    @EventHandler
    public void onShopDestroy(ShopDestroyedEvent event) {
        // Remove from database
        plugin.getDatabaseManager().removeShop(event.getSign().getLocation());
        
        // Remove hologram
        plugin.getHologramService().removeHologram(event.getSign().getLocation());
    }
}