package windanesz.byg.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import windanesz.byg.client.BYGTab;
import windanesz.byg.entity.EntityMudBall;

public class ItemMudballs extends Item {
    public ItemMudballs() {
        setTranslationKey("mud_balls");
        setRegistryName("mud_balls");
        setCreativeTab(BYGTab.tab);
        setMaxStackSize(64);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, player.getSoundCategory(),
                0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));

        if (!world.isRemote) {
            EntityMudBall mudBall = new EntityMudBall(world, player);
            mudBall.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f);
            world.spawnEntity(mudBall);
        }

        player.addStat(StatList.getObjectUseStats(this));
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
