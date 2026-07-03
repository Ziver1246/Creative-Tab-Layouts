package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.layout.CtlPageBuilder;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public final class MinecraftNaturalBlocksLayout {
    private MinecraftNaturalBlocksLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.NATURAL_BLOCKS)

                .page(CreativeTabLayouts.id("minecraft/natural_blocks/terrain"), page -> {
                    section(page, "ground",
                            Items.GRASS_BLOCK,
                            Items.PODZOL,
                            Items.MYCELIUM,
                            Items.DIRT_PATH,
                            Items.DIRT,
                            Items.COARSE_DIRT,
                            Items.ROOTED_DIRT,
                            Items.FARMLAND,
                            Items.MUD,
                            Items.CLAY,
                            Items.GRAVEL,
                            Items.SUSPICIOUS_GRAVEL
                    );

                    section(page, "sand",
                            Items.SAND,
                            Items.SUSPICIOUS_SAND,
                            Items.SANDSTONE,
                            Items.RED_SAND,
                            Items.RED_SANDSTONE
                    );

                    section(page, "ice_and_snow",
                            Items.ICE,
                            Items.PACKED_ICE,
                            Items.BLUE_ICE,
                            Items.SNOW_BLOCK,
                            Items.SNOW
                    );

                    section(page, "moss",
                            Items.MOSS_BLOCK,
                            Items.MOSS_CARPET
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/natural_blocks/stone_and_dimensions"), page -> {
                    section(page, "stone",
                            Items.BEDROCK,
                            Items.STONE,
                            Items.DEEPSLATE,
                            Items.GRANITE,
                            Items.DIORITE,
                            Items.ANDESITE,
                            Items.CALCITE,
                            Items.TUFF,
                            Items.DRIPSTONE_BLOCK,
                            Items.POINTED_DRIPSTONE
                    );

                    section(page, "nether",
                            Items.MAGMA_BLOCK,
                            Items.OBSIDIAN,
                            Items.CRYING_OBSIDIAN,
                            Items.NETHERRACK,
                            Items.CRIMSON_NYLIUM,
                            Items.WARPED_NYLIUM,
                            Items.SOUL_SAND,
                            Items.SOUL_SOIL,
                            Items.BONE_BLOCK,
                            Items.BLACKSTONE,
                            Items.BASALT,
                            Items.SMOOTH_BASALT
                    );

                    section(page, "end",
                            Items.END_STONE
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/natural_blocks/ores_and_crystals"), page -> {
                    section(page, "ores",
                            Items.COAL_ORE,
                            Items.DEEPSLATE_COAL_ORE,
                            Items.IRON_ORE,
                            Items.DEEPSLATE_IRON_ORE,
                            Items.COPPER_ORE,
                            Items.DEEPSLATE_COPPER_ORE,
                            Items.GOLD_ORE,
                            Items.DEEPSLATE_GOLD_ORE,
                            Items.REDSTONE_ORE,
                            Items.DEEPSLATE_REDSTONE_ORE,
                            Items.EMERALD_ORE,
                            Items.DEEPSLATE_EMERALD_ORE,
                            Items.LAPIS_ORE,
                            Items.DEEPSLATE_LAPIS_ORE,
                            Items.DIAMOND_ORE,
                            Items.DEEPSLATE_DIAMOND_ORE,
                            Items.NETHER_GOLD_ORE,
                            Items.NETHER_QUARTZ_ORE,
                            Items.ANCIENT_DEBRIS
                    );

                    section(page, "raw_blocks",
                            Items.RAW_IRON_BLOCK,
                            Items.RAW_COPPER_BLOCK,
                            Items.RAW_GOLD_BLOCK
                    );

                    section(page, "crystals",
                            Items.GLOWSTONE,
                            Items.AMETHYST_BLOCK,
                            Items.BUDDING_AMETHYST,
                            Items.SMALL_AMETHYST_BUD,
                            Items.MEDIUM_AMETHYST_BUD,
                            Items.LARGE_AMETHYST_BUD,
                            Items.AMETHYST_CLUSTER
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/natural_blocks/trees_and_plants"), page -> {
                    section(page, "leaves",
                            Items.OAK_LEAVES,
                            Items.SPRUCE_LEAVES,
                            Items.BIRCH_LEAVES,
                            Items.JUNGLE_LEAVES,
                            Items.ACACIA_LEAVES,
                            Items.DARK_OAK_LEAVES,
                            Items.MANGROVE_LEAVES,
                            Items.CHERRY_LEAVES,
                            Items.AZALEA_LEAVES,
                            Items.FLOWERING_AZALEA_LEAVES
                    );

                    section(page, "fungi_blocks",
                            Items.BROWN_MUSHROOM_BLOCK,
                            Items.RED_MUSHROOM_BLOCK,
                            Items.MUSHROOM_STEM,
                            Items.NETHER_WART_BLOCK,
                            Items.WARPED_WART_BLOCK,
                            Items.SHROOMLIGHT
                    );

                    section(page, "saplings",
                            Items.OAK_SAPLING,
                            Items.SPRUCE_SAPLING,
                            Items.BIRCH_SAPLING,
                            Items.JUNGLE_SAPLING,
                            Items.ACACIA_SAPLING,
                            Items.DARK_OAK_SAPLING,
                            Items.MANGROVE_PROPAGULE,
                            Items.CHERRY_SAPLING,
                            Items.AZALEA,
                            Items.FLOWERING_AZALEA
                    );

                    section(page, "small_plants",
                            Items.BROWN_MUSHROOM,
                            Items.RED_MUSHROOM,
                            Items.CRIMSON_FUNGUS,
                            Items.WARPED_FUNGUS,
                            Items.SHORT_GRASS,
                            Items.FERN,
                            Items.DEAD_BUSH
                    );

                    section(page, "flowers",
                            Items.DANDELION,
                            Items.POPPY,
                            Items.BLUE_ORCHID,
                            Items.ALLIUM,
                            Items.AZURE_BLUET,
                            Items.RED_TULIP,
                            Items.ORANGE_TULIP,
                            Items.WHITE_TULIP,
                            Items.PINK_TULIP,
                            Items.OXEYE_DAISY,
                            Items.CORNFLOWER,
                            Items.LILY_OF_THE_VALLEY,
                            Items.TORCHFLOWER,
                            Items.WITHER_ROSE,
                            Items.PINK_PETALS,
                            Items.SPORE_BLOSSOM
                    );

                    section(page, "roots_and_vines",
                            Items.MANGROVE_ROOTS,
                            Items.MUDDY_MANGROVE_ROOTS,
                            Items.BAMBOO,
                            Items.SUGAR_CANE,
                            Items.CACTUS,
                            Items.CRIMSON_ROOTS,
                            Items.WARPED_ROOTS,
                            Items.NETHER_SPROUTS,
                            Items.WEEPING_VINES,
                            Items.TWISTING_VINES,
                            Items.VINE
                    );

                    section(page, "tall_plants",
                            Items.TALL_GRASS,
                            Items.LARGE_FERN,
                            Items.SUNFLOWER,
                            Items.LILAC,
                            Items.ROSE_BUSH,
                            Items.PEONY,
                            Items.PITCHER_PLANT,
                            Items.BIG_DRIPLEAF,
                            Items.SMALL_DRIPLEAF,
                            Items.CHORUS_PLANT,
                            Items.CHORUS_FLOWER,
                            Items.GLOW_LICHEN,
                            Items.HANGING_ROOTS
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/natural_blocks/crops_and_aquatic"), page -> {
                    section(page, "eggs",
                            Items.FROGSPAWN,
                            Items.TURTLE_EGG,
                            Items.SNIFFER_EGG
                    );

                    section(page, "seeds_and_crops",
                            Items.WHEAT_SEEDS,
                            Items.COCOA_BEANS,
                            Items.PUMPKIN_SEEDS,
                            Items.MELON_SEEDS,
                            Items.BEETROOT_SEEDS,
                            Items.TORCHFLOWER_SEEDS,
                            Items.PITCHER_POD,
                            Items.GLOW_BERRIES,
                            Items.SWEET_BERRIES,
                            Items.NETHER_WART
                    );

                    section(page, "aquatic_plants",
                            Items.LILY_PAD,
                            Items.SEAGRASS,
                            Items.SEA_PICKLE,
                            Items.KELP,
                            Items.DRIED_KELP_BLOCK
                    );

                    section(page, "coral_blocks",
                            Items.TUBE_CORAL_BLOCK,
                            Items.BRAIN_CORAL_BLOCK,
                            Items.BUBBLE_CORAL_BLOCK,
                            Items.FIRE_CORAL_BLOCK,
                            Items.HORN_CORAL_BLOCK,
                            Items.DEAD_TUBE_CORAL_BLOCK,
                            Items.DEAD_BRAIN_CORAL_BLOCK,
                            Items.DEAD_BUBBLE_CORAL_BLOCK,
                            Items.DEAD_FIRE_CORAL_BLOCK,
                            Items.DEAD_HORN_CORAL_BLOCK
                    );

                    section(page, "corals",
                            Items.TUBE_CORAL,
                            Items.BRAIN_CORAL,
                            Items.BUBBLE_CORAL,
                            Items.FIRE_CORAL,
                            Items.HORN_CORAL,
                            Items.DEAD_TUBE_CORAL,
                            Items.DEAD_BRAIN_CORAL,
                            Items.DEAD_BUBBLE_CORAL,
                            Items.DEAD_FIRE_CORAL,
                            Items.DEAD_HORN_CORAL
                    );

                    section(page, "coral_fans",
                            Items.TUBE_CORAL_FAN,
                            Items.BRAIN_CORAL_FAN,
                            Items.BUBBLE_CORAL_FAN,
                            Items.FIRE_CORAL_FAN,
                            Items.HORN_CORAL_FAN,
                            Items.DEAD_TUBE_CORAL_FAN,
                            Items.DEAD_BRAIN_CORAL_FAN,
                            Items.DEAD_BUBBLE_CORAL_FAN,
                            Items.DEAD_FIRE_CORAL_FAN,
                            Items.DEAD_HORN_CORAL_FAN
                    );

                    section(page, "sponges",
                            Items.SPONGE,
                            Items.WET_SPONGE
                    );
                })

                .page(CreativeTabLayouts.id("minecraft/natural_blocks/organic_and_sculk"), page -> {
                    section(page, "organic_blocks",
                            Items.MELON,
                            Items.PUMPKIN,
                            Items.CARVED_PUMPKIN,
                            Items.JACK_O_LANTERN,
                            Items.HAY_BLOCK,
                            Items.BEE_NEST,
                            Items.HONEYCOMB_BLOCK,
                            Items.SLIME_BLOCK,
                            Items.HONEY_BLOCK
                    );

                    section(page, "froglights",
                            Items.OCHRE_FROGLIGHT,
                            Items.VERDANT_FROGLIGHT,
                            Items.PEARLESCENT_FROGLIGHT
                    );

                    section(page, "sculk",
                            Items.SCULK,
                            Items.SCULK_VEIN,
                            Items.SCULK_CATALYST,
                            Items.SCULK_SHRIEKER,
                            Items.SCULK_SENSOR
                    );
                });
    }

    private static void section(CtlPageBuilder page, String name, ItemLike... items) {
        page.section(CreativeTabLayouts.id("minecraft/natural_blocks/" + name), section -> section.add(items));
    }
}