package windanesz.byg.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import windanesz.byg.Config;

import java.util.function.BooleanSupplier;

/**
 * Condition factory for checking if an equipment set is enabled in the config.
 * This is used in the recipe JSON files to conditionally include (or exclude) recipes based on the config.
 */
@SuppressWarnings("unused")
public class EquipmentSetEnabledCondition implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        String setName = JsonUtils.getString(json, "set");
        return () -> Config.isEquipmentSetEnabled(setName);
    }
}
