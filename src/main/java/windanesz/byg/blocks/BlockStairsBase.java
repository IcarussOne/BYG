package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

public class BlockStairsBase extends BlockStairs {
    private final boolean flammable;

    public BlockStairsBase(String name, Material material, SoundType soundType, String toolClass, int harvestLevel, float hardness, float resistance, boolean flammable) {
        super(new Block(material).getDefaultState());
        this.flammable = flammable;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(soundType);
        this.setHarvestLevel(toolClass, harvestLevel);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return this.flammable;
    }
}

