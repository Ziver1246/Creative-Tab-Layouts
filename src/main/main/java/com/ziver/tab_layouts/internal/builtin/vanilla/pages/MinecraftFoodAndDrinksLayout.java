package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.layout.CtlSectionBuilder;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

public final class MinecraftFoodAndDrinksLayout {
    private MinecraftFoodAndDrinksLayout() {}

    public static void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.FOOD_AND_DRINKS)

                .page(CreativeTabLayouts.id("minecraft/food_and_drinks/fruits_and_vegetables"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/fruits"), section -> {
                        section.add(
                                Items.APPLE,
                                Items.GOLDEN_APPLE,
                                Items.ENCHANTED_GOLDEN_APPLE,
                                Items.MELON_SLICE,
                                Items.SWEET_BERRIES,
                                Items.GLOW_BERRIES,
                                Items.CHORUS_FRUIT
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/vegetables"), section -> {
                        section.add(
                                Items.CARROT,
                                Items.GOLDEN_CARROT,
                                Items.POTATO,
                                Items.BAKED_POTATO,
                                Items.POISONOUS_POTATO,
                                Items.BEETROOT,
                                Items.DRIED_KELP
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/food_and_drinks/meat_and_fish"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/meat"), section -> {
                        section.add(
                                Items.BEEF,
                                Items.COOKED_BEEF,
                                Items.PORKCHOP,
                                Items.COOKED_PORKCHOP,
                                Items.MUTTON,
                                Items.COOKED_MUTTON,
                                Items.CHICKEN,
                                Items.COOKED_CHICKEN,
                                Items.RABBIT,
                                Items.COOKED_RABBIT
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/fish"), section -> {
                        section.add(
                                Items.COD,
                                Items.COOKED_COD,
                                Items.SALMON,
                                Items.COOKED_SALMON,
                                Items.TROPICAL_FISH,
                                Items.PUFFERFISH
                        );
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/food_and_drinks/meals_and_snacks"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/bakery"), section -> {
                        section.add(
                                Items.BREAD,
                                Items.COOKIE,
                                Items.CAKE,
                                Items.PUMPKIN_PIE
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/stews"), section -> {
                        section.add(
                                Items.MUSHROOM_STEW,
                                Items.BEETROOT_SOUP,
                                Items.RABBIT_STEW
                        );

                        suspiciousStews(section);
                    });
                })

                .page(CreativeTabLayouts.id("minecraft/food_and_drinks/drinks_and_effects"), page -> {
                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/basic_drinks"), section -> {
                        section.add(
                                Items.MILK_BUCKET,
                                Items.HONEY_BOTTLE
                        );
                    });

                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/ominous_vials"), MinecraftFoodAndDrinksLayout::ominousVials);

                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/potions"), MinecraftFoodAndDrinksLayout::potions);

                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/splash_potions"), MinecraftFoodAndDrinksLayout::splashPotions);

                    page.section(CreativeTabLayouts.id("minecraft/food_and_drinks/lingering_potions"), MinecraftFoodAndDrinksLayout::lingeringPotions);
                });
    }

    private static void suspiciousStews(CtlSectionBuilder section) {
        SuspiciousEffectHolder.getAllEffectHolders().forEach(holder -> {
            section.stack(() -> {
                ItemStack stack = new ItemStack(Items.SUSPICIOUS_STEW);
                stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, holder.getSuspiciousEffects());
                return stack;
            });
        });
    }

    private static void ominousVials(CtlSectionBuilder section) {
        for (int amplifier = 0; amplifier < 5; amplifier++) {
            int value = amplifier;

            section.stack(() -> {
                ItemStack stack = new ItemStack(Items.OMINOUS_BOTTLE);
                stack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, value);
                return stack;
            });
        }
    }

    private static void potions(CtlSectionBuilder section) {
        BuiltInRegistries.POTION.holders().forEach(potion -> {
            section.stack(() -> PotionContents.createItemStack(Items.POTION, potion));
        });
    }

    private static void splashPotions(CtlSectionBuilder section) {
        BuiltInRegistries.POTION.holders().forEach(potion -> {
            section.stack(() -> PotionContents.createItemStack(Items.SPLASH_POTION, potion));
        });
    }

    private static void lingeringPotions(CtlSectionBuilder section) {
        BuiltInRegistries.POTION.holders().forEach(potion -> {
            section.stack(() -> PotionContents.createItemStack(Items.LINGERING_POTION, potion));
        });
    }
}