package windanesz.byg.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.items.ItemDoorBase;
import windanesz.byg.items.ItemGlowcaneStalkBase;
import windanesz.byg.registry.ModItems;

import java.util.Collections;

public class BYGTab {
    public static final CreativeTabs tab = new CreativeTabs("tabbyg") {

        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.byg_logo, 1);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            super.displayAllRelevantItems(items);
            Collections.sort(items, BYGTab::compareStacks);
        }
    };

    private BYGTab() {
    }

    private static int compareStacks(ItemStack left, ItemStack right) {
        Item leftItem = left.getItem();
        Item rightItem = right.getItem();
        int groupComparison = Integer.compare(getCreativeGroup(leftItem), getCreativeGroup(rightItem));
        if (groupComparison != 0) {
            return groupComparison;
        }

        int subgroupComparison = Integer.compare(getCreativeSubgroup(leftItem), getCreativeSubgroup(rightItem));
        if (subgroupComparison != 0) {
            return subgroupComparison;
        }

        int materialComparison = getCreativeSortKey(leftItem).compareTo(getCreativeSortKey(rightItem));
        if (materialComparison != 0) {
            return materialComparison;
        }

        return getRegistryPath(leftItem).compareTo(getRegistryPath(rightItem));
    }

    private static int getCreativeGroup(Item item) {
        if (isBlockLike(item)) {
            return 0;
        }
        if (isToolLike(item)) {
            return 1;
        }
        if (item instanceof ItemFood) {
            return 2;
        }
        if (item instanceof ItemArmor) {
            return 3;
        }
        return 4;
    }

    private static int getCreativeSubgroup(Item item) {
        String path = getRegistryPath(item);
        if (isBlockLike(item)) {
            return getBlockSubgroup(path);
        }
        if (isToolLike(item)) {
            return getToolTypeOrder(path);
        }
        if (item instanceof ItemFood) {
            return getFoodSubgroup(path);
        }
        if (item instanceof ItemArmor) {
            return getArmorSubgroup(path);
        }
        return 0;
    }

    private static boolean isBlockLike(Item item) {
        String path = getRegistryPath(item);
        return item instanceof ItemBlock
                || item instanceof ItemDoor
                || item instanceof ItemDoorBase
                || item instanceof ItemGlowcaneStalkBase
                || "cattail".equals(path)
                || "reeds".equals(path)
                || path.endsWith("glowshroomitem");
    }

    private static boolean isToolLike(Item item) {
        return item instanceof ItemTool
                || item instanceof ItemSword
                || item instanceof ItemHoe;
    }

    private static int getBlockSubgroup(String path) {
        if (path.contains("sapling")) {
            return 0;
        }
        if (path.endsWith("_log") || path.endsWith("_wood")) {
            return 1;
        }
        if (path.contains("leaves")) {
            return 2;
        }
        if (path.contains("planks")) {
            return 3;
        }
        if (path.contains("bookshelf")) {
            return 4;
        }
        if (path.contains("slab")) {
            return 5;
        }
        if (path.contains("stairs")) {
            return 6;
        }
        if (path.contains("fence_gate") || path.contains("gate")) {
            return 7;
        }
        if (path.contains("fence")) {
            return 8;
        }
        if (path.contains("wall")) {
            return 9;
        }
        if (path.contains("door") || path.contains("trapdoor")) {
            return 10;
        }
        if (path.contains("flower") || path.contains("bush") || path.contains("mushroom") || path.contains("glowshroom")
                || path.contains("cactus") || path.contains("reed") || path.contains("cattail") || path.contains("lily")
                || path.contains("vine") || path.contains("ivy") || path.contains("petal") || path.contains("thorn")) {
            return 11;
        }
        if (path.contains("grass") || path.contains("dirt") || path.contains("sand") || path.contains("mud") || path.contains("peat")) {
            return 12;
        }
        if (path.contains("ore") || path.contains("stone") || path.contains("rock") || path.contains("crystal") || path.contains("sodalite")) {
            return 13;
        }
        return 14;
    }

    private static int getToolTypeOrder(String path) {
        if (path.endsWith("sword")) {
            return 0;
        }
        if (path.endsWith("pickaxe")) {
            return 1;
        }
        if (path.endsWith("axe") && !path.endsWith("battleaxe")) {
            return 2;
        }
        if (path.endsWith("shovel")) {
            return 3;
        }
        if (path.endsWith("hoe")) {
            return 4;
        }
        if (path.endsWith("battleaxe")) {
            return 5;
        }
        return 6;
    }

    private static int getFoodSubgroup(String path) {
        if (path.contains("soup") || path.contains("stew") || path.contains("mash")) {
            return 2;
        }
        if (path.contains("pie") || path.contains("bread")) {
            return 1;
        }
        return 0;
    }

    private static int getArmorSubgroup(String path) {
        if (path.endsWith("_helmet")) {
            return 0;
        }
        if (path.endsWith("_chestplate")) {
            return 1;
        }
        if (path.endsWith("_leggings")) {
            return 2;
        }
        if (path.endsWith("_boots")) {
            return 3;
        }
        return 4;
    }

    private static String getCreativeSortKey(Item item) {
        String path = getRegistryPath(item);
        if (path.endsWith("_petal")) {
            return "petal_" + path.substring(0, path.length() - "_petal".length());
        }
        if (isToolLike(item)) {
            return stripToolSuffix(path);
        }
        if (item instanceof ItemArmor) {
            return stripArmorSuffix(path);
        }
        return path;
    }

    private static String stripToolSuffix(String path) {
        if (path.endsWith("battleaxe")) {
            return path.substring(0, path.length() - "battleaxe".length());
        }
        if (path.endsWith("pickaxe")) {
            return path.substring(0, path.length() - "pickaxe".length());
        }
        if (path.endsWith("shovel")) {
            return path.substring(0, path.length() - "shovel".length());
        }
        if (path.endsWith("sword")) {
            return path.substring(0, path.length() - "sword".length());
        }
        if (path.endsWith("hoe")) {
            return path.substring(0, path.length() - "hoe".length());
        }
        if (path.endsWith("axe")) {
            return path.substring(0, path.length() - "axe".length());
        }
        return path;
    }

    private static String stripArmorSuffix(String path) {
        if (path.endsWith("_helmet")) {
            return path.substring(0, path.length() - "_helmet".length());
        }
        if (path.endsWith("_chestplate")) {
            return path.substring(0, path.length() - "_chestplate".length());
        }
        if (path.endsWith("_leggings")) {
            return path.substring(0, path.length() - "_leggings".length());
        }
        if (path.endsWith("_boots")) {
            return path.substring(0, path.length() - "_boots".length());
        }
        return path;
    }

    private static String getRegistryPath(Item item) {
        ResourceLocation registryName = item.getRegistryName();
        return registryName == null ? "" : registryName.getPath();
    }
}
