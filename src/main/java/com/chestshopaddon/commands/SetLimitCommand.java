package com.chestshopaddon.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chestshopaddon.ChestShopAddon;

public class SetLimitCommand implements CommandExecutor {
    private final ChestShopAddon plugin;

    public SetLimitCommand(ChestShopAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chestshop.addon.setlimit")) {
            sender.sendMessage(plugin.getConfig().getString("messages.no-permission", "§cYou don't have permission!"));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cUsage: /shop setlimit <player> <limit>");
            return true;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        
        if (target == null) {
            sender.sendMessage(plugin.getConfig().getString("messages.player-not-found")
                    .replace("&", "§"));
            return true;
        }

        try {
            int newLimit = Integer.parseInt(args[1]);
            if (newLimit < -1) {
                sender.sendMessage("§cLimit cannot be less than -1 (-1 means unlimited)");
                return true;
            }

            // Save the custom limit to config
            String path = "custom-limits." + target.getUniqueId().toString();
            plugin.getConfig().set(path, newLimit);
            plugin.saveConfig();

            String message = plugin.getConfig().getString("messages.limit-set")
                    .replace("%player%", target.getName())
                    .replace("%limit%", String.valueOf(newLimit))
                    .replace("&", "§");
            sender.sendMessage(message);

            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number format!");
            return true;
        }
    }
}
