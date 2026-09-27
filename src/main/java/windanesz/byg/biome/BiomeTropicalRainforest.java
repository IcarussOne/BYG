package windanesz.byg.biome;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;

import java.util.Random;

public class BiomeTropicalRainforest
        extends Biome {

    private static final WorldGenerator FERN_GENERATOR = new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
    private static final WorldGenerator GRASS_GENERATOR = new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);

    public BiomeTropicalRainforest() {
        super(new Biome.BiomeProperties("Tropical Rainforest").setRainfall(0.7f).setBaseHeight(0.5f).setWaterColor(-10388636).setHeightVariation(0.25f).setTemperature(0.95f));
        this.setRegistryName("byg_tropical_rainforest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 14;
        this.decorator.flowersPerChunk = 10;
        this.decorator.grassPerChunk = 12;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 10;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -13395712;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -13395712;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        int tree = rand.nextInt(8);
        if (tree == 0) {
            return new CustomTree();
        }
        if (tree >= 5) {
            return new WorldGenTrees(false, 7 + rand.nextInt(5), Blocks.LOG.getStateFromMeta(3),
                    Blocks.LEAVES.getStateFromMeta(3), true);
        }
        if (!windanesz.byg.Config.isWoodSetEnabled("mahogany")) {
            return new WorldGenTrees(false, 7 + rand.nextInt(5), Blocks.LOG.getStateFromMeta(3),
                    Blocks.LEAVES.getStateFromMeta(3), true);
        }
        return new SaplingTreeGenerator(() -> ModBlocks.mahogany_log.getDefaultState(),
                () -> ModBlocks.mahogany_leaves.getDefaultState(),
                SaplingTreeGenerator.TreeStyle.MAHOGANY, 10, 4);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return rand.nextInt(3) == 0 ? FERN_GENERATOR : GRASS_GENERATOR;
    }

    static class CustomTree
            extends WorldGenAbstractTree {
        CustomTree() {
            super(false);
        }

        public boolean generate(World world, Random par2Random, BlockPos pos) {
            if (world.isRemote) {
                return false;
            }
            Template template = ((WorldServer) world).getStructureTemplateManager().getTemplate(world.getMinecraftServer(), new ResourceLocation("byg", "bush_1"));
            if (template == null) {
                return false;
            }
            Block ground = world.getBlockState(pos).getBlock();
            Block ground2 = world.getBlockState(pos.add(0, -1, 0)).getBlock();
            if (ground != Blocks.GRASS.getDefaultState().getBlock() && ground != Blocks.DIRT.getStateFromMeta(0).getBlock() && ground2 != Blocks.GRASS.getDefaultState().getBlock() && ground2 != Blocks.DIRT.getStateFromMeta(0).getBlock()) {
                return false;
            }
            Rotation rotation = Rotation.NONE;
            int rot = par2Random.nextInt(3);
            if (rot == 0) {
                rotation = Rotation.NONE;
            } else if (rot == 1) {
                rotation = Rotation.CLOCKWISE_90;
            } else if (rot == 2) {
                rotation = Rotation.CLOCKWISE_180;
            } else if (rot == 3) {
                rotation = Rotation.COUNTERCLOCKWISE_90;
            }
            Mirror mirror = Mirror.NONE;
            int mir = par2Random.nextInt(2);
            if (mir == 0) {
                mirror = Mirror.NONE;
            } else if (mir == 1) {
                mirror = Mirror.LEFT_RIGHT;
            } else if (mir == 2) {
                mirror = Mirror.FRONT_BACK;
            }
            int[] bounds = windanesz.byg.worldgen.TemplateWorldgenHelper.getFootprintBounds(template.getSize().getX(), template.getSize().getZ(), rotation, mirror);
            BlockPos placeTo = pos.add(-((bounds[0] + bounds[1]) / 2), 0, -((bounds[2] + bounds[3]) / 2));
            if (!windanesz.byg.worldgen.TemplateWorldgenHelper.fitsCurrentChunk(template, placeTo, rotation, mirror, pos)) {
                return false;
            }
            windanesz.byg.worldgen.TemplateWorldgenHelper.placeTemplateWithSettings(world, template, placeTo, windanesz.byg.worldgen.TemplateWorldgenHelper.chunkPlacementSettings(par2Random, rotation, mirror, pos));
            return true;
        }

        protected boolean canGrowInto(Block blockType) {
            Material material = blockType.getDefaultState().getMaterial();
            return material == Material.AIR || blockType == Blocks.GRASS.getDefaultState().getBlock() || blockType == Blocks.DIRT.getStateFromMeta(0).getBlock();
        }

        protected void setDirtAt(World world, BlockPos pos) {
        }

        public boolean isReplaceable(World world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            return state.getBlock().isAir(state, (IBlockAccess) world, pos) || this.canGrowInto(state.getBlock()) || state.getBlock().isReplaceable((IBlockAccess) world, pos);
        }
    }

}

