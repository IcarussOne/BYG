package windanesz.byg.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;

import java.util.List;
import java.util.function.Supplier;

public class ItemGlowcaneStalkBase extends Item {
    private final Supplier<Block> placedBlock;

    public ItemGlowcaneStalkBase(String registryName, Supplier<Block> placedBlock) {
        this.placedBlock = placedBlock;
        this.setMaxDamage(0);
        this.maxStackSize = 64;
        this.setTranslationKey(registryName);
        this.setRegistryName(registryName);
        this.setCreativeTab(BYGTab.tab);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(I18n.format("tooltip.byg.glowcane_stalk"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block blockToPlace = this.placedBlock.get();
        Block clickedBlock = world.getBlockState(pos).getBlock();
        if (clickedBlock != windanesz.byg.registry.ModBlocks.glowcelium && clickedBlock != blockToPlace) {
            return EnumActionResult.PASS;
        }
        BlockPos placePos = pos.up();
        if (blockToPlace == null || !player.canPlayerEdit(placePos, facing, player.getHeldItem(hand)) || !world.mayPlace(blockToPlace, placePos, false, facing, player) || !blockToPlace.canPlaceBlockAt(world, placePos)) {
            return EnumActionResult.FAIL;
        }
        if (!world.isRemote) {
            world.setBlockState(placePos, blockToPlace.getDefaultState(), 3);
            if (!player.capabilities.isCreativeMode) {
                player.getHeldItem(hand).shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

}
