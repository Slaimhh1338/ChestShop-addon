package com.chestshopaddon.commands;

import com.Acrobot.ChestShop.Configuration.Properties;
import com.Acrobot.ChestShop.Database.Account;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.Acrobot.ChestShop.Utils.uBlock;
import com.chestshopaddon.ChestShopAddon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import com.Acrobot.ChestShop.Utils.uSign;

public class ShopsCommand implements CommandExecutor {
    private final ChestShopAddon plugin;

    public ShopsCommand(ChestShopAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(config.getString("messages.player-only", "§cThis command can only be used by players!"));
            return true;
        }

        String targetPlayer;
        if (args.length > 0 && sender.hasPermission("chestshop.addon.shops.others")) {
            targetPlayer = args[0];
        } else if (sender instanceof Player) {
            if (!sender.hasPermission("chestshop.addon.shops")) {
                sender.sendMessage(config.getString("messages.no-permission"));
                return true;
            }
            targetPlayer = sender.getName();
        } else {
            return false;
        }

        showPlayerShops(sender, targetPlayer);
        return true;
    }

    private void showPlayerShops(CommandSender sender, String playerName) {
        FileConfiguration config = plugin.getConfig();
        Player target = Bukkit.getPlayer(playerName);
        
        if (target == null && !hasPlayedBefore(playerName)) {
            sender.sendMessage(config.getString("messages.player-not-found")
                    .replace("&", "§"));
            return;
        }

        List<Location> shops = findPlayerShops(playerName);
        
        if (shops.isEmpty()) {
            sender.sendMessage(config.getString("messages.no-shops")
                    .replace("&", "§"));
            return;
        }

        sender.sendMessage(config.getString("messages.shops-header")
                .replace("%player%", playerName)
                .replace("&", "§"));

        for (Location loc : shops) {
            Sign sign = (Sign) loc.getBlock().getState();
            String item = ((Sign) sign).getLine(3); // Get item name from the 4th line
            
            sender.sendMessage(config.getString("messages.shop-format")
                    .replace("%world%", loc.getWorld().getName())
                    .replace("%x%", String.valueOf(loc.getBlockX()))
                    .replace("%y%", String.valueOf(loc.getBlockY()))
                    .replace("%z%", String.valueOf(loc.getBlockZ()))
                    .replace("%item%", item)
                    .replace("&", "§"));
        }
    }

    private boolean hasPlayedBefore(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).hasPlayedBefore();
    }

    private List<Location> findPlayerShops(String playerName) {
        List<Location> shops = new ArrayList<>();
        
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Sign sign : findShopSigns(world)) {
                if (ChestShopSign.isValid(sign) && 
                    sign.getLine(0).equalsIgnoreCase(playerName)) { // Get owner from the 1st line
                    shops.add(sign.getLocation());
                }
            }
        }
        
        return shops;
    }

    private List<Sign> findShopSigns(org.bukkit.World world) {
        List<Sign> signs = new ArrayList<>();
        
        for (org.bukkit.Chunk chunk : world.getLoadedChunks()) {
            for (org.bukkit.block.BlockState state : chunk.getTileEntities()) {
                if (state instanceof Sign && ChestShopSign.isValid((Sign) state)) {
                    signs.add((Sign) state);
                }
            }
        }
        
        return signs;
    }
}
