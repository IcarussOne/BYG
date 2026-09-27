package windanesz.byg.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import windanesz.byg.client.BYGTab;

public class BlockFallingBase extends BlockFalling {
    public BlockFallingBase(String name, Material material, SoundType soundType, String harvestTool, int harvestLevel,
                            float hardness, float resistance, float lightLevel, int lightOpacity) {
        super(material);
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
}

