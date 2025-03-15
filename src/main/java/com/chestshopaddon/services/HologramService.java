package com.chestshopaddon.services;

import com.chestshopaddon.ChestShopAddon;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HologramService {
    private final ChestShopAddon plugin;
    private final HolographicDisplaysAPI api;
    private final Map<Location, Hologram> holograms;

    public HologramService(ChestShopAddon plugin) {
        this.plugin = plugin;
        this.api = HolographicDisplaysAPI.get(plugin);
        this.holograms = new HashMap<>();
    }

    public void createShopHologram(Location location, String owner, ItemStack item) {
        // Remove existing hologram if present
        removeHologram(location);

        // Create new hologram slightly above the shop
        Location holoLoc = location.clone().add(0.5, 2.5, 0.5);
        Hologram hologram = api.createHologram(holoLoc);

        // Add hologram lines
        CustomItemService itemService = plugin.getCustomItemService();
        String itemName = itemService.getItemDisplayName(item);

        hologram.getLines().appendText("§6Shop by " + owner);
        hologram.getLines().appendText("§7Selling: §f" + itemName);
        
        // Store hologram reference
        holograms.put(location, hologram);
    }

    public void removeHologram(Location location) {
        Hologram hologram = holograms.remove(location);
        if (hologram != null) {
            hologram.delete();
        }
    }

    public void removeAllHolograms() {
        holograms.values().forEach(Hologram::delete);
        holograms.clear();
    }
}
