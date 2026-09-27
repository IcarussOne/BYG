package windanesz.byg.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenStonePillarHighlands extends WorldGenerator {

    private static final IBlockState STONE = Blocks.STONE.getDefaultState();
    private static final IBlockState MOSSY_COBBLE = Blocks.MOSSY_COBBLESTONE.getDefaultState();
    private static final IBlockState DIRT = Blocks.DIRT.getDefaultState();
    private static final IBlockState GRASS = Blocks.GRASS.getDefaultState();

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        BlockPos surface = findSurface(world, position);
        if (surface == null || surface.getY() < 58) {
            return false;
        }

        int sizeTier = rand.nextInt(3);
        boolean massive = sizeTier == 2;
        boolean large = sizeTier >= 1;

        int baseRadius = massive ? (15 + rand.nextInt(6)) : (large ? (10 + rand.nextInt(4)) : (5 + rand.nextInt(4)));
        
        int height = massive ? (180 + rand.nextInt(70)) : (large ? (130 + rand.nextInt(50)) : (100 + rand.nextInt(30)));
        
        int capRadius = baseRadius + 2 + rand.nextInt(3);
        int capThickness = 2 + rand.nextInt(2);
        
        float taperFactor = 0.7f + rand.nextFloat() * 0.3f;
        
        int embedDepth = 3 + rand.nextInt(3);
        int baseY = Math.max(5, surface.getY() - embedDepth);
        int topY = baseY + height;
        
        int clearanceRadius = Math.max(capRadius, baseRadius) + 10;
        BlockPos min = new BlockPos(surface.getX() - clearanceRadius, baseY - 15, surface.getZ() - clearanceRadius);
        BlockPos max = new BlockPos(surface.getX() + clearanceRadius, Math.min(255, topY + capThickness + 10), surface.getZ() + clearanceRadius);

        if (!world.isAreaLoaded(min, max)) {
            return false;
        }
        if (!canAnchorTo(world.getBlockState(surface).getBlock())) {
            return false;
        }

        long shapeSeed = world.getSeed() ^ rand.nextLong();
        int centerX = surface.getX();
        int centerZ = surface.getZ();
        
        boolean hasArch = rand.nextInt(5) == 0;
        int archY = hasArch ? (baseY + height / 3 + rand.nextInt(height / 3)) : -1;
        int archHeight = hasArch ? (10 + rand.nextInt(15)) : 0;

        buildPillarShaft(world, baseY, topY, centerX, centerZ, baseRadius, taperFactor, archY, archHeight, shapeSeed);
        
        buildPlateauCap(world, topY, centerX, centerZ, capRadius, capThickness, shapeSeed);
        
        smoothBaseToGround(world, centerX, baseY, centerZ, baseRadius + 3, shapeSeed);
        
        addWeathering(world, centerX, baseY, topY, centerZ, baseRadius, shapeSeed);

        return true;
    }

    private void buildPillarShaft(World world, int baseY, int topY, int centerX, int centerZ, 
                                  int baseRadius, float taperFactor, int archY, int archHeight, long shapeSeed) {
        for (int y = baseY; y < topY; y++) {
            float heightFraction = (float)(y - baseY) / (float)(topY - baseY);
            float currentRadius = baseRadius * (1.0f - heightFraction * (1.0f - taperFactor));
            
            float noiseVariation = noiseValue(centerX, y, centerZ, shapeSeed + y * 13L) * 1.5f;
            currentRadius += noiseVariation;
            
            for (int dx = -(int)currentRadius - 2; dx <= (int)currentRadius + 2; dx++) {
                for (int dz = -(int)currentRadius - 2; dz <= (int)currentRadius + 2; dz++) {
                    float distance = (float)Math.sqrt(dx * dx + dz * dz);
                    float localNoise = noiseValue(centerX + dx, y, centerZ + dz, shapeSeed + 1000L) * 0.8f;
                    
                    if (distance < currentRadius + localNoise) {
                        if (y >= archY && y < archY + archHeight) {
                            if (distance < currentRadius * 0.6f) {
                                continue;
                            }
                        }
                        
                        BlockPos pos = new BlockPos(centerX + dx, y, centerZ + dz);
                        IBlockState state = choosePillarBlock(centerX + dx, y, centerZ + dz, shapeSeed);
                        world.setBlockState(pos, state, 2);
                    }
                }
            }
        }
    }

    private void buildPlateauCap(World world, int topY, int centerX, int centerZ, int capRadius, int capThickness, long shapeSeed) {
        List<BlockPos> topSurfacePositions = new ArrayList<>();
        
        for (int layer = 0; layer < capThickness; layer++) {
            int currentY = topY + layer;
            if (currentY >= 255) break;
            
            for (int dx = -capRadius; dx <= capRadius; dx++) {
                for (int dz = -capRadius; dz <= capRadius; dz++) {
                    float distance = (float)Math.sqrt(dx * dx + dz * dz);
                    float noiseOffset = noiseValue(centerX + dx, currentY, centerZ + dz, shapeSeed + 2000L + layer * 17L) * 1.2f;
                    
                    if (distance < capRadius + noiseOffset) {
                        BlockPos pos = new BlockPos(centerX + dx, currentY, centerZ + dz);
                        IBlockState state = layer == capThickness - 1 ? DIRT : choosePillarBlock(centerX + dx, currentY, centerZ + dz, shapeSeed);
                        world.setBlockState(pos, state, 2);
                        
                        if (layer == capThickness - 1) {
                            topSurfacePositions.add(pos);
                        }
                    }
                }
            }
        }
        
        for (BlockPos pos : topSurfacePositions) {
            if (world.getBlockState(pos.up()).getMaterial() == Material.AIR) {
                float mossNoise = noiseValue(pos.getX(), pos.getY(), pos.getZ(), shapeSeed + 3000L);
                world.setBlockState(pos, mossNoise > 0.3f ? GRASS : DIRT, 2);
            }
        }
    }

    private void smoothBaseToGround(World world, int centerX, int startY, int centerZ, int radius, long shapeSeed) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                float distance = (float)Math.sqrt(dx * dx + dz * dz);
                if (distance > radius) continue;
                
                float blendFactor = 1.0f - (distance / (float)radius);
                int supportHeight = startY + Math.round(blendFactor * 4.0f);
                
                fillDownToGround(world, new BlockPos(centerX + dx, supportHeight, centerZ + dz), shapeSeed);
            }
        }
    }

    private void addWeathering(World world, int centerX, int baseY, int topY, int centerZ, int baseRadius, long shapeSeed) {
        int samples = 10;
        for (int i = 0; i < samples; i++) {
            int y = baseY + 20 + (int)((topY - baseY - 40) * Math.random());
            int angle = (int)(360 * Math.random());
            int dx = (int)(Math.cos(Math.toRadians(angle)) * (baseRadius + 1));
            int dz = (int)(Math.sin(Math.toRadians(angle)) * (baseRadius + 1));
            
            BlockPos pos = new BlockPos(centerX + dx, y, centerZ + dz);
            if (world.getBlockState(pos).getBlock() == Blocks.STONE) {
                if (dz < 0 && Math.random() < 0.05) {
                    world.setBlockState(pos, MOSSY_COBBLE, 2);
                }
            }
        }
    }

    private IBlockState choosePillarBlock(int x, int y, int z, long shapeSeed) {
        return STONE;
    }

    private static float noiseValue(int x, int y, int z, long seed) {
        long hash = seed;
        hash = hash * 31L + x;
        hash = hash * 31L + y;
        hash = hash * 31L + z;
        hash = hash ^ (hash >> 13);
        hash = hash * 1103515245L + 12345L;
        return ((hash >> 16) & 0x7FFF) / (float)0x7FFF * 2.0f - 1.0f;
    }

    private static BlockPos findSurface(World world, BlockPos start) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(start.getX(), 0, start.getZ());
        for (int y = 90; y >= 50; y--) {
            pos.setY(y);
            Block block = world.getBlockState(pos).getBlock();
            if (canAnchorTo(block)) {
                return pos.toImmutable();
            }
        }
        return null;
    }

    private static boolean canAnchorTo(Block block) {
        Material mat = block.getMaterial(block.getDefaultState());
        return mat == Material.GRASS || mat == Material.GROUND || mat == Material.ROCK || mat == Material.SAND || mat == Material.CLAY;
    }

    private static void fillDownToGround(World world, BlockPos start, long shapeSeed) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(start);
        for (int y = start.getY(); y >= 1; y--) {
            pos.setY(y);
            Block existing = world.getBlockState(pos).getBlock();
            if (canAnchorTo(existing)) {
                break;
            }
            IBlockState fillState = y > start.getY() - 5 ? STONE : Blocks.STONE.getDefaultState();
            world.setBlockState(pos, fillState, 2);
        }
    }
}

