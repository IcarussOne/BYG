package windanesz.byg.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBerryJuice extends ItemFood {

    public ItemBerryJuice() {
        super(Config.getFoodLevel("berry_juice", 2), Config.getFoodSaturation("berry_juice", 0.6f), false);
        setTranslationKey("berry_juice");
        setRegistryName("berry_juice");
        setCreativeTab(BYGTab.tab);
        setMaxStackSize(16);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.berry_juice.heals"));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        super.onFoodEaten(stack, world, player);
        if (world.isRemote) {
            return;
        }
        float healAmount = Config.getBerryJuiceHealAmount();
        if (healAmount > 0.0F) {
            player.heal(healAmount);
        }
        int regenerationDuration = Config.getBerryJuiceRegenerationDuration();
        if (regenerationDuration > 0) {
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, regenerationDuration));
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        // ItemFood shrinks and returns the held stack, so the emptied bottle goes to the
        // inventory instead of replacing it the way the stack-size-1 soups do.
        ItemStack remainder = super.onItemUseFinish(stack, world, entityLiving);
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) {
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.inventory.addItemStackToInventory(bottle)) {
                    player.dropItem(bottle, false);
                }
            }
        }
        return remainder;
    }
}
