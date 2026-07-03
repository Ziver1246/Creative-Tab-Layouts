package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.world.item.Items;

public final class MinecraftRedstoneBlocksLayout {
    private MinecraftRedstoneBlocksLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.REDSTONE_BLOCKS)

                .page(CreativeTabLayouts.id("minecraft/redstone_blocks/redstone"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/redstone_blocks/signal"), section -> {
                        section.add(
                                Items.REDSTONE,
                                Items.REDSTONE_TORCH,
                                Items.REDSTONE_BLOCK,
                                Items.REPEATER,
                                Items.COMPARATOR,
                                Items.TARGET,
                                Items.LEVER
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/redstone_blocks/sensors"), section -> {
                        section.add(
                                Items.SCULK_SENSOR,
                                Items.CALIBRATED_SCULK_SENSOR,
                                Items.SCULK_SHRIEKER,
                                Items.TRIPWIRE_HOOK,
                                Items.DAYLIGHT_DETECTOR,
                                Items.LIGHTNING_ROD,
                                Items.OBSERVER,
                                Items.TRAPPED_CHEST
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/redstone_blocks/mechanisms"), section -> {
                        section.add(
                                Items.WAXED_COPPER_BULB,
                                Items.WAXED_EXPOSED_COPPER_BULB,
                                Items.WAXED_WEATHERED_COPPER_BULB,
                                Items.WAXED_OXIDIZED_COPPER_BULB,

                                Items.PISTON,
                                Items.STICKY_PISTON,
                                Items.SLIME_BLOCK,
                                Items.HONEY_BLOCK,

                                Items.DISPENSER,
                                Items.DROPPER,
                                Items.CRAFTER,
                                Items.HOPPER,
                                Items.NOTE_BLOCK
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/redstone_blocks/rails_and_minecarts"), section -> {
                        section.add(
                                Items.RAIL,
                                Items.POWERED_RAIL,
                                Items.DETECTOR_RAIL,
                                Items.ACTIVATOR_RAIL,
                                Items.MINECART,
                                Items.HOPPER_MINECART,
                                Items.CHEST_MINECART,
                                Items.FURNACE_MINECART,
                                Items.TNT_MINECART
                        );
                    });
                });
    }
}