# 🔨 Smith That!

**Smith That!** is a NeoForge 1.21.1 utility mod that dynamically expands the capabilities of the Vanilla Smithing Table. Instead of relying on rigid, hardcoded recipes, *Smith That!* uses a dynamic tagging system to allow seamless tool and armor upgrades across Vanilla and modded ecosystems!

## ➡️ Features
* **Universal Upgrades:** Upgrade your gear using the Vanilla Smithing Table without needing thousands of individual recipes.
* **Mod Compatibility:** Natively supports upgrading gear from massive content mods out of the box (e.g., The Twilight Forest, The Aether).
* **Dynamic Recipe Unrolling:** Calculates the output item dynamically based on what you place in the addition slot.

📖 Recipe Viewer Support
Smith That! features native compatibility with the "Big Three" recipe viewers. Blacklisted items are automatically hidden from the menus.

* [REI] Roughly Enough Items
* [JEI] Just Enough Items
* [EMI]

## ⚙️ Configuration
Server owners and modpack makers have full control over what can and cannot be upgraded.
Navigate to `config/smith_that-server.toml` to access the **Blacklist**. Any item ID added to this list will be strictly forbidden from being used as a base or an addition in the Smithing Table.

Example:
```toml
blacklist = ["minecraft:netherite_sword", "twilightforest:fiery_pickaxe"]
```

## 🛠️ Installation
* Install NeoForge 1.21.1.

* Drop the smith_that-[version].jar into your mods folder.

* (Optional but recommended) Install JEI, REI, or EMI to view the dynamic recipes in-game.

