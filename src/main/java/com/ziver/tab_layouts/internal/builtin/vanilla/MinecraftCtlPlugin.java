package com.ziver.tab_layouts.internal.builtin.vanilla;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import com.ziver.tab_layouts.internal.builtin.vanilla.pages.*;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import net.minecraft.world.item.CreativeModeTabs;

@CtlPlugin(modId = CreativeTabLayouts.MOD_ID, id = "minecraft_builtin", type = CtlPluginType.PRESET, targets = "minecraft")
public final class MinecraftCtlPlugin implements ICtlPlugin {

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
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.BUILDING_BLOCKS.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.COLORED_BLOCKS.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.NATURAL_BLOCKS.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.FUNCTIONAL_BLOCKS.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.REDSTONE_BLOCKS.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.TOOLS_AND_UTILITIES.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.COMBAT.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.FOOD_AND_DRINKS.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.INGREDIENTS.location());
        CtlTabRegistry.markBuiltinVanilla(CreativeModeTabs.SPAWN_EGGS.location());
    }
}