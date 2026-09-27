package windanesz.byg.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

import java.util.List;

public class ItemBaobabfruit
        extends ItemFood {
    public ItemBaobabfruit() {
        super(Config.getFoodLevel("baobab_fruit", 4), Config.getFoodSaturation("baobab_fruit", 0.3f), false);
        this.setTranslationKey("baobab_fruit");
        this.setRegistryName("baobab_fruit");
        if (Config.isBaobabfruitAlwaysEdible()) {
            this.setAlwaysEdible();
        }
        this.setCreativeTab(BYGTab.tab);
        this.setMaxStackSize(64);
    }

    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entity) {
        super.onFoodEaten(itemStack, world, entity);
        if (Config.doesBaobabfruitClearEffects()) {
            entity.clearActivePotions();
        }
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.baobab_fruit.clear_effects"));
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.baobab_fruit.plant"));
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!Config.isBaobabfruitPlantingEnabled()) {
            return EnumActionResult.PASS;
        }
        ItemStack stack = player.getHeldItem(hand);
        BlockPos plantPos = pos.down();
        if (world.getBlockState(pos).getBlock() != windanesz.byg.registry.ModBlocks.baobab_leaves || !world.isAirBlock(plantPos) || !player.canPlayerEdit(plantPos, facing, stack)) {
            return EnumActionResult.PASS;
        }
        if (!world.isRemote) {
            world.setBlockState(plantPos, windanesz.byg.registry.ModBlocks.baobab_fruit_block.getDefaultState().withProperty(windanesz.byg.blocks.BlockBaobabFruit.STAGE, 0), 3);
            if (!player.capabilities.isCreativeMode && Config.doesBaobabfruitPlantingConsumeItem()) {
                stack.shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

}
