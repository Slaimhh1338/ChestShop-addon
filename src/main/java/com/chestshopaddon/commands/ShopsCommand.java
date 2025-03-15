package com.chestshopaddon.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.chestshopaddon.ChestShopAddon;
import com.chestshopaddon.database.ShopInfo;

public class ShopsCommand implements CommandExecutor {
    private final ChestShopAddon plugin;

    public ShopsCommand(ChestShopAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        String targetPlayer;
        if (args.length > 0 && sender.hasPermission("chestshop.addon.shops.others")) {
            targetPlayer = args[0];
        } else if (sender instanceof Player) {
            if (!sender.hasPermission("chestshop.addon.shops")) {
                sender.sendMessage(config.getString("messages.no-permission")
                        .replace("&", "§"));
                return true;
            }
            targetPlayer = sender.getName();
        } else {
            sender.sendMessage(config.getString("messages.player-only")
                    .replace("&", "§"));
            return true;
        }

        Player target = Bukkit.getPlayer(targetPlayer);
        if (target == null && !Bukkit.getOfflinePlayer(targetPlayer).hasPlayedBefore()) {
            sender.sendMessage(config.getString("messages.player-not-found")
                    .replace("&", "§"));
            return true;
        }

        List<ShopInfo> shops = plugin.getDatabaseManager().getPlayerShops(targetPlayer);
        
        if (shops.isEmpty()) {
            sender.sendMessage(config.getString("messages.no-shops")
                    .replace("&", "§"));
            return true;
        }

        sender.sendMessage(config.getString("messages.shops-header")
                .replace("%player%", targetPlayer)
                .replace("&", "§"));

        for (ShopInfo shop : shops) {
            sender.sendMessage(config.getString("messages.shop-format")
                    .replace("%world%", shop.getWorld())
                    .replace("%x%", String.valueOf(shop.getX()))
                    .replace("%y%", String.valueOf(shop.getY()))
                    .replace("%z%", String.valueOf(shop.getZ()))
                    .replace("%item%", shop.getItem())
                    .replace("&", "§"));
        }
        
        return true;
    }
}
