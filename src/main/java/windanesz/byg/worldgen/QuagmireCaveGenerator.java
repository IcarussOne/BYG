package windanesz.byg.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

public final class QuagmireCaveGenerator extends MapGenCaves {
    private static final ResourceLocation QUAGMIRE = new ResourceLocation("byg", "byg_quagmire");
    private static final int SURFACE_SEAL_Y = 55;

    @Override
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ,
                            boolean foundTop, IBlockState state, IBlockState up) {
        if (y >= SURFACE_SEAL_Y && isQuagmire(chunkX, chunkZ)) {
            return;
        }
        super.digBlock(data, x, y, z, chunkX, chunkZ, foundTop, state, up);
    }

    private boolean isQuagmire(int chunkX, int chunkZ) {
        Biome biome = this.world.getBiome(new BlockPos(chunkX * 16 + 8, 64, chunkZ * 16 + 8));
        return QUAGMIRE.equals(Biome.REGISTRY.getNameForObject(biome));
    }
}
