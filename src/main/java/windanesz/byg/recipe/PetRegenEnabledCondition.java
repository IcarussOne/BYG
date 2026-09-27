package windanesz.byg.recipe;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import windanesz.byg.Config;

import java.util.function.BooleanSupplier;

/**
 * Condition factory for checking if the golden beetroot pet regeneration is enabled in the config.
 * This is used in the recipe JSON files to conditionally include (or exclude) recipes based on the config.
 */
@SuppressWarnings("unused")
public class PetRegenEnabledCondition implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return Config::isGoldenBeetrootPetRegenEnabled;
    }
}
