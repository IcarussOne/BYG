package windanesz.byg.biome;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.worldgen.treegenerator.WeepingWitchTreeGenerator;

import java.util.Random;

public class BiomeWeepingWitchForest extends Biome {
    private static final WorldGenAbstractTree WITCH_HAZEL_TREE = new WeepingWitchTreeGenerator();

    public BiomeWeepingWitchForest() {
        super(new Biome.BiomeProperties("Weeping Witch Forest").setRainfall(0.5f).setBaseHeight(0.35f).setWaterColor(-9971015).setHeightVariation(0.05f).setTemperature(0.25f));
        this.setRegistryName("byg_weeping_witch_forest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 3;
        this.decorator.flowersPerChunk = 1;
        this.decorator.grassPerChunk = 5;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 2;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 15;
        if (Config.allowDaytimeWitchesInWeepingWitchForest()) {
            this.spawnableMonsterList.removeIf(entry -> entry.entityClass == EntityWitch.class);
            this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitch.class, 25, 1, 1));
        }
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -13605059;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -13605059;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return WITCH_HAZEL_TREE;
    }
}
