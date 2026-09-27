package windanesz.byg.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public class WorldGenDeadSeaShipwreck extends WorldGenerator {
    private static final ResourceLocation DEAD_SEA = new ResourceLocation("byg", "byg_dead_sea");
    private static final ResourceLocation WRECK_LOOT = new ResourceLocation("byg", "chests/dead_sea_shipwreck");
    private static final ResourceLocation CRATE_LOOT = new ResourceLocation("byg", "chests/dead_sea_shipwreck_crate");
    private static final IBlockState FENCE = Blocks.SPRUCE_FENCE.getDefaultState();
    private static final IBlockState SAIL = Blocks.WOOL.getStateFromMeta(8);
    private static final IBlockState ROCK = ModBlocks.rocky_stone.getDefaultState();

    private static final int SLOOP = 0;
    private static final int COG = 1;
    private static final int CRASH = 2;

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        int roll = rand.nextInt(10);
        int style = roll < 4 ? SLOOP : roll < 7 ? COG : CRASH;
        int halfLength = style == SLOOP ? 5 + rand.nextInt(2) : style == COG ? 9 + rand.nextInt(3) : 9 + rand.nextInt(2);
        int halfWidth = style == SLOOP ? 2 : 3;
        boolean alongX = rand.nextBoolean();
        int xRadius = alongX ? halfLength + 6 : halfWidth + 5;
        int zRadius = alongX ? halfWidth + 5 : halfLength + 6;
        if (!world.isAreaLoaded(position.add(-xRadius, 1, -zRadius),
                position.add(xRadius, world.getSeaLevel() + 10, zRadius))) return false;

        int floor = findFloor(world, position);
        if (floor < 0) return false;
        // Verify the hull's length and sides before changing the world.
        for (int d = -halfLength; d <= halfLength; d += 2) {
            for (int side = -halfWidth; side <= halfWidth; side += halfWidth * 2) {
                int x = position.getX() + (alongX ? d : side);
                int z = position.getZ() + (alongX ? side : d);
                int localFloor = findFloor(world, new BlockPos(x, 0, z));
                if (localFloor < 0 || Math.abs(localFloor - floor) > 6) return false;
            }
        }

        boolean darkOak = rand.nextInt(3) == 0;
        Hull h = new Hull(position, alongX, halfLength, halfWidth, floor, style == CRASH,
                Blocks.PLANKS.getStateFromMeta(darkOak ? 5 : 1),
                darkOak ? Blocks.LOG2.getStateFromMeta(1) : Blocks.LOG.getStateFromMeta(1));

        // The crashed ship needs solid ground at its bow to slam into; otherwise it lies flat.
        if (style == CRASH && findFloor(world, h.at(halfLength + 1, 0, 0)) < 0) {
            style = COG;
            h = new Hull(position, alongX, halfLength, halfWidth, floor, false, h.plank, h.log);
        }
        boolean medium = style != SLOOP;
        boolean crash = style == CRASH;

        int brokenEnd = crash ? 1 : rand.nextBoolean() ? -1 : 1;
        boolean cliffCrash = crash;
        if (!crash && rand.nextInt(4) == 0 && findFloor(world, h.at(brokenEnd * (halfLength + 1), 0, 0)) >= 0) {
            cliffCrash = true;
        }
        if (cliffCrash) {
            buildImpactCliff(world, rand, h, brokenEnd, crash ? 3 : 2);
        } else if (rand.nextBoolean()) {
            // A smaller stone spur breaks through the side of some other wrecks.
            int snagLength = (rand.nextBoolean() ? 1 : -1) * (halfLength / 3);
            int snagSide = (rand.nextBoolean() ? 1 : -1) * (halfWidth - 1);
            int snagHeight = 4 + rand.nextInt(3);
            for (int y = 0; y < snagHeight; y++) {
                int radius = y < 2 ? 1 : 0;
                for (int d = -radius; d <= radius; d++) {
                    for (int side = -radius; side <= radius; side++) {
                        if (Math.abs(d) + Math.abs(side) > radius) continue;
                        place(world, h.at(snagLength + d, snagSide + side, floor + y),
                                rand.nextInt(4) == 0 ? Blocks.STONE.getDefaultState() : ROCK);
                    }
                }
            }
        }

        if (crash) {
            // The wreck rests on a rock ramp with its stern still low in the sand.
            for (int d = -2; d <= 2; d++) {
                for (int side = -1; side <= 1; side++) {
                    for (int y = floor; y < floor + h.lift(d); y++) {
                        if (rand.nextInt(5) != 0) {
                            place(world, h.at(d, side, y), rand.nextInt(4) == 0 ? Blocks.STONE.getDefaultState() : ROCK);
                        }
                    }
                }
            }
        }

        int breachStart = rand.nextInt(halfLength / 2 + 1) - halfLength / 3;
        int breachSide = rand.nextBoolean() ? -1 : 1;
        for (int d = -halfLength; d <= halfLength; d++) {
            int fromBow = halfLength - d;
            int fromStern = d + halfLength;
            // Pointed bow, blunt stern.
            int width = fromBow >= 4 ? halfWidth : Math.min(halfWidth, (fromBow + 1) / 2);
            boolean castle = medium && fromStern <= 3;
            boolean missingEnd = !crash && d * brokenEnd > halfLength - (medium ? 3 : 2);
            int rail = castle || fromBow <= 2 || fromStern <= 1 ? 3 : 2;
            for (int side = -width; side <= width; side++) {
                boolean edge = Math.abs(side) == width;
                if (missingEnd) {
                    // Only a splintered keel remains.
                    if (side == 0 && rand.nextInt(3) != 0) place(world, h.pos(d, 0, 0), h.log);
                    continue;
                }
                boolean breach = edge && side * breachSide > 0 && d >= breachStart && d <= breachStart + 2 && !castle;
                boolean chine = edge && width >= 2;
                IBlockState wood = d % 4 == 0 ? h.log : h.plank;
                if (crash && d > -halfLength && h.lift(d) > h.lift(d - 1)) {
                    place(world, h.pos(d, side, -1), wood); // close the step between tilted rows
                }
                if (!chine && !(breach && rand.nextInt(4) != 0)) {
                    place(world, h.pos(d, side, 0), wood);
                }
                if (edge && !breach) {
                    for (int y = 1; y <= rail; y++) {
                        if (y == rail && rand.nextInt(5) == 0) continue;
                        place(world, h.pos(d, side, y), wood);
                    }
                    if (castle && rand.nextInt(4) != 0) place(world, h.pos(d, side, 4), FENCE);
                } else if (breach && rand.nextInt(3) == 0) {
                    place(world, h.pos(d, side, 1), h.plank);
                }
                if (castle && !edge && rand.nextInt(6) != 0) {
                    place(world, h.pos(d, side, 3), h.plank);
                }
            }
            // Exposed ribs cross the open hold.
            if (d % 4 == 0 && !missingEnd && !castle) {
                for (int side = -width + 1; side < width; side++) {
                    if (rand.nextInt(5) != 0) place(world, h.pos(d, side, medium ? 2 : 1), h.log);
                }
            }
        }

        if (!crash && brokenEnd != 1) {
            // A bowsprit pointing out over the water.
            place(world, h.pos(halfLength + 1, 0, 3), FENCE);
            if (rand.nextBoolean()) place(world, h.pos(halfLength + 2, 0, 4), FENCE);
        }

        if (style == SLOOP) {
            buildMast(world, rand, h, 0, 6, 5, 3, halfWidth);
        } else if (style == COG) {
            buildMast(world, rand, h, -1, 9, 8, 4, halfWidth);
            buildMast(world, rand, h, halfLength - 4, 6, 5, 2, halfWidth - 1);
        } else {
            // The main mast snapped and its top now lies against the rock.
            int top = buildMast(world, rand, h, -2, 4, 4, 2, halfWidth);
            // Chain the fallen top orthogonally from the mast's last block so no fence floats.
            int topY = h.pos(-2, 0, top).getY();
            place(world, h.at(-1, 0, topY), FENCE);
            place(world, h.at(0, 0, topY), FENCE);
            place(world, h.at(0, 0, topY + 1), FENCE);
            place(world, h.at(1, 0, topY + 1), FENCE);
        }

        if (cliffCrash) {
            // Broken bow timbers point into the rock face.
            for (int side = -1; side <= 1; side++) {
                for (int distance = halfLength - 3; distance <= halfLength; distance++) {
                    if (rand.nextInt(4) == 0) continue;
                    place(world, h.pos(brokenEnd * distance, side, 2 + (side == 0 ? 1 : 0)), h.log);
                }
            }
        }

        // Scattered pieces keep the wreck irregular without filling its surroundings.
        int debris = medium ? 5 : 3;
        for (int i = 0; i < debris; i++) {
            int d = rand.nextInt(halfLength * 2 + 5) - halfLength - 2;
            int side = (rand.nextBoolean() ? 1 : -1) * (halfWidth + 2 + rand.nextInt(3));
            BlockPos piece = h.at(d, side, floor);
            int pieceFloor = findFloor(world, piece);
            if (pieceFloor >= 0) {
                place(world, new BlockPos(piece.getX(), pieceFloor, piece.getZ()),
                        rand.nextInt(3) == 0 ? h.log : h.plank);
            }
        }
        if (rand.nextInt(100) < 20) {
            placeLootContainer(world, rand, h, brokenEnd, Blocks.CHEST, WRECK_LOOT, 0);
        }
        if (rand.nextInt(100) < 45) {
            // Crates hold poorer loot and may sit anywhere along the hull, next to a chest if both spawn.
            placeLootContainer(world, rand, h, brokenEnd, ModBlocks.crate, CRATE_LOOT, rand.nextInt(h.half + 1) - h.half / 2);
        }
        return true;
    }

    /** Places a mast with a yard and a tattered sail hanging under it; returns the mast's top height. */
    private int buildMast(World world, Random rand, Hull h, int d, int height, int yardY,
                          int sailRows, int sailHalf) {
        // Only the topmost blocks may be snapped off, so the mast stays one unbroken column.
        int top = height - rand.nextInt(3);
        yardY = Math.min(yardY, top);
        for (int y = 1; y <= top; y++) {
            place(world, h.pos(d, 0, y), y <= 2 ? h.log : FENCE);
        }
        for (int side = -sailHalf - 1; side <= sailHalf + 1; side++) {
            if (side != 0 && rand.nextInt(6) != 0) place(world, h.pos(d, side, yardY), FENCE);
        }
        for (int row = 1; row <= sailRows; row++) {
            for (int side = -sailHalf; side <= sailHalf; side++) {
                if (side == 0 || rand.nextInt(6) < row) continue;
                place(world, h.pos(d, side, yardY - row), SAIL);
            }
        }
        return top;
    }

    private void placeLootContainer(World world, Random rand, Hull h, int brokenEnd, Block container,
                                    ResourceLocation lootTable, int shift) {
        int first = -brokenEnd * (h.half / 2) + shift;
        for (int offset = 0; offset < h.half * 2; offset++) {
            int d = first + (offset % 2 == 0 ? offset / 2 : -(offset + 1) / 2);
            if (Math.abs(d) > h.half - 2 || d * brokenEnd > h.half - 4 || d < -h.half + 4) continue;
            for (int side = -1; side <= 1; side++) {
                BlockPos chestPos = h.pos(d, side, 1);
                Block support = world.getBlockState(chestPos.down()).getBlock();
                if (support != Blocks.PLANKS && support != Blocks.LOG && support != Blocks.LOG2) continue;
                if (!isOpen(world, chestPos) || !isOpen(world, chestPos.up())) continue;
                world.setBlockState(chestPos, container.getDefaultState(), 2);
                TileEntity tile = world.getTileEntity(chestPos);
                if (tile instanceof TileEntityLockableLoot) {
                    ((TileEntityLockableLoot) tile).setLootTable(lootTable, rand.nextLong());
                }
                return;
            }
        }
    }

    private boolean isOpen(World world, BlockPos pos) {
        return world.isAirBlock(pos) || world.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    private void buildImpactCliff(World world, Random rand, Hull h, int end, int reach) {
        int cliffHeight = Math.min(32, Math.max(Math.max(13, world.getSeaLevel() - h.floor + 5), h.lift(h.half) + 9));
        for (int length = -reach; length <= reach; length++) {
            for (int side = -h.halfWidth - 2; side <= h.halfWidth + 2; side++) {
                int d = end * (h.half + 1) + length;
                BlockPos column = h.at(d, side, 0);
                int localFloor = findFloor(world, column);
                if (localFloor < 0) continue;
                int top = h.floor + cliffHeight - Math.abs(length) * 2
                        - Math.max(0, Math.abs(side) - h.halfWidth + 1) * 2 + rand.nextInt(3) - 1;
                for (int y = localFloor; y <= top; y++) {
                    int palette = rand.nextInt(10);
                    place(world, h.at(d, side, y),
                            palette < 7 ? ROCK : palette < 9
                                    ? Blocks.STONE.getDefaultState() : Blocks.MOSSY_COBBLESTONE.getDefaultState());
                }
            }
        }
    }

    private int findFloor(World world, BlockPos column) {
        BlockPos bed = new BlockPos(column.getX(), world.getSeaLevel() + 8, column.getZ());
        if (!DEAD_SEA.equals(world.getBiome(bed).getRegistryName())) return -1;
        while (bed.getY() > 1 && world.isAirBlock(bed)) bed = bed.down();
        if (bed.getY() <= 1) return -1;
        while (bed.getY() > 1 && world.getBlockState(bed.down()).getBlock() == Blocks.WATER) bed = bed.down();
        Block ground = world.getBlockState(bed.down()).getBlock();
        if (world.getBlockState(bed).getBlock() != Blocks.WATER) {
            ground = world.getBlockState(bed).getBlock();
            return isRock(ground) ? bed.getY() + 1 : -1;
        }
        return ground == ModBlocks.black_sand || ground == Blocks.SAND || ground == Blocks.GRAVEL
                || isRock(ground) ? bed.getY() : -1;
    }

    private boolean isRock(Block block) {
        return block == Blocks.STONE || block == Blocks.COBBLESTONE
                || block == Blocks.MOSSY_COBBLESTONE || block == ModBlocks.rocky_stone;
    }

    private void place(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlock() == Blocks.WATER || world.isAirBlock(pos)) {
            world.setBlockState(pos, state, 2);
        }
    }

    /** Ship-local coordinates: d runs stern (-) to bow (+), side is across the beam, y is above the keel. */
    private static final class Hull {
        final BlockPos center;
        final boolean alongX;
        final int half;
        final int halfWidth;
        final int floor;
        final boolean tilted;
        final IBlockState plank;
        final IBlockState log;

        Hull(BlockPos center, boolean alongX, int half, int halfWidth, int floor, boolean tilted,
             IBlockState plank, IBlockState log) {
            this.center = center;
            this.alongX = alongX;
            this.half = half;
            this.halfWidth = halfWidth;
            this.floor = floor;
            this.tilted = tilted;
            this.plank = plank;
            this.log = log;
        }

        /** Height of the keel above the sea floor: a tilted ship rises one block every two along its length. */
        int lift(int d) {
            return tilted ? Math.max(0, d + half) / 2 : 0;
        }

        BlockPos pos(int d, int side, int y) {
            return at(d, side, floor + lift(d) + y);
        }

        BlockPos at(int d, int side, int absY) {
            return new BlockPos(center.getX() + (alongX ? d : side), absY, center.getZ() + (alongX ? side : d));
        }
    }
}
