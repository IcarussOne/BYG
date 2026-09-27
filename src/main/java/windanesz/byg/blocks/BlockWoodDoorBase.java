package windanesz.byg.blocks;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class BlockWoodDoorBase extends BlockDoor {
    private final Supplier<Item> doorItem;

    public BlockWoodDoorBase(String registryName, int harvestLevel, float hardness, float resistance, Supplier<Item> doorItem) {
        super(Material.WOOD);
        this.doorItem = doorItem;
        this.setRegistryName(registryName);
        this.setTranslationKey(registryName);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(null);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == EnumDoorHalf.UPPER ? Items.AIR : this.doorItem.get();
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.doorItem.get());
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }
}
