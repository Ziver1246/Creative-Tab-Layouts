package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.layout.CtlSectionBuilder;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Fireworks;

import java.util.List;

public final class MinecraftToolsAndUtilitiesLayout {
    private MinecraftToolsAndUtilitiesLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.TOOLS_AND_UTILITIES)

                .page(CreativeTabLayouts.id("minecraft/tools_and_utilities/tools"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/tool_sets"), section -> {
                        section.add(
                                Items.WOODEN_AXE,
                                Items.WOODEN_PICKAXE,
                                Items.WOODEN_SHOVEL,
                                Items.WOODEN_HOE
                        ).empty().add(
                                Items.STONE_AXE,
                                Items.STONE_PICKAXE,
                                Items.STONE_SHOVEL,
                                Items.STONE_HOE,

                                Items.GOLDEN_AXE,
                                Items.GOLDEN_PICKAXE,
                                Items.GOLDEN_SHOVEL,
                                Items.GOLDEN_HOE
                        ).empty().add(
                                Items.IRON_AXE,
                                Items.IRON_PICKAXE,
                                Items.IRON_SHOVEL,
                                Items.IRON_HOE,

                                Items.DIAMOND_AXE,
                                Items.DIAMOND_PICKAXE,
                                Items.DIAMOND_SHOVEL,
                                Items.DIAMOND_HOE
                        ).empty().add(
                                Items.NETHERITE_AXE,
                                Items.NETHERITE_PICKAXE,
                                Items.NETHERITE_SHOVEL,
                                Items.NETHERITE_HOE
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/special_tools"), section -> {
                        section.add(
                                Items.FISHING_ROD,
                                Items.FLINT_AND_STEEL,
                                Items.FIRE_CHARGE,
                                Items.BONE_MEAL,
                                Items.SHEARS,
                                Items.BRUSH,
                                Items.NAME_TAG,
                                Items.LEAD,
                                Items.BUNDLE
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/tools_and_utilities/utilities"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/buckets"), section -> {
                        section.add(
                                Items.BUCKET,
                                Items.WATER_BUCKET,
                                Items.COD_BUCKET,
                                Items.SALMON_BUCKET,
                                Items.TROPICAL_FISH_BUCKET,
                                Items.PUFFERFISH_BUCKET,
                                Items.AXOLOTL_BUCKET,
                                Items.TADPOLE_BUCKET,
                                Items.LAVA_BUCKET,
                                Items.POWDER_SNOW_BUCKET,
                                Items.MILK_BUCKET
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/navigation"), section -> {
                        section.add(
                                Items.COMPASS,
                                Items.RECOVERY_COMPASS,
                                Items.CLOCK,
                                Items.SPYGLASS,
                                Items.MAP,
                                Items.WRITABLE_BOOK
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/mobility"), section -> {
                        section.add(
                                Items.WIND_CHARGE,
                                Items.ELYTRA
                        );

                        fireworks(section);
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/tools_and_utilities/transport"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/mounts"), section -> {
                        section.add(
                                Items.SADDLE,
                                Items.CARROT_ON_A_STICK,
                                Items.WARPED_FUNGUS_ON_A_STICK
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/boats"), section -> {
                        section.add(
                                Items.OAK_BOAT,
                                Items.OAK_CHEST_BOAT,
                                Items.SPRUCE_BOAT,
                                Items.SPRUCE_CHEST_BOAT,
                                Items.BIRCH_BOAT,
                                Items.BIRCH_CHEST_BOAT,
                                Items.JUNGLE_BOAT,
                                Items.JUNGLE_CHEST_BOAT,
                                Items.ACACIA_BOAT,
                                Items.ACACIA_CHEST_BOAT,
                                Items.DARK_OAK_BOAT,
                                Items.DARK_OAK_CHEST_BOAT,
                                Items.MANGROVE_BOAT,
                                Items.MANGROVE_CHEST_BOAT,
                                Items.CHERRY_BOAT,
                                Items.CHERRY_CHEST_BOAT,
                                Items.BAMBOO_RAFT,
                                Items.BAMBOO_CHEST_RAFT
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/tools_and_utilities/music"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/goat_horns"), section -> {
                        section.dynamic(MinecraftToolsAndUtilitiesLayout::goatHorns);
                    });

                    page.section(CreativeTabLayouts.id("minecraft/tools_and_utilities/music_discs"), section -> {
                        section.add(
                                Items.MUSIC_DISC_13,
                                Items.MUSIC_DISC_CAT,
                                Items.MUSIC_DISC_BLOCKS,
                                Items.MUSIC_DISC_CHIRP,
                                Items.MUSIC_DISC_FAR,
                                Items.MUSIC_DISC_MALL,
                                Items.MUSIC_DISC_MELLOHI,
                                Items.MUSIC_DISC_STAL,
                                Items.MUSIC_DISC_STRAD,
                                Items.MUSIC_DISC_WARD,
                                Items.MUSIC_DISC_11,
                                Items.MUSIC_DISC_CREATOR_MUSIC_BOX,
                                Items.MUSIC_DISC_WAIT,
                                Items.MUSIC_DISC_CREATOR,
                                Items.MUSIC_DISC_PRECIPICE,
                                Items.MUSIC_DISC_OTHERSIDE,
                                Items.MUSIC_DISC_RELIC,
                                Items.MUSIC_DISC_5,
                                Items.MUSIC_DISC_PIGSTEP
                        );
                    });
                });
    }

    private static void toolSet(CtlSectionBuilder section, net.minecraft.world.level.ItemLike shovel, net.minecraft.world.level.ItemLike pickaxe, net.minecraft.world.level.ItemLike axe, net.minecraft.world.level.ItemLike hoe) {
        section.add(shovel, pickaxe, axe, hoe).empty(1);
    }

    private static void fireworks(CtlSectionBuilder section) {
        for (byte duration : FireworkRocketItem.CRAFTABLE_DURATIONS) {
            section.stack(() -> firework(duration));
        }
    }

    private static ItemStack firework(byte duration) {
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
        stack.set(DataComponents.FIREWORKS, new Fireworks(duration, List.of()));
        return stack;
    }

    private static List<ItemStack> goatHorns(HolderLookup.Provider registries) {
        return registries.lookup(Registries.INSTRUMENT)
                .flatMap(instruments -> instruments.get(InstrumentTags.GOAT_HORNS))
                .map(horns -> horns.stream()
                        .map(instrument -> InstrumentItem.create(Items.GOAT_HORN, (Holder<Instrument>) instrument))
                        .toList()
                )
                .orElse(List.of());
    }
}