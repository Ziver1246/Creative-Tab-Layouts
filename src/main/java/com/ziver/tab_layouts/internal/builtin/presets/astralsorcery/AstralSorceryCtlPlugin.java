package com.ziver.tab_layouts.internal.builtin.presets.astralsorcery;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;

@CtlPlugin(modId = CreativeTabLayouts.MOD_ID, id = "astralsorcery_preset", type = CtlPluginType.PRESET, targets = "astralsorcery")
public final class AstralSorceryCtlPlugin implements ICtlPlugin {

    private static final ResourceLocation COMMON = ResourceLocation.fromNamespaceAndPath("astralsorcery", "common");
    private static final ResourceLocation PAPERS = ResourceLocation.fromNamespaceAndPath("astralsorcery", "papers");
    private static final ResourceLocation LUMEN = ResourceLocation.fromNamespaceAndPath("astralsorcery", "lumen");
    private static final ResourceLocation ATTUNED_CRYSTALS = ResourceLocation.fromNamespaceAndPath("astralsorcery", "attuned_crystals");
    private static final ResourceLocation ARTIFACTS = ResourceLocation.fromNamespaceAndPath("astralsorcery", "artifacts");

    @Override
    public void register(CtlPluginContext ctx) {
        ctx.subtabs(COMMON, PAPERS, LUMEN, ATTUNED_CRYSTALS, ARTIFACTS);
    }
}