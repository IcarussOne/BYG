package windanesz.byg.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BYGBiomeTab {
    public static final CreativeTabs tab = new CreativeTabs("tabbygbiomes") {

        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.FILLED_MAP);
        }
    };

    private BYGBiomeTab() {
    }
}
