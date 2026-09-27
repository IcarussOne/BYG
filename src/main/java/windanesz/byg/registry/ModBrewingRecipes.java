package windanesz.byg.registry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import windanesz.byg.Config;

public final class ModBrewingRecipes {
    private ModBrewingRecipes() {
    }

    public static void init() {
        if (!Config.isClarityPotionBrewingEnabled() || !Config.isBaobabContentEnabled()) {
            return;
        }
        ItemStack clarityPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), ModPotions.clarity_potion);
        ItemStack claritySplashPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), ModPotions.clarity_potion);
        ItemStack clarityLingeringPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), ModPotions.clarity_potion);

        BrewingRecipeRegistry.addRecipe(
                new ItemStack(Items.POTIONITEM),
                new ItemStack(ModItems.baobab_powder),
                clarityPotion);
        BrewingRecipeRegistry.addRecipe(
                clarityPotion,
                new ItemStack(Items.GUNPOWDER),
                claritySplashPotion);
        BrewingRecipeRegistry.addRecipe(
                claritySplashPotion,
                new ItemStack(Items.DRAGON_BREATH),
                clarityLingeringPotion);
    }
}
