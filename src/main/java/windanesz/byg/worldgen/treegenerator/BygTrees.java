package windanesz.byg.worldgen.treegenerator;

import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator.TreeStyle;

/**
 * Shared tree shapes grown by BYG saplings and placed by BYG biomes. Each shape is defined once here; saplings
 * and biomes reference these instead of building their own copies.
 */
public final class BygTrees {
    public static final SaplingTreeGenerator ASPEN = BygWood.ASPEN.tree(TreeStyle.ROUND, 5, 2);
    public static final SaplingTreeGenerator ASPEN_TALL = BygWood.ASPEN.tree(TreeStyle.TALL_ROUND, 7, 2);

    public static final SaplingTreeGenerator BAOBAB = BygWood.BAOBAB.tree(TreeStyle.BAOBAB, 7, 2);
    public static final SaplingTreeGenerator BAOBAB_YOUNG = BygWood.BAOBAB.tree(TreeStyle.BAOBAB_YOUNG, 6, 2);

    public static final SaplingTreeGenerator BLUE_ENCHANTED_STEPPED = BygWood.BLUE_ENCHANTED.tree(TreeStyle.ENCHANTED_STEPPED, 9, 2);
    public static final SaplingTreeGenerator BLUE_ENCHANTED_TIERED = BygWood.BLUE_ENCHANTED.tree(TreeStyle.ENCHANTED_TIERED, 12, 2);
    public static final SaplingTreeGenerator BLUE_ENCHANTED_TALL = BygWood.BLUE_ENCHANTED.tree(TreeStyle.ENCHANTED_TIERED, 16, 3);
    public static final SaplingTreeGenerator PINK_ENCHANTED_STEPPED = BygWood.PINK_ENCHANTED.tree(TreeStyle.ENCHANTED_STEPPED, 9, 2);
    public static final SaplingTreeGenerator PINK_ENCHANTED_TIERED = BygWood.PINK_ENCHANTED.tree(TreeStyle.ENCHANTED_TIERED, 12, 2);
    public static final SaplingTreeGenerator PINK_ENCHANTED_TALL = BygWood.PINK_ENCHANTED.tree(TreeStyle.ENCHANTED_TIERED, 16, 3);
    public static final SaplingTreeGenerator PURPLE_ENCHANTED_STEPPED = BygWood.PURPLE_ENCHANTED.tree(TreeStyle.ENCHANTED_STEPPED, 9, 2);
    public static final SaplingTreeGenerator PURPLE_ENCHANTED_TIERED = BygWood.PURPLE_ENCHANTED.tree(TreeStyle.ENCHANTED_TIERED, 12, 2);
    public static final SaplingTreeGenerator PURPLE_ENCHANTED_TALL = BygWood.PURPLE_ENCHANTED.tree(TreeStyle.ENCHANTED_TIERED, 16, 3);

    public static final SaplingTreeGenerator BLUE_SPRUCE = BygWood.BLUE_SPRUCE.tree(TreeStyle.CONIFER, 6, 2);
    public static final SaplingTreeGenerator BLUE_SPRUCE_TALL = BygWood.BLUE_SPRUCE.tree(TreeStyle.TALL_CONIFER, 8, 3);
    public static final SaplingTreeGenerator ORANGE_SPRUCE = BygWood.ORANGE_SPRUCE.tree(TreeStyle.CONIFER, 6, 2);
    public static final SaplingTreeGenerator ORANGE_SPRUCE_TALL = BygWood.ORANGE_SPRUCE.tree(TreeStyle.TALL_CONIFER, 8, 3);
    public static final SaplingTreeGenerator RED_SPRUCE = BygWood.RED_SPRUCE.tree(TreeStyle.CONIFER, 6, 2);
    public static final SaplingTreeGenerator RED_SPRUCE_TALL = BygWood.RED_SPRUCE.tree(TreeStyle.TALL_CONIFER, 8, 3);
    public static final SaplingTreeGenerator YELLOW_SPRUCE = BygWood.YELLOW_SPRUCE.tree(TreeStyle.CONIFER, 6, 2);
    public static final SaplingTreeGenerator YELLOW_SPRUCE_TALL = BygWood.YELLOW_SPRUCE.tree(TreeStyle.TALL_CONIFER, 8, 3);

