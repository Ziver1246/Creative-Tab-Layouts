package com.ziver.tab_layouts.internal.builtin.vanilla.pages;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.layout.CtlPageBuilder;
import com.ziver.tab_layouts.api.layout.CtlSectionBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class MinecraftEnchantedBooksLayout {
    private MinecraftEnchantedBooksLayout() {}

    public static void register(CtlPageBuilder page) {
        page.section(CreativeTabLayouts.id("minecraft/ingredients/enchanted_books_armor"), MinecraftEnchantedBooksLayout::armor);
        page.section(CreativeTabLayouts.id("minecraft/ingredients/enchanted_books_weapons"), MinecraftEnchantedBooksLayout::weapons);
        page.section(CreativeTabLayouts.id("minecraft/ingredients/enchanted_books_tools"), MinecraftEnchantedBooksLayout::tools);
        page.section(CreativeTabLayouts.id("minecraft/ingredients/enchanted_books_ranged"), MinecraftEnchantedBooksLayout::ranged);
        page.section(CreativeTabLayouts.id("minecraft/ingredients/enchanted_books_utility"), MinecraftEnchantedBooksLayout::utility);
        page.section(CreativeTabLayouts.id("minecraft/ingredients/enchanted_books_curses"), MinecraftEnchantedBooksLayout::curses);
    }

    private static void armor(CtlSectionBuilder section) {
        section.dynamic(registries -> books(registries, Category.ARMOR));
    }

    private static void weapons(CtlSectionBuilder section) {
        section.dynamic(registries -> books(registries, Category.WEAPONS));
    }

    private static void tools(CtlSectionBuilder section) {
        section.dynamic(registries -> books(registries, Category.TOOLS));
    }

    private static void ranged(CtlSectionBuilder section) {
        section.dynamic(registries -> books(registries, Category.RANGED));
    }

    private static void utility(CtlSectionBuilder section) {
        section.dynamic(registries -> books(registries, Category.UTILITY));
    }

    private static void curses(CtlSectionBuilder section) {
        section.dynamic(registries -> books(registries, Category.CURSES));
    }

    private static List<ItemStack> books(HolderLookup.Provider registries, Category category) {
        return registries.lookup(Registries.ENCHANTMENT).map(enchantments -> enchantments.listElements()
                        .sorted(Comparator.comparing(enchantment -> enchantment.key().location()))
                        .filter(category::matches)
                        .map(MinecraftEnchantedBooksLayout::book)
                        .toList())
                .orElse(List.of());
    }

    private static ItemStack book(Holder<Enchantment> enchantment) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, enchantment.value().getMaxLevel()));
    }

    private enum Category {
        ARMOR(
                "protection",
                "fire_protection",
                "feather_falling",
                "blast_protection",
                "projectile_protection",
                "respiration",
                "aqua_affinity",
                "thorns",
                "depth_strider",
                "frost_walker",
                "soul_speed",
                "swift_sneak"
        ),

        WEAPONS(
                "sharpness",
                "smite",
                "bane_of_arthropods",
                "knockback",
                "fire_aspect",
                "looting",
                "sweeping_edge",
                "density",
                "breach",
                "wind_burst",
                "impaling",
                "loyalty",
                "riptide",
                "channeling"
        ),

        TOOLS(
                "efficiency",
                "silk_touch",
                "fortune",
                "luck_of_the_sea",
                "lure"
        ),

        RANGED(
                "power",
                "punch",
                "flame",
                "infinity",
                "multishot",
                "quick_charge",
                "piercing"
        ),

        UTILITY(
                "unbreaking",
                "mending"
        ),

        CURSES(
                "binding_curse",
                "vanishing_curse"
        );

        private final List<String> ids;

        Category(String... ids) {
            this.ids = List.of(ids);
        }

        boolean matches(Holder<Enchantment> enchantment) {
            ResourceLocation id = Objects.requireNonNull(enchantment.getKey()).location();
            return ids.contains(id.getPath());
        }
    }
}