package com.chestshopaddon.database;

import com.chestshopaddon.ChestShopAddon;
import org.bukkit.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")"
                );
                
                stmt.execute(
                    "CREATE INDEX IF NOT EXISTS idx_owner ON shops(owner)"
                );
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
        }
    }

    public void addShop(String owner, Location location, String item) {
        String sql = "INSERT INTO shops (owner, world, x, y, z, item) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, owner);
            pstmt.setString(2, location.getWorld().getName());
            pstmt.setInt(3, location.getBlockX());
            pstmt.setInt(4, location.getBlockY());
            pstmt.setInt(5, location.getBlockZ());
            pstmt.setString(6, item);
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