package windanesz.byg.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Builds the crystal canyon mesas from {@link CrystalCanyonShapes} instead of
 * a structure template, so every mesa can be varied before it is written:
 * stretched or squashed in height, recoloured, weathered, and cut through by
 * walkable tunnels and archways.
 */
public final class CrystalCanyonWorldgen {
    public static final String BOTTOM_TEMPLATE = "canyon_crystal_bottom1";

    /** Local y of the ground block; the layers below it are buried in the terrain. */
    private static final int GROUND_Y = 6;
    /** The mesa body (stretchable) spans these source layers; the plateau cap starts above. */
    private static final int BODY_FIRST = 7;
    private static final int BODY_LAST = 19;
    private static final int SPIRE_BURIED_DEPTH = 5;
    private static final int SPIRE_SPACING = 6;
    private static final char[] CRYSTALS = {'B', 'P', 'R', 'W'};

    private static char[][][] source;

    private CrystalCanyonWorldgen() {
    }

    /**
     * @param spawnY world y of local y=0, i.e. ground block y minus {@link #GROUND_Y}
     * @param minX,maxX,minZ,maxZ the inclusive area that is safe to write to
     */
    public static void place(World world, Random random, BlockPos origin, Rotation rotation, Mirror mirror,
                             int minX, int maxX, int minZ, int maxZ) {
        char[][][] mesa = buildMesa(random);
        int spawnY = origin.getY();
        int height = mesa.length;

        PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
        for (int x = 0; x < CrystalCanyonShapes.MESA_X; x++) {
            for (int z = 0; z < CrystalCanyonShapes.WORK_Z; z++) {
                BlockPos offset = Template.transformedBlockPos(settings, new BlockPos(x, 0, z));
                int wx = origin.getX() + offset.getX();
                int wz = origin.getZ() + offset.getZ();
                if (wx < minX || wx > maxX || wz < minZ || wz > maxZ) {
                    continue;
                }
                for (int y = 0; y < height; y++) {
                    char c = mesa[y][z][x];
                    if (c != '.') {
                        world.setBlockState(new BlockPos(wx, spawnY + y, wz), stateFor(c), 2);
                    }
                }
            }
        }

        placeSpires(world, random, spawnY + capLayer(mesa), minX, maxX, minZ, maxZ);
    }

    private static IBlockState stateFor(char c) {
        switch (c) {
            case 'w':
                return ModBlocks.white_sand.getDefaultState();
            case 'B':
                return ModBlocks.light_blue_crystal_block.getDefaultState();
            case 'P':
                return ModBlocks.purple_crystal_block.getDefaultState();
            case 'R':
                return ModBlocks.red_crystal_block.getDefaultState();
            case 'W':
                return ModBlocks.white_crystal_block.getDefaultState();
            default:
                return Blocks.HARDENED_CLAY.getDefaultState();
        }
    }

    private static char[][][] source() {
        if (source == null) {
            char[][][] parsed = new char[CrystalCanyonShapes.MESA_Y][CrystalCanyonShapes.MESA_Z][CrystalCanyonShapes.MESA_X];
            for (int y = 0; y < CrystalCanyonShapes.MESA_Y; y++) {
                String[] rows = CrystalCanyonShapes.MESA[y].split("\\|");
                for (int z = 0; z < CrystalCanyonShapes.MESA_Z; z++) {
                    parsed[y][z] = rows[z].toCharArray();
                }
            }
            source = parsed;
        }
        return source;
    }

