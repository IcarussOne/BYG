package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

public class BlockStoneWallBase extends BlockWall {
    private final boolean cutoutMipped;

    public BlockStoneWallBase(String name, int harvestLevel, float hardness, float resistance, boolean cutoutMipped) {
        super(new Block(Material.ROCK));
        this.cutoutMipped = cutoutMipped;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", harvestLevel);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return this.cutoutMipped ? BlockRenderLayer.CUTOUT_MIPPED : super.getRenderLayer();
    }

}

