---
title: Crystal Crawler
sidebar_position: 3
description: The Crystal Crawler - spawn biome, combat stats and behavior.
---

# Crystal Crawler

<BiomeScreenshot id="crystal_crawler" name="Crystal Crawler" />

*A spider species that adapted to the harsh conditions of the Crystal Canyons, and turned highly aggressive in the process.*

A hostile crawling mob, only if crystal content is enabled. See [Creature Settings](../getting-started/configuration.md#creature-settings) and the [Content Settings](../getting-started/configuration.md#content-settings) to adjust its spawn weight or disable it.

| Stat | Value |
|---|---|
| Spawn biome | Crystal Canyons |
| Spawn weight / group size | 40 / 1–3 |
| Health | 15 |
| Attack damage | 5 |
| Movement speed | 0.2 |
| Daylight despawn chance | ~1 in 1200 per check (skipped if a player is within 16 blocks) |
| Drops | 0–2 string (more with Looting); if killed by a player, 33% chance (boosted by Looting) of a spider eye |

- **Climbs and clings to walls and ceilings**, not just the floor, and can crawl from one surface onto another.
- **Chases down the nearest player** it can see and attacks in melee, sticking to its target for a while even after losing sight of them.
- **Despawns fairly quickly in daylight**, unless a player is standing close by.
