package windanesz.byg.blocks;

import net.minecraft.block.BlockBookshelf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;

public class BlockBookshelfBase extends BlockBookshelf {
    private final boolean silkHarvest;

    public BlockBookshelfBase(String name, int harvestLevel, float hardness, float resistance, int lightOpacity, boolean silkHarvest) {
        this.silkHarvest = silkHarvest;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(0.0f);
        this.setLightOpacity(lightOpacity);
        this.setCreativeTab(BYGTab.tab);
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    protected boolean canSilkHarvest() {
        return this.silkHarvest;
    }

    @Override
    public float getEnchantPowerBonus(World world, BlockPos pos) {
        return 1.0f;
    }
}

