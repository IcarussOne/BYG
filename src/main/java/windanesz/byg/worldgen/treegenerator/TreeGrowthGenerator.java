package windanesz.byg.worldgen.treegenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@FunctionalInterface
public interface TreeGrowthGenerator {
    boolean generate(World world, Random random, BlockPos position);
}

