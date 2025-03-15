package com.chestshopaddon.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ShopInfo {
    private final String owner;
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final String item;

    public ShopInfo(String owner, String world, int x, int y, int z, String item) {
        this.owner = owner;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = item;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public String getOwner() { return owner; }
    public String getWorld() { return world; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public String getItem() { return item; }
}
