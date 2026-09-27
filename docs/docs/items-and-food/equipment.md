---
title: Equipment
sidebar_position: 2
description: The Kasai, Latharium, Pendorite and Tamrelite ores, and the armour, tools and set bonuses made from them.
---

# Equipment

BYG adds four ore materials. Each has its own ore, an armour set and a tool set. Each material can be turned off, along with its equipment, in the [content settings](../getting-started/configuration.md#ore-sets-and-equipment).

## Progression

1. **Tamrelite** ore needs a diamond-level pickaxe (harvest level 3). Its tools are the first step up.
2. **Pendorite** ore needs a level 4 pickaxe, which is what a **Tamrelite pickaxe** is.
3. **Kasai** and **Latharium** ore need a level 5 pickaxe, which is what a **Pendorite pickaxe** is.

## Ores

| Icon | Ore | Where it generates | Drops | Needs |
|:-:|---|---|---|---|
| <ItemIcon id="tamrelite_ore" name="Tamrelite Ore" source="blocks" size={32} inline /> | Tamrelite Ore | Overworld, in stone, Y 5 to 19, any biome. Veins of up to 4 blocks, 4 attempts per chunk. | Tamrelite Gem | Harvest level 3 |
| <ItemIcon id="pendorite_ore" name="Pendorite Ore" source="blocks" size={32} inline /> | Pendorite Ore | Overworld, in stone, Y 5 to 24, only in Ancient Forest, Skyris Highlands and Pine Mountains. Veins of up to 4, 4 attempts per chunk. | Pendorite Gem | Harvest level 4 |
| <ItemIcon id="latharium_ore" name="Latharium Ore" source="blocks" size={32} inline /> | Latharium Ore | Overworld, in stone, Y 1 to 34, only in the Enchanted Forest. Veins of up to 4, 4 attempts per chunk. | Latharium Gem | Harvest level 5 |
| <ItemIcon id="kasai_ore" name="Kasai Ore" source="blocks" size={32} inline /> | Kasai Ore | The Nether, in netherrack, Y 106 to 111. Veins of up to 6, 5 attempts per chunk. | Itself; smelted in a Nether Furnace into a Kasai Ingot | Harvest level 5 |

Nine gems (or ingots) craft into a storage block, and a block crafts back into nine gems.

The numbers of attempts and the height range of each ore are set in the [Worldgen Settings](../getting-started/configuration.md#worldgen-settings). Only chunks generated after the mod is installed contain the ores.

### Smelting Kasai ore

<ItemIcon id="kasai_ingot" name="Kasai Ingot" size={96} />

Kasai ore cannot go in a normal furnace. It is smelted in a **Nether Furnace**:

- **Craft it** with 8 red nether bricks around a furnace.
- **Light it** by placing fire on the block directly below it. It goes out again when the fire is gone.
- Put **Kasai Ore** in the input slot and **Blaze Powder** in the fuel slot. Each ore and blaze powder pair becomes one **Kasai Ingot**. A lit furnace works through one item every 10 ticks.

## Armour

The table shows the default stats. Every value can be changed in the [Equipment Settings](../getting-started/configuration.md#equipment-settings). For comparison, the vanilla diamond set has durability multiplier 33, protection 3/6/8/3 (boots, leggings, chestplate, helmet), enchantability 10 and toughness 2.

| Set | Durability multiplier | Boots | Leggings | Chestplate | Helmet | Total protection | Enchantability | Toughness |
|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| Kasai | 30 | 4 | 6 | 6 | 4 | 20 | 11 | 3.5 |
| Latharium | 24 | 3 | 5 | 5 | 3 | 16 | 16 | 2.0 |
| Pendorite | 25 | 2 | 2 | 4 | 2 | 10 | 15 | 1.0 |
| Tamrelite | 44 | 4 | 7 | 8 | 4 | 23 | 10 | 2.5 |

| Set | Helmet | Chestplate | Leggings | Boots |
|---|:-:|:-:|:-:|:-:|
| Kasai | <ItemIcon id="kasai_armour_helmet" name="Kasai Helmet" size={40} inline /> | <ItemIcon id="kasai_armour_body" name="Kasai Chainmail" size={40} inline /> | <ItemIcon id="kasai_armour_legs" name="Kasai Leggings" size={40} inline /> | <ItemIcon id="kasai_armour_boots" name="Kasai Boots" size={40} inline /> |
| Latharium | <ItemIcon id="latharium_armour_helmet" name="Latharium Helmet" size={40} inline /> | <ItemIcon id="latharium_armour_body" name="Latharium Chestplate" size={40} inline /> | <ItemIcon id="latharium_armour_legs" name="Latharium Leggings" size={40} inline /> | <ItemIcon id="latharium_armour_boots" name="Latharium Boots" size={40} inline /> |
| Pendorite | <ItemIcon id="pendorite_armour_helmet" name="Pendorite Helmet" size={40} inline /> | <ItemIcon id="pendorite_armour_body" name="Pendorite Chestplate" size={40} inline /> | <ItemIcon id="pendorite_armour_legs" name="Pendorite Leggings" size={40} inline /> | <ItemIcon id="pendorite_armour_boots" name="Pendorite Boots" size={40} inline /> |
| Tamrelite | <ItemIcon id="tamrelite_armour_helmet" name="Tamrelite Helmet" size={40} inline /> | <ItemIcon id="tamrelite_armour_body" name="Tamrelite Chestplate" size={40} inline /> | <ItemIcon id="tamrelite_armour_legs" name="Tamrelite Leggings" size={40} inline /> | <ItemIcon id="tamrelite_armour_boots" name="Tamrelite Boots" size={40} inline /> |

**Crafting.** Armour pieces need the same number of gems (or Kasai Chain Plating for Kasai) as vanilla armour needs of the base material, in the vanilla shapes:

| Piece | Gems / plating needed |
|---|:-:|
| Helmet | 5 |
| Chestplate | 8 |
| Leggings | 7 |
| Boots | 4 |

Kasai armour is crafted from **Kasai Chain Plating** (5 Kasai ingots give 2 plating). The Kasai helmet and boots are shapeless recipes, while the chestplate and leggings use vanilla shapes. The Latharium, Pendorite and Tamrelite pieces use their gems in the usual vanilla shapes.

### Set bonuses

- **Kasai, full set:** when you are set on fire, you get **Fire Resistance for 30 seconds**. After that the whole set is on a **5 minute cooldown**. It only triggers if you do not already have Fire Resistance.
- **Latharium, full set:** while you **sneak** you **levitate** (Levitation III, refreshed continuously), and you take no fall damage while doing so. White sparkles surround you.
- **Pendorite and Tamrelite** have no set bonus.

The bonuses can be changed or turned off in the [Equipment Settings](../getting-started/configuration.md#equipment-settings).

## Tools

Harvest level is the tool's mining tier (diamond is 3). Mining speed is the base speed on suitable blocks.

| Icon | Material | Tool | Harvest level | Durability | Mining speed | Enchantability |
|:-:|---|---|:-:|:-:|:-:|:-:|
| <ItemIcon id="kasai_pickaxe" name="Kasai Pickaxe" size={32} inline /> | Kasai | Pickaxe | 6 | 1800 | 10 | 10 |
| <ItemIcon id="kasai_shovel" name="Kasai Shovel" size={32} inline /> | Kasai | Shovel | 6 | 1800 | 12 | 10 |
| <ItemIcon id="kasai_sword" name="Kasai Sword" size={32} inline /> | Kasai | Sword | 1 | 1800 | 5 | 10 |
| <ItemIcon id="latharium_pickaxe" name="Latharium Pickaxe" size={32} inline /> | Latharium | Pickaxe | 7 | 950 | 9 | 8 |
| <ItemIcon id="latharium_axe" name="Latharium Axe" size={32} inline /> | Latharium | Axe | 2 | 950 | 9 | 8 |
| <ItemIcon id="latharium_battleaxe" name="Latharium Battleaxe" size={32} inline /> | Latharium | Battleaxe | 2 | 950 | 9 | 8 |
| <ItemIcon id="latharium_shovel" name="Latharium Shovel" size={32} inline /> | Latharium | Shovel | 2 | 950 | 9 | 8 |
| <ItemIcon id="latharium_hoe" name="Latharium Hoe" size={32} inline /> | Latharium | Hoe | 1 | 950 | 4 | 2 |
| <ItemIcon id="latharium_sword" name="Latharium Sword" size={32} inline /> | Latharium | Sword | 1 | 950 | 4 | 8 |
| <ItemIcon id="pendorite_pickaxe" name="Pendorite Pickaxe" size={32} inline /> | Pendorite | Pickaxe | 5 | 1200 | 12 | 15 |
| <ItemIcon id="pendorite_axe" name="Pendorite Axe" size={32} inline /> | Pendorite | Axe | 1 | 1200 | 12 | 15 |
| <ItemIcon id="pendorite_battleaxe" name="Pendorite Battleaxe" size={32} inline /> | Pendorite | Battleaxe | 1 | 1200 | 12 | 15 |
| <ItemIcon id="pendorite_shovel" name="Pendorite Shovel" size={32} inline /> | Pendorite | Shovel | 3 | 1200 | 12 | 15 |
| <ItemIcon id="pendorite_hoe" name="Pendorite Hoe" size={32} inline /> | Pendorite | Hoe | 1 | 1200 | 4 | 2 |
| <ItemIcon id="pendorite_sword" name="Pendorite Sword" size={32} inline /> | Pendorite | Sword | 1 | 1200 | 4 | 15 |
| <ItemIcon id="tamrelite_pickaxe" name="Tamrelite Pickaxe" size={32} inline /> | Tamrelite | Pickaxe | 4 | 750 | 5 | 8 |
| <ItemIcon id="tamrelite_axe" name="Tamrelite Axe" size={32} inline /> | Tamrelite | Axe | 1 | 750 | 5 | 8 |
| <ItemIcon id="tamrelite_battleaxe" name="Tamrelite Battleaxe" size={32} inline /> | Tamrelite | Battleaxe | 1 | 750 | 5 | 8 |
| <ItemIcon id="tamrelite_shovel" name="Tamrelite Shovel" size={32} inline /> | Tamrelite | Shovel | 2 | 750 | 6 | 8 |
| <ItemIcon id="tamrelite_hoe" name="Tamrelite Hoe" size={32} inline /> | Tamrelite | Hoe | 1 | 750 | 4 | 2 |
| <ItemIcon id="tamrelite_sword" name="Tamrelite Sword" size={32} inline /> | Tamrelite | Sword | 1 | 750 | 4 | 8 |

Kasai has no axe or hoe. The axes and battleaxes mine wood, plants and vines at the listed speed.

### Special hits

- **Kasai Sword:** sets the target on fire for 10 seconds.
- **Latharium Battleaxe:** makes the target **levitate** (Levitation III) for 2 seconds, with a burst of sparkles.

### Crafting

The sticks are part of what makes each material special:

| Icon | Material | Stick used |
|:-:|---|---|
| <ItemIcon id="stone_stick" name="Stone Stick" size={32} inline /> | Kasai | Stone stick |
| <ItemIcon id="enchanted_stick" name="Enchanted Stick" size={32} inline /> | Latharium | Enchanted stick (from enchanted planks) |
| <ItemIcon id="stone_stick" name="Stone Stick" size={32} inline /> | Pendorite | Stone stick |
| | Tamrelite | Ordinary wooden stick |

A **stone stick** is made from stone and cobblestone (shapeless, gives 4). An **enchanted stick** is made from 2 enchanted planks (gives 4). Pickaxes use 3 gems and 2 sticks, axes 3 gems and 2 sticks, shovels 1 gem and 2 sticks, swords 2 gems and 1 stick, and hoes 2 gems and 2 sticks. The Latharium Battleaxe uses 5 gems and 2 sticks. **No crafting recipe exists** for the Pendorite and Tamrelite battleaxes, or for Kasai axes and hoes (Kasai has none).
