package com.chestshopaddon.services;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemService {
    public boolean isCustomItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName() || meta.hasLore();
    }

    public String getItemDisplayName(ItemStack item) {
        if (item == null) return "AIR";
        if (!item.hasItemMeta()) return item.getType().name();
        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName() ? meta.getDisplayName() : item.getType().name();
    }

    public ItemStack parseItemString(String itemString) {
        try {
            String[] parts = itemString.split(":");
            Material material = Material.valueOf(parts[0]);
            ItemStack item = new ItemStack(material);
            
            if (parts.length > 1) {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(parts[1].replace('&', '§'));
                item.setItemMeta(meta);
            }
            
            return item;
        } catch (IllegalArgumentException e) {
            return new ItemStack(Material.AIR);
        }
    }
}
