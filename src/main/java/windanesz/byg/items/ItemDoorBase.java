package windanesz.byg.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.blocks.BlockDoorPartBase;
import windanesz.byg.client.BYGTab;

import java.util.function.Supplier;

public class ItemDoorBase extends Item {
    private final Supplier<Block> bottomBlock;
    private final Supplier<Block> topBlock;

    public ItemDoorBase(String registryName, Supplier<Block> bottomBlock, Supplier<Block> topBlock) {
        this.bottomBlock = bottomBlock;
        this.topBlock = topBlock;
        this.setMaxDamage(0);
        this.maxStackSize = 64;
        this.setTranslationKey(registryName);
        this.setRegistryName(registryName);
        this.setCreativeTab(BYGTab.tab);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block bottom = this.bottomBlock.get();
        Block top = this.topBlock.get();
        if (bottom == null || top == null) {
            return EnumActionResult.FAIL;
        }
        BlockPos bottomPos = pos.up();
        BlockPos topPos = bottomPos.up();
        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(bottomPos, side, stack) || !player.canPlayerEdit(topPos, side, stack) || !world.mayPlace(bottom, bottomPos, false, side, player) || !world.mayPlace(top, topPos, false, side, player)) {
            return EnumActionResult.FAIL;
        }
        EnumFacing facing = player.getHorizontalFacing().getOpposite();
        if (!world.isRemote) {
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation("block.wood.place")), SoundCategory.NEUTRAL, 1.0f, 1.0f);
            world.setBlockState(bottomPos, bottom.getDefaultState().withProperty(BlockDoorPartBase.FACING, facing), 3);
            world.setBlockState(topPos, top.getDefaultState().withProperty(BlockDoorPartBase.FACING, facing), 3);
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
