package windanesz.byg.worldgen;

import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.Template;

import java.util.Random;

public class EnchantedVillage {
    public static void generateWorld(Random random, int i2, int k2, World world, int dimID, IChunkGenerator cg, IChunkProvider cp) {
        boolean dimensionCriteria = false;
        boolean isNetherType = false;
        if (dimID == 0) {
            dimensionCriteria = true;
        }
        if (!dimensionCriteria) {
            return;
        }
        if (random.nextInt(1000) < 3) {
            int height = world.getActualHeight() - 1;
            int i = i2 + random.nextInt(16);
            int k = k2 + random.nextInt(16);
            if (isNetherType) {
                boolean notpassed = true;
                for (height = 255; height > 0; --height) {
                    if (notpassed && (world.isAirBlock(new BlockPos(i, height, k)) || world.getBlockState(new BlockPos(i, height, k)).getBlock().isReplaceable((IBlockAccess) world, new BlockPos(i, height, k)))) {
                        notpassed = false;
                        continue;
                    }
                    if (notpassed || world.isAirBlock(new BlockPos(i, height, k)) || world.getBlockState(new BlockPos(i, height, k)).getBlock().isReplaceable((IBlockAccess) world, new BlockPos(i, height, k))) {
                        continue;
                    }
                    break;
                }
            } else {
                while (height > 0 && (world.isAirBlock(new BlockPos(i, height, k)) || world.getBlockState(new BlockPos(i, height, k)).getBlock().isReplaceable((IBlockAccess) world, new BlockPos(i, height, k)))) {
                    --height;
                }
            }
            int j = height + random.nextInt(50) + 16;
            boolean biomeCriteria = false;
            Biome biome = world.getBiome(new BlockPos(i, j, k));
            if (((ResourceLocation) Biome.REGISTRY.getNameForObject(biome)).equals(new ResourceLocation("byg:byg_enchanted_forest"))) {
                biomeCriteria = true;
            }
            if (!biomeCriteria) {
                return;
            }
            if (world.isRemote) {
                return;
            }
            Template template = ((WorldServer) world).getStructureTemplateManager().getTemplate(world.getMinecraftServer(), new ResourceLocation("byg", "enchanted_village"));
            if (template == null) {
                return;
            }
            Rotation rotation = Rotation.NONE;
            Mirror mirror = Mirror.NONE;
            int rot = random.nextInt(4);
            if (rot == 0) {
                rotation = Rotation.NONE;
            } else if (rot == 1) {
                rotation = Rotation.CLOCKWISE_90;
            } else if (rot == 2) {
                rotation = Rotation.CLOCKWISE_180;
            } else if (rot == 3) {
                rotation = Rotation.COUNTERCLOCKWISE_90;
            }
            int mir = random.nextInt(3);
            if (mir == 0) {
                mirror = Mirror.NONE;
            } else if (mir == 1) {
                mirror = Mirror.LEFT_RIGHT;
            } else if (mir == 2) {
                mirror = Mirror.FRONT_BACK;
            }
            int[] bounds = TemplateWorldgenHelper.getFootprintBounds(template.getSize().getX(), template.getSize().getZ(), rotation, mirror);
            int centerX = i - ((bounds[0] + bounds[1]) / 2);
            int centerZ = k - ((bounds[2] + bounds[3]) / 2);
            BlockPos spawnTo = new BlockPos(centerX, j, centerZ);
            if (!TemplateWorldgenHelper.fitsCurrentChunk(template, spawnTo, rotation, mirror, i2 - 8, k2 - 8)) {
                return;
            }
            TemplateWorldgenHelper.placeTemplateWithSettings(world, template, spawnTo, TemplateWorldgenHelper.chunkPlacementSettings(rotation, mirror, i2, k2));
        }
    }
}

