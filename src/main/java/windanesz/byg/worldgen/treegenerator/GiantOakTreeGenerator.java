package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public final class GiantOakTreeGenerator extends WoodlandTreeGenerator {
    private static final int CENTRE = 8;
    private static final int SIZE = 2 * CENTRE + 1;
    private static final int MAX_REACH = 7;

    @Override
    public boolean generate(World world, Random random, BlockPos origin) {
        int bare = 10 + random.nextInt(3);
        int top = bare + 9 + random.nextInt(5);
        if (!this.canStandAt(world, origin, top + 1, MAX_REACH, top + 6)) {
            return false;
        }
        IBlockState oakLog = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
        IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
                .withProperty(BlockLeaves.DECAYABLE, false).withProperty(BlockLeaves.CHECK_DECAY, false);

        int height = top + 6;
        boolean[][][] leaves = new boolean[SIZE][height][SIZE];
        boolean[][][] wood = new boolean[SIZE][height][SIZE];
        for (int y = 0; y <= top; y++) {
            wood[CENTRE][y][CENTRE] = true;
        }

        // Crown core: overlapping blobs stacked up the trunk, then a cap.
        for (int y = bare + 2; y < top - 1; y += 4) {
            blob(leaves, CENTRE, y, CENTRE, 3.4, 0.8, random, bare);
        }
        blob(leaves, CENTRE, top - 1, CENTRE, 4.2, 0.75, random, bare);
        blob(leaves, CENTRE, top + 2, CENTRE, 2.4, 0.9, random, bare);

        // Limbs spiral up the crown; each ends in a leaf blob and may carry another halfway out.
        int limbs = 6 + random.nextInt(3);
        double angle = random.nextDouble() * Math.PI * 2;
        int[][] limbLogs = new int[limbs * 6][];
        int logCount = 0;
        for (int i = 0; i < limbs; i++) {
            angle += Math.PI * 2 / limbs + (random.nextDouble() - 0.5) * 0.6;
            int startY = bare + 1 + (int) ((top - bare - 5) * (i / (double) limbs)) + random.nextInt(2);
            int length = 3 + random.nextInt(2);
            int rise = random.nextInt(2);
            int previousX = 0;
            int previousZ = 0;
            int lastX = 0;
            int lastY = startY;
            int lastZ = 0;
            for (int step = 1; step <= length; step++) {
                int dx = (int) Math.round(Math.cos(angle) * step);
                int dz = (int) Math.round(Math.sin(angle) * step);
                int y = startY + (rise > 0 ? step / 2 : 0);
                if (logCount < limbLogs.length) {
                    limbLogs[logCount++] = new int[]{dx, y, dz, Math.abs(dx - previousX) >= Math.abs(dz - previousZ) ? 0 : 2};
                }
                wood[CENTRE + dx][y][CENTRE + dz] = true;
                previousX = dx;
                previousZ = dz;
                lastX = dx;
                lastY = y;
                lastZ = dz;
                if (step == length / 2 && random.nextBoolean()) {
                    blob(leaves, CENTRE + dx, y + 1, CENTRE + dz, 2.3, 0.8, random, bare);
                }
            }
            blob(leaves, CENTRE + lastX, lastY + 1, CENTRE + lastZ, 3.0 + random.nextDouble(), 0.8, random, bare);
        }
        pruneIsolated(leaves, wood);

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if (leaves[x][y][z]) {
                        this.placeLeaf(world, origin.add(x - CENTRE, y, z - CENTRE), leaf);
                    }
                }
            }
        }
        for (int y = 0; y <= top; y++) {
            this.placeLog(world, origin.up(y), oakLog.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
        }
        for (int i = 0; i < logCount; i++) {
            int[] limb = limbLogs[i];
            this.placeLog(world, origin.add(limb[0], limb[1], limb[2]),
                    oakLog.withProperty(BlockLog.LOG_AXIS, limb[3] == 0 ? BlockLog.EnumAxis.X : BlockLog.EnumAxis.Z));
        }
        return true;
    }

    /** Fills an ellipsoid with leaves, thinning the rim so the silhouette stays ragged. */
    private static void blob(boolean[][][] cells, int cx, int cy, int cz, double radius, double vertical,
                             Random random, int minY) {
        int reach = (int) Math.ceil(radius);
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dy = -reach; dy <= reach; dy++) {
                for (int dz = -reach; dz <= reach; dz++) {
                    int x = cx + dx;
                    int y = cy + dy;
                    int z = cz + dz;
                    if (y < minY || y >= cells[0].length || x < 1 || z < 1 || x >= SIZE - 1 || z >= SIZE - 1
                            || Math.abs(x - CENTRE) > MAX_REACH || Math.abs(z - CENTRE) > MAX_REACH) {
                        continue;
                    }
                    double distance = (dx * dx + dz * dz) / (radius * radius)
                            + (dy * dy) / (radius * vertical * radius * vertical);
                    if (distance <= 1.0 && (distance < 0.6 || random.nextInt(10) < 7)) {
                        cells[x][y][z] = true;
                    }
                }
            }
        }
    }
}
