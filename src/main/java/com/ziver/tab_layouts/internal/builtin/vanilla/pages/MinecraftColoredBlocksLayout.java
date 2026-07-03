package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.layout.CtlPageBuilder;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public final class MinecraftColoredBlocksLayout {
    private MinecraftColoredBlocksLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.COLORED_BLOCKS)

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/neutral"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/colored_blocks/neutral_blocks"), section -> {
                        section.add(
                                Items.TERRACOTTA,
                                Items.GLASS,
                                Items.TINTED_GLASS,
                                Items.GLASS_PANE,
                                Items.SHULKER_BOX,
                                Items.CANDLE
                        );

                        section.dynamic(registries -> List.of(Raid.getLeaderBannerInstance(
                                registries.lookupOrThrow(Registries.BANNER_PATTERN)))
                        );
                    });
                })



                .page(CreativeTabLayouts.id("minecraft/colored_blocks/white"), page -> colorPage(page, "white",
                        Items.WHITE_WOOL,
                        Items.WHITE_CARPET,
                        Items.WHITE_TERRACOTTA,
                        Items.WHITE_GLAZED_TERRACOTTA,
                        Items.WHITE_CONCRETE,
                        Items.WHITE_CONCRETE_POWDER,
                        Items.WHITE_STAINED_GLASS,
                        Items.WHITE_STAINED_GLASS_PANE,
                        Items.WHITE_BED,
                        Items.WHITE_CANDLE,
                        Items.WHITE_BANNER,
                        Items.WHITE_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/light_gray"), page -> colorPage(page, "light_gray",
                        Items.LIGHT_GRAY_WOOL,
                        Items.LIGHT_GRAY_CARPET,
                        Items.LIGHT_GRAY_TERRACOTTA,
                        Items.LIGHT_GRAY_GLAZED_TERRACOTTA,
                        Items.LIGHT_GRAY_CONCRETE,
                        Items.LIGHT_GRAY_CONCRETE_POWDER,
                        Items.LIGHT_GRAY_STAINED_GLASS,
                        Items.LIGHT_GRAY_STAINED_GLASS_PANE,
                        Items.LIGHT_GRAY_BED,
                        Items.LIGHT_GRAY_CANDLE,
                        Items.LIGHT_GRAY_BANNER,
                        Items.LIGHT_GRAY_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/gray"), page -> colorPage(page, "gray",
                        Items.GRAY_WOOL,
                        Items.GRAY_CARPET,
                        Items.GRAY_TERRACOTTA,
                        Items.GRAY_GLAZED_TERRACOTTA,
                        Items.GRAY_CONCRETE,
                        Items.GRAY_CONCRETE_POWDER,
                        Items.GRAY_STAINED_GLASS,
                        Items.GRAY_STAINED_GLASS_PANE,
                        Items.GRAY_BED,
                        Items.GRAY_CANDLE,
                        Items.GRAY_BANNER,
                        Items.GRAY_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/black"), page -> colorPage(page, "black",
                        Items.BLACK_WOOL,
                        Items.BLACK_CARPET,
                        Items.BLACK_TERRACOTTA,
                        Items.BLACK_GLAZED_TERRACOTTA,
                        Items.BLACK_CONCRETE,
                        Items.BLACK_CONCRETE_POWDER,
                        Items.BLACK_STAINED_GLASS,
                        Items.BLACK_STAINED_GLASS_PANE,
                        Items.BLACK_BED,
                        Items.BLACK_CANDLE,
                        Items.BLACK_BANNER,
                        Items.BLACK_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/brown"), page -> colorPage(page, "brown",
                        Items.BROWN_WOOL,
                        Items.BROWN_CARPET,
                        Items.BROWN_TERRACOTTA,
                        Items.BROWN_GLAZED_TERRACOTTA,
                        Items.BROWN_CONCRETE,
                        Items.BROWN_CONCRETE_POWDER,
                        Items.BROWN_STAINED_GLASS,
                        Items.BROWN_STAINED_GLASS_PANE,
                        Items.BROWN_BED,
                        Items.BROWN_CANDLE,
                        Items.BROWN_BANNER,
                        Items.BROWN_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/red"), page -> colorPage(page, "red",
                        Items.RED_WOOL,
                        Items.RED_CARPET,
                        Items.RED_TERRACOTTA,
                        Items.RED_GLAZED_TERRACOTTA,
                        Items.RED_CONCRETE,
                        Items.RED_CONCRETE_POWDER,
                        Items.RED_STAINED_GLASS,
                        Items.RED_STAINED_GLASS_PANE,
                        Items.RED_BED,
                        Items.RED_CANDLE,
                        Items.RED_BANNER,
                        Items.RED_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/orange"), page -> colorPage(page, "orange",
                        Items.ORANGE_WOOL,
                        Items.ORANGE_CARPET,
                        Items.ORANGE_TERRACOTTA,
                        Items.ORANGE_GLAZED_TERRACOTTA,
                        Items.ORANGE_CONCRETE,
                        Items.ORANGE_CONCRETE_POWDER,
                        Items.ORANGE_STAINED_GLASS,
                        Items.ORANGE_STAINED_GLASS_PANE,
                        Items.ORANGE_BED,
                        Items.ORANGE_CANDLE,
                        Items.ORANGE_BANNER,
                        Items.ORANGE_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/yellow"), page -> colorPage(page, "yellow",
                        Items.YELLOW_WOOL,
                        Items.YELLOW_CARPET,
                        Items.YELLOW_TERRACOTTA,
                        Items.YELLOW_GLAZED_TERRACOTTA,
                        Items.YELLOW_CONCRETE,
                        Items.YELLOW_CONCRETE_POWDER,
                        Items.YELLOW_STAINED_GLASS,
                        Items.YELLOW_STAINED_GLASS_PANE,
                        Items.YELLOW_BED,
                        Items.YELLOW_CANDLE,
                        Items.YELLOW_BANNER,
                        Items.YELLOW_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/lime"), page -> colorPage(page, "lime",
                        Items.LIME_WOOL,
                        Items.LIME_CARPET,
                        Items.LIME_TERRACOTTA,
                        Items.LIME_GLAZED_TERRACOTTA,
                        Items.LIME_CONCRETE,
                        Items.LIME_CONCRETE_POWDER,
                        Items.LIME_STAINED_GLASS,
                        Items.LIME_STAINED_GLASS_PANE,
                        Items.LIME_BED,
                        Items.LIME_CANDLE,
                        Items.LIME_BANNER,
                        Items.LIME_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/green"), page -> colorPage(page, "green",
                        Items.GREEN_WOOL,
                        Items.GREEN_CARPET,
                        Items.GREEN_TERRACOTTA,
                        Items.GREEN_GLAZED_TERRACOTTA,
                        Items.GREEN_CONCRETE,
                        Items.GREEN_CONCRETE_POWDER,
                        Items.GREEN_STAINED_GLASS,
                        Items.GREEN_STAINED_GLASS_PANE,
                        Items.GREEN_BED,
                        Items.GREEN_CANDLE,
                        Items.GREEN_BANNER,
                        Items.GREEN_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/cyan"), page -> colorPage(page, "cyan",
                        Items.CYAN_WOOL,
                        Items.CYAN_CARPET,
                        Items.CYAN_TERRACOTTA,
                        Items.CYAN_GLAZED_TERRACOTTA,
                        Items.CYAN_CONCRETE,
                        Items.CYAN_CONCRETE_POWDER,
                        Items.CYAN_STAINED_GLASS,
                        Items.CYAN_STAINED_GLASS_PANE,
                        Items.CYAN_BED,
                        Items.CYAN_CANDLE,
                        Items.CYAN_BANNER,
                        Items.CYAN_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/light_blue"), page -> colorPage(page, "light_blue",
                        Items.LIGHT_BLUE_WOOL,
                        Items.LIGHT_BLUE_CARPET,
                        Items.LIGHT_BLUE_TERRACOTTA,
                        Items.LIGHT_BLUE_GLAZED_TERRACOTTA,
                        Items.LIGHT_BLUE_CONCRETE,
                        Items.LIGHT_BLUE_CONCRETE_POWDER,
                        Items.LIGHT_BLUE_STAINED_GLASS,
                        Items.LIGHT_BLUE_STAINED_GLASS_PANE,
                        Items.LIGHT_BLUE_BED,
                        Items.LIGHT_BLUE_CANDLE,
                        Items.LIGHT_BLUE_BANNER,
                        Items.LIGHT_BLUE_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/blue"), page -> colorPage(page, "blue",
                        Items.BLUE_WOOL,
                        Items.BLUE_CARPET,
                        Items.BLUE_TERRACOTTA,
                        Items.BLUE_GLAZED_TERRACOTTA,
                        Items.BLUE_CONCRETE,
                        Items.BLUE_CONCRETE_POWDER,
                        Items.BLUE_STAINED_GLASS,
                        Items.BLUE_STAINED_GLASS_PANE,
                        Items.BLUE_BED,
                        Items.BLUE_CANDLE,
                        Items.BLUE_BANNER,
                        Items.BLUE_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/purple"), page -> colorPage(page, "purple",
                        Items.PURPLE_WOOL,
                        Items.PURPLE_CARPET,
                        Items.PURPLE_TERRACOTTA,
                        Items.PURPLE_GLAZED_TERRACOTTA,
                        Items.PURPLE_CONCRETE,
                        Items.PURPLE_CONCRETE_POWDER,
                        Items.PURPLE_STAINED_GLASS,
                        Items.PURPLE_STAINED_GLASS_PANE,
                        Items.PURPLE_BED,
                        Items.PURPLE_CANDLE,
                        Items.PURPLE_BANNER,
                        Items.PURPLE_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/magenta"), page -> colorPage(page, "magenta",
                        Items.MAGENTA_WOOL,
                        Items.MAGENTA_CARPET,
                        Items.MAGENTA_TERRACOTTA,
                        Items.MAGENTA_GLAZED_TERRACOTTA,
                        Items.MAGENTA_CONCRETE,
                        Items.MAGENTA_CONCRETE_POWDER,
                        Items.MAGENTA_STAINED_GLASS,
                        Items.MAGENTA_STAINED_GLASS_PANE,
                        Items.MAGENTA_BED,
                        Items.MAGENTA_CANDLE,
                        Items.MAGENTA_BANNER,
                        Items.MAGENTA_SHULKER_BOX
                ))

                .page(CreativeTabLayouts.id("minecraft/colored_blocks/pink"), page -> colorPage(page, "pink",
                        Items.PINK_WOOL,
                        Items.PINK_CARPET,
                        Items.PINK_TERRACOTTA,
                        Items.PINK_GLAZED_TERRACOTTA,
                        Items.PINK_CONCRETE,
                        Items.PINK_CONCRETE_POWDER,
                        Items.PINK_STAINED_GLASS,
                        Items.PINK_STAINED_GLASS_PANE,
                        Items.PINK_BED,
                        Items.PINK_CANDLE,
                        Items.PINK_BANNER,
                        Items.PINK_SHULKER_BOX
                ));
    }

    private static void colorPage(CtlPageBuilder page, String colorName, ItemLike... items) {
        page.section(CreativeTabLayouts.id("minecraft/colored_blocks/" + colorName + "_blocks"), section -> section.add(items));
    }
}