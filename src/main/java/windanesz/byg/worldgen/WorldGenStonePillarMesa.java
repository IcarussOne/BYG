package windanesz.byg.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenStonePillarMesa extends WorldGenerator {

    private static final IBlockState HARDENED_CLAY = Blocks.HARDENED_CLAY.getDefaultState();
    private static final IBlockState RED_CLAY = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(14);
    private static final IBlockState DIRT = Blocks.DIRT.getDefaultState();
    private static final IBlockState COARSE_DIRT = Blocks.DIRT.getStateFromMeta(1);
    private static final IBlockState GRASS = Blocks.GRASS.getDefaultState();

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        BlockPos surface = findSurface(world, position);
        if (surface == null || surface.getY() < 62) {
            return false;
        }

        int sizeTierRoll = rand.nextInt(5);
        int sizeTier = sizeTierRoll == 0 ? 2 : (sizeTierRoll <= 2 ? 1 : 0);
        boolean large = sizeTier > 0;
        boolean huge = sizeTier == 2;
        boolean grandDisc = huge && rand.nextInt(3) == 0;
        boolean grandPillar = huge && !grandDisc && rand.nextInt(3) == 0;
        boolean plateByThree = !grandDisc && !grandPillar && large && rand.nextInt(huge ? 3 : 4) == 0;
        int shaftRadius = grandPillar ? 9 + rand.nextInt(3) : (huge ? 7 + rand.nextInt(2) : (large ? 5 + rand.nextInt(2) : 4 + rand.nextInt(2)));
        int capRadiusX = shaftRadius + (grandPillar ? 15 + rand.nextInt(6) : (huge ? 11 + rand.nextInt(5) : (large ? 8 + rand.nextInt(4) : 6 + rand.nextInt(3)))) + (plateByThree ? 3 : 0) + (grandDisc ? 4 : 0);
        int capRadiusZ = shaftRadius + (grandPillar ? 14 + rand.nextInt(6) : (huge ? 10 + rand.nextInt(5) : (large ? 7 + rand.nextInt(4) : 5 + rand.nextInt(3)))) + (plateByThree ? 3 : 0) + (grandDisc ? 4 : 0);
        int height = grandPillar ? 31 + rand.nextInt(8) : (huge ? 26 + rand.nextInt(8) : 21 + rand.nextInt(8));
        int flareHeight = grandPillar ? 16 + rand.nextInt(5) : (huge ? 12 + rand.nextInt(4) : (large ? 9 + rand.nextInt(4) : 7 + rand.nextInt(3)));
        int capThickness = (grandPillar ? 4 + rand.nextInt(2) : (huge ? 3 + rand.nextInt(2) : 2 + rand.nextInt(2))) + (grandDisc ? 1 : 0);
        int embedDepth = 2 + rand.nextInt(2);
        int baseY = Math.max(5, surface.getY() - embedDepth);
        int topY = baseY + height;
        int maxCapRadius = Math.max(capRadiusX, capRadiusZ);
        int clearanceRadius = maxCapRadius + 8;
        BlockPos min = new BlockPos(surface.getX() - clearanceRadius, baseY - 12, surface.getZ() - clearanceRadius);
        BlockPos max = new BlockPos(surface.getX() + clearanceRadius, topY + capThickness + 12, surface.getZ() + clearanceRadius);

        if (!world.isAreaLoaded(min, max)) {
            return false;
        }
        if (!canAnchorTo(world.getBlockState(surface).getBlock())) {
            return false;
        }

        long shapeSeed = world.getSeed() ^ rand.nextLong();
        int flareStartY = topY - flareHeight;
        int capShiftX = rand.nextInt(3) - 1;
        int capShiftZ = rand.nextInt(3) - 1;
        int capCenterX = surface.getX() + capShiftX;
        int capCenterZ = surface.getZ() + capShiftZ;
        List<SupportColumn> supports = createSupportColumns(rand, large, huge, plateByThree, grandDisc, grandPillar, surface.getX(), surface.getZ(), capRadiusX, capRadiusZ, shaftRadius);

        for (SupportColumn support : supports) {
            buildSupportColumn(world, baseY, topY, flareStartY, support, capCenterX, capCenterZ, shaftRadius, shapeSeed);
        }
        int lobeShiftX = (rand.nextBoolean() ? 1 : -1) * (capRadiusX / 4 + rand.nextInt(3));
        int lobeShiftZ = (rand.nextBoolean() ? 1 : -1) * (capRadiusZ / 4 + rand.nextInt(3));
        int lobeRadiusX = Math.max(5, capRadiusX - 2 - rand.nextInt(2));
        int lobeRadiusZ = Math.max(5, capRadiusZ - 2 - rand.nextInt(2));
        boolean domedTop = rand.nextBoolean();

        buildUpperMesaShoulder(world, flareStartY, topY, capCenterX, capCenterZ, shaftRadius, capRadiusX, capRadiusZ,
                lobeShiftX, lobeShiftZ, lobeRadiusX, lobeRadiusZ, grandPillar, huge, shapeSeed);

        for (int layer = 0; layer < capThickness; layer++) {
            float layerInset = layer == 0 ? 1.0f : 0.0f;
            fillNoisyMesaLayer(world, capCenterX, topY + layer, capCenterZ, capRadiusX - layerInset, capRadiusZ - layerInset,
                    capCenterX + lobeShiftX, capCenterZ + lobeShiftZ, lobeRadiusX - layerInset, lobeRadiusZ - layerInset, 0.18f,
                    shapeSeed + 1000L + layer * 43L);
        }
        sealCapUnderside(world, capCenterX, topY, capCenterZ, capRadiusX - 1.0f, capRadiusZ - 1.0f,
                capCenterX + lobeShiftX, capCenterZ + lobeShiftZ, lobeRadiusX - 1.0f, lobeRadiusZ - 1.0f, shapeSeed + 1400L);

        for (SupportColumn support : supports) {
            smoothBaseToGround(world, support.x, baseY + 2, support.z, support.baseRadius + 2, shapeSeed + support.x * 17L + support.z * 29L);
        }
        List<BlockPos> topPositions = buildTopSurface(world, rand, capCenterX, topY + capThickness, capCenterZ, capRadiusX, capRadiusZ,
                capCenterX + lobeShiftX, capCenterZ + lobeShiftZ, lobeRadiusX, lobeRadiusZ, domedTop, shapeSeed);
        placeTrees(world, rand, topPositions, large, huge);
        return true;
    }

    private static void buildUpperMesaShoulder(World world, int startY, int topY, int capCenterX, int capCenterZ, int shaftRadius,
                                               int capRadiusX, int capRadiusZ, int lobeShiftX, int lobeShiftZ,
                                               int lobeRadiusX, int lobeRadiusZ, boolean grandPillar, boolean huge, long shapeSeed) {
        int height = Math.max(1, topY - startY);
        float startScale = grandPillar ? 0.62f : (huge ? 0.5f : 0.42f);
        float startLobeScale = grandPillar ? 0.72f : (huge ? 0.58f : 0.46f);
        float startRadiusX = Math.max(shaftRadius + 2.0f, capRadiusX * startScale);
        float startRadiusZ = Math.max(shaftRadius + 2.0f, capRadiusZ * startScale);
        float startLobeRadiusX = Math.max(startRadiusX - 1.0f, lobeRadiusX * startLobeScale);
        float startLobeRadiusZ = Math.max(startRadiusZ - 1.0f, lobeRadiusZ * startLobeScale);

        for (int y = startY; y < topY; y++) {
            float progress = (float) (y - startY) / (float) height;
            float eased = progress * progress * (3.0f - 2.0f * progress);
            float radiusX = lerpFloat(startRadiusX, capRadiusX - 1.0f, eased);
            float radiusZ = lerpFloat(startRadiusZ, capRadiusZ - 1.0f, eased);
            int lobeCenterX = capCenterX + Math.round(lobeShiftX * eased);
            int lobeCenterZ = capCenterZ + Math.round(lobeShiftZ * eased);
            float layerLobeRadiusX = lerpFloat(startLobeRadiusX, lobeRadiusX - 1.0f, eased);
            float layerLobeRadiusZ = lerpFloat(startLobeRadiusZ, lobeRadiusZ - 1.0f, eased);
            float edgeNoiseScale = y >= topY - 3 ? 0.14f : 0.08f;
            fillNoisyMesaLayer(world, capCenterX, y, capCenterZ, radiusX, radiusZ,
                    lobeCenterX, lobeCenterZ, layerLobeRadiusX, layerLobeRadiusZ, edgeNoiseScale,
                    shapeSeed + 820L + y * 29L);
        }
    }

    private void buildSupportColumn(World world, int baseY, int topY, int flareStartY, SupportColumn support, int capCenterX, int capCenterZ, int shaftRadius, long shapeSeed) {
        int supportTopY = topY - support.topYOffset;
        int supportFlareStartY = support.topRadius > support.baseRadius
                ? Math.max(baseY + 3, Math.min(baseY + (supportTopY - baseY) / 3, supportTopY - 8))
                : Math.min(flareStartY, supportTopY - 3);
        for (int y = baseY; y < supportTopY; y++) {
            boolean isFlareLayer = y >= supportFlareStartY;
            int centerX = support.x;
            int centerZ = support.z;
            if (isFlareLayer) {
                float flareShiftProgress = (float) (y - supportFlareStartY) / (float) Math.max(1, supportTopY - supportFlareStartY);
                centerX += Math.round((capCenterX - support.x) * flareShiftProgress * 0.55f);
                centerZ += Math.round((capCenterZ - support.z) * flareShiftProgress * 0.55f);
            }

            float heightProgress = (float) (y - baseY) / (float) Math.max(1, supportTopY - baseY - 1);
            float widenProgress = support.topRadius > support.baseRadius ? Math.max(0.0f, (heightProgress - 0.05f) / 0.95f) : 0.0f;
            float easedWiden = widenProgress * widenProgress * (3.0f - 2.0f * widenProgress);
            int radius = support.baseRadius + Math.round((support.topRadius - support.baseRadius) * easedWiden);
            if (y <= baseY + 2) {
                radius += 1;
            }
            if (isFlareLayer) {
                float flareProgress = (float) (y - supportFlareStartY) / (float) Math.max(1, supportTopY - supportFlareStartY);
                radius = Math.max(radius, support.baseRadius + Math.round((support.topRadius - support.baseRadius) * (0.2f + flareProgress * flareProgress * 0.8f)));
                radius += Math.round((shaftRadius - support.baseRadius + 2) * flareProgress * flareProgress * 0.35f);
                radius += Math.round(noiseValue(centerX, y, centerZ, shapeSeed + support.x * 13L + support.z * 19L) * 0.35f);
            }
            radius = Math.max(support.baseRadius, radius);

            if (isFlareLayer) {
                fillNoisyDisc(world, centerX, y, centerZ, radius, 0.32f, shapeSeed + y * 31L + support.x * 13L + support.z * 19L);
            } else {
                fillDisc(world, centerX, y, centerZ, radius, shapeSeed + y * 31L + support.x * 13L + support.z * 19L);
            }
        }
    }

    private List<BlockPos> buildTopSurface(World world, Random rand, int centerX, int surfaceY, int centerZ,
                                           int capRadiusX, int capRadiusZ, int lobeCenterX, int lobeCenterZ,
                                           int lobeRadiusX, int lobeRadiusZ, boolean domedTop, long shapeSeed) {
        List<BlockPos> topPositions = new ArrayList<>();
        int maxRadiusX = Math.max(capRadiusX, lobeRadiusX);
        int maxRadiusZ = Math.max(capRadiusZ, lobeRadiusZ);

        for (int dx = -maxRadiusX - 2; dx <= maxRadiusX + 2; dx++) {
            for (int dz = -maxRadiusZ - 2; dz <= maxRadiusZ + 2; dz++) {
                int worldX = centerX + dx;
                int worldZ = centerZ + dz;
                if (!isInsideMesaTop(worldX, worldZ, centerX, centerZ, capRadiusX, capRadiusZ, lobeCenterX, lobeCenterZ, lobeRadiusX, lobeRadiusZ, surfaceY, shapeSeed)) {
                    continue;
                }

                float mainDistance = ellipseDistanceSq(worldX, worldZ, centerX, centerZ, Math.max(1.0f, capRadiusX - 4.0f), Math.max(1.0f, capRadiusZ - 4.0f));
                float lobeDistance = ellipseDistanceSq(worldX, worldZ, lobeCenterX, lobeCenterZ, Math.max(1.0f, lobeRadiusX - 3.0f), Math.max(1.0f, lobeRadiusZ - 3.0f));
                boolean fertileCore = mainDistance <= 1.0f || lobeDistance <= 1.0f;

                float topNoise = noiseValue(worldX, surfaceY + 7, worldZ, shapeSeed + 3000L);
                int rise = domedTop ? getTopRise(worldX, surfaceY, worldZ, Math.min(mainDistance, lobeDistance), shapeSeed) : 0;
                BlockPos basePos = new BlockPos(worldX, surfaceY - 1 + rise, worldZ);
                BlockPos topPos = basePos.up();

                clearAbove(world, topPos, 7);

                if (topNoise > 0.0f || (fertileCore && topNoise > -0.30f)) {
                    fillTopColumn(world, worldX, surfaceY - 1, worldZ, basePos.getY(), DIRT);
                    world.setBlockState(basePos, DIRT, 2);
                    world.setBlockState(topPos, GRASS, 2);
                    if (fertileCore) {
                        topPositions.add(topPos);
                    }
                } else {
                    fillTopColumn(world, worldX, surfaceY - 1, worldZ, basePos.getY(), COARSE_DIRT);
                    world.setBlockState(basePos, COARSE_DIRT, 2);
                    world.setBlockState(topPos, topNoise > -0.15f ? DIRT : COARSE_DIRT, 2);
                }
            }
        }

        if (topPositions.isEmpty()) {
            BlockPos fallback = new BlockPos(centerX, surfaceY, centerZ);
            world.setBlockState(fallback.down(), DIRT, 2);
            world.setBlockState(fallback, GRASS, 2);
            topPositions.add(fallback);
        }
        return topPositions;
    }

    private static int getTopRise(int x, int surfaceY, int z, float mesaDistance, long shapeSeed) {
        float centerWeight = Math.max(0.0f, 1.0f - mesaDistance);
        if (centerWeight <= 0.08f) {
            return 0;
        }

        float moundNoise = 0.8f + noiseValue(x, surfaceY + 11, z, shapeSeed + 5000L) * 0.4f;
        float riseStrength = centerWeight * centerWeight * (2.4f + moundNoise);
        int rise = Math.round(riseStrength);
        return Math.min(3, Math.max(0, rise));
    }

    private static void fillTopColumn(World world, int x, int startY, int z, int endY, IBlockState fillState) {
        for (int y = startY; y < endY; y++) {
            world.setBlockState(new BlockPos(x, y, z), fillState, 2);
        }
    }

    private static void fillTopClayColumn(World world, int x, int startY, int z, int endY, long shapeSeed) {
        for (int y = startY; y < endY; y++) {
            world.setBlockState(new BlockPos(x, y, z), chooseClayState(x, y, z, shapeSeed + y * 17L), 2);
        }
    }

    private void smoothBaseToGround(World world, int centerX, int startY, int centerZ, int radius, long shapeSeed) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                float effectiveRadius = radius + noiseValue(centerX + dx, startY, centerZ + dz, shapeSeed + 4000L);
                if (dx * dx + dz * dz > effectiveRadius * effectiveRadius) {
                    continue;
                }

                int supportStartY = startY + Math.max(0, Math.round((1.0f - ((float) (dx * dx + dz * dz) / (float) Math.max(1, radius * radius))) * 3.0f));
                fillDownToGround(world, new BlockPos(centerX + dx, supportStartY, centerZ + dz), shapeSeed);
            }
        }
    }

    private static void fillDownToGround(World world, BlockPos start, long shapeSeed) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(start);
        while (cursor.getY() > 1) {
            IBlockState state = world.getBlockState(cursor);
            Material material = state.getMaterial();
            if (!material.isReplaceable() && !material.isLiquid() && !state.getBlock().isLeaves(state, world, cursor)) {
                break;
            }
            world.setBlockState(cursor, chooseClayState(cursor.getX(), cursor.getY(), cursor.getZ(), shapeSeed), 2);
            cursor.move(EnumFacing.DOWN);
        }
    }

    private static void fillDisc(World world, int centerX, int y, int centerZ, int radius, long shapeSeed) {
        int radiusSq = radius * radius;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radiusSq) {
                    world.setBlockState(new BlockPos(centerX + dx, y, centerZ + dz), chooseClayState(centerX + dx, y, centerZ + dz, shapeSeed), 2);
                }
            }
        }
    }

    private static void fillNoisyDisc(World world, int centerX, int y, int centerZ, int radius, float edgeNoiseScale, long shapeSeed) {
        for (int dx = -radius - 1; dx <= radius + 1; dx++) {
            for (int dz = -radius - 1; dz <= radius + 1; dz++) {
                float effectiveRadius = radius + noiseValue(centerX + dx, y, centerZ + dz, shapeSeed) * edgeNoiseScale;
                if (dx * dx + dz * dz <= effectiveRadius * effectiveRadius) {
                    world.setBlockState(new BlockPos(centerX + dx, y, centerZ + dz), chooseClayState(centerX + dx, y, centerZ + dz, shapeSeed), 2);
                }
            }
        }
    }

    private static void fillNoisyMesaLayer(World world, int centerX, int y, int centerZ, float radiusX, float radiusZ,
                                           int lobeCenterX, int lobeCenterZ, float lobeRadiusX, float lobeRadiusZ,
                                           float edgeNoiseScale, long shapeSeed) {
        int maxRadiusX = Math.max(Math.round(radiusX), Math.round(lobeRadiusX));
        int maxRadiusZ = Math.max(Math.round(radiusZ), Math.round(lobeRadiusZ));
        for (int dx = -maxRadiusX - 2; dx <= maxRadiusX + 2; dx++) {
            for (int dz = -maxRadiusZ - 2; dz <= maxRadiusZ + 2; dz++) {
                int worldX = centerX + dx;
                int worldZ = centerZ + dz;
                if (isInsideMesaTop(worldX, worldZ, centerX, centerZ, radiusX, radiusZ, lobeCenterX, lobeCenterZ, lobeRadiusX, lobeRadiusZ, y, shapeSeed + 700L, edgeNoiseScale)) {
                    world.setBlockState(new BlockPos(worldX, y, worldZ), chooseClayState(worldX, y, worldZ, shapeSeed), 2);
                }
            }
        }
    }

    private static void sealCapUnderside(World world, int centerX, int y, int centerZ, float radiusX, float radiusZ,
                                         int lobeCenterX, int lobeCenterZ, float lobeRadiusX, float lobeRadiusZ, long shapeSeed) {
        int maxRadiusX = Math.max(Math.round(radiusX), Math.round(lobeRadiusX));
        int maxRadiusZ = Math.max(Math.round(radiusZ), Math.round(lobeRadiusZ));
        for (int dx = -maxRadiusX - 1; dx <= maxRadiusX + 1; dx++) {
            for (int dz = -maxRadiusZ - 1; dz <= maxRadiusZ + 1; dz++) {
                int worldX = centerX + dx;
                int worldZ = centerZ + dz;
                if (!isInsideMesaTop(worldX, worldZ, centerX, centerZ, radiusX, radiusZ, lobeCenterX, lobeCenterZ, lobeRadiusX, lobeRadiusZ, y, shapeSeed, 0.08f)) {
                    continue;
                }

                BlockPos capPos = new BlockPos(worldX, y, worldZ);
                if (world.isAirBlock(capPos)) {
                    continue;
                }

                int gapDepth = 0;
                BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(worldX, y - 1, worldZ);
                while (cursor.getY() > 1 && gapDepth < 6 && world.isAirBlock(cursor)) {
                    gapDepth++;
                    cursor.move(EnumFacing.DOWN);
                }

                if (gapDepth > 0 && !world.isAirBlock(cursor)) {
                    for (int fillY = y - 1; fillY > y - 1 - gapDepth; fillY--) {
                        world.setBlockState(new BlockPos(worldX, fillY, worldZ), chooseClayState(worldX, fillY, worldZ, shapeSeed + fillY * 23L), 2);
                    }
                }
            }
        }
    }

    private static boolean isInsideMesaTop(int x, int z, int centerX, int centerZ, float radiusX, float radiusZ,
                                           int lobeCenterX, int lobeCenterZ, float lobeRadiusX, float lobeRadiusZ,
                                           int y, long shapeSeed) {
        return isInsideMesaTop(x, z, centerX, centerZ, radiusX, radiusZ, lobeCenterX, lobeCenterZ, lobeRadiusX, lobeRadiusZ, y, shapeSeed, 0.12f);
    }

    private static boolean isInsideMesaTop(int x, int z, int centerX, int centerZ, float radiusX, float radiusZ,
                                           int lobeCenterX, int lobeCenterZ, float lobeRadiusX, float lobeRadiusZ,
                                           int y, long shapeSeed, float edgeNoiseScale) {
        float mainDistance = ellipseDistanceSq(x, z, centerX, centerZ, radiusX, radiusZ);
        float lobeDistance = ellipseDistanceSq(x, z, lobeCenterX, lobeCenterZ, lobeRadiusX, lobeRadiusZ);
        float edgeNoise = noiseValue(x, y, z, shapeSeed) * edgeNoiseScale;
        return mainDistance <= 1.0f + edgeNoise || lobeDistance <= 1.0f + edgeNoise * 0.85f;
    }

    private static float ellipseDistanceSq(int x, int z, int centerX, int centerZ, float radiusX, float radiusZ) {
        float nx = (x - centerX) / Math.max(1.0f, radiusX);
        float nz = (z - centerZ) / Math.max(1.0f, radiusZ);
        return nx * nx + nz * nz;
    }

    private static IBlockState chooseClayState(int x, int y, int z, long shapeSeed) {
        float noise = noiseValue(x, y / 2, z, shapeSeed);
        if (noise < -0.55f) {
            return RED_CLAY;
        }
        return HARDENED_CLAY;
    }

    private static float noiseValue(int x, int y, int z, long seed) {
        long hash = seed;
        hash ^= (long) x * 341873128712L;
        hash ^= (long) y * 132897987541L;
        hash ^= (long) z * 42317861L;
        hash = hash * hash * 42317861L + hash * 11L;
        return ((hash >>> 16) & 1023L) / 511.5f - 1.0f;
    }

    private static int lerpInt(int start, int end, float progress) {
        return Math.round(start + (end - start) * progress);
    }

    private static float lerpFloat(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    private static BlockPos findSurface(World world, BlockPos origin) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(origin.getX(), world.getHeight(origin).getY(), origin.getZ());
        while (cursor.getY() > 1) {
            IBlockState state = world.getBlockState(cursor);
            Material material = state.getMaterial();
            if (!material.isReplaceable() && !material.isLiquid()) {
                return cursor.toImmutable();
            }
            cursor.move(EnumFacing.DOWN);
        }
        return null;
    }

    private static boolean canAnchorTo(Block block) {
        return block == Blocks.GRASS
                || block == Blocks.DIRT
                || block == Blocks.STONE
                || block == Blocks.HARDENED_CLAY
                || block == Blocks.STAINED_HARDENED_CLAY
                || block == Blocks.SANDSTONE
                || block == Blocks.RED_SANDSTONE
                || block == Blocks.SAND;
    }

    private static void clearAbove(World world, BlockPos start, int height) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(start);
        for (int i = 0; i < height; i++) {
            IBlockState state = world.getBlockState(cursor);
            if (state.getMaterial().isReplaceable() || state.getMaterial().isLiquid() || state.getBlock().isLeaves(state, world, cursor)) {
                world.setBlockToAir(cursor);
            }
            cursor.move(EnumFacing.UP);
        }
    }

    private static void placeTrees(World world, Random rand, List<BlockPos> topPositions, boolean large, boolean huge) {
        if (rand.nextInt(huge ? 5 : (large ? 4 : 3)) == 0) {
            return;
        }

        int targetTrees = huge ? 3 + rand.nextInt(3) : (large ? 2 + rand.nextInt(3) : 1 + rand.nextInt(2));
        int attempts = huge ? 28 : (large ? 18 : 10);
        List<BlockPos> placed = new ArrayList<>();

        for (int i = 0; i < attempts && placed.size() < targetTrees; i++) {
            BlockPos candidate = topPositions.get(rand.nextInt(topPositions.size()));
            if (!hasTreeSpacing(candidate, placed, large ? 5 : 6)) {
                continue;
            }

            BlockPos treePos = candidate.up();
            WorldGenAbstractTree tree = new WorldGenSavannaTree(false);
            tree.setDecorationDefaults();
            if (tree.generate(world, rand, treePos)) {
                placed.add(treePos);
            }
        }
    }

    private static boolean hasTreeSpacing(BlockPos candidate, List<BlockPos> placed, int minDistance) {
        for (BlockPos existing : placed) {
            int dx = candidate.getX() - existing.getX();
            int dz = candidate.getZ() - existing.getZ();
            if (dx * dx + dz * dz < minDistance * minDistance) {
                return false;
            }
        }
        return true;
    }

    private static List<SupportColumn> createSupportColumns(Random rand, boolean large, boolean huge, boolean plateByThree, boolean grandDisc, boolean grandPillar, int centerX, int centerZ, int capRadiusX, int capRadiusZ, int shaftRadius) {
        List<SupportColumn> supports = new ArrayList<>();
        if (grandPillar) {
            int topRadius = Math.max(shaftRadius + 8, Math.min(capRadiusX, capRadiusZ) / 2 + 3);
            supports.add(new SupportColumn(centerX, centerZ, shaftRadius, topRadius, 0));
            return supports;
        }

        if (grandDisc) {
            int supportRadius = Math.max(4, shaftRadius - 1);
            int offsetX = Math.max(6, capRadiusX / 2);
            int offsetZ = Math.max(6, capRadiusZ / 2);
            int centerTopRadius = Math.max(shaftRadius + 6, Math.min(capRadiusX, capRadiusZ) / 2);
            int outerTopRadius = Math.max(supportRadius + 8, Math.min(capRadiusX, capRadiusZ) - 1);
            supports.add(new SupportColumn(centerX, centerZ, shaftRadius, centerTopRadius, 0));
            supports.add(new SupportColumn(centerX - offsetX, centerZ, supportRadius, outerTopRadius, 1 + rand.nextInt(2)));
            supports.add(new SupportColumn(centerX + offsetX, centerZ, supportRadius, outerTopRadius, 1 + rand.nextInt(2)));
            supports.add(new SupportColumn(centerX, centerZ - offsetZ, supportRadius, outerTopRadius, 1 + rand.nextInt(2)));
            supports.add(new SupportColumn(centerX, centerZ + offsetZ, supportRadius, outerTopRadius, 1 + rand.nextInt(2)));
            return supports;
        }

        if (plateByThree) {
            int supportRadius = Math.max(3, shaftRadius - 1);
            int offsetX = Math.max(4, capRadiusX / 2);
            int offsetZ = Math.max(4, capRadiusZ / 2);
            int topRadius = Math.max(supportRadius + 7, Math.min(capRadiusX, capRadiusZ) - 1);
            supports.add(new SupportColumn(centerX - offsetX, centerZ - offsetZ / 2, supportRadius, topRadius, 1 + rand.nextInt(2)));
            supports.add(new SupportColumn(centerX + offsetX, centerZ - offsetZ / 2, supportRadius, topRadius, 1 + rand.nextInt(2)));
            supports.add(new SupportColumn(centerX, centerZ + offsetZ, supportRadius, topRadius, 1 + rand.nextInt(2)));
            return supports;
        }

        int centerTopRadius = huge ? shaftRadius + 5 : (large && rand.nextInt(4) == 0 ? shaftRadius + 3 : shaftRadius);
        supports.add(new SupportColumn(centerX, centerZ, shaftRadius, centerTopRadius, 0));

        int extraSupports = huge ? 2 + rand.nextInt(2) : (large ? 1 + rand.nextInt(2) : (rand.nextInt(3) == 0 ? 1 : 0));
        for (int i = 0; i < extraSupports; i++) {
            int offsetX = (rand.nextBoolean() ? 1 : -1) * (capRadiusX / 3 + rand.nextInt(Math.max(2, capRadiusX / 3)));
            int offsetZ = (rand.nextBoolean() ? 1 : -1) * (capRadiusZ / 3 + rand.nextInt(Math.max(2, capRadiusZ / 3)));
            int radius = Math.max(huge ? 4 : 3, shaftRadius - 1 - rand.nextInt(huge ? 1 : 2));
            int topYOffset = 1 + rand.nextInt(huge ? 3 : 4);
            int topRadius = large && rand.nextInt(3) == 0
                    ? Math.max(radius + (huge ? 6 + rand.nextInt(3) : 4 + rand.nextInt(2)), Math.min(capRadiusX, capRadiusZ) / 3)
                    : radius;
            supports.add(new SupportColumn(centerX + offsetX, centerZ + offsetZ, radius, topRadius, topYOffset));
        }
        return supports;
    }

    private static final class SupportColumn {
        private final int x;
        private final int z;
        private final int baseRadius;
        private final int topRadius;
        private final int topYOffset;

        private SupportColumn(int x, int z, int baseRadius, int topRadius, int topYOffset) {
            this.x = x;
            this.z = z;
            this.baseRadius = baseRadius;
            this.topRadius = topRadius;
            this.topYOffset = topYOffset;
        }
    }
}

