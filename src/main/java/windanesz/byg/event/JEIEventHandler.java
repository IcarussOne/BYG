package windanesz.byg.event;

import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import windanesz.byg.registry.ModItems;

@JEIPlugin
public final class JEIEventHandler implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        IItemBlacklist blacklist = registry.getJeiHelpers().getItemBlacklist();
        blacklist.addItemToBlacklist(new ItemStack(ModItems.byg_logo));
    }
}
