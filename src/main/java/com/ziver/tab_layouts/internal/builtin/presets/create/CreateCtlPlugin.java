package com.ziver.tab_layouts.internal.builtin.presets.create;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;

@CtlPlugin(modId = CreativeTabLayouts.MOD_ID, id = "create_preset", type = CtlPluginType.PRESET, targets = "create")
public final class CreateCtlPlugin implements ICtlPlugin {

    private static final ResourceLocation BASE_TAB = ResourceLocation.fromNamespaceAndPath("create", "base");
    private static final ResourceLocation PALETTES_TAB = ResourceLocation.fromNamespaceAndPath("create", "palettes");

    @Override
    public void register(CtlPluginContext ctx) {
        ctx.subtab(PALETTES_TAB, BASE_TAB);
    }
}