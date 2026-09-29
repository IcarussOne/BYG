package windanesz.byg.worldgen;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.blocks.BlockThornBranches;
import windanesz.byg.registry.ModBlocks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

final class AncientForestThornsWorldgen {
    private AncientForestThornsWorldgen() {}

    static void generate(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (ModBlocks.thorn_block == null || ModBlocks.thorn_branches == null) return;
        if (!Config.isFeatureDimension(dimID) || world.isRemote || !BygWorldGenerator.matchesBiome(world, chunkX, chunkZ, "byg:byg_ancient_forest")
                || random.nextInt(1000000) >= Config.scaleTemplateChance(150000)) return;
        for (int attempt = 0; attempt < 3; attempt++) {
            int x = chunkX + 4 + random.nextInt(8);
            int z = chunkZ + 4 + random.nextInt(8);
            if (placePatch(world, random, x, z, random.nextBoolean())) return;
        }
    }

    private static boolean placePatch(World world, Random random, int x, int z, boolean tall) {
        BlockPos ground = AncientForestFloorWorldgen.findGround(world, x, z);
        if (ground == null || !world.isAirBlock(ground.up())) return false;
        Map<BlockPos, IBlockState> shape = new LinkedHashMap<>();
        put(shape, ground.up(), EnumFacing.SOUTH);
        int arms = tall ? 5 + random.nextInt(3) : 4 + random.nextInt(3);
        double turn = random.nextDouble() * Math.PI * 2;
        for (int arm = 0; arm < arms; arm++) {
            double angle = turn + arm * Math.PI * 2 / arms + (random.nextDouble() - 0.5) * 0.35;
            int length = (tall ? 4 : 3) + random.nextInt(3);
            int previousX = x;
            int previousZ = z;
            int previousY = ground.getY() + 1;
            for (int step = 1; step <= length; step++) {
                int nextX = x + (int) Math.round(Math.cos(angle) * step);
                int nextZ = z + (int) Math.round(Math.sin(angle) * step);
                BlockPos localGround = AncientForestFloorWorldgen.findGround(world, nextX, nextZ);
                if (localGround == null || Math.abs(localGround.getY() - ground.getY()) > 2) break;
                int lift = tall && step > 1 && step < length - 1 ? (step == 3 ? 2 : 1) : 0;
                int nextY = Math.max(localGround.getY() + 1, ground.getY() + 1 + lift);
                if (Math.abs(nextY - previousY) > 2) break;
                for (int y = Math.min(previousY, nextY); y <= Math.max(previousY, nextY); y++) {
                    put(shape, new BlockPos(previousX, y, previousZ), EnumFacing.SOUTH);
                }
                int walkX = previousX;
                if (nextX != previousX) {
                    put(shape, new BlockPos(previousX, nextY, previousZ), EnumFacing.UP);
                }
                while (walkX != nextX) {
                    walkX += Integer.signum(nextX - walkX);
                    put(shape, new BlockPos(walkX, nextY, previousZ), EnumFacing.UP);
                }
                int walkZ = previousZ;
                if (nextZ != previousZ) {
                    put(shape, new BlockPos(nextX, nextY, previousZ), EnumFacing.EAST);
                }
                while (walkZ != nextZ) {
                    walkZ += Integer.signum(nextZ - walkZ);
                    put(shape, new BlockPos(nextX, nextY, walkZ), EnumFacing.EAST);
                }
                if (step >= 2 && step < length && random.nextInt(3) == 0) {
                    int sideX = -Integer.signum(nextZ - z);
                    int sideZ = Integer.signum(nextX - x);
                    if (random.nextBoolean()) { sideX = -sideX; sideZ = -sideZ; }
                    if (sideX != 0 && sideZ != 0) {
                        put(shape, new BlockPos(nextX + sideX, nextY, nextZ), EnumFacing.UP);
                    }
                    put(shape, new BlockPos(nextX + sideX, nextY, nextZ + sideZ),
                            sideX != 0 ? EnumFacing.UP : EnumFacing.EAST);
                }
                previousX = nextX;
                previousZ = nextZ;
                previousY = nextY;
            }
        }
        if (shape.size() < 14) return false;
        // Keep the whole patch within its biome and away from trunks and stone.
        for (BlockPos pos : shape.keySet()) {
            if (!BygWorldGenerator.matchesBiome(world, pos.getX(), pos.getZ(), "byg:byg_ancient_forest")) return false;
            IBlockState existing = world.getBlockState(pos);
            if (existing.getMaterial() != Material.AIR && !existing.getBlock().isReplaceable(world, pos)) return false;
        }
        for (Map.Entry<BlockPos, IBlockState> entry : shape.entrySet()) {
            world.setBlockState(entry.getKey(), entry.getValue(), 2);
        }
        return true;
    }

    private static void put(Map<BlockPos, IBlockState> shape, BlockPos pos, EnumFacing direction) {
        IBlockState previous = shape.get(pos);
        if (previous != null && (previous.getBlock() == ModBlocks.thorn_block
                || previous.getValue(BlockThornBranches.FACING) != direction)) {
            shape.put(pos, ModBlocks.thorn_block.getDefaultState());
        } else {
            shape.put(pos, ModBlocks.thorn_branches.getDefaultState().withProperty(BlockThornBranches.FACING, direction));
        }
    }
}
