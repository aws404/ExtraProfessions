package com.github.aws404.extra_professions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aws404.extra_professions.block.ExtraBlocks;
import com.github.aws404.extra_professions.block.ExtraPointOfInterestTypes;
import com.github.aws404.extra_professions.mixin.accessor.VillagerEntityAccessor;
import com.github.aws404.extra_professions.recipe.ExtraRecipeTypes;
import com.github.aws404.extra_professions.screen.AnnealerScreen;
import com.github.aws404.extra_professions.screen.DippingStationScreen;
import com.github.aws404.extra_professions.screen.ExtraScreenHandlers;
import com.github.aws404.extra_professions.screen.SawmillScreen;
import com.github.aws404.extra_professions.structure.ExtraStructurePools;
import com.github.aws404.extra_professions.structure.ExtraStructureProcessorTypes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;

@SuppressWarnings("unused")
public class ExtraProfessionsMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "extra_professions";
	public static final Logger LOGGER = LoggerFactory.getLogger("Extra Professions");

	public static final VillagerProfession LUMBERJACK_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, id("lumberjack"), VillagerProfessionBuilder.create()
			.id(id("lumberjack"))
			.workstation(ExtraPointOfInterestTypes.LUMBERJACK)
			.harvestableItems(
					Items.ACACIA_SAPLING, Items.BIRCH_SAPLING, Items.SPRUCE_SAPLING, Items.JUNGLE_SAPLING, Items.OAK_SAPLING, Items.DARK_OAK_SAPLING,
					Items.ACACIA_LOG, Items.BIRCH_LOG, Items.SPRUCE_LOG, Items.JUNGLE_LOG, Items.OAK_LOG, Items.DARK_OAK_LOG,
					Items.STICK
			)
			.secondaryJobSites(Blocks.PODZOL, Blocks.ACACIA_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.DARK_OAK_LOG)
			.workSound(SoundEvents.BLOCK_WOOD_HIT)
			.build()
	);

	public static final VillagerProfession CHANDLER_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, id("chandler"), VillagerProfessionBuilder.create()
			.id(id("chandler"))
			.workstation(ExtraPointOfInterestTypes.CHANDLER)
			.secondaryJobSites(Blocks.FURNACE)
			.workSound(SoundEvents.BLOCK_BEEHIVE_WORK)
			.build()
	);

	public static final VillagerProfession GLAZIER_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, id("glazier"), VillagerProfessionBuilder.create()
			.id(id("glazier"))
			.workstation(ExtraPointOfInterestTypes.GLAZIER)
			.workSound(SoundEvents.BLOCK_GLASS_HIT)
			.build()
	);

	public static final VillagerProfession MINER_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, id("miner"), VillagerProfessionBuilder.create()
			.id(id("miner"))
			.workstation(ExtraPointOfInterestTypes.MINER)
			.secondaryJobSites(Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.COPPER_ORE, Blocks.EMERALD_ORE)
			.workSound(SoundEvents.BLOCK_STONE_BREAK)
			.build()
	);

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ExtraBlocks.SAWMILL_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ExtraBlocks.DIPPING_STATION_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ExtraBlocks.MINING_DRILL_BLOCK, RenderLayer.getCutout());
		HandledScreens.register(ExtraScreenHandlers.SAWMILL, SawmillScreen::new);
		HandledScreens.register(ExtraScreenHandlers.ANNEALER, AnnealerScreen::new);
		HandledScreens.register(ExtraScreenHandlers.DIPPING_STATION, DippingStationScreen::new);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Extra Professions");

		// Bootstrap registries
		ExtraRecipeTypes.init();
		ExtraScreenHandlers.init();
		ExtraStats.init();
		ExtraStructureProcessorTypes.init();

		// Add Apples as a valid Villager food item
		VillagerEntityAccessor.setITEM_FOOD_VALUES(ImmutableMap.<Item, Integer>builder().putAll(VillagerEntity.ITEM_FOOD_VALUES).put(Items.APPLE, 1).build());
		VillagerEntityAccessor.setGATHERABLE_ITEMS(ImmutableSet.<Item>builder().addAll(VillagerEntityAccessor.getGATHERABLE_ITEMS()).add(Items.APPLE).build());

		// Register hero of the village gifts
		ExtraLootTables.registerHeroOfTheVillageGifts();

		// Add new buildings to villages
		ServerLifecycleEvents.SERVER_STARTING.register(ExtraStructurePools::addAllStructures);

		// Command registration
		CommandRegistrationCallback.EVENT.register(ExtraCommands::registerCommands);
	}

	/**
	 * Create an identifier with the mod ID as the namespace.
	 * @param string the identifier's path
	 * @return the identifier
	 */
	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}
