package windanesz.byg.blocks;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;
import windanesz.byg.worldgen.treegenerator.TreeGrowthGenerator;

import java.util.Random;
import java.util.function.BiPredicate;

public class BlockGeneratedSaplingBase extends BlockFlower implements IGrowable {
    private static final int RANDOM_GROWTH_CHANCE = 7;
    private final TreePlacement[] tickPlacements;
    private final TreePlacement[] bonemealPlacements;
    private final BiPredicate<World, BlockPos> tickGrowthCondition;
    private final BiPredicate<World, BlockPos> bonemealGrowthCondition;
    private final SaplingFormation formation;

    public BlockGeneratedSaplingBase(String name, TreePlacement[] placements, double bonemealSuccessChance,
                                     BiPredicate<World, BlockPos> growthCondition) {
        this(name, placements, bonemealSuccessChance, placements, growthCondition, growthCondition, SaplingFormation.single());
    }

    public BlockGeneratedSaplingBase(String name, TreePlacement[] tickPlacements, double bonemealSuccessChance,
                                     TreePlacement[] bonemealPlacements, BiPredicate<World, BlockPos> growthCondition) {
        this(name, tickPlacements, bonemealSuccessChance, bonemealPlacements, growthCondition, growthCondition, SaplingFormation.single());
    }

    public BlockGeneratedSaplingBase(String name, TreePlacement[] tickPlacements, double bonemealSuccessChance,
                                     TreePlacement[] bonemealPlacements, BiPredicate<World, BlockPos> tickGrowthCondition,
                                     BiPredicate<World, BlockPos> bonemealGrowthCondition) {
        this(name, tickPlacements, bonemealSuccessChance, bonemealPlacements, tickGrowthCondition, bonemealGrowthCondition,
                SaplingFormation.single());
    }

    public BlockGeneratedSaplingBase(String name, TreePlacement[] tickPlacements, double bonemealSuccessChance,
                                     TreePlacement[] bonemealPlacements, BiPredicate<World, BlockPos> tickGrowthCondition,
                                     BiPredicate<World, BlockPos> bonemealGrowthCondition, SaplingFormation formation) {
        this.tickPlacements = tickPlacements;
        this.bonemealPlacements = bonemealPlacements;
        this.tickGrowthCondition = tickGrowthCondition;
        this.bonemealGrowthCondition = bonemealGrowthCondition;
        this.formation = formation;
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(BYGTab.tab);
        this.setHardness(0.01f);
        this.setResistance(2.0f);
        this.setLightLevel(0.0f);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setTickRandomly(true);
    }

    public static TreePlacement tree(TreeGrowthGenerator generator, double chance) {
        return new TreePlacement(generator, chance);
    }

    public static BiPredicate<World, BlockPos> alwaysGrow() {
        return (world, pos) -> true;
    }

    public static SaplingFormation squareFormation(int size) {
        return SaplingFormation.square(size);
    }

    public static BiPredicate<World, BlockPos> outsideScaledTemperatureRange(double low, double high) {
        return (world, pos) -> {
            double temperature = world.getBiome(pos).getTemperature(pos) * 100.0f;
            return temperature >= low || temperature <= high;
        };
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public EnumFlowerColor getBlockType() {
        return EnumFlowerColor.YELLOW;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        for (EnumFlowerType type : EnumFlowerType.getTypes(this.getBlockType())) {
            list.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote || random.nextInt(RANDOM_GROWTH_CHANCE) != 0 || !this.canAttemptGrowth(world, pos, this.tickGrowthCondition)) {
            return;
        }
        this.tryGrow(world, pos, this.tickPlacements, random);
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
        return this.bonemealPlacements.length > 0;
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World world, Random random, BlockPos pos, IBlockState state) {
        if (this.canAttemptGrowth(world, pos, this.bonemealGrowthCondition)) {
            this.tryGrow(world, pos, this.bonemealPlacements, random);
        }
    }

    private boolean canAttemptGrowth(World world, BlockPos pos, BiPredicate<World, BlockPos> condition) {
        return world.canSeeSky(pos) && world.isDaytime() && condition.test(world, pos);
    }

    private boolean tryGrow(World world, BlockPos pos, TreePlacement[] placements, Random random) {
        BlockPos growthOrigin = this.resolveGrowthOrigin(world, pos);
        if (growthOrigin == null) {
            return false;
        }
        for (TreePlacement placement : placements) {
            if (random.nextDouble() < placement.chance && placement.generator.generate(world, random, growthOrigin)) {
                return true;
            }
        }
        return false;
    }

    private BlockPos resolveGrowthOrigin(World world, BlockPos pos) {
        if (this.formation.isSingle()) {
            return pos;
        }
        int radius = this.formation.getRadius();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos center = pos.add(x, 0, z);
                if (this.matchesFormation(world, center)) {
                    return center;
                }
            }
        }
        return null;
    }

    private boolean matchesFormation(World world, BlockPos center) {
        int radius = this.formation.getRadius();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (world.getBlockState(center.add(x, 0, z)).getBlock() != this) {
                    return false;
                }
            }
        }
        return true;
    }

    public static final class TreePlacement {
        private final TreeGrowthGenerator generator;
        private final double chance;

        private TreePlacement(TreeGrowthGenerator generator, double chance) {
            this.generator = generator;
            this.chance = chance;
        }
    }

    public static final class SaplingFormation {
        private final int radius;

        private SaplingFormation(int radius) {
            this.radius = radius;
        }

        private static SaplingFormation single() {
            return new SaplingFormation(0);
        }

        private static SaplingFormation square(int size) {
            if (size < 1 || size % 2 == 0) {
                throw new IllegalArgumentException("Sapling formation size must be a positive odd number");
            }
            return new SaplingFormation(size / 2);
        }

        private int getRadius() {
            return this.radius;
        }

        private boolean isSingle() {
            return this.radius == 0;
        }
    }
}
