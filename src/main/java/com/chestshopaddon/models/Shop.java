package com.chestshopaddon.models;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Shop {
    private final String owner;
    private final Location location;
    private final ItemStack item;
    private final double buyPrice;
    private final double sellPrice;

    public Shop(String owner, Location location, ItemStack item, double buyPrice, double sellPrice) {
        this.owner = owner;
        this.location = location;
        this.item = item;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getOwner() { return owner; }
    public Location getLocation() { return location; }
    public ItemStack getItem() { return item; }
    public double getBuyPrice() { return buyPrice; }
    public double getSellPrice() { return sellPrice; }
}