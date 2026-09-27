package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import windanesz.byg.client.BYGTab;

public class BlockSandygrass
        extends Block {


    public BlockSandygrass() {
        super(Material.GRASS);
        this.setRegistryName("sandy_grass");
        this.setTranslationKey("sandy_grass");
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", 1);
        this.setHardness(0.6f);
        this.setResistance(8.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(255);
        this.setCreativeTab(BYGTab.tab);
    }

    public int tickRate(World world) {
        return 900;
    }

    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return true;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(windanesz.byg.registry.ModBlocks.sandy_dirt, 1));
    }

}
