package windanesz.byg.worldgen;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class TemplateWorldgenRegistry {
    private static final TemplateWorldgenHelper.Config[] CONFIGS = new TemplateWorldgenHelper.Config[]{
            TemplateWorldgenHelper.config(3000,
                    "ancient_outpost",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(25000,
                    "ancient_tree10",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(45000,
                    "ancient_tree9",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(40000,
                    "ancient_tree11",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(300000,
                    "ancient_tree2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(700000,
                    "ancient_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(600000,
                    "ancient_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(350000,
                    "af_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(120000,
                    "ancient_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(80000,
                    "ancient_tree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(35000,
                    "ancient_tree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "ancient_tree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(70000,
                    "ancient_tree8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(90000,
                    "greatoak_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(60000,
                    "greatoak_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "baobab_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_baobab_savanna"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "baobab_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_baobab_savanna"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "baobab_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_baobab_savanna"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "baobabtree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_baobab_savanna"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(950000,
                    "bayou_ground_glow1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(990000,
                    "bayou_patch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.FLOWING_WATER.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.DIRT.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "bayoutree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou",
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "bayoutree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou",
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "bayoutree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou",
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "bayoutree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou",
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "bayoutree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou",
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "bayoutree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou",
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "bayoutree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "bayoutree8",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "bayoutreeglow1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "bayoutreeglow2",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_glowshroom_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "bayou_village",
                    -1,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(3500,
                    "bayou_witch_hut1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bluespruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bluespruce2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bluespruce3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bluespruce4",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bluespruce5",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bog_bush1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_bog",
                            "byg:byg_chaparral_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(140000,
                    "bog_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_bog"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(140000,
                    "bog_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_bog"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(140000,
                    "bog_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_bog"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(140000,
                    "bog_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_bog"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(140000,
                    "bog_tree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_bog"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(0,
                    "boreal_podzolpatch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(0,
                    "boreal_podzolpatch2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_boreal_forest",
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_lakes",
                            "byg:byg_boreal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(300000,
                    "borealtree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_maple_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "boulder1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_pine_mountains",
                            "byg:byg_grassland_plateau",
                            "byg:byg_snowy_pine_mountains",
                            "byg:byg_stone_brushlands",
                            "byg:byg_bluff_mountains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_woodlands",
                            "byg:byg_fungal_jungle",
                            "byg:byg_redwood_tropics",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_weeping_witch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_shrublands",
                            "byg:byg_woodlands",
                            "byg:byg_ancient_forest",
                            "byg:byg_fungal_jungle",
                            "byg:byg_redwood_tropics",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_weeping_witch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_ancient_forest",
                            "byg:byg_fungal_jungle",
                            "byg:byg_redwood_tropics",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_shrublands",
                            "byg:byg_weeping_witch_forest",
                            "byg:byg_lush_desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_woodlands",
                            "byg:byg_ancient_forest",
                            "byg:byg_fungal_jungle",
                            "byg:byg_redwood_tropics",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_shrublands",
                            "byg:byg_weeping_witch_forest",
                            "byg:byg_lush_desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "canyon_color_bottom1",
                    -5,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_colored_canyons"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.red_cracked_sand.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "canyon_color_bottom2",
                    -5,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_colored_canyons"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.red_cracked_sand.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "canyon_crystal_bottom1",
                    -5,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_crystal_canyons"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.cracked_sand.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "cattail",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "cattail",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "cattail",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "cattail",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "chaparral_stone1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_chaparral_lowlands",
                            "byg:byg_outback",
                            "byg:byg_stone_brushlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cikatree10",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cikatree11",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cikatree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "cikatree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "cikatree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "cikatree8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(792000,
                    "cikatree9",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_cika_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "plant_clover",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "plant_clover",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "plant_clover",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "con_moss1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_coniferous_forest",
                            "byg:byg_snowy_coniferous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "con_moss2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_coniferous_forest",
                            "byg:byg_snowy_coniferous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(4000,
                    "cypress_tower",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_cypress_swamplands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cypresstree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_cypress_swamplands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cypresstree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_cypress_swamplands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cypresstree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_cypress_swamplands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cypresstree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_cypress_swamplands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cypresstree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_cypress_swamplands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(720000,
                    "cypresstree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_cypress_swamplands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush_birch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush_birch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush_birch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "bush_birch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "deadleaf",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest",
                            "byg:byg_bog",
                            "byg:byg_seasonal_deciduous",
                            "byg:byg_seasonal_taiga",
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "deciduous_village",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "deciduous_village_seasonal",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dsbush1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dsbush2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dsbush3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(50000,
                    "ds_guardiancove1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_dead_sea"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(4000,
                    "ds_maraudersport1",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_dead_sea"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            // Keep all rock and cliff variants; the helper anchors them to the seabed.
            TemplateWorldgenHelper.config(59800, "ds_rock1", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(82225, "ds_rock2", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(82225, "ds_rock3", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(82225, "ds_rock4", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(82225, "ds_rock1", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(82225, "ds_rock2", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(82225, "ds_rock3", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(59800, "ds_rock2", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(59800, "ds_rock3", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(59800, "ds_rock4", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(104650, "ds_rock5", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(59800, "ds_rock1", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(59800, "ds_rock2", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(59800, "ds_rock3", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(82225, "ds_rock1", 0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY, new String[]{"byg:byg_dead_sea"},
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dstree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_deciduous"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "dtree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_deciduous_forest",
                            "byg:byg_snowy_deciduous_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "deadbush",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_dunes"
                    },
                    TemplateWorldgenHelper.state(Blocks.SAND.getStateFromMeta(0))),
            TemplateWorldgenHelper.config(100000,
                    "dead_grass",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_dunes"
                    },
                    TemplateWorldgenHelper.state(Blocks.SAND.getStateFromMeta(0))),
            TemplateWorldgenHelper.config(1000000,
                    "evergreen_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_evergreen_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "evergreen_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_evergreen_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(3500,
                    "farm_house1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_prairie"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains",
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower3",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower4",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower5",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower6",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower7",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower8",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "flower9",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_flowering_plains",
                            "byg:byg_ancient_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "flower_patch",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow",
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "flower_patch",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow",
                            "byg:byg_flowering_plains"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "fung_mush1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "fung_mush2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "fung_mush3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "borealtree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(120000,
                    "greatoak_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_oak_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(120000,
                    "greatoak_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_oak_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(120000,
                    "greatoak_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_oak_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(120000,
                    "greatoak_tree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_oak_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "great_oak_house1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_great_oak_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(20000,
                    "hotspring",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_stone_brushlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "ironwood2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_lush_desert"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.sandy_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "ironwood3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_lush_desert"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.sandy_grass.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "lake_village",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_great_lakes"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "leaf",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_woodlands",
                            "byg:byg_red_oak_forest",
                            "byg:byg_pine_mountains",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_evergreen_taiga",
                            "byg:byg_boreal_forest",
                            "byg:byg_whispering_woods",
                            "byg:byg_great_lakes",
                            "byg:byg_ebony_woods"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "lilypad",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_bayou",
                            "byg:byg_mangrove_marshes",
                            "byg:byg_quagmire",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "lilypad2",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_quagmire",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "lilypad",
                    0,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_quagmire",
                            "byg:byg_great_lakes",
                            "byg:byg_bayou",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_tropical_islands",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_fungal_jungle"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "mahoganytree_bird",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree_bird",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_tropical_islands",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_fungal_jungle"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "mahoganytree8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(100000,
                    "mahoganytree_bird",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_fungal_jungle",
                            "byg:byg_tropical_rainforest",
                            "byg:byg_tropical_islands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(600000,
                    "mead_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState())),
            TemplateWorldgenHelper.config(650000,
                    "mead_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState())),
            TemplateWorldgenHelper.config(850000,
                    "mead_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "mead_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_meadow"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.meadow_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "plant_mini_cactus",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_sonoran_desert"
                    },
                    TemplateWorldgenHelper.state(Blocks.SAND.getStateFromMeta(0))),
            TemplateWorldgenHelper.config(1000000,
                    "plant_mini_cactus_redsand",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_desert"
                    },
                    TemplateWorldgenHelper.state(Blocks.SAND.getStateFromMeta(1))),
            TemplateWorldgenHelper.config(50000,
                    "mob_snowman",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_frosty_forest",
                            "byg:byg_northern_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.SNOW_LAYER.getDefaultState())),
            TemplateWorldgenHelper.config(50000,
                    "mob_wolfpack",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_bluff_mountains",
                            "byg:byg_blue_taiga",
                            "byg:byg_seasonal_taiga",
                            "byg:byg_evergreen_taiga",
                            "byg:byg_coniferous_forest",
                            "byg:byg_pine_mountains",
                            "byg:byg_snowy_evergreen_taiga",
                            "byg:byg_weeping_witch_forest",
                            "byg:byg_seasonal_birch_forest",
                            "byg:byg_whispering_woods",
                            "byg:byg_dover_mountains",
                            "byg:byg_giant_blue_spruce_taiga",
                            "byg:byg_giant_seasonal_spruce_taiga",
                            "byg:byg_pine_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "northern_tree1",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_northern_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "northern_tree2",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_northern_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "northern_tree3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_northern_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "northern_tree4",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_northern_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(3000,
                    "oasis",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(990000,
                    "orch_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_orchard"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "orangespruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "orangespruce2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "orangespruce3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "orangespruce4",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "orangespruce5",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(990000,
                    "orch_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_orchard"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(990000,
                    "orch_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_orchard"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(990000,
                    "orch_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_orchard"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "orchard_village",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_orchard"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(45000,
                    "palo_verde3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_desert",
                            "byg:byg_outback",
                            "byg:byg_lush_desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.SAND.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.sandy_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "palo_verde3",
                    -1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_lush_desert"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.sandy_grass.getDefaultState())),
            TemplateWorldgenHelper.config(3000,
                    "pine_campsite",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_pine_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(3000,
                    "pine_house1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_pine_lowlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "quag_bush1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_quagmire"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "quag_village",
                    -1,
                    TemplateWorldgenHelper.HeightScan.AIR_ONLY,
                    new String[]{
                            "byg:byg_quagmire"
                    },
                    TemplateWorldgenHelper.block(Blocks.WATER.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "rainbow_tree2",
                    4,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "jungle_hills"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "rainbow_tree3",
                    4,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "jungle_hills"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "rainbow_tree3",
                    4,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "jungle_hills"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce4",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce5",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(3200,
                    "red_village1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_desert"
                    },
                    TemplateWorldgenHelper.state(Blocks.SAND.getStateFromMeta(1))),
            TemplateWorldgenHelper.config(1000000,
                    "reeds",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "reeds",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "reeds",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "reeds",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_mangrove_marshes",
                            "byg:byg_bayou",
                            "byg:byg_great_lakes",
                            "byg:byg_glowshroom_bayou",
                            "byg:byg_cypress_swamplands",
                            "byg:byg_marshlands"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.mud_block.getDefaultState()),
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "redoak_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_oak_forest",
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "redoak_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_oak_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "redoak_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_oak_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "redoak_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_oak_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "redoak_tree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_oak_forest",
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "redoak_tree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_oak_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "redoak_tree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_red_oak_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "rw_bush1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_redwood_tropics"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "rw_bush2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_redwood_tropics"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(9500,
                    "salem_village",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_whispering_woods"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "sc_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "sc_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "sc_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "sc_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(950000,
                    "sc_tree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(900000,
                    "sc_tree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(900000,
                    "sc_tree7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(900000,
                    "sc_tree8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(900000,
                    "sc_tree9",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_savanna_canopy"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch9",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch7",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch9",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "seasonbirch1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_birch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState()),
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_grass.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_10",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_11",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_12",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_13",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_14",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_15",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_16",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_17",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_18",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree5",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree6",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree8",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(150000,
                    "sf_tree9",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_seasonal_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "cact1",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_sonoran_desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.SAND.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "cact2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_sonoran_desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.SAND.getDefaultState())),
            TemplateWorldgenHelper.config(600000,
                    "cact3",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_sonoran_desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.SAND.getDefaultState())),
            TemplateWorldgenHelper.config(600000,
                    "cact4",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_sonoran_desert"
                    },
                    TemplateWorldgenHelper.block(Blocks.SAND.getDefaultState())),
            TemplateWorldgenHelper.config(40000,
                    "stone_spike2",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_pine_mountains",
                            "byg:byg_chaparral_lowlands",
                            "byg:byg_grassland_plateau",
                            "byg:byg_stone_brushlands",
                            "byg:byg_bluff_mountains"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "spruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_dover_mountains",
                            "byg:byg_giant_snowy_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "spruce2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_dover_mountains",
                            "byg:byg_giant_snowy_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "spruce3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_dover_mountains",
                            "byg:byg_giant_snowy_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "spruce4",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_giant_blue_spruce_taiga",
                            "byg:byg_giant_snowy_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "spruce5",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_dover_mountains",
                            "byg:byg_giant_blue_spruce_taiga",
                            "byg:byg_giant_snowy_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "datura",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_stellata_pasture"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "tbluespruce1",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "tbluespruce2",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "tbluespruce3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_blue_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "torangespruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "torangespruce2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "torangespruce3",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "redspruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(0,
                    "tspruce3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_blue_taiga",
                            "byg:byg_dover_mountains",
                            "byg:byg_giant_snowy_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "tyellowspruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "tyellowspruce2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "tyellowspruce3",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(4000,
                    "weepingwitchhut",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_weeping_witch_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(200000,
                    "weepingtree_fallen",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_weeping_witch_forest"
                    },
                    TemplateWorldgenHelper.block(windanesz.byg.registry.ModBlocks.peat_dirt.getDefaultState())),
            TemplateWorldgenHelper.config(800000,
                    "woodland_log1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_woodlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(300000,
                    "woodland_log2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_woodlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "woodland_log3",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_woodlands"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "yellowspruce1",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "yellowspruce2",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "yellowspruce3",
                    2,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "yellowspruce4",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(500000,
                    "yellowspruce5",
                    1,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_giant_seasonal_spruce_taiga"
                    },
                    TemplateWorldgenHelper.block(Blocks.GRASS.getDefaultState())),
            TemplateWorldgenHelper.config(1000000,
                    "kovan",
                    0,
                    TemplateWorldgenHelper.HeightScan.REPLACEABLE,
                    new String[]{
                            "byg:byg_zelkova_forest"
                    },
                    TemplateWorldgenHelper.block(Blocks.TALLGRASS.getDefaultState()))
    };

    private static final Map<ResourceLocation, List<TemplateWorldgenHelper.Config>> CONFIGS_BY_BIOME = indexByBiome();

    /**
     * This is a helper method to index the configs by biome for faster lookup during world generation.
     */
    private static Map<ResourceLocation, List<TemplateWorldgenHelper.Config>> indexByBiome() {
        Map<ResourceLocation, List<TemplateWorldgenHelper.Config>> index = new HashMap<>();
        for (TemplateWorldgenHelper.Config config : CONFIGS) {
            for (ResourceLocation biomeId : config.biomeIds()) {
                List<TemplateWorldgenHelper.Config> configs = index.computeIfAbsent(biomeId, ignored -> new ArrayList<>());
                if (!configs.contains(config)) {
                    configs.add(config);
                }
            }
        }
        return index;
    }

    private TemplateWorldgenRegistry() {
    }

    /**
     * Generates all configured templates for the given chunk and world.
     */
    public static void generateAll(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (dimID != 0 || world.isRemote) {
            return;
        }
        ResourceLocation biomeId = Biome.REGISTRY.getNameForObject(world.getBiome(new BlockPos(chunkX, 128, chunkZ)));
        List<TemplateWorldgenHelper.Config> configs = CONFIGS_BY_BIOME.get(biomeId);
        if (configs != null) {
            for (TemplateWorldgenHelper.Config config : configs) {
                TemplateWorldgenHelper.generate(random, chunkX, chunkZ, world, dimID, biomeId, config);
            }
        }
    }
}
