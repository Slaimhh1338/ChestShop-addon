# ChestShop Addon

A Minecraft plugin addon for ChestShop that adds additional features.

## Features

- `/shops` command to view your own shops and their coordinates
- `/shops <player>` command for admins to view other players' shops
- Configurable shop limits per permission group
- Integration with ChestShop's sign system

## Installation

1. Make sure you have [ChestShop](https://www.spigotmc.org/resources/chestshop.51856/) installed
2. Download the latest release from the releases page
3. Place the jar file in your plugins folder
4. Restart your server

## Configuration

```yaml
# Default shop limits for permission groups
shop-limits:
  default: 5
  vip: 10
  mvp: 20
  admin: -1  # -1 means unlimited

# Messages
messages:
  no-permission: "&cYou don't have permission to use this command!"
  no-shops: "&cNo shops found."
  shops-header: "&6=== Shops owned by %player% ==="
  shop-format: "&7- Location: %world% %x%, %y%, %z% &8(&7Item: %item%&8)"
  player-not-found: "&cPlayer not found!"

## License

This project is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License.

This means you can:
- Download and use the plugin for free
- Share the plugin with others
- Use the plugin for any purpose, including commercial use

But you cannot:
- Modify the code and distribute the modified version without explicit permission from the owner

For more details, see the [LICENSE](LICENSE) file.