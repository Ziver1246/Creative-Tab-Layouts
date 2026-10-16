package com.ziver.tab_layouts.internal.builtin.presets.cataclysm;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;

@CtlPlugin(modId = CreativeTabLayouts.MOD_ID, id = "cataclysm_preset", type = CtlPluginType.PRESET, targets = "cataclysm")
public final class CataclysmCtlPlugin implements ICtlPlugin {

    private static final ResourceLocation CATACLYSM_ITEMS = ResourceLocation.fromNamespaceAndPath("cataclysm", "cataclysm_item");
    private static final ResourceLocation CATACLYSM_BLOCKS = ResourceLocation.fromNamespaceAndPath("cataclysm", "cataclysm_block");

    @Override
    public void register(CtlPluginContext ctx) {
        ctx.subtab(CATACLYSM_BLOCKS, CATACLYSM_ITEMS);
    }
}