    public static final SaplingTreeGenerator BROWN_BIRCH = BygWood.BROWN_BIRCH.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator BROWN_BIRCH_TALL = BygWood.BROWN_BIRCH.tree(TreeStyle.TALL_ROUND, 6, 1);
    public static final SaplingTreeGenerator ORANGE_BIRCH = BygWood.ORANGE_BIRCH.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator ORANGE_BIRCH_TALL = BygWood.ORANGE_BIRCH.tree(TreeStyle.TALL_ROUND, 6, 1);
    public static final SaplingTreeGenerator RED_BIRCH = BygWood.RED_BIRCH.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator RED_BIRCH_TALL = BygWood.RED_BIRCH.tree(TreeStyle.TALL_ROUND, 6, 1);
    public static final SaplingTreeGenerator YELLOW_BIRCH = BygWood.YELLOW_BIRCH.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator YELLOW_BIRCH_TALL = BygWood.YELLOW_BIRCH.tree(TreeStyle.TALL_ROUND, 6, 1);

    public static final SaplingTreeGenerator DRY_BROWN_OAK = BygWood.DRY_BROWN_OAK.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator DRY_BROWN_OAK_TALL = BygWood.DRY_BROWN_OAK.tree(TreeStyle.TALL_ROUND, 6, 2);
    public static final SaplingTreeGenerator DRY_GREEN_OAK = BygWood.DRY_GREEN_OAK.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator DRY_GREEN_OAK_TALL = BygWood.DRY_GREEN_OAK.tree(TreeStyle.TALL_ROUND, 6, 2);
    public static final SaplingTreeGenerator ORANGE_OAK = BygWood.ORANGE_OAK.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator ORANGE_OAK_TALL = BygWood.ORANGE_OAK.tree(TreeStyle.TALL_ROUND, 6, 2);
    public static final SaplingTreeGenerator RED_OAK = BygWood.RED_OAK.tree(TreeStyle.ROUND, 5, 1);
    public static final SaplingTreeGenerator RED_OAK_TALL = BygWood.RED_OAK.tree(TreeStyle.TALL_ROUND, 6, 2);
    public static final SaplingTreeGenerator ORCHARD = BygWood.ORCHARD.tree(TreeStyle.ROUND, 5, 2);

    public static final SaplingTreeGenerator CIKA = BygWood.CIKA.tree(TreeStyle.CIKA, 22, 6);

    public static final SaplingTreeGenerator CYPRESS = BygWood.CYPRESS.tree(TreeStyle.DROOPING, 7, 2);
    public static final SaplingTreeGenerator CYPRESS_TALL = BygWood.CYPRESS.tree(TreeStyle.TALL_CONIFER, 9, 2);

    public static final SaplingTreeGenerator EBONY = BygWood.EBONY.tree(TreeStyle.EBONY, 11, 4);

    public static final SaplingTreeGenerator FIR = BygWood.FIR.tree(TreeStyle.FIR, 15, 0);
    public static final SaplingTreeGenerator FIR_TALL = BygWood.FIR.tree(TreeStyle.FIR, 20, 0);

    public static final SaplingTreeGenerator GREAT_OAK = BygWood.GREAT_OAK.tree(TreeStyle.GREAT_OAK, 0, 0);

    public static final SaplingTreeGenerator HOLLY = BygWood.HOLLY.tree(TreeStyle.HOLLY, 12, 2);
    public static final SaplingTreeGenerator HOLLY_TALL = BygWood.HOLLY.tree(TreeStyle.HOLLY, 16, 3);

    public static final SaplingTreeGenerator IRONWOOD = BygWood.IRONWOOD.tree(TreeStyle.TALL_ROUND, 7, 2);

    public static final SaplingTreeGenerator JACARANDA = BygWood.JACARANDA.tree(TreeStyle.JACARANDA, 6, 2);
    public static final SaplingTreeGenerator JACARANDA_LARGE = BygWood.JACARANDA.tree(TreeStyle.JACARANDA, 8, 2);
    public static final SaplingTreeGenerator JACARANDA_TALL = BygWood.JACARANDA.tree(TreeStyle.JACARANDA_TALL, 8, 2);
    public static final SaplingTreeGenerator JACARANDA_TALL_SHORT = BygWood.JACARANDA.tree(TreeStyle.JACARANDA_TALL, 6, 2);

