# ChestShop Addon

A Minecraft plugin addon for ChestShop that adds additional features like shop limits, custom items support, and holographic displays.

## Features

### Shop Management
- `/shops` command to view your own shops and their coordinates
- `/shops <player>` command for admins to view other players' shops
- SQLite database for efficient shop storage and querying

### Shop Limits
- Configurable shop limits per permission group
- Support for LuckPerms groups
- Direct permission limits (`chestshop.limit.X`)
- Custom per-player limits via `/shop setlimit`
- `-1` for unlimited shops

### Custom Items
- Support for custom named items
- Custom item metadata storage
- Item display names in shop listings

### Holographic Displays
- Floating holograms above shops
- Shows shop owner and item being sold
- Toggle holograms with `/shophologram <on|off>`
- Automatic hologram cleanup

## Dependencies
- [ChestShop](https://www.spigotmc.org/resources/chestshop.51856/) (Required)
- [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays) (Required)
- [LuckPerms](https://luckperms.net/) (Optional, for group limits)

## Installation

1. Install required dependencies
2. Download the latest release
3. Place the jar in your plugins folder
4. Restart your server
5. Configure in `plugins/ChestShopAddon/config.yml`

## Permissions

```yaml
chestshop.addon.shops:
  description: Allows using /shops command for own shops
  default: true

chestshop.addon.shops.others:
  description: Allows viewing other players' shops
  default: op

chestshop.addon.admin:
  description: Gives access to all admin features
  default: op
  children:
    chestshop.addon.shops.others: true
    chestshop.addon.setlimit: true

chestshop.addon.setlimit:
  description: Allows setting shop limits for players
  default: op

chestshop.addon.holograms:
  description: Allows toggling shop holograms
  default: op
```

## Configuration

```yaml
# Shop limits per group
shop-limits:
  default: 5
  vip: 10
  mvp: 20
  admin: -1  # -1 means unlimited

# Custom player limits
custom-limits: {}

# Messages
messages:
  no-permission: "&cYou don't have permission to use this command!"
  no-shops: "&cNo shops found."
  shops-header: "&6=== Shops owned by %player% ==="
  shop-format: "&7- Location: %world% %x%, %y%, %z% &8(&7Item: %item%&8)"
  player-not-found: "&cPlayer not found!"
  shop-limit-reached: "&cYou have reached your shop limit! (%limit% shops)"
  shop-limit-set: "&aShop limit for %player% has been set to %limit%"
  limit-set: "&aShop limit for %player% has been set to %limit%"
  limit-reached: "&cYou have reached your shop limit! (%limit% shops)"
```

## License

This project is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License.

You can:
- Download and use the plugin for free
- Share the plugin with others
- Use the plugin for any purpose, including commercial use

You cannot:
- Modify the code and distribute the modified version without explicit permission from the owner

For more details, see the [LICENSE](LICENSE) file.