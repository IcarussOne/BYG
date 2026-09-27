package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public final class DeadFrozenOakTreeGenerator extends WoodlandTreeGenerator {
    @Override
    public boolean generate(World world, Random random, BlockPos origin) {
        int height = 14 + random.nextInt(6);
        if (!this.canStandAt(world, origin, height + 1, 5, height + 2)) {
            return false;
        }
        IBlockState logBase = ModBlocks.frozen_oak_log.getDefaultState();
        IBlockState vertical = log(logBase, EnumFacing.Axis.Y);
        IBlockState leaves = ModBlocks.frozen_oak_leaves.getDefaultState();

        this.placeAnchoredLog(world, origin, vertical, height + 1);
        this.placeAnchoredLog(world, origin.east(), vertical, height);
        this.placeAnchoredLog(world, origin.south(), vertical, height - random.nextInt(2));
        this.placeAnchoredLog(world, origin.south().east(), vertical, height - 2 - random.nextInt(3));

        int limbs = 3 + random.nextInt(2);
        for (int i = 0; i < limbs; i++) {
            EnumFacing side = randomSide(random);
            int y = 3 + random.nextInt(Math.max(1, height - 6));
            int columnX = side.getXOffset() > 0 ? 1 : side.getXOffset() < 0 ? 0 : random.nextInt(2);
            int columnZ = side.getZOffset() > 0 ? 1 : side.getZOffset() < 0 ? 0 : random.nextInt(2);
            BlockPos cursor = origin.add(columnX, y, columnZ);
            IBlockState horizontal = log(logBase, side.getAxis());
            int length = 1 + random.nextInt(3);
            for (int step = 0; step < length; step++) {
                cursor = cursor.offset(side);
                this.placeLog(world, cursor, horizontal);
                if (step == length - 1 && random.nextBoolean()) {
                    cursor = cursor.up();
                    this.placeLog(world, cursor, vertical);
                }
            }
            if (random.nextInt(5) < 3) {
                this.placeLeaf(world, cursor.up(), leaves);
                for (EnumFacing around : EnumFacing.HORIZONTALS) {
                    this.placeLeaf(world, cursor.offset(around), leaves);
                }
            }
        }
        return true;
    }
}
