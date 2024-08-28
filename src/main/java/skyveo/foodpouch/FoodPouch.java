package skyveo.foodpouch;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyveo.foodpouch.item.ModItems;

public class FoodPouch implements ModInitializer {
	public static final String MOD_ID = "foodpouch";
	public static final String MOD_NAME = "Food Pouch";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModItems.load();
	}
}