package windanesz.byg.registry;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.Config;
import windanesz.byg.biome.*;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@ObjectHolder(BiomesYouGo.MODID)
@Mod.EventBusSubscriber
public final class ModBiomes {
    public static final Biome byg_allium_fields = placeholder();
    public static final Biome byg_alps = placeholder();
    public static final Biome byg_amaranth_fields = placeholder();
    public static final Biome byg_ancient_forest = placeholder();
    public static final Biome byg_aspen_forest = placeholder();
    public static final Biome byg_baobab_savanna = placeholder();
    public static final Biome byg_bayou = placeholder();
    public static final Biome byg_blue_taiga = placeholder();
    public static final Biome byg_bluff_mountains = placeholder();
    public static final Biome byg_bog = placeholder();
    public static final Biome byg_boreal_forest = placeholder();
    public static final Biome byg_chaparral_lowlands = placeholder();
    public static final Biome byg_cherry_grove = placeholder();
    public static final Biome byg_cika_forest = placeholder();
    public static final Biome byg_colored_canyons = placeholder();
    public static final Biome byg_coniferous_forest = placeholder();
    public static final Biome byg_crystal_canyons = placeholder();
    public static final Biome byg_cypress_swamplands = placeholder();
    public static final Biome byg_dead_sea = placeholder();
    public static final Biome byg_deciduous_forest = placeholder();
    public static final Biome byg_dover_mountains = placeholder();
    public static final Biome byg_dunes = placeholder();
    public static final Biome byg_ebony_woods = placeholder();
    public static final Biome byg_enchanted_forest = placeholder();
    public static final Biome byg_evergreen_taiga = placeholder();
    public static final Biome byg_flowering_plains = placeholder();
    public static final Biome byg_frosty_forest = placeholder();
    public static final Biome byg_fungal_jungle = placeholder();
    public static final Biome byg_giant_blue_spruce_taiga = placeholder();
    public static final Biome byg_giant_seasonal_spruce_taiga = placeholder();
    public static final Biome byg_giant_snowy_spruce_taiga = placeholder();
    public static final Biome byg_glaciers = placeholder();
    public static final Biome byg_glowshroom_bayou = placeholder();
    public static final Biome byg_grassland_plateau = placeholder();
    public static final Biome byg_great_lakes = placeholder();
    public static final Biome byg_great_oak_lowlands = placeholder();
    public static final Biome byg_jacaranda_forest = placeholder();
    public static final Biome byg_lush_desert = placeholder();
    public static final Biome byg_mangrove_marshes = placeholder();
    public static final Biome byg_maple_taiga = placeholder();
    public static final Biome byg_marshlands = placeholder();
    public static final Biome byg_meadow = placeholder();
    public static final Biome byg_stone_pillar_savanna = placeholder();
    public static final Biome byg_northern_forest = placeholder();
    public static final Biome byg_orchard = placeholder();
    public static final Biome byg_outback = placeholder();
    public static final Biome byg_outlands = placeholder();
    public static final Biome byg_pine_lowlands = placeholder();
    public static final Biome byg_pine_mountains = placeholder();
    public static final Biome byg_prairie = placeholder();
    public static final Biome byg_quagmire = placeholder();
    public static final Biome byg_red_desert = placeholder();
    public static final Biome byg_red_oak_forest = placeholder();
    public static final Biome byg_red_outlands = placeholder();
    public static final Biome byg_redwood_tropics = placeholder();
    public static final Biome byg_savanna_canopy = placeholder();
    public static final Biome byg_seasonal_birch_forest = placeholder();
    public static final Biome byg_seasonal_deciduous = placeholder();
    public static final Biome byg_seasonal_forest = placeholder();
    public static final Biome byg_seasonal_taiga = placeholder();
    public static final Biome byg_shrublands = placeholder();
    public static final Biome byg_skyris_highlands = placeholder();
    public static final Biome byg_snowy_coniferous_forest = placeholder();
    public static final Biome byg_snowy_deciduous_forest = placeholder();
    public static final Biome byg_snowy_evergreen_taiga = placeholder();
    public static final Biome byg_snowy_pine_mountains = placeholder();
    public static final Biome byg_sonoran_desert = placeholder();
    public static final Biome byg_stellata_pasture = placeholder();
    public static final Biome byg_stone_brushlands = placeholder();
    public static final Biome byg_tropical_islands = placeholder();
    public static final Biome byg_tropical_mountains = placeholder();
    public static final Biome byg_tropical_rainforest = placeholder();
    public static final Biome byg_weeping_witch_forest = placeholder();
    public static final Biome byg_whispering_woods = placeholder();
    public static final Biome byg_woodlands = placeholder();
    public static final Biome byg_zelkova_forest = placeholder();

