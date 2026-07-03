package com.ziver.tab_layouts.api.plugin;

import net.minecraft.resources.ResourceLocation;

public interface ICtlPlugin {
    ResourceLocation getPluginUid();

    void register(CtlPluginContext ctx);
}