    public static final SaplingTreeGenerator MAHOGANY = BygWood.MAHOGANY.tree(TreeStyle.MAHOGANY, 10, 4);
    public static final SaplingTreeGenerator MAHOGANY_MOUNTAIN = BygWood.MAHOGANY.tree(TreeStyle.TROPICAL_MOUNTAIN, 10, 4);

    public static final SaplingTreeGenerator MANGROVE = BygWood.MANGROVE.tree(TreeStyle.MANGROVE, 6, 2);
    public static final SaplingTreeGenerator MANGROVE_LARGE = BygWood.MANGROVE.tree(TreeStyle.MANGROVE, 8, 2);

    public static final SaplingTreeGenerator RED_MAPLE = BygWood.RED_MAPLE.tree(TreeStyle.MAPLE, 7, 2);
    public static final SaplingTreeGenerator RED_MAPLE_LARGE = BygWood.RED_MAPLE.tree(TreeStyle.MAPLE, 9, 2);
    public static final SaplingTreeGenerator RED_MAPLE_TAIGA = BygWood.RED_MAPLE.tree(TreeStyle.MAPLE, 7, 3);
    public static final SaplingTreeGenerator SILVER_MAPLE = BygWood.SILVER_MAPLE.tree(TreeStyle.MAPLE, 7, 2);
    public static final SaplingTreeGenerator SILVER_MAPLE_LARGE = BygWood.SILVER_MAPLE.tree(TreeStyle.MAPLE, 9, 2);
    public static final SaplingTreeGenerator SILVER_MAPLE_TAIGA = BygWood.SILVER_MAPLE.tree(TreeStyle.MAPLE, 7, 3);

    public static final SaplingTreeGenerator PALM = BygWood.PALM.tree(TreeStyle.PALM, 11, 3);

    public static final SaplingTreeGenerator PALO_VERDE = BygWood.PALO_VERDE.tree(TreeStyle.ROUND, 5, 2);

    public static final SaplingTreeGenerator PINK_CHERRY = BygWood.PINK_CHERRY.tree(TreeStyle.CHERRY_BLOSSOM, 5, 2);
    public static final SaplingTreeGenerator PINK_CHERRY_LARGE = BygWood.PINK_CHERRY.tree(TreeStyle.CHERRY_BLOSSOM, 7, 2);
    public static final SaplingTreeGenerator WHITE_CHERRY = BygWood.WHITE_CHERRY.tree(TreeStyle.CHERRY_BLOSSOM, 5, 2);
    public static final SaplingTreeGenerator WHITE_CHERRY_LARGE = BygWood.WHITE_CHERRY.tree(TreeStyle.CHERRY_BLOSSOM, 7, 2);

    public static final SaplingTreeGenerator RAINBOW_EUCALYPTUS = BygWood.RAINBOW_EUCALYPTUS.tree(TreeStyle.ROUND, 7, 2);
    public static final SaplingTreeGenerator RAINBOW_EUCALYPTUS_TALL = BygWood.RAINBOW_EUCALYPTUS.tree(TreeStyle.TALL_ROUND, 9, 3);

    public static final SaplingTreeGenerator SKYRIS = BygWood.SKYRIS.tree(TreeStyle.SKYRIS, 8, 2);
    public static final SaplingTreeGenerator SKYRIS_TALL = BygWood.SKYRIS.tree(TreeStyle.SKYRIS, 11, 2);

    public static final SaplingTreeGenerator WILLOW = BygWood.WILLOW.tree(TreeStyle.WILLOW, 6, 2);
    public static final SaplingTreeGenerator WILLOW_LARGE = BygWood.WILLOW.tree(TreeStyle.WILLOW, 8, 2);

    public static final SaplingTreeGenerator WITCH_HAZEL = BygWood.WITCH_HAZEL.tree(TreeStyle.DROOPING, 6, 2);

    public static final SaplingTreeGenerator ZELKOVA = BygWood.ZELKOVA.tree(TreeStyle.ZELKOVA, 0, 0);

    private BygTrees() {
    }
}
