package windanesz.byg.armour;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ArmorMaterialKasai {
    @GameRegistry.ObjectHolder("byg:kasai_armour_helmet")
    public static final Item helmet = null;
    @GameRegistry.ObjectHolder("byg:kasai_armour_body")
    public static final Item body = null;
    @GameRegistry.ObjectHolder("byg:kasai_armour_legs")
    public static final Item legs = null;
    @GameRegistry.ObjectHolder("byg:kasai_armour_boots")
    public static final Item boots = null;

    public static int countWornPieces(EntityPlayer player) {
        int count = 0;
        count += isWorn(player, EntityEquipmentSlot.HEAD, helmet) ? 1 : 0;
        count += isWorn(player, EntityEquipmentSlot.CHEST, body) ? 1 : 0;
        count += isWorn(player, EntityEquipmentSlot.LEGS, legs) ? 1 : 0;
        count += isWorn(player, EntityEquipmentSlot.FEET, boots) ? 1 : 0;
        return count;
    }

    private static boolean isWorn(EntityPlayer player, EntityEquipmentSlot slot, Item expected) {
        return expected != null && player.getItemStackFromSlot(slot).getItem() == expected;
    }
}
