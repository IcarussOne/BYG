---
title: Plants and Flowers
sidebar_position: 3
description: BYG's flowers, cacti, mushrooms, ground cover and other decorative plants, including which ones cause damage.
---

# Plants and Flowers

## Flowers

BYG adds a large number of decorative flowers, plus petal blocks (blue, light blue, purple, red, white, yellow) that double as dyes. All of it is under `flowersContentEnabled`. Flowers can be ground with a [Wooden Mortar](../items-and-food/misc-items.md#wooden-mortar) into vanilla dye.

## Cacti

| Plant | Behaviour |
|---|---|
| Sonoran Cactus (+ flowering) | A tall, growing cactus like vanilla's, in the Sonoran Desert and similar hot biomes. |
| Golden Spined Cactus | A single-block plant, generates in canyon and desert biomes. |
| Prickly Pear | A single-block plant, same biomes as the golden spined cactus. |
| Mini Cactus | A small single-block cactus that never grows. Nothing can be placed on top of it. |

All BYG cacti hurt entities that touch them, like vanilla cactus. Each has its own generation toggle (`golden_spined_cactus`, `prickly_pear`, `minicactus`) under [Worldgen Settings](../getting-started/configuration.md#worldgen-settings), and the whole group under `cactiContentEnabled`.

## Mushrooms and fungi

Black Puff, Shelf Fungi, Weeping Milk Cap and Wood Blewit are decorative mushrooms.

## Ground cover and hazards

`groundCoverContentEnabled` covers a set of small plants and debris: Algae, Blanket Weed, Ivy, Poison Ivy, Tiny Lilypad, Stone Pebbles, Stone Spikes, Thorn Block, Thorn Branches, Clover, and dead grass and leaf piles.

- **Algae** floats on the surface of still water and connects visually to neighbouring algae. It only survives on water and drops nothing unless broken with Silk Touch.
- **Ivy** hangs on the side of a block; **Poison Ivy** looks the same but, if `poisonIvyAppliesPoison` is on, poisons anything that touches it (Poison for 15 seconds, amplifier 1, both configurable).
- **Thorn Block** and **Thorn Branches** damage anything that touches them (`thornblockDamage` and `thornBranchesDamage`, 1.0 each by default). They generate together as a thorny undergrowth in the Ancient Forest.
- **Stone Pebbles** and **Stone Spikes** are small decorative stone clutter.

## Damage plants in general

Several plants share a generic damage setting, `damagingPlantDamage` (default 1.0), separate from the specific thorn and cactus damage settings above. All of these can be changed in [Block Settings](../getting-started/configuration.md#block-settings).
