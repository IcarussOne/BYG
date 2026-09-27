package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;

public class BlockBasicBase extends Block {
    private final boolean silkHarvest;

    public BlockBasicBase(String name, Material material, SoundType soundType, String harvestTool, int harvestLevel,
                          float hardness, float resistance, float lightLevel, int lightOpacity, boolean silkHarvest) {
        super(material);
        this.silkHarvest = silkHarvest;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(soundType);
        this.setHarvestLevel(harvestTool, harvestLevel);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(lightLevel);
        this.setLightOpacity(lightOpacity);
        this.setCreativeTab(BYGTab.tab);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return this.silkHarvest;
    }
}

