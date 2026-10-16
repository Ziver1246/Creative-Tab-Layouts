package com.ziver.tab_layouts.internal.ctoapi;

import com.ziver.tab_layouts.CtlConfig;

public final class CtlCtoBridge {
    private CtlCtoBridge() {}

    public static boolean areBuiltinVanillaLayoutsEnabled() {
        return CtlConfig.ENABLE_BUILTIN_VANILLA_LAYOUTS.get();
    }

    public static boolean areSubtabsEnabled() {
        return CtlConfig.ENABLE_SUBTABS.get();
    }
}