package windanesz.byg.registry;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.potion.PotionClarity;

@ObjectHolder(BiomesYouGo.MODID)
@Mod.EventBusSubscriber(modid = BiomesYouGo.MODID)
public final class ModPotions {
    public static final Potion clarity = placeholder();
    public static final PotionType clarity_potion = placeholder();
    private static Potion registeredClarity;

    private ModPotions() {
    }

    @SuppressWarnings("ConstantConditions")
    private static <T> T placeholder() {
        return null;
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        registeredClarity = new PotionClarity().setRegistryName("clarity");
        event.getRegistry().register(registeredClarity);
    }

    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        event.getRegistry().register(new PotionType(new PotionEffect(registeredClarity, 300)).setRegistryName("clarity_potion"));
    }
}
