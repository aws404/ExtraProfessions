package com.github.aws404.extra_professions;

import com.github.aws404.booking_it.BookingIt;
import com.github.aws404.extra_professions.block.ExtraBlocks;
import com.github.aws404.extra_professions.block.ExtraPointOfInterestTypes;
import com.github.aws404.extra_professions.mixin.SerializerAccessor;
import com.github.aws404.extra_professions.mixin.VillagerEntityAccessor;
import com.github.aws404.extra_professions.recipe.AnnealingRecipe;
import com.github.aws404.extra_professions.recipe.SawmillRecipe;
import com.github.aws404.extra_professions.screen.AnnealerScreen;
import com.github.aws404.extra_professions.screen.ExtraScreenHandlers;
import com.github.aws404.extra_professions.screen.SawmillScreen;
import com.github.aws404.extra_professions.structure.ExtraStructurePools;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.structure.v1.StructurePoolAddCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			.secondaryJobSites(Blocks.ACACIA_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.DARK_OAK_LOG)
			.workSound(SoundEvents.BLOCK_WOOD_HIT)
			.build()
	);

	public static final VillagerProfession CHANDLER_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, id("chandler"), VillagerProfessionBuilder.create()
			.id(id("chandler"))
			.workstation(ExtraPointOfInterestTypes.CHANDLER)
			.workSound(SoundEvents.BLOCK_BEEHIVE_WORK)
			.build()
	);

	public static final VillagerProfession GLAZIER_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, id("glazier"), VillagerProfessionBuilder.create()
			.id(id("glazier"))
			.workstation(ExtraPointOfInterestTypes.GLAZIER)
			.workSound(SoundEvents.BLOCK_GLASS_HIT)
			.build()
	);

	public static final RecipeType<SawmillRecipe> SAWMILL_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, id("sawmill"), new RecipeType<>() {
		@Override
		public String toString() {
			return sId("sawmill");
		}
	});
	public static final RecipeType<AnnealingRecipe> ANNEALING_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, id("annealing"), new RecipeType<>() {
		@Override
		public String toString() {
			return sId("annealing");
		}
	});

	public static final RecipeSerializer<SawmillRecipe> SAWMILL_RECIPE_SERIALISER = Registry.register(Registry.RECIPE_SERIALIZER, id("sawmill"), SerializerAccessor.createSerializer(SawmillRecipe::new));
	public static final RecipeSerializer<AnnealingRecipe> ANNEALING_RECIPE_SERIALISER = Registry.register(Registry.RECIPE_SERIALIZER, id("annealing"), new CookingRecipeSerializer<>(AnnealingRecipe::new, 100));

	public static final RecipeBookCategory ANNEALING_RECIPE_BOOK_CATEGORY = BookingIt.getCategory("ANNEALING");

	/**
	 * Create an identifier with the mod ID as the namespace.
	 * @param string the identifier's path
	 * @return the identifier
	 */
	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}

	/**
	 * Create a string identifier with the mod ID as the namespace.
	 * @param string the identifier's path
	 * @return the string identifier
	 */
	public static String sId(String string) {
		return MOD_ID + ":" + string;
	}

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ExtraBlocks.SAWMILL_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ExtraBlocks.DIPPING_STATION_BLOCK, RenderLayer.getTranslucent());
		ScreenRegistry.register(ExtraScreenHandlers.SAWMILL_SCREEN_HANDLER, SawmillScreen::new);
		ScreenRegistry.register(ExtraScreenHandlers.LEHR_SCREEN_HANDLER, AnnealerScreen::new);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Extra Professions");

		// Add Apples as a valid Villager food item
		VillagerEntityAccessor.setITEM_FOOD_VALUES(ImmutableMap.<Item, Integer>builder().putAll(VillagerEntity.ITEM_FOOD_VALUES).put(Items.APPLE, 1).build());
		VillagerEntityAccessor.setGATHERABLE_ITEMS(ImmutableSet.<Item>builder().addAll(VillagerEntityAccessor.getGATHERABLE_ITEMS()).add(Items.APPLE).build());

		// Add new buildings to villages
		StructurePoolAddCallback.EVENT.register(structurePool -> {
			if (ExtraStructurePools.EXTRA_STRUCTURES.containsKey(structurePool.getId())) {
				ExtraStructurePools.EXTRA_STRUCTURES.get(structurePool.getId()).forEach(pair -> structurePool.addStructurePoolElement(pair.getFirst(), pair.getSecond()));
			}
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			ExtraCommands.registerCommands(dispatcher);
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				ExtraCommands.registerDevCommands(dispatcher);
			}
		});

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			ExtraStructurePools.registerDevPools();
		}
	}
}
