package com.ziver.tab_layouts.api;

import net.minecraft.resources.ResourceLocation;

public final class CtlVanillaTabs {
    private CtlVanillaTabs() {}

    public static final ResourceLocation BUILDING_BLOCKS = vanilla("building_blocks");
    public static final ResourceLocation COLORED_BLOCKS = vanilla("colored_blocks");
    public static final ResourceLocation NATURAL_BLOCKS = vanilla("natural_blocks");
    public static final ResourceLocation FUNCTIONAL_BLOCKS = vanilla("functional_blocks");
    public static final ResourceLocation REDSTONE_BLOCKS = vanilla("redstone_blocks");

    public static final ResourceLocation TOOLS_AND_UTILITIES = vanilla("tools_and_utilities");
    public static final ResourceLocation COMBAT = vanilla("combat");
    public static final ResourceLocation FOOD_AND_DRINKS = vanilla("food_and_drinks");
    public static final ResourceLocation INGREDIENTS = vanilla("ingredients");
    public static final ResourceLocation SPAWN_EGGS = vanilla("spawn_eggs");
    public static final ResourceLocation OP_BLOCKS = vanilla("op_blocks");

    public static final ResourceLocation INVENTORY = vanilla("inventory");
    public static final ResourceLocation HOTBAR = vanilla("hotbar");
    public static final ResourceLocation SEARCH = vanilla("search");

    public static ResourceLocation vanilla(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}