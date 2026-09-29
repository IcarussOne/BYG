package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.worldgen.AncientForestFloorWorldgen;
import windanesz.byg.worldgen.BygTreePlacement;
import windanesz.byg.worldgen.BygWorldGenerator;

import java.util.Random;

public final class AncientForestWorldTreeWorldgen {
    private static final int RADIUS = 13;
    private static final int HEIGHT = 126;

    private AncientForestWorldTreeWorldgen() {}

    public static void generate(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || world.isRemote || !BygWorldGenerator.matchesBiome(world, chunkX, chunkZ, "byg:byg_ancient_forest")
                || Config.scaleTemplateChance(700) <= random.nextInt(1000000)) return;
        // The +8 decoration offset makes this 27-block footprint fit inside
        // Forge's loaded 2x2 chunk window, with two blocks left at each edge.
        int x = chunkX + 7;
        int z = chunkZ + 7;
        BlockPos ground = AncientForestFloorWorldgen.findGround(world, x, z);
        if (ground == null || ground.getY() + HEIGHT + 4 >= world.getActualHeight()) return;
        for (int dx = -RADIUS; dx <= RADIUS; dx += 4) {
            for (int dz = -RADIUS; dz <= RADIUS; dz += 4) {
                if (!BygWorldGenerator.matchesBiome(world, x + dx, z + dz, "byg:byg_ancient_forest")) return;
            }
        }
        int supported = 0;
        for (int dx = -5; dx <= 5; dx += 5) {
            for (int dz = -5; dz <= 5; dz += 5) {
                BlockPos sample = AncientForestFloorWorldgen.findGround(world, x + dx, z + dz);
                if (sample != null && Math.abs(sample.getY() - ground.getY()) <= 3) supported++;
            }
        }
        if (supported < 7) return;
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                if (dx * dx + dz * dz > 16) continue;
                for (int dy = 1; dy <= 8; dy++) {
                    BlockPos pos = ground.add(dx, dy, dz);
                    if (world.getBlockState(pos).getMaterial() == Material.WOOD) return;
                }
            }
        }

        if (!BygTreePlacement.allowTrees(world, random, ground)) return;
        IBlockState log = ModBlocks.great_oak_log.getDefaultState();
        IBlockState wood = ModBlocks.great_oak_wood.getDefaultState();
        IBlockState leaves = ModBlocks.great_oak_leaves.getDefaultState();
        // A broad taper and uneven flutes make the trunk read as ancient at ground level.
        for (int y = 1; y <= HEIGHT; y++) {
            double radius = y < 13 ? 5.6 - y * 0.12 : y < 76 ? 4.05 - (y - 13) * 0.020 : 2.8 - (y - 76) * 0.026;
            for (int dx = -7; dx <= 7; dx++) {
                for (int dz = -7; dz <= 7; dz++) {
                    double angle = Math.atan2(dz, dx);
                    double flute = 0.55 * Math.cos(7 * angle + y * 0.025);
                    if (dx * dx + dz * dz <= (radius + flute) * (radius + flute)) {
                        placeWood(world, ground.add(dx, y, dz), (dx + dz + y) % 7 == 0 ? wood : log);
                    }
                }
            }
        }
        // Buttress roots taper into the surrounding terrain, then break into knuckles.
        for (int arm = 0; arm < 10; arm++) {
            double angle = arm * Math.PI * 2 / 10 + random.nextDouble() * 0.16;
            int length = 9 + random.nextInt(4);
            for (int step = 3; step <= length; step++) {
                int rx = x + (int) Math.round(Math.cos(angle) * step);
                int rz = z + (int) Math.round(Math.sin(angle) * step);
                BlockPos surface = AncientForestFloorWorldgen.findGround(world, rx, rz);
                if (surface == null || Math.abs(surface.getY() - ground.getY()) > 4) continue;
                int rise = Math.max(1, (length - step) * (length - step) / 12);
                for (int dy = 1; dy <= rise; dy++) {
                    placeWood(world, surface.up(dy), dy == rise ? wood : log);
                }
                if (step < length - 2) {
                    int sideX = (int) Math.round(-Math.sin(angle));
                    int sideZ = (int) Math.round(Math.cos(angle));
                    placeWood(world, surface.add(sideX, 1, sideZ), wood);
                }
            }
        }
        // Radial boughs carry layered leaf masses, leaving glimpses of the trunk.
        for (int arm = 0; arm < 13; arm++) {
            double angle = arm * Math.PI * 2 / 13 + random.nextDouble() * 0.16;
            int forkY = 78 + (arm % 5) * 7;
            int reach = 8 + random.nextInt(2);
            for (int step = 2; step <= reach; step++) {
                int bx = x + (int) Math.round(Math.cos(angle) * step);
                int bz = z + (int) Math.round(Math.sin(angle) * step);
                int by = ground.getY() + forkY + step / 3;
                placeWood(world, new BlockPos(bx, by, bz), wood);
                if (step > reach - 4) leafCrown(world, random, bx, by + 2, bz, 3, leaves);
            }
        }
        leafCrown(world, random, x, ground.getY() + HEIGHT - 2, z, 8, leaves);
        leafCrown(world, random, x, ground.getY() + HEIGHT + 2, z, 5, leaves);
        // Light is tucked under the high crown, keeping the silhouette dark by day.
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3;
            BlockPos pos = ground.add((int) Math.round(Math.cos(angle) * 6), 111 + i % 3, (int) Math.round(Math.sin(angle) * 6));
            if (world.getBlockState(pos).getBlock() == ModBlocks.great_oak_leaves) world.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState(), 2);
        }
    }

    private static void placeWood(World world, BlockPos pos, IBlockState wood) {
        IBlockState existing = world.getBlockState(pos);
        if (existing.getMaterial() == Material.AIR || existing.getBlock().isReplaceable(world, pos)
                || existing.getBlock().isLeaves(existing, world, pos)) world.setBlockState(pos, wood, 2);
    }

    private static void leafCrown(World world, Random random, int x, int y, int z, int radius, IBlockState leaves) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -3; dy <= 3; dy++) {
                    double distance = (double) (dx * dx + dz * dz) / (radius * radius) + (double) (dy * dy) / 12;
                    if (distance > 1.0 + random.nextDouble() * 0.25) continue;
                    BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);
                    if (world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                        world.setBlockState(pos, leaves, 2);
                    }
                }
            }
        }
    }
}