    /** Result is indexed [y][z][x]. */
    private static char[][][] buildMesa(Random random) {
        char[][][] src = source();
        int srcBody = BODY_LAST - BODY_FIRST + 1;
        int capLayers = CrystalCanyonShapes.MESA_Y - BODY_LAST - 1;

        // Height variation: resample the body layers between the buried base
        // and the plateau cap, so cliffs get taller or shorter but the cap,
        // crystals and overhangs keep their shape.
        int body = 9 + random.nextInt(13);
        int height = BODY_FIRST + body + capLayers;
        char[][][] mesa = new char[height][CrystalCanyonShapes.WORK_Z][CrystalCanyonShapes.MESA_X];

        // Domain warp: every column is read from a slightly displaced source
        // column, using two octaves of noise, so each mesa gets its own
        // bent, lobed outline. The displacement ignores y, so walls stay
        // vertical and the plateau stays one connected slab.
        long seed = random.nextLong();
        int cols = CrystalCanyonShapes.MESA_X;
        int rows = CrystalCanyonShapes.MESA_Z;
        int[][] warpX = new int[rows][cols];
        int[][] warpZ = new int[rows][cols];
        for (int z = 0; z < rows; z++) {
            for (int x = 0; x < cols; x++) {
                double dx = (noise2(seed, x / 12.0D, z / 12.0D) - 0.5D) * 6.0D
                        + (noise2(seed + 1, x / 5.0D, z / 5.0D) - 0.5D) * 1.6D;
                double dz = (noise2(seed + 2, x / 12.0D, z / 12.0D) - 0.5D) * 5.0D
                        + (noise2(seed + 3, x / 5.0D, z / 5.0D) - 0.5D) * 1.4D;
                warpX[z][x] = x + (int) Math.round(dx);
                warpZ[z][x] = z + (int) Math.round(dz);
            }
        }
        for (int y = 0; y < height; y++) {
            int sy;
            if (y < BODY_FIRST) {
                sy = y;
            } else if (y < BODY_FIRST + body) {
                sy = BODY_FIRST + Math.min(srcBody - 1, (y - BODY_FIRST) * srcBody / body);
            } else {
                sy = BODY_LAST + 1 + (y - BODY_FIRST - body);
            }
            for (int z = 0; z < rows; z++) {
                for (int x = 0; x < cols; x++) {
                    int sx = warpX[z][x];
                    int sz = warpZ[z][x];
                    mesa[y][z][x] = sx >= 0 && sx < cols && sz >= 0 && sz < rows ? src[sy][sz][sx] : '.';
                }
            }
        }

        recolour(mesa, random);
        // at most one tunnel, and up to two gates across it
        if (random.nextInt(100) < 70) {
            carveTunnel(mesa, random, body);
        }
        int firstGate = -100;
        if (random.nextInt(100) < 60) {
            firstGate = 6 + random.nextInt(22);
            carveArchway(mesa, body, firstGate);
        }
        if (random.nextInt(100) < 15) {
            int secondGate = 6 + random.nextInt(22);
            if (Math.abs(secondGate - firstGate) >= 9) {
                carveArchway(mesa, body, secondGate);
            }
        }
        erode(mesa, seed + 4, BODY_FIRST + body);
        removeDetached(mesa);
        return mesa;
    }

    private static int capLayer(char[][][] mesa) {
        // the plateau layer is the first one that is mostly white sand
        for (int y = mesa.length - 1; y >= 0; y--) {
            int sand = 0;
            for (char[] row : mesa[y]) {
                for (char c : row) {
                    if (c == 'w') {
                        sand++;
                    }
                }
            }
            if (sand > 40) {
                return y;
            }
        }
        return BODY_LAST + 1;
    }

    /** Shifts every crystal colour, or turns the whole mesa one or two colours. */
    private static void recolour(char[][][] mesa, Random random) {
        int mode = random.nextInt(10);
        if (mode < 3) {
            return;
        }
        char[] map = new char[128];
        for (int i = 0; i < CRYSTALS.length; i++) {
            map[CRYSTALS[i]] = CRYSTALS[i];
        }
        if (mode < 7) {
            int shift = 1 + random.nextInt(3);
            for (int i = 0; i < CRYSTALS.length; i++) {
                map[CRYSTALS[i]] = CRYSTALS[(i + shift) % CRYSTALS.length];
            }
        } else {
            char a = CRYSTALS[random.nextInt(CRYSTALS.length)];
            char b = mode == 7 ? a : CRYSTALS[random.nextInt(CRYSTALS.length)];
            for (int i = 0; i < CRYSTALS.length; i++) {
                map[CRYSTALS[i]] = (i & 1) == 0 ? a : b;
            }
        }
        for (char[][] layer : mesa) {
            for (char[] row : layer) {
                for (int x = 0; x < row.length; x++) {
                    if (row[x] < 128 && map[row[x]] != 0) {
                        row[x] = map[row[x]];
                    }
                }
            }
        }
    }

