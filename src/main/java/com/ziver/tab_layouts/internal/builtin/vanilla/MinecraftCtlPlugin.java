package com.ziver.tab_layouts.internal.builtin.vanilla;

import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftBuildingBlocksLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftColoredBlocksLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftCombatLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftFoodAndDrinksLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftFunctionalBlocksLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftIngredientsLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftNaturalBlocksLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftRedstoneBlocksLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftSpawnEggsLayout;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.MinecraftToolsAndUtilitiesLayout;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import net.minecraft.resources.ResourceLocation;

public final class MinecraftCtlPlugin implements ICtlPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return CtlVanillaTabs.BUILDING_BLOCKS.withPath("minecraft_builtin_layouts");
    }

    @Override
    public void register(CtlPluginContext ctx) {
        markBuiltinVanillaTabs();

        MinecraftBuildingBlocksLayout.register(ctx);
        MinecraftColoredBlocksLayout.register(ctx);
        MinecraftNaturalBlocksLayout.register(ctx);
        MinecraftFunctionalBlocksLayout.register(ctx);
        MinecraftRedstoneBlocksLayout.register(ctx);

        MinecraftToolsAndUtilitiesLayout.register(ctx);
        MinecraftCombatLayout.register(ctx);
        MinecraftFoodAndDrinksLayout.register(ctx);
        MinecraftIngredientsLayout.register(ctx);
        MinecraftSpawnEggsLayout.register(ctx);
    }

    private static void markBuiltinVanillaTabs() {
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.BUILDING_BLOCKS);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.COLORED_BLOCKS);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.NATURAL_BLOCKS);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.FUNCTIONAL_BLOCKS);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.REDSTONE_BLOCKS);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.TOOLS_AND_UTILITIES);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.COMBAT);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.FOOD_AND_DRINKS);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.INGREDIENTS);
        CtlTabRegistry.markBuiltinVanilla(CtlVanillaTabs.SPAWN_EGGS);
    }
}