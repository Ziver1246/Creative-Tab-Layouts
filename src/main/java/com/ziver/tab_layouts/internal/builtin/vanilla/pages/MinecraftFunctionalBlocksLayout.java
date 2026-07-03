package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.world.item.Items;

public final class MinecraftFunctionalBlocksLayout {
    private MinecraftFunctionalBlocksLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.FUNCTIONAL_BLOCKS)

                .page(CreativeTabLayouts.id("minecraft/functional_blocks/functional"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/functional_blocks/light"), section -> {
                        section.add(
                                Items.TORCH,
                                Items.SOUL_TORCH,
                                Items.LANTERN,
                                Items.SOUL_LANTERN,
                                Items.END_ROD,
                                Items.SEA_LANTERN,
                                Items.REDSTONE_LAMP,
                                Items.GLOWSTONE,
                                Items.SHROOMLIGHT,
                                Items.OCHRE_FROGLIGHT,
                                Items.VERDANT_FROGLIGHT,
                                Items.PEARLESCENT_FROGLIGHT
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/functional_blocks/workstations"), section -> {
                        section.add(
                                Items.CRAFTING_TABLE,
                                Items.STONECUTTER,
                                Items.CARTOGRAPHY_TABLE,
                                Items.FLETCHING_TABLE,
                                Items.SMITHING_TABLE,
                                Items.GRINDSTONE,
                                Items.LOOM,

                                Items.FURNACE,
                                Items.SMOKER,
                                Items.BLAST_FURNACE,
                                Items.CAMPFIRE,
                                Items.SOUL_CAMPFIRE,

                                Items.ANVIL,
                                Items.CHIPPED_ANVIL,
                                Items.DAMAGED_ANVIL,

                                Items.BEEHIVE,
                                Items.COMPOSTER,
                                Items.ENCHANTING_TABLE,
                                Items.BEACON,
                                Items.CONDUIT,
                                Items.BREWING_STAND,
                                Items.CAULDRON,
                                Items.LODESTONE,
                                Items.RESPAWN_ANCHOR,
                                Items.VAULT,
                                Items.JUKEBOX
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/functional_blocks/utility"), section -> {
                        section.add(
                                Items.DRAGON_EGG,
                                Items.END_PORTAL_FRAME,
                                Items.END_CRYSTAL,
                                Items.BELL,

                                Items.LADDER,
                                Items.SCAFFOLDING,
                                Items.LIGHTNING_ROD,

                                Items.FLOWER_POT,
                                Items.DECORATED_POT,
                                Items.ARMOR_STAND,
                                Items.COBWEB
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/functional_blocks/storage"), section -> {
                        section.add(
                                Items.CHEST,
                                Items.BARREL,
                                Items.ENDER_CHEST
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/functional_blocks/display"), section -> {
                        section.add(
                                Items.BOOKSHELF,
                                Items.CHISELED_BOOKSHELF,
                                Items.LECTERN,
                                Items.ITEM_FRAME,
                                Items.GLOW_ITEM_FRAME,
                                Items.PAINTING
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/functional_blocks/skulls"), section -> {
                        section.add(
                                Items.SKELETON_SKULL,
                                Items.WITHER_SKELETON_SKULL,
                                Items.PLAYER_HEAD,
                                Items.ZOMBIE_HEAD,
                                Items.CREEPER_HEAD,
                                Items.PIGLIN_HEAD,
                                Items.DRAGON_HEAD
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/functional_blocks/infested_blocks"), section -> {
                        section.add(
                                Items.INFESTED_STONE,
                                Items.INFESTED_COBBLESTONE,
                                Items.INFESTED_STONE_BRICKS,
                                Items.INFESTED_MOSSY_STONE_BRICKS,
                                Items.INFESTED_CRACKED_STONE_BRICKS,
                                Items.INFESTED_CHISELED_STONE_BRICKS,
                                Items.INFESTED_DEEPSLATE
                        );
                    });
                });
    }
}