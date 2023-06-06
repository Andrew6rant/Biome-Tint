package io.github.andrew6rant;

import io.github.andrew6rant.BiomeTintConfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BiomeTint implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("biome-tint");
	public static final BiomeTintConfig CONFIG = BiomeTintConfig.createAndLoad();

	@Override
	public void onInitializeClient() {
		for(String blockId : CONFIG.blockIds()) {
			try {
				BlockRenderLayerMap.INSTANCE.putBlock(Registries.BLOCK.get(new Identifier(blockId.substring(0, blockId.indexOf('#')))), RenderLayer.getCutoutMipped());
			} catch (Exception e) {
				LOGGER.error("BIOME-TINT: "+blockId+ " is malformed!");
				LOGGER.error("Each config value must be in the format: namespace:block#hexcolor");
			}
		}
		ServerLifecycleEvents.SERVER_STARTED.register(server -> provideColors());
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> provideColors());
	}

	public void provideColors() {
		//BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MOSSY_COBBLESTONE, RenderLayer.getCutoutMipped());
		for(String blockId : CONFIG.blockIds()) {
			try {
				Identifier id = new Identifier(blockId.substring(0, blockId.indexOf('#')));
				String color = blockId.substring(blockId.indexOf('#'));
				switch(color) {
					case "#grass":
						ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
							return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor();
						}, Registries.BLOCK.get(id));
						ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
							return GrassColors.getColor(0.5, 1.0); // default grass color used in Vanilla
						}, Registries.BLOCK.get(id));
						break;
					case "#foliage":
						ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
							return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
						}, Registries.BLOCK.get(id));
						ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
							return FoliageColors.getDefaultColor(); // default foliage color used in Vanilla (actual block colors in Vanila are hardcoded)
						}, Registries.BLOCK.get(id));
						break;
					case "#water":
						ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
							return world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1;
						}, Registries.BLOCK.get(id));
						//ColorProviderRegistry.ITEM.register((stack, tintIndex) -> { 	// I have to figure out what to do with the item
						//	return FoliageColors.getDefaultColor();					  	// because Vanilla has no water item tint at all
						//}, Registries.BLOCK.get(id));
						break;
					case "#foliage_spruce":
						ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> FoliageColors.getSpruceColor(), Registries.BLOCK.get(id));
						ColorProviderRegistry.ITEM.register((stack, tintIndex) -> FoliageColors.getSpruceColor(), Registries.BLOCK.get(id));
						break;
					case "#foliage_birch":
						ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> FoliageColors.getBirchColor(), Registries.BLOCK.get(id));
						ColorProviderRegistry.ITEM.register((stack, tintIndex) -> FoliageColors.getBirchColor(), Registries.BLOCK.get(id));
						break;
					case "#foliage_mangrove":
						ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> FoliageColors.getMangroveColor(), Registries.BLOCK.get(id));
						ColorProviderRegistry.ITEM.register((stack, tintIndex) -> FoliageColors.getMangroveColor(), Registries.BLOCK.get(id));
						break;
					default:
						try {
							ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> Integer.decode(color), Registries.BLOCK.get(id));
							ColorProviderRegistry.ITEM.register((stack, tintIndex) -> Integer.decode(color), Registries.BLOCK.get(id));
						} catch (Exception e) {
							LOGGER.error("BIOME-TINT: "+blockId+ " is malformed!");
							LOGGER.error("the color part of each config value must be either \"grass\", or a hex color (RRGGBB)");
						}
					// yes, I know the block's renderLayer was already set on init, but this just catches changes to the config as to not require a restart.
					BlockRenderLayerMap.INSTANCE.putBlock(Registries.BLOCK.get(new Identifier(blockId.substring(0, blockId.indexOf('#')))), RenderLayer.getCutoutMipped());
				}
			} catch (Exception e) {
				LOGGER.error("BIOME-TINT: "+blockId+ " is malformed!");
				LOGGER.error("Each config value must be in the format: namespace:block#hexcolor");
			}
		}
	}
}

// 8425a3
// 2572a3