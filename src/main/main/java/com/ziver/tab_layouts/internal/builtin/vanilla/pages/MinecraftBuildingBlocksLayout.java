package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.layout.CtlPageBuilder;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public final class MinecraftBuildingBlocksLayout {
    private MinecraftBuildingBlocksLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.BUILDING_BLOCKS)

                .page(CreativeTabLayouts.id("minecraft/building_blocks/wood"), page -> {
                    wood(page, "oak",
                            Items.OAK_LOG,
                            Items.OAK_WOOD,
                            Items.STRIPPED_OAK_LOG,
                            Items.STRIPPED_OAK_WOOD,
                            Items.OAK_PLANKS,
                            Items.OAK_STAIRS,
                            Items.OAK_SLAB,
                            Items.OAK_FENCE,
                            Items.OAK_FENCE_GATE,
                            Items.OAK_DOOR,
                            Items.OAK_TRAPDOOR,
                            Items.OAK_PRESSURE_PLATE,
                            Items.OAK_BUTTON,
                            Items.OAK_SIGN,
                            Items.OAK_HANGING_SIGN
                    );

                    wood(page, "spruce",
                            Items.SPRUCE_LOG,
                            Items.SPRUCE_WOOD,
                            Items.STRIPPED_SPRUCE_LOG,
                            Items.STRIPPED_SPRUCE_WOOD,
                            Items.SPRUCE_PLANKS,
                            Items.SPRUCE_STAIRS,
                            Items.SPRUCE_SLAB,
                            Items.SPRUCE_FENCE,
                            Items.SPRUCE_FENCE_GATE,
                            Items.SPRUCE_DOOR,
                            Items.SPRUCE_TRAPDOOR,
                            Items.SPRUCE_PRESSURE_PLATE,
                            Items.SPRUCE_BUTTON,
                            Items.SPRUCE_SIGN,
                            Items.SPRUCE_HANGING_SIGN
                    );

                    wood(page, "birch",
                            Items.BIRCH_LOG,
                            Items.BIRCH_WOOD,
                            Items.STRIPPED_BIRCH_LOG,
                            Items.STRIPPED_BIRCH_WOOD,
                            Items.BIRCH_PLANKS,
                            Items.BIRCH_STAIRS,
                            Items.BIRCH_SLAB,
                            Items.BIRCH_FENCE,
                            Items.BIRCH_FENCE_GATE,
                            Items.BIRCH_DOOR,
                            Items.BIRCH_TRAPDOOR,
                            Items.BIRCH_PRESSURE_PLATE,
                            Items.BIRCH_BUTTON,
                            Items.BIRCH_SIGN,
                            Items.BIRCH_HANGING_SIGN
                    );

                    wood(page, "jungle",
                            Items.JUNGLE_LOG,
                            Items.JUNGLE_WOOD,
                            Items.STRIPPED_JUNGLE_LOG,
                            Items.STRIPPED_JUNGLE_WOOD,
                            Items.JUNGLE_PLANKS,
                            Items.JUNGLE_STAIRS,
                            Items.JUNGLE_SLAB,
                            Items.JUNGLE_FENCE,
                            Items.JUNGLE_FENCE_GATE,
                            Items.JUNGLE_DOOR,
                            Items.JUNGLE_TRAPDOOR,
                            Items.JUNGLE_PRESSURE_PLATE,
                            Items.JUNGLE_BUTTON,
                            Items.JUNGLE_SIGN,
                            Items.JUNGLE_HANGING_SIGN
                    );

                    wood(page, "acacia",
                            Items.ACACIA_LOG,
                            Items.ACACIA_WOOD,
                            Items.STRIPPED_ACACIA_LOG,
                            Items.STRIPPED_ACACIA_WOOD,
                            Items.ACACIA_PLANKS,
                            Items.ACACIA_STAIRS,
                            Items.ACACIA_SLAB,
                            Items.ACACIA_FENCE,
                            Items.ACACIA_FENCE_GATE,
                            Items.ACACIA_DOOR,
                            Items.ACACIA_TRAPDOOR,
                            Items.ACACIA_PRESSURE_PLATE,
                            Items.ACACIA_BUTTON,
                            Items.ACACIA_SIGN,
                            Items.ACACIA_HANGING_SIGN
                    );

                    wood(page, "dark_oak",
                            Items.DARK_OAK_LOG,
                            Items.DARK_OAK_WOOD,
                            Items.STRIPPED_DARK_OAK_LOG,
                            Items.STRIPPED_DARK_OAK_WOOD,
                            Items.DARK_OAK_PLANKS,
                            Items.DARK_OAK_STAIRS,
                            Items.DARK_OAK_SLAB,
                            Items.DARK_OAK_FENCE,
                            Items.DARK_OAK_FENCE_GATE,
                            Items.DARK_OAK_DOOR,
                            Items.DARK_OAK_TRAPDOOR,
                            Items.DARK_OAK_PRESSURE_PLATE,
                            Items.DARK_OAK_BUTTON,
                            Items.DARK_OAK_SIGN,
                            Items.DARK_OAK_HANGING_SIGN
                    );

                    wood(page, "mangrove",
                            Items.MANGROVE_LOG,
                            Items.MANGROVE_WOOD,
                            Items.STRIPPED_MANGROVE_LOG,
                            Items.STRIPPED_MANGROVE_WOOD,
                            Items.MANGROVE_PLANKS,
                            Items.MANGROVE_STAIRS,
                            Items.MANGROVE_SLAB,
                            Items.MANGROVE_FENCE,
                            Items.MANGROVE_FENCE_GATE,
                            Items.MANGROVE_DOOR,
                            Items.MANGROVE_TRAPDOOR,
                            Items.MANGROVE_PRESSURE_PLATE,
                            Items.MANGROVE_BUTTON,
                            Items.MANGROVE_SIGN,
                            Items.MANGROVE_HANGING_SIGN
                    );

                    wood(page, "cherry",
                            Items.CHERRY_LOG,
                            Items.CHERRY_WOOD,
                            Items.STRIPPED_CHERRY_LOG,
                            Items.STRIPPED_CHERRY_WOOD,
                            Items.CHERRY_PLANKS,
                            Items.CHERRY_STAIRS,
                            Items.CHERRY_SLAB,
                            Items.CHERRY_FENCE,
                            Items.CHERRY_FENCE_GATE,
                            Items.CHERRY_DOOR,
                            Items.CHERRY_TRAPDOOR,
                            Items.CHERRY_PRESSURE_PLATE,
                            Items.CHERRY_BUTTON,
                            Items.CHERRY_SIGN,
                            Items.CHERRY_HANGING_SIGN
                    );

                    wood(page, "bamboo",
                            Items.BAMBOO_BLOCK,
                            Items.STRIPPED_BAMBOO_BLOCK,
                            Items.BAMBOO_PLANKS,
                            Items.BAMBOO_MOSAIC,
                            Items.BAMBOO_STAIRS,
                            Items.BAMBOO_MOSAIC_STAIRS,
                            Items.BAMBOO_SLAB,
                            Items.BAMBOO_MOSAIC_SLAB,
                            Items.BAMBOO_FENCE,
                            Items.BAMBOO_FENCE_GATE,
                            Items.BAMBOO_DOOR,
                            Items.BAMBOO_TRAPDOOR,
                            Items.BAMBOO_PRESSURE_PLATE,
                            Items.BAMBOO_BUTTON,
                            Items.BAMBOO_SIGN,
                            Items.BAMBOO_HANGING_SIGN
                    );

                    wood(page, "crimson",
                            Items.CRIMSON_STEM,
                            Items.CRIMSON_HYPHAE,
                            Items.STRIPPED_CRIMSON_STEM,
                            Items.STRIPPED_CRIMSON_HYPHAE,
                            Items.CRIMSON_PLANKS,
                            Items.CRIMSON_STAIRS,
                            Items.CRIMSON_SLAB,
                            Items.CRIMSON_FENCE,
                            Items.CRIMSON_FENCE_GATE,
                            Items.CRIMSON_DOOR,
                            Items.CRIMSON_TRAPDOOR,
                            Items.CRIMSON_PRESSURE_PLATE,
                            Items.CRIMSON_BUTTON,
                            Items.CRIMSON_SIGN,
                            Items.CRIMSON_HANGING_SIGN
                    );

                    wood(page, "warped",
                            Items.WARPED_STEM,
                            Items.WARPED_HYPHAE,
                            Items.STRIPPED_WARPED_STEM,
                            Items.STRIPPED_WARPED_HYPHAE,
                            Items.WARPED_PLANKS,
                            Items.WARPED_STAIRS,
                            Items.WARPED_SLAB,
                            Items.WARPED_FENCE,
                            Items.WARPED_FENCE_GATE,
                            Items.WARPED_DOOR,
                            Items.WARPED_TRAPDOOR,
                            Items.WARPED_PRESSURE_PLATE,
                            Items.WARPED_BUTTON,
                            Items.WARPED_SIGN,
                            Items.WARPED_HANGING_SIGN
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/building_blocks/stone"), page -> {
                    section(page, "stone",
                            Items.STONE,
                            Items.STONE_STAIRS,
                            Items.STONE_SLAB,
                            Items.STONE_PRESSURE_PLATE,
                            Items.STONE_BUTTON,
                            Items.SMOOTH_STONE,
                            Items.SMOOTH_STONE_SLAB
                    );

                    section(page, "cobblestone",
                            Items.COBBLESTONE,
                            Items.COBBLESTONE_STAIRS,
                            Items.COBBLESTONE_SLAB,
                            Items.COBBLESTONE_WALL,
                            Items.MOSSY_COBBLESTONE,
                            Items.MOSSY_COBBLESTONE_STAIRS,
                            Items.MOSSY_COBBLESTONE_SLAB,
                            Items.MOSSY_COBBLESTONE_WALL
                    );

                    section(page, "stone_bricks",
                            Items.STONE_BRICKS,
                            Items.CRACKED_STONE_BRICKS,
                            Items.STONE_BRICK_STAIRS,
                            Items.STONE_BRICK_SLAB,
                            Items.STONE_BRICK_WALL,
                            Items.CHISELED_STONE_BRICKS,
                            Items.MOSSY_STONE_BRICKS,
                            Items.MOSSY_STONE_BRICK_STAIRS,
                            Items.MOSSY_STONE_BRICK_SLAB,
                            Items.MOSSY_STONE_BRICK_WALL
                    );

                    section(page, "granite",
                            Items.GRANITE,
                            Items.GRANITE_STAIRS,
                            Items.GRANITE_SLAB,
                            Items.GRANITE_WALL,
                            Items.POLISHED_GRANITE,
                            Items.POLISHED_GRANITE_STAIRS,
                            Items.POLISHED_GRANITE_SLAB
                    );

                    section(page, "diorite",
                            Items.DIORITE,
                            Items.DIORITE_STAIRS,
                            Items.DIORITE_SLAB,
                            Items.DIORITE_WALL,
                            Items.POLISHED_DIORITE,
                            Items.POLISHED_DIORITE_STAIRS,
                            Items.POLISHED_DIORITE_SLAB
                    );

                    section(page, "andesite",
                            Items.ANDESITE,
                            Items.ANDESITE_STAIRS,
                            Items.ANDESITE_SLAB,
                            Items.ANDESITE_WALL,
                            Items.POLISHED_ANDESITE,
                            Items.POLISHED_ANDESITE_STAIRS,
                            Items.POLISHED_ANDESITE_SLAB
                    );

                    section(page, "bricks",
                            Items.BRICKS,
                            Items.BRICK_STAIRS,
                            Items.BRICK_SLAB,
                            Items.BRICK_WALL
                    );

                    section(page, "mud_bricks",
                            Items.PACKED_MUD,
                            Items.MUD_BRICKS,
                            Items.MUD_BRICK_STAIRS,
                            Items.MUD_BRICK_SLAB,
                            Items.MUD_BRICK_WALL
                    );

                    section(page, "sandstone",
                            Items.SANDSTONE,
                            Items.SANDSTONE_STAIRS,
                            Items.SANDSTONE_SLAB,
                            Items.SANDSTONE_WALL,
                            Items.CHISELED_SANDSTONE,
                            Items.SMOOTH_SANDSTONE,
                            Items.SMOOTH_SANDSTONE_STAIRS,
                            Items.SMOOTH_SANDSTONE_SLAB,
                            Items.CUT_SANDSTONE,
                            Items.CUT_STANDSTONE_SLAB
                    );

                    section(page, "red_sandstone",
                            Items.RED_SANDSTONE,
                            Items.RED_SANDSTONE_STAIRS,
                            Items.RED_SANDSTONE_SLAB,
                            Items.RED_SANDSTONE_WALL,
                            Items.CHISELED_RED_SANDSTONE,
                            Items.SMOOTH_RED_SANDSTONE,
                            Items.SMOOTH_RED_SANDSTONE_STAIRS,
                            Items.SMOOTH_RED_SANDSTONE_SLAB,
                            Items.CUT_RED_SANDSTONE,
                            Items.CUT_RED_SANDSTONE_SLAB
                    );

                    section(page, "prismarine",
                            Items.SEA_LANTERN,
                            Items.PRISMARINE,
                            Items.PRISMARINE_STAIRS,
                            Items.PRISMARINE_SLAB,
                            Items.PRISMARINE_WALL,
                            Items.PRISMARINE_BRICKS,
                            Items.PRISMARINE_BRICK_STAIRS,
                            Items.PRISMARINE_BRICK_SLAB,
                            Items.DARK_PRISMARINE,
                            Items.DARK_PRISMARINE_STAIRS,
                            Items.DARK_PRISMARINE_SLAB
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/building_blocks/deepslate_and_tuff"), page -> {
                    section(page, "deepslate",
                            Items.DEEPSLATE,
                            Items.COBBLED_DEEPSLATE,
                            Items.COBBLED_DEEPSLATE_STAIRS,
                            Items.COBBLED_DEEPSLATE_SLAB,
                            Items.COBBLED_DEEPSLATE_WALL,
                            Items.CHISELED_DEEPSLATE
                    );

                    section(page, "polished_deepslate",
                            Items.POLISHED_DEEPSLATE,
                            Items.POLISHED_DEEPSLATE_STAIRS,
                            Items.POLISHED_DEEPSLATE_SLAB,
                            Items.POLISHED_DEEPSLATE_WALL
                    );

                    section(page, "deepslate_bricks",
                            Items.DEEPSLATE_BRICKS,
                            Items.CRACKED_DEEPSLATE_BRICKS,
                            Items.DEEPSLATE_BRICK_STAIRS,
                            Items.DEEPSLATE_BRICK_SLAB,
                            Items.DEEPSLATE_BRICK_WALL
                    );

                    section(page, "deepslate_tiles",
                            Items.DEEPSLATE_TILES,
                            Items.CRACKED_DEEPSLATE_TILES,
                            Items.DEEPSLATE_TILE_STAIRS,
                            Items.DEEPSLATE_TILE_SLAB,
                            Items.DEEPSLATE_TILE_WALL,
                            Items.REINFORCED_DEEPSLATE
                    );

                    section(page, "tuff",
                            Items.TUFF,
                            Items.TUFF_STAIRS,
                            Items.TUFF_SLAB,
                            Items.TUFF_WALL,
                            Items.CHISELED_TUFF
                    );

                    section(page, "polished_tuff",
                            Items.POLISHED_TUFF,
                            Items.POLISHED_TUFF_STAIRS,
                            Items.POLISHED_TUFF_SLAB,
                            Items.POLISHED_TUFF_WALL
                    );

                    section(page, "tuff_bricks",
                            Items.TUFF_BRICKS,
                            Items.TUFF_BRICK_STAIRS,
                            Items.TUFF_BRICK_SLAB,
                            Items.TUFF_BRICK_WALL,
                            Items.CHISELED_TUFF_BRICKS
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/building_blocks/nether_and_end"), page -> {
                    section(page, "nether_bricks",
                            Items.NETHERRACK,
                            Items.NETHER_BRICKS,
                            Items.CRACKED_NETHER_BRICKS,
                            Items.NETHER_BRICK_STAIRS,
                            Items.NETHER_BRICK_SLAB,
                            Items.NETHER_BRICK_WALL,
                            Items.NETHER_BRICK_FENCE,
                            Items.CHISELED_NETHER_BRICKS,
                            Items.RED_NETHER_BRICKS,
                            Items.RED_NETHER_BRICK_STAIRS,
                            Items.RED_NETHER_BRICK_SLAB,
                            Items.RED_NETHER_BRICK_WALL
                    );

                    section(page, "basalt",
                            Items.BASALT,
                            Items.SMOOTH_BASALT,
                            Items.POLISHED_BASALT
                    );

                    section(page, "blackstone",
                            Items.BLACKSTONE,
                            Items.GILDED_BLACKSTONE,
                            Items.BLACKSTONE_STAIRS,
                            Items.BLACKSTONE_SLAB,
                            Items.BLACKSTONE_WALL,
                            Items.CHISELED_POLISHED_BLACKSTONE,
                            Items.POLISHED_BLACKSTONE,
                            Items.POLISHED_BLACKSTONE_STAIRS,
                            Items.POLISHED_BLACKSTONE_SLAB,
                            Items.POLISHED_BLACKSTONE_WALL,
                            Items.POLISHED_BLACKSTONE_PRESSURE_PLATE,
                            Items.POLISHED_BLACKSTONE_BUTTON,
                            Items.POLISHED_BLACKSTONE_BRICKS,
                            Items.CRACKED_POLISHED_BLACKSTONE_BRICKS,
                            Items.POLISHED_BLACKSTONE_BRICK_STAIRS,
                            Items.POLISHED_BLACKSTONE_BRICK_SLAB,
                            Items.POLISHED_BLACKSTONE_BRICK_WALL
                    );

                    section(page, "quartz",
                            Items.QUARTZ_BLOCK,
                            Items.QUARTZ_STAIRS,
                            Items.QUARTZ_SLAB,
                            Items.CHISELED_QUARTZ_BLOCK,
                            Items.QUARTZ_BRICKS,
                            Items.QUARTZ_PILLAR,
                            Items.SMOOTH_QUARTZ,
                            Items.SMOOTH_QUARTZ_STAIRS,
                            Items.SMOOTH_QUARTZ_SLAB
                    );

                    section(page, "end_stone",
                            Items.END_STONE,
                            Items.END_STONE_BRICKS,
                            Items.END_STONE_BRICK_STAIRS,
                            Items.END_STONE_BRICK_SLAB,
                            Items.END_STONE_BRICK_WALL
                    );

                    section(page, "purpur",
                            Items.PURPUR_BLOCK,
                            Items.PURPUR_PILLAR,
                            Items.PURPUR_STAIRS,
                            Items.PURPUR_SLAB
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/building_blocks/resource_blocks"), page -> {
                    section(page, "coal",
                            Items.COAL_BLOCK
                    );

                    section(page, "iron",
                            Items.IRON_BLOCK,
                            Items.IRON_BARS,
                            Items.IRON_DOOR,
                            Items.IRON_TRAPDOOR,
                            Items.HEAVY_WEIGHTED_PRESSURE_PLATE,
                            Items.CHAIN
                    );

                    section(page, "gold",
                            Items.GOLD_BLOCK,
                            Items.LIGHT_WEIGHTED_PRESSURE_PLATE
                    );

                    section(page, "redstone",
                            Items.REDSTONE_BLOCK
                    );

                    section(page, "gems",
                            Items.EMERALD_BLOCK,
                            Items.LAPIS_BLOCK,
                            Items.DIAMOND_BLOCK,
                            Items.NETHERITE_BLOCK,
                            Items.AMETHYST_BLOCK
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/building_blocks/copper"), page -> {
                    section(page, "copper",
                            Items.COPPER_BLOCK,
                            Items.CHISELED_COPPER,
                            Items.COPPER_GRATE,
                            Items.CUT_COPPER,
                            Items.CUT_COPPER_STAIRS,
                            Items.CUT_COPPER_SLAB,
                            Items.COPPER_DOOR,
                            Items.COPPER_TRAPDOOR,
                            Items.COPPER_BULB,

                            Items.WAXED_COPPER_BLOCK,
                            Items.WAXED_CHISELED_COPPER,
                            Items.WAXED_COPPER_GRATE,
                            Items.WAXED_CUT_COPPER,
                            Items.WAXED_CUT_COPPER_STAIRS,
                            Items.WAXED_CUT_COPPER_SLAB,
                            Items.WAXED_COPPER_DOOR,
                            Items.WAXED_COPPER_TRAPDOOR,
                            Items.WAXED_COPPER_BULB
                    );

                    section(page, "exposed_copper",
                            Items.EXPOSED_COPPER,
                            Items.EXPOSED_CHISELED_COPPER,
                            Items.EXPOSED_COPPER_GRATE,
                            Items.EXPOSED_CUT_COPPER,
                            Items.EXPOSED_CUT_COPPER_STAIRS,
                            Items.EXPOSED_CUT_COPPER_SLAB,
                            Items.EXPOSED_COPPER_DOOR,
                            Items.EXPOSED_COPPER_TRAPDOOR,
                            Items.EXPOSED_COPPER_BULB,

                            Items.WAXED_EXPOSED_COPPER,
                            Items.WAXED_EXPOSED_CHISELED_COPPER,
                            Items.WAXED_EXPOSED_COPPER_GRATE,
                            Items.WAXED_EXPOSED_CUT_COPPER,
                            Items.WAXED_EXPOSED_CUT_COPPER_STAIRS,
                            Items.WAXED_EXPOSED_CUT_COPPER_SLAB,
                            Items.WAXED_EXPOSED_COPPER_DOOR,
                            Items.WAXED_EXPOSED_COPPER_TRAPDOOR,
                            Items.WAXED_EXPOSED_COPPER_BULB
                    );

                    section(page, "weathered_copper",
                            Items.WEATHERED_COPPER,
                            Items.WEATHERED_CHISELED_COPPER,
                            Items.WEATHERED_COPPER_GRATE,
                            Items.WEATHERED_CUT_COPPER,
                            Items.WEATHERED_CUT_COPPER_STAIRS,
                            Items.WEATHERED_CUT_COPPER_SLAB,
                            Items.WEATHERED_COPPER_DOOR,
                            Items.WEATHERED_COPPER_TRAPDOOR,
                            Items.WEATHERED_COPPER_BULB,

                            Items.WAXED_WEATHERED_COPPER,
                            Items.WAXED_WEATHERED_CHISELED_COPPER,
                            Items.WAXED_WEATHERED_COPPER_GRATE,
                            Items.WAXED_WEATHERED_CUT_COPPER,
                            Items.WAXED_WEATHERED_CUT_COPPER_STAIRS,
                            Items.WAXED_WEATHERED_CUT_COPPER_SLAB,
                            Items.WAXED_WEATHERED_COPPER_DOOR,
                            Items.WAXED_WEATHERED_COPPER_TRAPDOOR,
                            Items.WAXED_WEATHERED_COPPER_BULB
                    );

                    section(page, "oxidized_copper",
                            Items.OXIDIZED_COPPER,
                            Items.OXIDIZED_CHISELED_COPPER,
                            Items.OXIDIZED_COPPER_GRATE,
                            Items.OXIDIZED_CUT_COPPER,
                            Items.OXIDIZED_CUT_COPPER_STAIRS,
                            Items.OXIDIZED_CUT_COPPER_SLAB,
                            Items.OXIDIZED_COPPER_DOOR,
                            Items.OXIDIZED_COPPER_TRAPDOOR,
                            Items.OXIDIZED_COPPER_BULB,

                            Items.WAXED_OXIDIZED_COPPER,
                            Items.WAXED_OXIDIZED_CHISELED_COPPER,
                            Items.WAXED_OXIDIZED_COPPER_GRATE,
                            Items.WAXED_OXIDIZED_CUT_COPPER,
                            Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
                            Items.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                            Items.WAXED_OXIDIZED_COPPER_DOOR,
                            Items.WAXED_OXIDIZED_COPPER_TRAPDOOR,
                            Items.WAXED_OXIDIZED_COPPER_BULB
                    );
                });
    }

    private static void wood(CtlPageBuilder page, String name, ItemLike... items) {
        section(page, name, items);
    }

    private static void section(CtlPageBuilder page, String name, ItemLike... items) {
        page.section(CreativeTabLayouts.id("minecraft/building_blocks/" + name), section -> section.add(items));
    }
}