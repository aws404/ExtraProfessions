package com.github.aws404.extra_professions;

import com.github.aws404.extra_professions.block.SawmillBlock;
import com.github.aws404.extra_professions.block_entity.SawmillBlockEntity;
import com.github.aws404.extra_professions.mixin.SerializerAccessor;
import com.github.aws404.extra_professions.mixin.VillagerEntityAccessor;
import com.github.aws404.extra_professions.recipe.SawmillRecipe;
import com.github.aws404.extra_professions.screen.SawmillScreen;
import com.github.aws404.extra_professions.screen.SawmillScreenHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.structure.v1.StructurePoolAddCallback;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.feature.VillagePlacedFeatures;
import net.minecraft.world.poi.PointOfInterestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtraProfessionsMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "extra_professions";
	public static final Logger LOGGER = LoggerFactory.getLogger("Extra Professions");

	public static final Block SAWMILL_BLOCK = Registry.register(Registry.BLOCK, id("sawmill"), new SawmillBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)));

	public static final Item SAWMILL_ITEM = Registry.register(Registry.ITEM, id("sawmill"), new BlockItem(SAWMILL_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

	public static final BlockEntityType<SawmillBlockEntity> SAWMILL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("sawmill"), FabricBlockEntityTypeBuilder.create(SawmillBlockEntity::new, SAWMILL_BLOCK).build());

	public static final PointOfInterestType LUMBERJACK_POI = PointOfInterestHelper.register(id("lumberjack"), 1, 1, SAWMILL_BLOCK);

	public static final VillagerProfession LUMBERJACK_PROFESSION = Registry.register(Registry.VILLAGER_PROFESSION, id("lumberjack"), VillagerProfessionBuilder.create()
			.id(id("lumberjack"))
			.workstation(LUMBERJACK_POI)
			.harvestableItems(Items.ACACIA_SAPLING, Items.BIRCH_SAPLING, Items.SPRUCE_SAPLING, Items.JUNGLE_SAPLING, Items.OAK_SAPLING, Items.DARK_OAK_SAPLING,
					Items.ACACIA_LOG, Items.BIRCH_LOG, Items.SPRUCE_LOG, Items.JUNGLE_LOG, Items.OAK_LOG, Items.DARK_OAK_LOG,
					Items.STICK)
			.secondaryJobSites(Blocks.ACACIA_LOG, Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.DARK_OAK_LOG)
			.workSound(SoundEvents.BLOCK_WOOD_HIT)
			.build()
	);

	public static final ScreenHandlerType<SawmillScreenHandler> SAWMILL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(id("sawmill"), SawmillScreenHandler::new);

	public static final RecipeType<SawmillRecipe> SAWMILL_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, id("sawmill"), new RecipeType<>() {
		@Override
		public String toString() {
			return "extra_professions:sawmill";
		}
	});

	public static final RecipeSerializer<SawmillRecipe> SAWMILL_RECIPE_SERIALISER = Registry.register(Registry.RECIPE_SERIALIZER, id("sawmill"), SerializerAccessor.createSerializer(SawmillRecipe::new));

	public static final Tag<Item> WOODEN_ITEMS_TAG = TagFactory.ITEM.create(id("wooden_items"));

	/**
	 * Create an identifier with the mod ID as the namespace.
	 * @param string the identifier's path
	 * @return the identifier
	 */
	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Extra Professions");

		// Add Apples as a valid Villager food item
		VillagerEntityAccessor.setITEM_FOOD_VALUES(ImmutableMap.<Item, Integer>builder().putAll(VillagerEntity.ITEM_FOOD_VALUES).put(Items.APPLE, 1).build());
		VillagerEntityAccessor.setGATHERABLE_ITEMS(ImmutableSet.<Item>builder().addAll(VillagerEntityAccessor.getGATHERABLE_ITEMS()).add(Items.APPLE).build());

		// Create tree structure pools
		StructurePools.register(new StructurePool(id("trees/spruce"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofFeature(VillagePlacedFeatures.SPRUCE), 1), Pair.of(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":trees/spruce_sapling", StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.RIGID));
		StructurePools.register(new StructurePool(id("trees/oak"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofFeature(VillagePlacedFeatures.OAK), 1), Pair.of(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":trees/oak_sapling", StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.RIGID));
		StructurePools.register(new StructurePool(id("trees/acacia"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofFeature(VillagePlacedFeatures.ACACIA), 1), Pair.of(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":trees/acacia_sapling", StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.RIGID));

		// Add new buildings to villages
		StructurePoolAddCallback.EVENT.register(structurePool -> {
			if (structurePool.getId().equals(new Identifier("minecraft:village/plains/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/plains/houses/plains_lumber_farm_1").apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/plains/zombie/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":village/plains/houses/plains_lumber_farm_1", StructureProcessorLists.ZOMBIE_PLAINS).apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/desert/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/desert/houses/desert_lumber_farm_1").apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/desert/zombie/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":village/desert/houses/desert_lumber_farm_1", StructureProcessorLists.ZOMBIE_DESERT).apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/savanna/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/savanna/houses/savanna_lumber_farm_1").apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/savanna/zombie/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":village/savanna/houses/savanna_lumber_farm_1", StructureProcessorLists.ZOMBIE_SAVANNA).apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/snowy/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/snowy/houses/snowy_lumber_farm_1").apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/snowy/zombie/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":village/snowy/houses/snowy_lumber_farm_1", StructureProcessorLists.ZOMBIE_SNOWY).apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/taiga/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/taiga/houses/taiga_lumber_farm_1").apply(StructurePool.Projection.RIGID), 3);
			}
			if (structurePool.getId().equals(new Identifier("minecraft:village/taiga/zombie/houses"))) {
				structurePool.addStructurePoolElement(StructurePoolElement.ofProcessedLegacySingle(MOD_ID + ":village/taiga/houses/taiga_lumber_farm_1", StructureProcessorLists.ZOMBIE_TAIGA).apply(StructurePool.Projection.RIGID), 3);
			}
		});

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
				dispatcher.register(CommandManager.literal("structure")
						.then(CommandManager.argument("structure", IdentifierArgumentType.identifier())
								.executes(context -> {
									Identifier id = IdentifierArgumentType.getIdentifier(context, "structure");
									Structure structure = context.getSource().getServer().getStructureManager().getStructure(id).orElseThrow(() -> new CommandException(new LiteralText("Could not find structure")));
									BlockPos pos = new BlockPos(context.getSource().getPosition());
									structure.place(context.getSource().getWorld(), pos, pos, new StructurePlacementData(), context.getSource().getWorld().random, 2);
									return 1;
								})
						)
				);
			});

			StructurePools.register(new StructurePool(
					id("test_lumber_farm"),
					new Identifier("empty"),
					ImmutableList.of(
							Pair.of(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/plains/houses/plains_lumber_farm_1"), 1),
							Pair.of(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/desert/houses/desert_lumber_farm_1"), 1),
							Pair.of(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/savanna/houses/savanna_lumber_farm_1"), 1),
							Pair.of(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/snowy/houses/snowy_lumber_farm_1"), 1),
							Pair.of(StructurePoolElement.ofLegacySingle(MOD_ID + ":village/taiga/houses/taiga_lumber_farm_1"), 1)
					),
					StructurePool.Projection.RIGID)
			);
		}
	}

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(SAWMILL_BLOCK, RenderLayer.getCutout());
		ScreenRegistry.register(SAWMILL_SCREEN_HANDLER, SawmillScreen::new);
	}
}
