package windanesz.byg.biome;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.MangroveTreeGenerator;

import java.util.Random;

public class BiomeMangroveMarshes
        extends Biome {

    public BiomeMangroveMarshes() {
        super(new Biome.BiomeProperties("Mangrove Marshes").setRainfall(0.7f).setBaseHeight(-0.4f).setWaterColor(-9801139).setHeightVariation(0.01f).setTemperature(0.8f));
        this.setRegistryName("byg_mangrove_marshes");
        this.topBlock = windanesz.byg.registry.ModBlocks.white_sand.getDefaultState();
        this.fillerBlock = windanesz.byg.registry.ModBlocks.white_sand.getDefaultState();
        this.decorator.generateFalls = true;
        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 7;
        this.decorator.grassPerChunk = 10;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 3;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.waterlilyPerChunk = 4;
        this.decorator.reedsPerChunk = 35;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 35;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -11757017;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -11757017;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer primer, int x, int z, double noiseVal) {
        super.genTerrainBlocks(world, random, primer, x, z, noiseVal);

        // Vanilla water occupies sea level - 1 downwards. Seal any deeper
        // column with a muddy bed, leaving at most four water blocks
        // (sea level - 4 through sea level - 1) above it.
        int seaLevel = world.getSeaLevel();
        int bedY = seaLevel - 5;
        int primerX = z & 15;
        int primerZ = x & 15;
        if (primer.getBlockState(primerX, seaLevel - 1, primerZ).getMaterial() != Material.WATER) {
            return;
        }

        if (primer.getBlockState(primerX, bedY, primerZ).getMaterial() == Material.WATER) {
            primer.setBlockState(primerX, bedY, primerZ, windanesz.byg.registry.ModBlocks.mud_block.getDefaultState());
        }

        int floorY = seaLevel - 1;
        while (floorY > 0 && primer.getBlockState(primerX, floorY, primerZ).getMaterial() == Material.WATER) {
            floorY--;
        }
        if (primer.getBlockState(primerX, floorY, primerZ).getMaterial() != Material.AIR) {
            primer.setBlockState(primerX, floorY, primerZ, windanesz.byg.registry.ModBlocks.mud_block.getDefaultState());
        }
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new MangroveTreeGenerator();
    }

}


