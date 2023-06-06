package io.github.andrew6rant;

import io.github.andrew6rant.BiomeTintConfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BiomeTint implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("biome-tint");
	public static final BiomeTintConfig CONFIG = BiomeTintConfig.createAndLoad();

	@Override
	public void onInitializeClient() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> provideColors());
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> provideColors());
	}

	public void provideColors() {
		for(String blockId : CONFIG.blockIds()) {
			try {
				Identifier id = new Identifier(blockId.substring(0, blockId.indexOf('#')));
				String color = blockId.substring(blockId.indexOf('#'));
				if (color.equals("#grass")) {
					ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
						return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor();
					}, Registries.BLOCK.get(id));
					ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
						return GrassColors.getColor(0.5, 1.0); // default grass color used in Vanilla
					}, Registries.BLOCK.get(id));
				} else {
					try {
						ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> Integer.decode(color), Registries.BLOCK.get(id));
						ColorProviderRegistry.ITEM.register((stack, tintIndex) -> Integer.decode(color), Registries.BLOCK.get(id));
					} catch (Exception e) {
						LOGGER.error("BIOME-TINT: "+blockId+ " is malformed!");
						LOGGER.error("the color part of each config value must be either \"grass\", or a hex color (RRGGBB)");
					}
				}
			} catch (Exception e) {
				LOGGER.error("BIOME-TINT: "+blockId+ " is malformed!");
				LOGGER.error("Each config value must be in the format: namespace:block#hexcolor");
			}
		}
	}
}