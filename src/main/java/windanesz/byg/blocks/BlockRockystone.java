package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;

import java.util.function.Supplier;

public class BlockRockystone
        extends Block {

    private final Supplier<Block> pickBlock;
    private final boolean silkHarvest;

    public BlockRockystone() {
        this("rocky_stone", true, null, true);
    }

    public BlockRockystone(String registryName, boolean showInCreativeTab, Supplier<Block> pickBlock, boolean silkHarvest) {
        super(Material.ROCK);
        this.pickBlock = pickBlock;
        this.silkHarvest = silkHarvest;
        this.setRegistryName(registryName);
        this.setTranslationKey(registryName);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(255);
        this.setCreativeTab(showInCreativeTab ? BYGTab.tab : null);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return this.pickBlock == null ? super.getPickBlock(state, target, world, pos, player) : new ItemStack(this.pickBlock.get(), 1);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return this.silkHarvest;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(Blocks.COBBLESTONE, 1));
    }

}

