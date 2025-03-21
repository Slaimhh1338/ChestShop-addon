package com.chestshopaddon.services;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.google.gson.Gson;

public class CustomItemService {
    private final Gson gson = new Gson();

    public boolean isCustomItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && (meta.hasDisplayName() || meta.hasLore());
    }

    public String serializeItem(ItemStack item) {
        if (item == null) return null;
        
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.toString();
    }

    public ItemStack deserializeItem(String serialized) {
        if (serialized == null) return new ItemStack(Material.AIR);
        
        try {
            NBTContainer container = new NBTContainer(serialized);
            return NBTItem.convertNBTtoItem(container);
        } catch (Exception e) {
            return new ItemStack(Material.AIR);
        }
    }

    public boolean isSameItem(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) return false;
        
        NBTItem nbt1 = new NBTItem(item1);
        NBTItem nbt2 = new NBTItem(item2);
        
        return nbt1.toString().equals(nbt2.toString());
    }

    public ItemStack parseItemString(String itemString) {
        try {
            String[] parts = itemString.split(":");
            
            // Parse basic material
            Material material = Material.valueOf(parts[0].toUpperCase());
            ItemStack item = new ItemStack(material);
            
            // Add custom name if provided
            if (parts.length > 1) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(parts[1].replace('&', '§'));
                    item.setItemMeta(meta);
                }
            }
            
            return item;
        } catch (IllegalArgumentException e) {
            return new ItemStack(Material.AIR);
        }
    }

    public String getItemDisplayName(ItemStack item) {
        if (item == null) return "AIR";
        if (!item.hasItemMeta()) return item.getType().name();
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName() ? meta.getDisplayName() : item.getType().name();
    }
}
