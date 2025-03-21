package com.chestshopaddon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.chestshopaddon.ChestShopAddon;
import com.chestshopaddon.services.CustomItemService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DatabaseManager {
    private final ChestShopAddon plugin;
    private Connection connection;

    public DatabaseManager(ChestShopAddon plugin) {
        this.plugin = plugin;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.SQLITE");
            connection = DriverManager.getConnection(
                "jdbc:sqlite:" + plugin.getDataFolder() + "/shops.db"
            );

            // Create tables if they don't exist
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(
                    "CREATE TABLE IF NOT EXISTS shops (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "owner VARCHAR(36) NOT NULL," +
                    "world VARCHAR(64) NOT NULL," +
                    "x INTEGER NOT NULL," +
                    "y INTEGER NOT NULL," +
                    "z INTEGER NOT NULL," +
                    "item VARCHAR(64) NOT NULL," +
                    "item_meta TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")"
                );
                
                // Add item_meta column if it doesn't exist
                try {
                    stmt.execute("ALTER TABLE shops ADD COLUMN item_meta TEXT");
                } catch (SQLException e) {
                    // Column probably already exists
                }

                stmt.execute(
                    "CREATE INDEX IF NOT EXISTS idx_owner ON shops(owner)"
                );
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
        }
    }

    public void addShop(String owner, Location location, ItemStack item) {
        String sql = "INSERT INTO shops (owner, world, x, y, z, item, item_meta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, owner);
            pstmt.setString(2, location.getWorld().getName());
            pstmt.setInt(3, location.getBlockX());
            pstmt.setInt(4, location.getBlockY());
            pstmt.setInt(5, location.getBlockZ());
            
            CustomItemService itemService = plugin.getCustomItemService();
            pstmt.setString(6, item.getType().name());
            pstmt.setString(7, itemService.serializeItem(item)); // Сохраняем полные NBT данные
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to add shop: " + e.getMessage());
        }
    }

    public void removeShop(Location location) {
        String sql = "DELETE FROM shops WHERE world = ? AND x = ? AND y = ? AND z = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, location.getWorld().getName());
            pstmt.setInt(2, location.getBlockX());
            pstmt.setInt(3, location.getBlockY());
            pstmt.setInt(4, location.getBlockZ());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to remove shop: " + e.getMessage());
        }
    }

    public List<ShopInfo> getPlayerShops(String owner) {
        List<ShopInfo> shops = new ArrayList<>();
        String sql = "SELECT * FROM shops WHERE owner = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, owner);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                shops.add(new ShopInfo(
                    rs.getString("owner"),
                    rs.getString("world"),
                    rs.getInt("x"),
                    rs.getInt("y"),
                    rs.getInt("z"),
                    rs.getString("item")
                ));
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to get shops: " + e.getMessage());
        }
        
        return shops;
    }

    public int getPlayerShopCount(String owner) {
        String sql = "SELECT COUNT(*) FROM shops WHERE owner = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, owner);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to count shops: " + e.getMessage());
        }
        
        return 0;
    }

    private String serializeItemMeta(ItemStack item) {
        if (!item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        Map<String, Object> serialized = new HashMap<>();
        
        if (meta.hasDisplayName()) {
            serialized.put("name", meta.getDisplayName());
        }
        
        if (meta.hasLore()) {
            serialized.put("lore", meta.getLore());
        }
        
        return new Gson().toJson(serialized);
    }

    private ItemStack deserializeItem(String type, String metaJson) {
        ItemStack item = new ItemStack(Material.valueOf(type));
        if (metaJson == null) return item;
        
        try {
            Map<String, Object> serialized = new Gson().fromJson(metaJson, 
                new TypeToken<Map<String, Object>>(){}.getType());
            
            ItemMeta meta = item.getItemMeta();
            
            if (serialized.containsKey("name")) {
                meta.setDisplayName((String) serialized.get("name"));
            }
            
            if (serialized.containsKey("lore")) {
                meta.setLore((List<String>) serialized.get("lore"));
            }
            
            item.setItemMeta(meta);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to deserialize item meta: " + e.getMessage());
        }
        
        return item;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Error closing database: " + e.getMessage());
        }
    }
}