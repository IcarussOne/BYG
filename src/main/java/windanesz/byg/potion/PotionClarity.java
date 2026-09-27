package windanesz.byg.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.BiomesYouGo;

public class PotionClarity extends Potion {
    public PotionClarity() {
        super(false, 0xA8E6CF);
        setPotionName("effect.clarity");
        setIconIndex(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(BiomesYouGo.MODID, "textures/gui/potions/clarity.png"));
        return true;
    }
}
