package windanesz.byg.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import windanesz.byg.Config;

import java.util.function.BooleanSupplier;

/**
 * Condition factory for checking if an ore content is enabled in the config.
 * This is used in the recipe JSON files to conditionally include (or exclude) recipes based on the config.
 */
@SuppressWarnings("unused")
public class OreContentEnabledCondition implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        if (json.has("any")) {
            String[] ores = new String[json.getAsJsonArray("any").size()];
            for (int i = 0; i < ores.length; i++) {
                ores[i] = json.getAsJsonArray("any").get(i).getAsString();
                Config.isOreContentEnabled(ores[i]);
            }
            return () -> {
                for (String any : ores) {
                    if (Config.isOreContentEnabled(any)) {
                        return true;
                    }
                }
                return false;
            };
        }
        String ore = JsonUtils.getString(json, "ore");
        Config.isOreContentEnabled(ore);
        return () -> Config.isOreContentEnabled(ore);
    }
}
