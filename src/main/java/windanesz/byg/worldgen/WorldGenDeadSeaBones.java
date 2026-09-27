package windanesz.byg.worldgen;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public class WorldGenDeadSeaBones extends WorldGenerator {
    private static final ResourceLocation DEAD_SEA = new ResourceLocation("byg", "byg_dead_sea");
    private static final IBlockState VERTICAL = Blocks.BONE_BLOCK.getDefaultState();
    private static final IBlockState ALONG_X = VERTICAL.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
    private static final IBlockState ALONG_Z = VERTICAL.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        boolean alongX = rand.nextBoolean();
        int halfLength = 5 + rand.nextInt(3);
        int ribRadius = 3 + rand.nextInt(2);
        int xRadius = alongX ? halfLength + 3 : ribRadius + 2;
        int zRadius = alongX ? ribRadius + 2 : halfLength + 3;
        if (!world.isAreaLoaded(position.add(-xRadius, 1, -zRadius),
                position.add(xRadius, world.getSeaLevel() + 8, zRadius))) return false;

        int floor = findFloor(world, position);
        if (floor < 0 || floor + 8 >= world.getHeight()) return false;
        for (int d = -halfLength; d <= halfLength; d += 3) {
            int localFloor = findFloor(world, offset(position, alongX, d, 0, 0));
            if (localFloor < 0 || Math.abs(localFloor - floor) > 3) return false;
        }
        for (int side : new int[]{-ribRadius, ribRadius}) {
            int localFloor = findFloor(world, offset(position, alongX, 0, side, 0));
            if (localFloor < 0 || Math.abs(localFloor - floor) > 3) return false;
        }

        // Every bone is anchored to the floor of its own column so uneven ground never leaves it floating.
        int brokenRib = rand.nextInt(3);
        for (int d = -halfLength + 3, rib = 0; d <= halfLength - 3; d += 3, rib++) {
            int peak = 2 + rand.nextInt(2);
            for (int sign : new int[]{-1, 1}) {
                int spine = findFloor(world, offset(position, alongX, d, 0, 0));
                int previousY = (spine < 0 ? floor : spine) + 2;
                int tip = rib == brokenRib && sign == 1 ? ribRadius - 2 : ribRadius;
                for (int lateral = 1; lateral <= tip; lateral++) {
                    int local = findFloor(world, offset(position, alongX, d, sign * lateral, 0));
                    if (local < 0) break;
                    int y = local + 1 + (int) Math.round(peak * Math.sin(Math.PI * (lateral - 0.5) / ribRadius));
                    int lower = Math.min(previousY, y);
                    int upper = Math.max(previousY, y);
                    for (int jointY = lower; jointY <= upper; jointY++) {
                        place(world, offset(position, alongX, d, sign * lateral, jointY),
                                jointY == y ? (alongX ? ALONG_Z : ALONG_X) : VERTICAL);
                    }
                    previousY = y;
                }
            }
        }

        // Vertebrae and a tapered tail, with a few missing bones for an eroded look.
        int missing = rand.nextInt(halfLength * 2 - 5) - halfLength + 3;
        for (int d = -halfLength; d <= halfLength; d++) {
            if (d == missing || d == missing + 1 || (d > halfLength - 4 && rand.nextInt(4) == 0)) continue;
            int local = findFloor(world, offset(position, alongX, d, 0, 0));
            if (local < 0) continue;
            place(world, offset(position, alongX, d, 0, local + 1), alongX ? ALONG_X : ALONG_Z);
            if (d % 3 == 0 && d < halfLength - 3) {
                place(world, offset(position, alongX, d, 0, local + 2), VERTICAL);
            }
        }

        // Two separated jaw bones mark the head without making a solid skull.
        for (int side : new int[]{-1, 1}) {
            for (int d = -halfLength - 2; d <= -halfLength; d++) {
                int local = findFloor(world, offset(position, alongX, d, side, 0));
                if (local >= 0) {
                    place(world, offset(position, alongX, d, side, local + 1), alongX ? ALONG_X : ALONG_Z);
                }
            }
        }
        return true;
    }

    private BlockPos offset(BlockPos center, boolean alongX, int length, int side, int y) {
        return new BlockPos(center.getX() + (alongX ? length : side), y,
                center.getZ() + (alongX ? side : length));
    }

    private int findFloor(World world, BlockPos column) {
        BlockPos bed = new BlockPos(column.getX(), world.getSeaLevel(), column.getZ());
        if (!DEAD_SEA.equals(world.getBiome(bed).getRegistryName())) return -1;
        for (int i = 0; i < 3 && world.isAirBlock(bed); i++) bed = bed.down();
        if (world.getBlockState(bed).getBlock() != Blocks.WATER) return -1;
        while (bed.getY() > 1 && world.getBlockState(bed.down()).getBlock() == Blocks.WATER) bed = bed.down();
        net.minecraft.block.Block ground = world.getBlockState(bed.down()).getBlock();
        return ground == ModBlocks.black_sand || ground == Blocks.SAND || ground == Blocks.GRAVEL
                || ground == Blocks.STONE || ground == ModBlocks.rocky_stone ? bed.getY() : -1;
    }

    private void place(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlock() == Blocks.WATER || world.isAirBlock(pos)) {
            world.setBlockState(pos, state, 2);
        }
    }
}
