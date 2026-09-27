---
title: Stone and Ores
sidebar_position: 1
description: Sepinite, soapstone, sodalite and scoria, and the four BYG crystal blocks - where they generate and how to craft their variants.
---

# Stone and Ores

The Kasai, Latharium, Pendorite and Tamrelite metal ores are covered on the [Equipment](../items-and-food/equipment.md#ores) page. This page covers the four decorative stones and the crystal blocks. All of them mine at pickaxe level 1, with hardness 1.5 and blast resistance 30, the same as vanilla stone.

## The four stones

| Icon | Stone | Where it generates | Content toggle |
|:-:|---|---|---|
| <ItemIcon id="sepinite" name="Sepinite" source="blocks" size={32} inline /> | Sepinite | Underground deposits in stone, Y 60–119 (configurable), only in **Dover Mountains**. 32 attempts per chunk by default. | `sepiniteContentEnabled` |
| <ItemIcon id="soapstone" name="Soapstone" source="blocks" size={32} inline /> | Soapstone | Underground deposits in stone, Y 16–21, any biome. 30 attempts per chunk. | `soapstoneContentEnabled` |
| <ItemIcon id="sodalite" name="Sodalite" source="blocks" size={32} inline /> | Sodalite | Surface veins in stone, Y 45–180, in desert, savanna and mesa-type biomes (including several vanilla ones). 30 attempts per chunk. | `sodaliteContentEnabled` |
| <ItemIcon id="scoria" name="Scoria" source="blocks" size={32} inline /> | Scoria | Underground deposits in stone, Y 1–14 (configurable), any biome. 30 attempts per chunk. | `scoriaContentEnabled` |

Attempt counts and, for Sepinite and Scoria, the Y range, are set in [Worldgen Settings](../getting-started/configuration.md#worldgen-settings).

### Variants and crafting

Each stone has raw, brick, tile, pillar, stairs and wall forms. Soapstone and Sodalite also have a "polished" form that stands in for bricks in their recipe chain.

| Result | Recipe |
|---|---|
| Bricks | 4 raw stone (shapeless), except Sodalite Bricks, which take 4 Sodalite Tile |
| Tile | 2 raw stone (shapeless), or for Soapstone 2 raw Soapstone |
| Polished (Soapstone, Sodalite only) | 4 raw stone (shapeless) |
| Pillars | 3 raw stone + 2 clay balls, shaped (gives 3); Soapstone and Sodalite pillars use the polished block instead of raw |
| Stairs | 6 bricks (or polished block), vanilla stair shape, gives 4 |
| Walls | 6 bricks (or polished block), vanilla wall shape, gives 6 |

Sepinite and Scoria have no polished block; their pillars, stairs and walls are built straight from bricks.

## Crystal blocks

Four glowing crystal blocks generate in **Crystal Canyons**, when crystal content is enabled: light blue, purple, red and white. Breaking one drops crystal items - 3 for light blue, purple and white, or 1 for red with a 50% chance of a second - and 4 crystals of the same color craft back into a block. See [Misc Items](../items-and-food/misc-items.md#crystals) for the item side.

| Light blue | Purple | Red | White |
|:-:|:-:|:-:|:-:|
| <ItemIcon id="light_blue_crystal_block" name="Light blue crystal block" source="blocks" size={40} inline /> | <ItemIcon id="purple_crystal_block" name="Purple crystal block" source="blocks" size={40} inline /> | <ItemIcon id="red_crystal_block" name="Red crystal block" source="blocks" size={40} inline /> | <ItemIcon id="white_crystal_block" name="White crystal block" source="blocks" size={40} inline /> |
