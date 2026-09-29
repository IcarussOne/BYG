package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import windanesz.byg.registry.ModBlocks;

import java.util.function.Supplier;

/**
 * The log and leaves pairing of every BYG sapling-grown tree. Blocks are resolved lazily, so a wood whose
 * content is disabled in the config is only a problem if one of its trees is actually generated.
 */
public enum BygWood {
    ASPEN(block(() -> ModBlocks.aspen_log), block(() -> ModBlocks.aspen_leaves)),
    BAOBAB(block(() -> ModBlocks.baobab_log), block(() -> ModBlocks.baobab_leaves)),
    BLUE_ENCHANTED(block(() -> ModBlocks.enchanted_log), block(() -> ModBlocks.enchanted_leaves_blue)),
    PINK_ENCHANTED(block(() -> ModBlocks.enchanted_log), block(() -> ModBlocks.enchanted_leaves_pink)),
    PURPLE_ENCHANTED(block(() -> ModBlocks.enchanted_log), block(() -> ModBlocks.enchanted_leaves_purple)),
    BLUE_SPRUCE(spruceLog(), block(() -> ModBlocks.spruce_leaves_blue)),
    ORANGE_SPRUCE(spruceLog(), block(() -> ModBlocks.spruce_leaves_orange)),
    RED_SPRUCE(spruceLog(), block(() -> ModBlocks.spruce_leaves_red)),
    YELLOW_SPRUCE(spruceLog(), block(() -> ModBlocks.spruce_leaves_yellow)),
    BROWN_BIRCH(birchLog(), block(() -> ModBlocks.brown_birch_leaves)),
    ORANGE_BIRCH(birchLog(), block(() -> ModBlocks.orange_birch_leaves)),
    RED_BIRCH(birchLog(), block(() -> ModBlocks.red_birch_leaves)),
    YELLOW_BIRCH(birchLog(), block(() -> ModBlocks.birch_leaves_yellow)),
    DRY_BROWN_OAK(oakLog(), block(() -> ModBlocks.oak_leaves_dry_brown)),
    DRY_GREEN_OAK(oakLog(), block(() -> ModBlocks.oak_leaves_dry_green)),
    ORANGE_OAK(oakLog(), block(() -> ModBlocks.oak_leaves_orange)),
    RED_OAK(oakLog(), block(() -> ModBlocks.oak_leaves_red)),
    ORCHARD(oakLog(), block(() -> ModBlocks.orchard_leaves_flowering)),
    CIKA(block(() -> ModBlocks.cika_log), block(() -> ModBlocks.cika_leaves)),
    CYPRESS(block(() -> ModBlocks.cypress_log), block(() -> ModBlocks.cypress_leaves)),
    EBONY(block(() -> ModBlocks.ebony_log), block(() -> ModBlocks.ebony_leaves)),
    FIR(block(() -> ModBlocks.fir_log), block(() -> ModBlocks.fir_leaves)),
    GREAT_OAK(block(() -> ModBlocks.great_oak_log), block(() -> ModBlocks.great_oak_leaves)),
    HOLLY(block(() -> ModBlocks.holly_log), block(() -> ModBlocks.holly_leaves), block(() -> ModBlocks.holly_berry_leaves)),
    IRONWOOD(block(() -> ModBlocks.ironwood_log), block(() -> ModBlocks.ironwood_leaves)),
    JACARANDA(block(() -> ModBlocks.jacaranda_log), block(() -> ModBlocks.jacaranda_leaves)),
    MAHOGANY(block(() -> ModBlocks.mahogany_log), block(() -> ModBlocks.mahogany_leaves)),
    MANGROVE(block(() -> ModBlocks.mangrove_log), block(() -> ModBlocks.mangrove_leaves)),
    RED_MAPLE(block(() -> ModBlocks.maple_log), block(() -> ModBlocks.maple_leaves_red)),
    SILVER_MAPLE(block(() -> ModBlocks.maple_log), block(() -> ModBlocks.maple_leaves_silver)),
    PALM(block(() -> ModBlocks.palm_log), block(() -> ModBlocks.palm_leaves)),
    PALO_VERDE(block(() -> ModBlocks.palo_verde_log), block(() -> ModBlocks.palo_verde_leaves_flowering)),
    PINK_CHERRY(block(() -> ModBlocks.cherry_log), block(() -> ModBlocks.cherry_leaves_pink)),
    WHITE_CHERRY(block(() -> ModBlocks.cherry_log), block(() -> ModBlocks.cherry_leaves_white)),
    RAINBOW_EUCALYPTUS(block(() -> ModBlocks.rainbow_eucalyptus_log), block(() -> ModBlocks.rainbow_eucalyptus_leaves)),
    SKYRIS(block(() -> ModBlocks.skyris_log), block(() -> ModBlocks.skyris_leaves)),
    WILLOW(block(() -> ModBlocks.willow_log), block(() -> ModBlocks.willow_leaves)),
    WITCH_HAZEL(block(() -> ModBlocks.witch_hazel_log), block(() -> ModBlocks.witch_hazel_leaves_blooming)),
    ZELKOVA(block(() -> ModBlocks.zelkova_log), block(() -> ModBlocks.zelkova_leaves));

    private final Supplier<IBlockState> log;
    private final Supplier<IBlockState> leaves;
    private final Supplier<IBlockState> accentLeaves;

    BygWood(Supplier<IBlockState> log, Supplier<IBlockState> leaves) {
        this(log, leaves, null);
    }

    BygWood(Supplier<IBlockState> log, Supplier<IBlockState> leaves, Supplier<IBlockState> accentLeaves) {
        this.log = log;
        this.leaves = leaves;
        this.accentLeaves = accentLeaves;
    }

    public Supplier<IBlockState> log() {
        return this.log;
    }

    public Supplier<IBlockState> leaves() {
        return this.leaves;
    }

    public SaplingTreeGenerator tree(SaplingTreeGenerator.TreeStyle style, int minHeight, int extraHeight) {
        return this.accentLeaves == null
                ? new SaplingTreeGenerator(this.log, this.leaves, style, minHeight, extraHeight)
                : new SaplingTreeGenerator(this.log, this.leaves, this.accentLeaves, style, minHeight, extraHeight);
    }

    private static Supplier<IBlockState> block(Supplier<Block> block) {
        return () -> block.get().getDefaultState();
    }

    private static Supplier<IBlockState> oakLog() {
        return () -> Blocks.LOG.getDefaultState();
    }

    private static Supplier<IBlockState> spruceLog() {
        return () -> Blocks.LOG.getStateFromMeta(1);
    }

    private static Supplier<IBlockState> birchLog() {
        return () -> Blocks.LOG.getStateFromMeta(2);
    }
}
