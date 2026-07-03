package com.ziver.tab_layouts.internal.util;

import net.minecraft.resources.ResourceLocation;

public final class CtlVisualPaths {
    private CtlVisualPaths() {}

    public static String headerPath(ResourceLocation id) {
        return "assets/" + id.getNamespace() + "/ctl/headers/" + id.getPath() + ".json";
    }

    public static String bannerPath(ResourceLocation id) {
        return "assets/" + id.getNamespace() + "/ctl/banners/" + id.getPath() + ".json";
    }
}