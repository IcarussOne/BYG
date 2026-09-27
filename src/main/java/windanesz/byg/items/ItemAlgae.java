package windanesz.byg.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/** Places algae on top of a still-water source instead of replacing that water. */
public class ItemAlgae extends ItemBlock {
    public ItemAlgae(Block block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (isStillWater(state)) {
            return placeOnWater(player, world, pos, hand, hitX, hitY, hitZ);
        }

        RayTraceResult liquidHit = this.rayTrace(world, player, true);
        if (liquidHit != null && liquidHit.typeOfHit == RayTraceResult.Type.BLOCK
                && isStillWater(world.getBlockState(liquidHit.getBlockPos()))) {
            return placeOnWater(player, world, liquidHit.getBlockPos(), hand, hitX, hitY, hitZ);
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        RayTraceResult liquidHit = this.rayTrace(world, player, true);
        if (liquidHit != null && liquidHit.typeOfHit == RayTraceResult.Type.BLOCK
                && isStillWater(world.getBlockState(liquidHit.getBlockPos()))) {
            EnumActionResult result = placeOnWater(player, world, liquidHit.getBlockPos(), hand, 0.5F, 1.0F, 0.5F);
            return new ActionResult<>(result, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    private boolean isStillWater(IBlockState state) {
        return state.getMaterial() == net.minecraft.block.material.Material.WATER
                && (!state.getPropertyKeys().contains(BlockLiquid.LEVEL) || state.getValue(BlockLiquid.LEVEL) == 0);
    }

    private EnumActionResult placeOnWater(EntityPlayer player, World world, BlockPos waterPos, EnumHand hand, float hitX, float hitY, float hitZ) {
        BlockPos placementPos = waterPos.up();
        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem.isEmpty() || !player.canPlayerEdit(placementPos, EnumFacing.UP, heldItem)
                || !world.getBlockState(placementPos).getBlock().isReplaceable(world, placementPos)
                || !this.block.canPlaceBlockAt(world, placementPos)) {
            return EnumActionResult.FAIL;
        }

        IBlockState placementState = this.block.getStateForPlacement(world, placementPos, EnumFacing.UP, hitX, hitY, hitZ, 0, player);
        if (!this.placeBlockAt(heldItem, player, world, placementPos, EnumFacing.UP, hitX, hitY, hitZ, placementState)) {
            return EnumActionResult.FAIL;
        }

        SoundType soundType = this.block.getSoundType(placementState, world, placementPos, player);
        world.playSound(player, placementPos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
                (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        if (!player.capabilities.isCreativeMode) {
            heldItem.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }
}
