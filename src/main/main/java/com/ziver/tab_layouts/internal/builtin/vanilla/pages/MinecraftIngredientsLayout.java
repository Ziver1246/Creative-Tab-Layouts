package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.world.item.Items;

public final class MinecraftIngredientsLayout {
    private MinecraftIngredientsLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.INGREDIENTS)

                .page(CreativeTabLayouts.id("minecraft/ingredients/materials"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/ingredients/resources"), section -> {
                        section.add(
                                Items.COAL,
                                Items.CHARCOAL,

                                Items.RAW_IRON,
                                Items.IRON_INGOT,
                                Items.IRON_NUGGET,
                                Items.RAW_GOLD,
                                Items.GOLD_INGOT,
                                Items.GOLD_NUGGET,
                                Items.RAW_COPPER,
                                Items.COPPER_INGOT,

                                Items.NETHERITE_SCRAP,
                                Items.NETHERITE_INGOT,
                                Items.DIAMOND,
                                Items.EMERALD,
                                Items.LAPIS_LAZULI,
                                Items.QUARTZ,
                                Items.AMETHYST_SHARD

                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/ingredients/crafting_materials"), section -> {
                        section.add(
                                Items.STICK,
                                Items.FLINT,
                                Items.WHEAT,
                                Items.BONE,
                                Items.BONE_MEAL,
                                Items.STRING,
                                Items.FEATHER,
                                Items.LEATHER,
                                Items.RABBIT_HIDE,
                                Items.HONEYCOMB,
                                Items.CLAY_BALL,
                                Items.BRICK,
                                Items.NETHER_BRICK,
                                Items.PAPER,
                                Items.BOOK,
                                Items.BOWL,
                                Items.GLASS_BOTTLE,
                                Items.FIREWORK_STAR
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/ingredients/brewing_and_organics"), section -> {
                        section.add(
                                Items.ROTTEN_FLESH,
                                Items.SPIDER_EYE,
                                Items.FERMENTED_SPIDER_EYE,

                                Items.SLIME_BALL,
                                Items.MAGMA_CREAM,

                                Items.BLAZE_ROD,
                                Items.BLAZE_POWDER,
                                Items.BREEZE_ROD,

                                Items.SUGAR,
                                Items.RABBIT_FOOT,
                                Items.GLISTERING_MELON_SLICE,
                                Items.GOLDEN_CARROT,
                                Items.GHAST_TEAR,
                                Items.PHANTOM_MEMBRANE,

                                Items.NETHER_WART,
                                Items.REDSTONE,
                                Items.GLOWSTONE_DUST,
                                Items.GUNPOWDER,
                                Items.DRAGON_BREATH,
                                Items.INK_SAC,
                                Items.GLOW_INK_SAC,
                                Items.TURTLE_SCUTE
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/ingredients/loot"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/ingredients/loot"), section -> {
                        section.add(
                                Items.ENDER_PEARL,
                                Items.ENDER_EYE,
                                Items.SHULKER_SHELL,

                                Items.PRISMARINE_SHARD,
                                Items.PRISMARINE_CRYSTALS,
                                Items.NAUTILUS_SHELL,
                                Items.HEART_OF_THE_SEA,

                                Items.TURTLE_HELMET,
                                Items.ARMADILLO_SCUTE,

                                Items.POPPED_CHORUS_FRUIT,
                                Items.ECHO_SHARD,
                                Items.DISC_FRAGMENT_5,

                                Items.NETHER_STAR,
                                Items.HEAVY_CORE,

                                Items.EXPERIENCE_BOTTLE,
                                Items.TRIAL_KEY,
                                Items.OMINOUS_TRIAL_KEY
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/ingredients/patterns_and_colors"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/ingredients/dyes"), section -> {
                        section.add(
                                Items.WHITE_DYE,
                                Items.LIGHT_GRAY_DYE,
                                Items.GRAY_DYE,
                                Items.BLACK_DYE,
                                Items.BROWN_DYE,
                                Items.RED_DYE,
                                Items.ORANGE_DYE,
                                Items.YELLOW_DYE,
                                Items.LIME_DYE,
                                Items.GREEN_DYE,
                                Items.CYAN_DYE,
                                Items.LIGHT_BLUE_DYE,
                                Items.BLUE_DYE,
                                Items.PURPLE_DYE,
                                Items.MAGENTA_DYE,
                                Items.PINK_DYE
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/ingredients/banner_patterns"), section -> {
                        section.add(
                                Items.FLOWER_BANNER_PATTERN,
                                Items.CREEPER_BANNER_PATTERN,
                                Items.SKULL_BANNER_PATTERN,
                                Items.MOJANG_BANNER_PATTERN,
                                Items.GLOBE_BANNER_PATTERN,
                                Items.PIGLIN_BANNER_PATTERN,
                                Items.FLOW_BANNER_PATTERN,
                                Items.GUSTER_BANNER_PATTERN
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/ingredients/pottery_sherds"), section -> {
                        section.add(
                                Items.ANGLER_POTTERY_SHERD,
                                Items.ARCHER_POTTERY_SHERD,
                                Items.ARMS_UP_POTTERY_SHERD,
                                Items.BLADE_POTTERY_SHERD,
                                Items.BREWER_POTTERY_SHERD,
                                Items.BURN_POTTERY_SHERD,
                                Items.DANGER_POTTERY_SHERD,
                                Items.FLOW_POTTERY_SHERD,
                                Items.EXPLORER_POTTERY_SHERD,
                                Items.FRIEND_POTTERY_SHERD,
                                Items.GUSTER_POTTERY_SHERD,
                                Items.HEART_POTTERY_SHERD,
                                Items.HEARTBREAK_POTTERY_SHERD,
                                Items.HOWL_POTTERY_SHERD,
                                Items.MINER_POTTERY_SHERD,
                                Items.MOURNER_POTTERY_SHERD,
                                Items.PLENTY_POTTERY_SHERD,
                                Items.PRIZE_POTTERY_SHERD,
                                Items.SCRAPE_POTTERY_SHERD,
                                Items.SHEAF_POTTERY_SHERD,
                                Items.SHELTER_POTTERY_SHERD,
                                Items.SKULL_POTTERY_SHERD,
                                Items.SNORT_POTTERY_SHERD
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/ingredients/smithing_templates"), section -> {
                        section.add(
                                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                                Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,
                                Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/ingredients/enchanted_books"), MinecraftEnchantedBooksLayout::register);
    }
}