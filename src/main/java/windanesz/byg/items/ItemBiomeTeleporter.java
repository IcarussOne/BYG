package windanesz.byg.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.client.BYGBiomeTab;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Creative-only testing aid: right-click to teleport to the nearest instance of the item's biome.
 */
public class ItemBiomeTeleporter extends Item {
    /** Cells are sampled at biome-generation resolution (4 blocks); a tile covers 64 x 64 cells. */
    private static final int TILE_CELLS = 64;
    private static final int TILE_BLOCKS = TILE_CELLS * 4;
    /** Search radius in tiles (~ 25,000 blocks). */
    private static final int MAX_TILE_RADIUS = 48;

    private static final String TAG_BIOME = "biome";

    public ItemBiomeTeleporter() {
        setRegistryName(BiomesYouGo.MODID, "biome_teleporter");
        setTranslationKey("biome_teleporter");
        setCreativeTab(BYGBiomeTab.tab);
        setMaxStackSize(1);
    }

    public static ItemStack createStack(Item item, ResourceLocation biomeId) {
        ItemStack stack = new ItemStack(item);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString(TAG_BIOME, biomeId.toString());
        return stack;
    }

    @Nullable
    public static ResourceLocation getBiomeId(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey(TAG_BIOME, 8) ? new ResourceLocation(tag.getString(TAG_BIOME)) : null;
    }

    @Nullable
    public static Biome getBiome(ItemStack stack) {
        ResourceLocation id = getBiomeId(stack);
        return id == null ? null : Biome.REGISTRY.getObject(id);
    }

    /** Populated when the creative tab is opened, so biomes registered by any mod are included. */
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) {
            return;
        }
        List<ResourceLocation> ids = new ArrayList<>(Biome.REGISTRY.getKeys());
        ids.sort(Comparator.comparing((ResourceLocation id) -> !BiomesYouGo.MODID.equals(id.getNamespace()))
                .thenComparing(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath));
        for (ResourceLocation id : ids) {
            items.add(createStack(this, id));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Biome biome = getBiome(stack);
        ResourceLocation id = getBiomeId(stack);
        String name = biome != null ? biome.getBiomeName() : id != null ? id.toString() : I18n.format("item.byg.biome_teleporter.no_biome");
        return I18n.format("item.byg.biome_teleporter.named", name);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.byg.biome_teleporter"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote || !(player instanceof EntityPlayerMP)) {
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        if (!player.capabilities.isCreativeMode) {
            player.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.creative_only"));
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        Biome biome = getBiome(stack);
        if (biome == null) {
            player.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.invalid_biome", String.valueOf(getBiomeId(stack))));
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }

        BlockPos target = findBiome(world, biome, player.getPosition());
        if (target == null) {
            player.sendMessage(msg(TextFormatting.RED, "message.byg.biome_teleporter.not_found",
                    biome.getBiomeName(), MAX_TILE_RADIUS * TILE_BLOCKS));
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }

        // Load the surrounding chunks so decoration (trees etc.) exists before choosing the landing height.
        int chunkX = target.getX() >> 4;
        int chunkZ = target.getZ() >> 4;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                world.getChunk(chunkX + dx, chunkZ + dz);
            }
        }
        BlockPos ground = world.getTopSolidOrLiquidBlock(target);
        player.dismountRidingEntity();
        ((EntityPlayerMP) player).connection.setPlayerLocation(ground.getX() + 0.5, ground.getY() + 1, ground.getZ() + 0.5,
                player.rotationYaw, player.rotationPitch);
        player.fallDistance = 0;
        player.sendMessage(msg(TextFormatting.GREEN, "message.byg.biome_teleporter.teleported",
                biome.getBiomeName(), ground.getX(), ground.getZ()));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    private static ITextComponent msg(TextFormatting color, String key, Object... args) {
        return new TextComponentTranslation(key, args).setStyle(new Style().setColor(color));
    }

    /** Spirals outwards tile by tile and returns the matching cell closest to the tile centre, or null. */
    @Nullable
    private static BlockPos findBiome(World world, Biome wanted, BlockPos origin) {
        int originCellX = origin.getX() >> 2;
        int originCellZ = origin.getZ() >> 2;
        for (int radius = 0; radius <= MAX_TILE_RADIUS; radius++) {
            BlockPos best = null;
            long bestDistance = Long.MAX_VALUE;
            for (int tx = -radius; tx <= radius; tx++) {
                for (int tz = -radius; tz <= radius; tz++) {
                    if (Math.max(Math.abs(tx), Math.abs(tz)) != radius) {
                        continue;
                    }
                    int cellX = originCellX + tx * TILE_CELLS - TILE_CELLS / 2;
                    int cellZ = originCellZ + tz * TILE_CELLS - TILE_CELLS / 2;
                    Biome[] biomes = world.getBiomeProvider().getBiomesForGeneration(null, cellX, cellZ, TILE_CELLS, TILE_CELLS);
                    for (int i = 0; i < biomes.length; i++) {
                        if (biomes[i] != wanted) {
                            continue;
                        }
                        int x = (cellX + i % TILE_CELLS) * 4 + 2;
                        int z = (cellZ + i / TILE_CELLS) * 4 + 2;
                        long dx = x - origin.getX();
                        long dz = z - origin.getZ();
                        long distance = dx * dx + dz * dz;
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            best = new BlockPos(x, 0, z);
                        }
                    }
                }
            }
            if (best != null) {
                return best;
            }
        }
        return null;
    }
}
