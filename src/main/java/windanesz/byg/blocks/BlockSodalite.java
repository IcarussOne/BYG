package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import windanesz.byg.client.BYGTab;

public class BlockSodalite
        extends Block {


    public BlockSodalite() {
        super(Material.ROCK);
        this.setRegistryName("sodalite");
        this.setTranslationKey("sodalite");
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(1.5f);
        this.setResistance(30.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(255);
        this.setCreativeTab(BYGTab.tab);
    }

}
