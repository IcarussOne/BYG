package windanesz.byg.worldgen.treegenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Grows a tree from a sapling. Deliberately not named {@code generate}: reobfuscation renames
 * {@code WorldGenerator.generate} overrides to their SRG name, which would leave this method unimplemented.
 */
@FunctionalInterface
public interface TreeGrowthGenerator {
    boolean growTree(World world, Random random, BlockPos position);
}

