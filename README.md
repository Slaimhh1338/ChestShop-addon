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

# Messages can be configured in [config.yml]