    /**
     * A three-wide passage along the mesa's long axis at ground level. It
     * follows the middle of the solid rock at head height, so it stays inside
     * the mesa however the outline was warped.
     */
    private static void carveTunnel(char[][][] mesa, Random random, int body) {
        int clearance = Math.min(3, body - 3);
        int probeY = GROUND_Y + 2;
        int rows = CrystalCanyonShapes.MESA_Z;
        int centre = -1;
        int untilTurn = 3 + random.nextInt(3);
        int drift = 0;
        for (int x = 0; x < CrystalCanyonShapes.MESA_X; x++) {
            int lowest = -1;
            int highest = -1;
            for (int z = 0; z < rows; z++) {
                if (mesa[probeY][z][x] != '.') {
                    if (lowest < 0) {
                        lowest = z;
                    }
                    highest = z;
                }
            }
            if (lowest >= 0 && highest - lowest >= 4) {
                int target = (lowest + highest) / 2;
                centre = centre < 0 ? target : centre + Integer.compare(target, centre);
            }
            if (centre < 0) {
                continue;
            }
            if (--untilTurn <= 0) {
                drift = random.nextInt(3) - 1;
                untilTurn = 3 + random.nextInt(3);
            }
            int lane = Math.max(1, Math.min(rows - 2, centre + drift));
            for (int dz = -1; dz <= 1; dz++) {
                int top = clearance - (dz == 0 ? 0 : 1);
                for (int dy = 1; dy <= top; dy++) {
                    mesa[GROUND_Y + dy][lane + dz][x] = '.';
                }
            }
        }
    }

    /** A wide rounded gate cut across the mesa, crossing any tunnel. */
    private static void carveArchway(char[][][] mesa, int body, int centreX) {
        int clearance = Math.min(6, body - 3);
        int halfWidth = 2;
        for (int z = 0; z < CrystalCanyonShapes.MESA_Z; z++) {
            for (int dx = -halfWidth; dx <= halfWidth; dx++) {
                int top = clearance - (2 * dx * dx + 2) / (halfWidth * halfWidth);
                for (int dy = 1; dy <= top; dy++) {
                    mesa[GROUND_Y + dy][z][centreX + dx] = '.';
                }
            }
        }
    }

    /**
     * Whether this chunk gets a mesa. Density follows a smooth noise field, so
     * mesas gather into clusters with sparser stretches between them.
     */
    public static boolean clusterAllows(World world, Random random, int x, int z) {
        double cell = 96.0D;
        double fx = x / cell;
        double fz = z / cell;
        int ix = (int) Math.floor(fx);
        int iz = (int) Math.floor(fz);
        double tx = smooth(fx - ix);
        double tz = smooth(fz - iz);
        long seed = world.getSeed();
        double top = lerp(hash(seed, ix, iz), hash(seed, ix + 1, iz), tx);
        double bottom = lerp(hash(seed, ix, iz + 1), hash(seed, ix + 1, iz + 1), tx);
        double density = 0.3D + 0.7D * lerp(top, bottom, tz);
        return random.nextDouble() < density;
    }

