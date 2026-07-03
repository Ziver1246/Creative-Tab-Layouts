package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.layout.CtlSectionBuilder;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Fireworks;

import java.util.List;

public final class MinecraftCombatLayout {
    private MinecraftCombatLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.COMBAT)

                .page(CreativeTabLayouts.id("minecraft/combat/equipment"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/combat/equipment"), section -> {
                        section.add(
                                Items.LEATHER_HELMET,
                                Items.LEATHER_CHESTPLATE,
                                Items.LEATHER_LEGGINGS,
                                Items.LEATHER_BOOTS,
                                Items.WOODEN_SWORD,
                                Items.WOODEN_AXE
                        ).empty(3).add(
                                Items.CHAINMAIL_HELMET,
                                Items.CHAINMAIL_CHESTPLATE,
                                Items.CHAINMAIL_LEGGINGS,
                                Items.CHAINMAIL_BOOTS,
                                Items.STONE_SWORD,
                                Items.STONE_AXE
                        ).empty(3).add(
                                Items.GOLDEN_HELMET,
                                Items.GOLDEN_CHESTPLATE,
                                Items.GOLDEN_LEGGINGS,
                                Items.GOLDEN_BOOTS,
                                Items.GOLDEN_SWORD,
                                Items.GOLDEN_AXE
                        ).empty(3).add(
                                Items.IRON_HELMET,
                                Items.IRON_CHESTPLATE,
                                Items.IRON_LEGGINGS,
                                Items.IRON_BOOTS,
                                Items.IRON_SWORD,
                                Items.IRON_AXE
                        ).empty(3).add(
                                Items.DIAMOND_HELMET,
                                Items.DIAMOND_CHESTPLATE,
                                Items.DIAMOND_LEGGINGS,
                                Items.DIAMOND_BOOTS,
                                Items.DIAMOND_SWORD,
                                Items.DIAMOND_AXE
                        ).empty(3).add(
                                Items.NETHERITE_HELMET,
                                Items.NETHERITE_CHESTPLATE,
                                Items.NETHERITE_LEGGINGS,
                                Items.NETHERITE_BOOTS,
                                Items.NETHERITE_SWORD,
                                Items.NETHERITE_AXE
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/combat/special_gear"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/combat/special_gear"), section -> {
                        section.add(
                                Items.TRIDENT,
                                Items.MACE,
                                Items.SHIELD,
                                Items.BOW,
                                Items.CROSSBOW,
                                Items.TURTLE_HELMET,
                                Items.TNT,
                                Items.TOTEM_OF_UNDYING,
                                Items.END_CRYSTAL,

                                Items.LEATHER_HORSE_ARMOR,
                                Items.IRON_HORSE_ARMOR,
                                Items.GOLDEN_HORSE_ARMOR,
                                Items.DIAMOND_HORSE_ARMOR,
                                Items.WOLF_ARMOR

                        );

                        fireworks(section);
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/combat/projectiles"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/combat/throwables"), section -> {
                        section.add(
                                Items.SNOWBALL,
                                Items.EGG,
                                Items.WIND_CHARGE
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/combat/arrows"), section -> {
                        section.add(
                                Items.ARROW,
                                Items.SPECTRAL_ARROW
                        );

                        tippedArrows(section);
                    });
                });
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

    private static void tippedArrows(CtlSectionBuilder section) {
        BuiltInRegistries.POTION.holders().forEach(potion -> {
            section.stack(() -> PotionContents.createItemStack(Items.TIPPED_ARROW, potion));
        });
    }
}