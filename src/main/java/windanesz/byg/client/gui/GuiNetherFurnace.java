package windanesz.byg.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.blocks.BlockNetherFurnaceLit;

public class GuiNetherFurnace {
    public static int GUIID = 13;

    public static IInventory inherited;




    public static void openGui(Entity entity, World world, BlockPos pos) {
        if (entity instanceof EntityPlayer) {
            ((EntityPlayer) entity).openGui(BiomesYouGo.instance, GUIID, world, pos.getX(), pos.getY(), pos.getZ());
        }
    }




    public static class GuiWindow
            extends GuiContainer {
        private static final ResourceLocation texture = new ResourceLocation("byg:textures/netherfurnacegui.png");
        World world;
        int x;
        int y;
        int z;
        EntityPlayer entity;

        public GuiWindow(World world, int x, int y, int z, EntityPlayer entity) {
            super((Container) new ContainerNetherFurnaceLit(world, x, y, z, entity));
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.entity = entity;
            this.xSize = 176;
            this.ySize = 166;
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.renderHoveredToolTip(mouseX, mouseY);
        }

        protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.renderEngine.bindTexture(texture);
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
            this.zLevel = 100.0f;
            this.mc.renderEngine.bindTexture(new ResourceLocation("byg:textures/nether_furnacegui.png"));
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 256, 256);
        }



        public void initGui() {
            super.initGui();
            this.guiLeft = (this.width - 176) / 2;
            this.guiTop = (this.height - 166) / 2;
            Keyboard.enableRepeatEvents(true);
            this.buttonList.clear();
        }

    }

    public static class ContainerNetherFurnaceLit
            extends Container {
        World world;
        EntityPlayer entity;
        int x;
        int y;
        int z;

        public ContainerNetherFurnaceLit(World world, int x, int y, int z, EntityPlayer player) {
            int si;
            this.world = world;
            this.entity = player;
            this.x = x;
            this.y = y;
            this.z = z;
            TileEntity ent = world.getTileEntity(new BlockPos(x, y, z));
            inherited = ent instanceof BlockNetherFurnaceLit.TileEntityNetherFurnaceLit ? (IInventory) ent : new InventoryBasic("", true, 9);
            this.addSlotToContainer(new Slot(inherited, 0, 56, 53) {
            });
            this.addSlotToContainer(new Slot(inherited, 1, 56, 17) {
            });
            this.addSlotToContainer(new Slot(inherited, 2, 116, 35) {

                public boolean isItemValid(ItemStack stack) {
                    return false;
                }
            });
            for (si = 0; si < 3; ++si) {
                for (int sj = 0; sj < 9; ++sj) {
                    this.addSlotToContainer(new Slot(player.inventory, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
                }
            }
            for (si = 0; si < 9; ++si) {
                this.addSlotToContainer(new Slot(player.inventory, si, 8 + si * 18, 142));
            }
        }

        public boolean canInteractWith(EntityPlayer player) {
            return true;
        }

        public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
            ItemStack itemstack = ItemStack.EMPTY;
            Slot slot = (Slot) this.inventorySlots.get(index);
            if (slot != null && slot.getHasStack()) {
                ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();
                if (index < 3) {
                    if (!this.mergeItemStack(itemstack1, 3, this.inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(itemstack1, itemstack);
                } else if (!this.mergeItemStack(itemstack1, 0, 3, false)) {
                    if (index < 30 ? !this.mergeItemStack(itemstack1, 30, this.inventorySlots.size(), true) : !this.mergeItemStack(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                    return ItemStack.EMPTY;
                }
                if (itemstack1.getCount() == 0) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }
                if (itemstack1.getCount() == itemstack.getCount()) {
                    return ItemStack.EMPTY;
                }
                slot.onTake(playerIn, itemstack1);
            }
            return itemstack;
        }


    }
}