    private static double smooth(double t) {
        return t * t * (3.0D - 2.0D * t);
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private static double hash(long seed, int x, int z) {
        long h = seed ^ (x * 341873128712L) ^ (z * 132897987541L);
        h = (h ^ (h >>> 33)) * 0xff51afd7ed558ccdL;
        h = (h ^ (h >>> 33)) * 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return (h >>> 11) / (double) (1L << 53);
    }

    /**
     * Erodes exposed cliff blocks wherever 3D noise is high, in two passes.
     * Noise is coherent, so this cuts gullies and notches into the faces
     * instead of scattering single missing blocks.
     */
    private static void erode(char[][][] mesa, long seed, int capY) {
        int rows = CrystalCanyonShapes.MESA_Z;
        int cols = CrystalCanyonShapes.MESA_X;
        for (int pass = 0; pass < 2; pass++) {
            List<int[]> doomed = new ArrayList<>();
            for (int y = GROUND_Y + 2; y < capY; y++) {
                for (int z = 0; z < rows; z++) {
                    for (int x = 0; x < cols; x++) {
                        char c = mesa[y][z][x];
                        if (c == '.' || c == 'w' || mesa[y + 1][z][x] == 'w') {
                            continue;
                        }
                        boolean exposed = x == 0 || x == cols - 1 || z == 0 || z == rows - 1
                                || mesa[y][z][x - 1] == '.' || mesa[y][z][x + 1] == '.'
                                || mesa[y][z - 1][x] == '.' || mesa[y][z + 1][x] == '.';
                        if (exposed && noise3(seed, x / 4.5D, y / 3.5D, z / 4.5D) > 0.68D) {
                            doomed.add(new int[]{y, z, x});
                        }
                    }
                }
            }
            for (int[] cell : doomed) {
                mesa[cell[0]][cell[1]][cell[2]] = '.';
            }
        }
    }

    /**
     * Deletes every group of blocks that is no longer joined, face to face,
     * to the buried base. Warping, erosion and tunnels can leave crystal
     * clumps and chips hanging in mid-air; this clears them.
     */
    private static void removeDetached(char[][][] mesa) {
        int height = mesa.length;
        int rows = CrystalCanyonShapes.MESA_Z;
        int cols = CrystalCanyonShapes.MESA_X;
        boolean[][][] attached = new boolean[height][rows][cols];
        java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();
        for (int y = 0; y <= GROUND_Y && y < height; y++) {
            for (int z = 0; z < rows; z++) {
                for (int x = 0; x < cols; x++) {
                    if (mesa[y][z][x] != '.') {
                        attached[y][z][x] = true;
                        queue.add(new int[]{y, z, x});
                    }
                }
            }
        }
        int[][] steps = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] step : steps) {
                int y = cell[0] + step[0];
                int z = cell[1] + step[1];
                int x = cell[2] + step[2];
                if (y >= 0 && y < height && z >= 0 && z < rows && x >= 0 && x < cols
                        && !attached[y][z][x] && mesa[y][z][x] != '.') {
                    attached[y][z][x] = true;
                    queue.add(new int[]{y, z, x});
                }
            }
        }
        for (int y = 0; y < height; y++) {
            for (int z = 0; z < rows; z++) {
                for (int x = 0; x < cols; x++) {
                    if (!attached[y][z][x]) {
                        mesa[y][z][x] = '.';
                    }
                }
            }
        }
    }

    /** Smooth 2D value noise in [0, 1). */
    private static double noise2(long seed, double x, double z) {
        int ix = (int) Math.floor(x);
        int iz = (int) Math.floor(z);
        double tx = smooth(x - ix);
        double tz = smooth(z - iz);
        return lerp(lerp(hash(seed, ix, iz), hash(seed, ix + 1, iz), tx),
                lerp(hash(seed, ix, iz + 1), hash(seed, ix + 1, iz + 1), tx), tz);
    }

    /** Smooth 3D value noise in [0, 1). */
    private static double noise3(long seed, double x, double y, double z) {
        int iy = (int) Math.floor(y);
        double ty = smooth(y - iy);
        return lerp(noise2(seed + iy * 0x9E3779B97F4A7C15L, x, z), noise2(seed + (iy + 1) * 0x9E3779B97F4A7C15L, x, z), ty);
    }

    private static void placeSpires(World world, Random random, int plateauY, int minX, int maxX, int minZ, int maxZ) {
        double blue = Config.getCrystalCanyonBlueTemplateChance();
        double purple = Config.getCrystalCanyonPurpleTemplateChance();
        double red = Config.getCrystalCanyonRedTemplateChance();
        double white = Config.getCrystalCanyonWhiteTemplateChance();
        double total = blue + purple + red + white;
        if (total <= 0.0D) {
            return;
        }

        List<BlockPos> candidates = new ArrayList<>();
        for (int x = minX + 3; x <= maxX - 3; x++) {
            for (int z = minZ + 3; z <= maxZ - 3; z++) {
                BlockPos pos = new BlockPos(x, plateauY, z);
                if (isSolidSand(world, pos)) {
                    candidates.add(pos);
                }
            }
        }
        Collections.shuffle(candidates, random);

        int wanted = 1 + random.nextInt(4);
        List<BlockPos> placed = new ArrayList<>();
        for (BlockPos pos : candidates) {
            if (placed.size() >= wanted) {
                break;
            }
            boolean tooClose = false;
            for (BlockPos other : placed) {
                if (Math.abs(other.getX() - pos.getX()) < SPIRE_SPACING && Math.abs(other.getZ() - pos.getZ()) < SPIRE_SPACING) {
                    tooClose = true;
                    break;
                }
            }
            if (tooClose) {
                continue;
            }

            double roll = random.nextDouble() * total;
            Block block;
            boolean slim = false;
            if ((roll -= blue) < 0.0D) {
                block = ModBlocks.light_blue_crystal_block;
                slim = true;
            } else if ((roll -= purple) < 0.0D) {
                block = ModBlocks.purple_crystal_block;
            } else if ((roll -= red) < 0.0D) {
                block = ModBlocks.red_crystal_block;
            } else {
                block = ModBlocks.white_crystal_block;
            }
            placeSpire(world, random, pos, block, slim ? CrystalCanyonShapes.SPIRE_SLIM : CrystalCanyonShapes.SPIRE_WIDE,
                    slim ? 6 : 7, plateauY - SPIRE_BURIED_DEPTH, minX, maxX, minZ, maxZ);
            placed.add(pos);
        }
    }

    /** White sand with white sand all around it, so a spire never overhangs the plateau edge. */
    private static boolean isSolidSand(World world, BlockPos pos) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (world.getBlockState(pos.add(dx, 0, dz)).getBlock() != ModBlocks.white_sand) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void placeSpire(World world, Random random, BlockPos anchor, Block block, String[] layers, int size,
                                   int baseY, int minX, int maxX, int minZ, int maxZ) {
        int srcHeight = layers.length;
        // Height variation: stretch or squash the spire between 80% and 140%.
        int height = srcHeight * (80 + random.nextInt(61)) / 100;
        int turns = random.nextInt(4);
        int half = (size - 1) / 2;
        IBlockState state = block.getDefaultState();
        for (int y = 0; y < height; y++) {
            String[] rows = layers[Math.min(srcHeight - 1, y * srcHeight / height)].split("\\|");
            for (int z = 0; z < size; z++) {
                for (int x = 0; x < size; x++) {
                    if (rows[z].charAt(x) != 'X') {
                        continue;
                    }
                    int rx = x;
                    int rz = z;
                    for (int t = 0; t < turns; t++) {
                        int nx = size - 1 - rz;
                        rz = rx;
                        rx = nx;
                    }
                    int wx = anchor.getX() + rx - half;
                    int wz = anchor.getZ() + rz - half;
                    if (wx < minX || wx > maxX || wz < minZ || wz > maxZ) {
                        continue;
                    }
                    world.setBlockState(new BlockPos(wx, baseY + y, wz), state, 2);
                }
            }
        }
    }
}
