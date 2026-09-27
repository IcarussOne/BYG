package windanesz.byg.blocks;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSimpleFlowerBase extends BlockFlower {
    public BlockSimpleFlowerBase(String name, CreativeTabs tab) {
        this(name, tab, 0.0f);
    }

    public BlockSimpleFlowerBase(String name, CreativeTabs tab, float lightLevel) {
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(tab);
        this.setHardness(0.01f);
        this.setResistance(2.0f);
        this.setLightLevel(lightLevel);
        this.setTranslationKey(name);
        this.setRegistryName(name);
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
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }
}
