package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import windanesz.byg.Config;
import windanesz.byg.client.gui.GuiNetherFurnace;
import windanesz.byg.registry.ModItems;

import java.util.Random;

public class BlockNetherFurnaceLit
        extends Block
        implements ITileEntityProvider {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;


    public BlockNetherFurnaceLit() {
        super(Material.ROCK);
        this.setRegistryName("nether_furnace_lit");
        this.setTranslationKey("nether_furnace_lit");
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(5.0f);
        this.setResistance(14.0f);
        this.setLightLevel(0.8f);
        this.setLightOpacity(2);
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public static void init(FMLInitializationEvent event) {
        GameRegistry.registerTileEntity(TileEntityNetherFurnaceLit.class, "byg:tileentitynetherfurnacelit");
    }



    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block) this, new IProperty[]{FACING});
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex((int) meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(windanesz.byg.registry.ModBlocks.nether_furnace, 1));
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityNetherFurnaceLit();
    }

    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
        super.eventReceived(state, worldIn, pos, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, ((TileEntityNetherFurnaceLit) tileentity));
        world.removeTileEntity(pos);
        super.breakBlock(world, pos, state);
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockNetherFurnaceLit block = this;
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }

    public int tickRate(World world) {
        return Config.getNetherFurnaceLitTickRate();
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLockableLoot) {
            TileEntityLockableLoot inv = (TileEntityLockableLoot) te;
            ItemStack slot0 = inv.getStackInSlot(0); // Fuel
            ItemStack slot1 = inv.getStackInSlot(1); // Input
            ItemStack slot2 = inv.getStackInSlot(2); // Output
            
            // Check Kasai Ore recipe
            if (Config.isOreContentEnabled("kasai") && !slot1.isEmpty() && slot1.getItem() == net.minecraft.item.Item.getItemFromBlock(windanesz.byg.registry.ModBlocks.kasai_ore) && slot1.getMetadata() == 0) {
                if (!slot0.isEmpty() && slot0.getItem() == Items.BLAZE_POWDER && slot0.getMetadata() == 0) {
                    if (slot2.isEmpty() || (slot2.getItem() == ModItems.kasai_ingot && slot2.getMetadata() == 0 && slot2.getCount() < inv.getInventoryStackLimit() && slot2.getCount() < slot2.getMaxStackSize())) {
                        inv.decrStackSize(0, 1);
                        inv.decrStackSize(1, 1);
                        if (slot2.isEmpty()) {
                            inv.setInventorySlotContents(2, new ItemStack(ModItems.kasai_ingot, 1));
                        } else {
                            slot2.grow(1);
                        }
                    }
                }
            }
        }
        
        if (world.getBlockState(pos.down()).getBlock() != Blocks.FIRE) {
            world.playSound(null, x + 0.5D, y + 0.5D, z + 0.5D, net.minecraft.init.SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 3.0f, 1.0f);
            world.setBlockState(pos, windanesz.byg.registry.ModBlocks.nether_furnace.getDefaultState(), 3);
        } else if (Math.random() < Config.getNetherFurnaceAmbientEffectChance()) {
            if (world instanceof WorldServer) {
                int particleCount = Config.getNetherFurnaceAmbientParticleCount();
                if (particleCount > 0) {
                    ((WorldServer) world).spawnParticle(EnumParticleTypes.DRAGON_BREATH, x + 0.5, y + 0.5, z + 0.5, particleCount, 0.5, 0.5, 0.5, 0.05, new int[0]);
                }
            }
            world.playSound(null, x + 0.5D, y + 0.5D, z + 0.5D, net.minecraft.init.SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, entity, hand, side, hitX, hitY, hitZ);
        GuiNetherFurnace.openGui(entity, world, pos);
        return true;
    }

    public static class TileEntityNetherFurnaceLit
            extends TileEntityLockableLoot {
        private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);

        public int getSizeInventory() {
            return 3;
        }

        public boolean isEmpty() {
            for (ItemStack itemstack : this.stacks) {
                if (itemstack.isEmpty()) continue;
                return false;
            }
            return true;
        }

        public ItemStack getStackInSlot(int slot) {
            return (ItemStack) this.stacks.get(slot);
        }

        public String getName() {
            return this.hasCustomName() ? this.customName : "container.nether_furnace_lit";
        }

        public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);
            this.stacks = NonNullList.withSize((int) this.getSizeInventory(), ItemStack.EMPTY);
            if (!this.checkLootAndRead(compound)) {
                ItemStackHelper.loadAllItems((NBTTagCompound) compound, this.stacks);
            }
            if (compound.hasKey("CustomName", 8)) {
                this.customName = compound.getString("CustomName");
            }
        }

        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            super.writeToNBT(compound);
            if (!this.checkLootAndWrite(compound)) {
                ItemStackHelper.saveAllItems((NBTTagCompound) compound, this.stacks);
            }
            if (this.hasCustomName()) {
                compound.setString("CustomName", this.customName);
            }
            return compound;
        }

        public int getInventoryStackLimit() {
            return 64;
        }

        public String getGuiID() {
            return "byg:nether_furnace_lit";
        }

        public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
            this.fillWithLoot(playerIn);
            return new ContainerChest(playerInventory, this, playerIn);
        }

        protected NonNullList<ItemStack> getItems() {
            return this.stacks;
        }
    }
}