    private ModBiomes() {
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T placeholder() {
        return null;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Biome> event) {
        IForgeRegistry<Biome> registry = event.getRegistry();
        registerBiome(registry, "byg_allium_fields", BiomeAlliumFields::new);
        registerBiome(registry, "byg_alps", BiomeAlps::new);
        registerBiome(registry, "byg_amaranth_fields", BiomeAmaranthFields::new);
        registerBiome(registry, "byg_ancient_forest", BiomeAncientForest::new);
        registerBiome(registry, "byg_aspen_forest", BiomeAspenForest::new);
        registerBiome(registry, "byg_baobab_savanna", BiomeBaobabSavanna::new);
        registerBiome(registry, "byg_bayou", BiomeBayou::new);
        registerBiome(registry, "byg_blue_taiga", BiomeBlueTaiga::new);
        registerBiome(registry, "byg_bluff_mountains", BiomeBluffMountains::new);
        registerBiome(registry, "byg_bog", BiomeBog::new);
        registerBiome(registry, "byg_boreal_forest", BiomeBorealForest::new);
        registerBiome(registry, "byg_chaparral_lowlands", BiomeChaparralLowlands::new);
        registerBiome(registry, "byg_cherry_grove", BiomeCherryGrove::new);
        registerBiome(registry, "byg_cika_forest", BiomeCikaForest::new);
        registerBiome(registry, "byg_colored_canyons", BiomeColoredCanyons::new);
        registerBiome(registry, "byg_coniferous_forest", BiomeConiferousForest::new);
        registerBiome(registry, "byg_crystal_canyons", BiomeCrystalCanyons::new);
        registerBiome(registry, "byg_cypress_swamplands", BiomeCypressSwamplands::new);
        registerBiome(registry, "byg_dead_sea", BiomeDeadSea::new);
        registerBiome(registry, "byg_deciduous_forest", BiomeDeciduousForest::new);
        registerBiome(registry, "byg_dover_mountains", BiomeDoverMoutains::new);
        registerBiome(registry, "byg_dunes", BiomeDunes::new);
        registerBiome(registry, "byg_ebony_woods", BiomeEbonyWoods::new);
        registerBiome(registry, "byg_enchanted_forest", BiomeEnchantedForest::new);
        registerBiome(registry, "byg_evergreen_taiga", BiomeEvergreenTaiga::new);
        registerBiome(registry, "byg_flowering_plains", BiomeFloweringPlains::new);
        registerBiome(registry, "byg_frosty_forest", BiomeFrostyForest::new);
        registerBiome(registry, "byg_fungal_jungle", BiomeFungalJungle::new);
        registerBiome(registry, "byg_giant_blue_spruce_taiga", BiomeGiantBlueSpruceTaiga::new);
        registerBiome(registry, "byg_giant_seasonal_spruce_taiga", BiomeGiantSeasonalSpruceTaiga::new);
        registerBiome(registry, "byg_giant_snowy_spruce_taiga", BiomeGiantSnowySpruceTaiga::new);
        registerBiome(registry, "byg_glaciers", BiomeGlaciers::new);
        registerBiome(registry, "byg_glowshroom_bayou", BiomeGlowshroomBayou::new);
        registerBiome(registry, "byg_grassland_plateau", BiomeGrasslandPlateau::new);
        registerBiome(registry, "byg_great_lakes", BiomeGreatLakes::new);
        registerBiome(registry, "byg_great_oak_lowlands", BiomeGreatOakLowlands::new);
        registerBiome(registry, "byg_jacaranda_forest", BiomeJacarandaForest::new);
        registerBiome(registry, "byg_lush_desert", BiomeLushDesert::new);
        registerBiome(registry, "byg_mangrove_marshes", BiomeMangroveMarshes::new);
        registerBiome(registry, "byg_maple_taiga", BiomeMapleTaiga::new);
        registerBiome(registry, "byg_marshlands", BiomeMarshlands::new);
        registerBiome(registry, "byg_meadow", BiomeMeadow::new);
        registerBiome(registry, "byg_stone_pillar_savanna", BiomeStonePillarSavanna::new);
        registerBiome(registry, "byg_northern_forest", BiomeNorthernForest::new);
        registerBiome(registry, "byg_orchard", BiomeOrchard::new);
        registerBiome(registry, "byg_outback", BiomeOutback::new);
        registerBiome(registry, "byg_outlands", BiomeOutlands::new);
        registerBiome(registry, "byg_pine_lowlands", BiomePineLowlands::new);
        registerBiome(registry, "byg_pine_mountains", BiomePineMountains::new);
        registerBiome(registry, "byg_prairie", BiomePrairie::new);
        registerBiome(registry, "byg_quagmire", BiomeQuagmire::new);
        registerBiome(registry, "byg_red_desert", BiomeRedDesert::new);
        registerBiome(registry, "byg_red_oak_forest", BiomeRedOakForest::new);
        registerBiome(registry, "byg_red_outlands", BiomeRedOutlands::new);
        registerBiome(registry, "byg_redwood_tropics", BiomeRedwoodTropics::new);
        registerBiome(registry, "byg_savanna_canopy", BiomeSavannaCanopy::new);
        registerBiome(registry, "byg_seasonal_birch_forest", BiomeSeasonalBirchForest::new);
        registerBiome(registry, "byg_seasonal_deciduous", BiomeSeasonalDeciduous::new);
        registerBiome(registry, "byg_seasonal_forest", BiomeSeasonalForest::new);
        registerBiome(registry, "byg_seasonal_taiga", BiomeSeasonalTaiga::new);
        registerBiome(registry, "byg_shrublands", BiomeShrublands::new);
        registerBiome(registry, "byg_skyris_highlands", BiomeSkyrisHighlands::new);
        registerBiome(registry, "byg_snowy_coniferous_forest", BiomeSnowyConiferousForest::new);
        registerBiome(registry, "byg_snowy_deciduous_forest", BiomeSnowyDeciduousForest::new);
        registerBiome(registry, "byg_snowy_evergreen_taiga", BiomeSnowyEvergeenTaiga::new);
        registerBiome(registry, "byg_snowy_pine_mountains", BiomeSnowyPineMountains::new);
        registerBiome(registry, "byg_sonoran_desert", BiomeSonoranDesert::new);
        registerBiome(registry, "byg_stellata_pasture", BiomeStellataPasture::new);
        registerBiome(registry, "byg_stone_brushlands", BiomeStoneBrushlands::new);
        registerBiome(registry, "byg_tropical_islands", BiomeTropicalIslands::new);
        registerBiome(registry, "byg_tropical_mountains", BiomeTropicalMountains::new);
        registerBiome(registry, "byg_tropical_rainforest", BiomeTropicalRainforest::new);
        registerBiome(registry, "byg_weeping_witch_forest", BiomeWeepingWitchForest::new);
        registerBiome(registry, "byg_whispering_woods", BiomeWhisperingWoods::new);
        registerBiome(registry, "byg_woodlands", BiomeWoodlands::new);
        registerBiome(registry, "byg_zelkova_forest", BiomeZelkovaForest::new);
    }

    public static void init() {
        configureBiome("byg_allium_fields", byg_allium_fields, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.RARE);
        configureBiome("byg_alps", byg_alps, BiomeManager.BiomeType.ICY, 4, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS);
        configureBiome("byg_amaranth_fields", byg_amaranth_fields, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.RARE);
        configureBiome("byg_ancient_forest", byg_ancient_forest, BiomeManager.BiomeType.WARM, 2, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE, BiomeDictionary.Type.DENSE);
        configureBiome("byg_aspen_forest", byg_aspen_forest, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.FOREST);
        configureBiome("byg_baobab_savanna", byg_baobab_savanna, BiomeManager.BiomeType.DESERT, 3, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE);
        configureBiome("byg_bayou", byg_bayou, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET);
        configureBiome("byg_blue_taiga", byg_blue_taiga, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST);
        configureBiome("byg_bluff_mountains", byg_bluff_mountains, BiomeManager.BiomeType.COOL, 3, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.COLD);
        configureBiome("byg_bog", byg_bog, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.WET, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.COLD);
        configureBiome("byg_boreal_forest", byg_boreal_forest, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.COLD, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
        configureBiome("byg_chaparral_lowlands", byg_chaparral_lowlands, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.DRY);
        configureBiome("byg_cherry_grove", byg_cherry_grove, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HILLS);
        configureBiome("byg_cika_forest", byg_cika_forest, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.COLD, BiomeDictionary.Type.FOREST);
        configureBiome("byg_colored_canyons", byg_colored_canyons, BiomeManager.BiomeType.DESERT, 3, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.RARE);
        configureBiome("byg_coniferous_forest", byg_coniferous_forest, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD);
        configureBiome("byg_crystal_canyons", byg_crystal_canyons, BiomeManager.BiomeType.DESERT, 2, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE);
        configureBiome("byg_cypress_swamplands", byg_cypress_swamplands, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE);
        configureBiome("byg_dead_sea", byg_dead_sea, BiomeManager.BiomeType.DESERT, 4, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.DEAD);
        configureBiome("byg_deciduous_forest", byg_deciduous_forest, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.FOREST);
        configureBiome("byg_dover_mountains", byg_dover_mountains, BiomeManager.BiomeType.COOL, 3, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST);
        configureBiome("byg_dunes", byg_dunes, BiomeManager.BiomeType.DESERT, 4, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
        configureBiome("byg_ebony_woods", byg_ebony_woods, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
        configureBiome("byg_enchanted_forest", byg_enchanted_forest, BiomeManager.BiomeType.WARM, 2, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE);
        configureBiome("byg_evergreen_taiga", byg_evergreen_taiga, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_flowering_plains", byg_flowering_plains, BiomeManager.BiomeType.WARM, 2, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.RARE);
        configureBiome("byg_frosty_forest", byg_frosty_forest, BiomeManager.BiomeType.ICY, 3, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.RARE);
        configureBiome("byg_fungal_jungle", byg_fungal_jungle, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.WET, BiomeDictionary.Type.JUNGLE);
        configureBiome("byg_giant_blue_spruce_taiga", byg_giant_blue_spruce_taiga, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_giant_seasonal_spruce_taiga", byg_giant_seasonal_spruce_taiga, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_giant_snowy_spruce_taiga", byg_giant_snowy_spruce_taiga, BiomeManager.BiomeType.ICY, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_glaciers", byg_glaciers, BiomeManager.BiomeType.ICY, 3, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS);
        configureBiome("byg_glowshroom_bayou", byg_glowshroom_bayou, BiomeManager.BiomeType.WARM, 2, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE);
        configureBiome("byg_grassland_plateau", byg_grassland_plateau, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.MOUNTAIN);
        configureBiome("byg_great_lakes", byg_great_lakes, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.COLD, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.WATER, BiomeDictionary.Type.RIVER);
        configureBiome("byg_great_oak_lowlands", byg_great_oak_lowlands, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE);
        configureBiome("byg_jacaranda_forest", byg_jacaranda_forest, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.FOREST);
        configureBiome("byg_lush_desert", byg_lush_desert, BiomeManager.BiomeType.DESERT, 4, BiomeDictionary.Type.MESA, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.LUSH);
        configureBiome("byg_mangrove_marshes", byg_mangrove_marshes, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET);
        configureBiome("byg_maple_taiga", byg_maple_taiga, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD);
        configureBiome("byg_marshlands", byg_marshlands, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET, BiomeDictionary.Type.LUSH);
        configureBiome("byg_meadow", byg_meadow, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.PLAINS);
        configureBiome("byg_stone_pillar_savanna", byg_stone_pillar_savanna, BiomeManager.BiomeType.DESERT, 3, BiomeDictionary.Type.MESA, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SPARSE);
        configureBiome("byg_northern_forest", byg_northern_forest, BiomeManager.BiomeType.ICY, 2, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.RARE, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_orchard", byg_orchard, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST);
        configureBiome("byg_outback", byg_outback, BiomeManager.BiomeType.DESERT, 3, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
        configureBiome("byg_outlands", byg_outlands, BiomeManager.BiomeType.DESERT, 4, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
        configureBiome("byg_pine_lowlands", byg_pine_lowlands, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_pine_mountains", byg_pine_mountains, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.HILLS);
        configureBiome("byg_prairie", byg_prairie, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.DRY);
        configureBiome("byg_quagmire", byg_quagmire, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.WET);
        configureBiome("byg_red_desert", byg_red_desert, BiomeManager.BiomeType.DESERT, 4, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
        configureBiome("byg_red_oak_forest", byg_red_oak_forest, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.FOREST);
        configureBiome("byg_red_outlands", byg_red_outlands, BiomeManager.BiomeType.DESERT, 4, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
        configureBiome("byg_redwood_tropics", byg_redwood_tropics, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.FOREST);
        configureBiome("byg_savanna_canopy", byg_savanna_canopy, BiomeManager.BiomeType.DESERT, 3, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.FOREST);
        configureBiome("byg_seasonal_birch_forest", byg_seasonal_birch_forest, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD);
        configureBiome("byg_seasonal_deciduous", byg_seasonal_deciduous, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.COLD, BiomeDictionary.Type.FOREST);
        configureBiome("byg_seasonal_forest", byg_seasonal_forest, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD);
        configureBiome("byg_seasonal_taiga", byg_seasonal_taiga, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST);
        configureBiome("byg_shrublands", byg_shrublands, BiomeManager.BiomeType.DESERT, 3, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
        configureBiome("byg_skyris_highlands", byg_skyris_highlands, BiomeManager.BiomeType.COOL, 2, BiomeDictionary.Type.COLD, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE);
        configureBiome("byg_snowy_coniferous_forest", byg_snowy_coniferous_forest, BiomeManager.BiomeType.ICY, 4, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_snowy_deciduous_forest", byg_snowy_deciduous_forest, BiomeManager.BiomeType.ICY, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SNOWY);
        configureBiome("byg_snowy_evergreen_taiga", byg_snowy_evergreen_taiga, BiomeManager.BiomeType.ICY, 3, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.DENSE);
        configureBiome("byg_snowy_pine_mountains", byg_snowy_pine_mountains, BiomeManager.BiomeType.ICY, 4, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS);
        configureBiome("byg_sonoran_desert", byg_sonoran_desert, BiomeManager.BiomeType.DESERT, 3, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY);
        configureBiome("byg_stellata_pasture", byg_stellata_pasture, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MAGICAL);
        configureBiome("byg_stone_brushlands", byg_stone_brushlands, BiomeManager.BiomeType.COOL, 3, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY);
        configureBiome("byg_tropical_islands", byg_tropical_islands, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.JUNGLE);
        configureBiome("byg_tropical_mountains", byg_tropical_mountains, BiomeManager.BiomeType.WARM, 3, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.WET, BiomeDictionary.Type.JUNGLE);
        configureBiome("byg_tropical_rainforest", byg_tropical_rainforest, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.JUNGLE);
        configureBiome("byg_weeping_witch_forest", byg_weeping_witch_forest, BiomeManager.BiomeType.COOL, 2, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE);
        configureBiome("byg_whispering_woods", byg_whispering_woods, BiomeManager.BiomeType.WARM, 2, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.RARE);
        configureBiome("byg_woodlands", byg_woodlands, BiomeManager.BiomeType.WARM, 4, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HILLS);
        configureBiome("byg_zelkova_forest", byg_zelkova_forest, BiomeManager.BiomeType.COOL, 4, BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS);
    }

    private static <T extends Biome> void registerBiome(IForgeRegistry<Biome> registry, String biomeName, Supplier<T> biomeFactory) {
        if (Config.isBiomeEnabled(biomeName)) {
            registry.register(biomeFactory.get());
        }
    }

    private static void configureBiome(String biomeName, Biome biome, BiomeManager.BiomeType climate, int weight, BiomeDictionary.Type... types) {
        if (!Config.isBiomeEnabled(biomeName)) {
            return;
        }
        int configuredWeight = Config.getBiomeWeight(biomeName, weight);
        BiomeDictionary.addTypes(biome, types);
        if (Config.shouldAddBiomesToSpawnList()) {
            BiomeManager.addSpawnBiome(biome);
        }
        BiomeManager.addBiome(climate, new BiomeManager.BiomeEntry(biome, configuredWeight));
    }
}
