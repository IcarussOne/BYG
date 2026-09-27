package windanesz.byg.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

public class ItemBlueberry extends ItemFood {
    public ItemBlueberry() {
        super(Config.getFoodLevel("blueberry", 2), Config.getFoodSaturation("blueberry", 0.3f), false);
        this.setTranslationKey("blueberry");
        this.setRegistryName("blueberry");
        this.setCreativeTab(BYGTab.tab);
        this.setMaxStackSize(64);
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 22;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!Config.isBlueberryPlantingEnabled()) {
            return EnumActionResult.PASS;
        }
        ItemStack stack = player.getHeldItem(hand);
        BlockPos plantPos = pos.up();
        if (world.getBlockState(pos).getBlock() != Blocks.GRASS || !world.isAirBlock(plantPos) || !player.canPlayerEdit(plantPos, facing, stack)) {
            return EnumActionResult.PASS;
        }
        if (!world.isRemote) {
            world.setBlockState(plantPos, windanesz.byg.registry.ModBlocks.blueberry_bush.getDefaultState(), 3);
            if (!player.capabilities.isCreativeMode && Config.doesBlueberryPlantingConsumeItem()) {
                stack.shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

}
