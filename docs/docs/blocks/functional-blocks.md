---
title: Functional Blocks
sidebar_position: 4
description: The Nether Furnace, storage Crate, Maple Tap and Bookshelves - BYG's blocks with their own behaviour.
---

# Functional Blocks

## Nether Furnace

A furnace for smelting [Kasai Ore](../items-and-food/equipment.md#smelting-kasai-ore). Craft it with 8 red nether bricks around a furnace.

- **Light it** by placing fire directly under it (`netherFurnaceIgnitesFromFireBelow`). It responds when the fire is placed and goes out if the fire below disappears.
- While lit, put **Kasai Ore** in the input slot and **Blaze Powder** in the fuel slot to convert them into Kasai Ingots, one pair every 10 ticks (`netherFurnaceLitTickRate`).

## Crate

A wooden storage container, similar to a chest, found as part of the Dead Sea Shipwreck structure with its own loot table. See [Structures](../structures.md#dead-sea-shipwreck) for its contents.

## Maple Tap

Hung on the "sap side" of a **Sappy Maple Log** - the log records which of its four sides is dripping, and a tap can only be placed on that side.

- The tap fills through **3 stages** (a full tap has stage 3), one stage every 200 ticks by default (`mapleTapStageTickRate`), so a full tap takes about 3× that.
- **Right-click with a glass bottle** at stage 3 to collect a **bottle of Maple Sap** and empty the tap.
- Each time sap is collected there is a chance (`mapleTapLogDepletionChance`, default 20%) that the **Sappy Maple Log turns into a plain Maple Log**, and the tap falls off.
- Maple Sap smelts into [Maple Syrup](../items-and-food/food.md#maple-syrup) in a furnace.

## Bookshelves

Every wood set with a bookshelf variant crafts it from 6 planks and 3 books in the vanilla bookshelf pattern. They give the same enchanting-power bonus as vanilla bookshelves when placed near an enchanting table.
