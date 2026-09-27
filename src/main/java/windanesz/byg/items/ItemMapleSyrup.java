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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMapleSyrup extends ItemFood {

    private static final int SIP_COOLDOWN_TICKS = 20;

    public ItemMapleSyrup() {
        super(Config.getFoodLevel("maple_syrup", 3), Config.getFoodSaturation("maple_syrup", 0.5f), false);
        setTranslationKey("maple_syrup");
        setRegistryName("maple_syrup");
        setCreativeTab(BYGTab.tab);
        setMaxStackSize(1);
        setMaxDamage(Config.getMapleSyrupSips());
        setNoRepair();
        setContainerItem(Items.GLASS_BOTTLE);
        setAlwaysEdible();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.maple_syrup"));
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.maple_syrup_sips", stack.getMaxDamage() - stack.getItemDamage()));
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
        int speedDuration = Config.getMapleSyrupSpeedDuration();
        if (!world.isRemote && speedDuration > 0) {
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, speedDuration, 0));
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        // Each drink is one sip: the food and Speed apply per sip, and the last sip leaves the glass bottle.
        int sips = stack.getItemDamage() + 1;
        super.onItemUseFinish(stack.copy(), world, entityLiving);
        if (entityLiving instanceof EntityPlayer) {
            ((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, SIP_COOLDOWN_TICKS);
        }
        if (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer) entityLiving).capabilities.isCreativeMode) {
            return stack;
        }
        if (sips >= stack.getMaxDamage()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        stack.setItemDamage(sips);
        return stack;
    }
}
