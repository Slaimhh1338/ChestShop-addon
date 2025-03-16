package com.chestshopaddon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.chestshopaddon.ChestShopAddon;

public class HologramCommand implements CommandExecutor {
    private final ChestShopAddon plugin;

    public HologramCommand(ChestShopAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chestshop.addon.holograms")) {
            sender.sendMessage(plugin.getConfig().getString("messages.no-permission")
                    .replace("&", "§"));
            return true;
        }

        if (args.length != 1 || (!args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off"))) {
            sender.sendMessage("§cUsage: /shophologram <on|off>");
            return true;
        }

        boolean enable = args[0].equalsIgnoreCase("on");
        plugin.getConfig().set("holograms-enabled", enable);
        plugin.saveConfig();

        if (enable) {
            sender.sendMessage("§aShop holograms enabled!");
        } else {
            plugin.getHologramService().removeAllHolograms();
            sender.sendMessage("§cShop holograms disabled!");
        }

        return true;
    }
}
