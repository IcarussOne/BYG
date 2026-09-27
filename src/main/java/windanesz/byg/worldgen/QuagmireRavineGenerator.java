package windanesz.byg.worldgen;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;

public final class QuagmireRavineGenerator extends MapGenRavine {
    private static final ResourceLocation QUAGMIRE = new ResourceLocation("byg", "byg_quagmire");

    @Override
    protected void recursiveGenerate(World world, int sourceChunkX, int sourceChunkZ,
                                     int targetChunkX, int targetChunkZ, ChunkPrimer primer) {
        if (isQuagmire(world, targetChunkX, targetChunkZ)) {
            return;
        }
        super.recursiveGenerate(world, sourceChunkX, sourceChunkZ, targetChunkX, targetChunkZ, primer);
    }

    private static boolean isQuagmire(World world, int chunkX, int chunkZ) {
        Biome biome = world.getBiome(new BlockPos(chunkX * 16 + 8, 64, chunkZ * 16 + 8));
        return QUAGMIRE.equals(Biome.REGISTRY.getNameForObject(biome));
    }
}
