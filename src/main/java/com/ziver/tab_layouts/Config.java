package com.ziver.tab_layouts;

import com.ziver.tab_layouts.internal.layout.CtlFallbackMode;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    private Config() {}

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.BooleanValue ENABLE_BUILTIN_VANILLA_LAYOUTS;
    public static final ModConfigSpec.BooleanValue ENABLE_DEVELOPER_VISUAL_DEBUG;
    public static final ModConfigSpec.BooleanValue ENABLE_FALLBACK_PAGES;
    public static final ModConfigSpec.EnumValue<CtlFallbackMode> FALLBACK_MODE;
    public static final ModConfigSpec SPEC;

    static {
        ENABLE_BUILTIN_VANILLA_LAYOUTS = BUILDER.comment(
                "Enables CTL's built-in layouts for selected vanilla creative tabs.",
                "This does not disable the CTL API.",
                "External plugins can still control their own tabs independently.",
                "Changes apply immediately. Reopen the Creative Inventory if needed."
        ).define("enableBuiltinVanillaLayouts", true);

        ENABLE_DEVELOPER_VISUAL_DEBUG = BUILDER.comment(
                "Enables developer-only visual debug helpers for CTL headers and banners.",
                        "When enabled, CTL may show extra metadata/tooltips such as visual ids and JSON paths."
                ).define("enableDeveloperVisualDebug", true);

        ENABLE_FALLBACK_PAGES = BUILDER.comment(
                "Automatically adds a fallback page named 'Mods' to CTL-controlled tabs.",
                "This page contains non-Minecraft items that were added to the original vanilla tab but were not claimed by CTL pages.",
                "This helps preserve items from mods without CTL compatibility."
        ).define("enableFallbackPages", true);

        FALLBACK_MODE = BUILDER.comment(
                "Controls how fallback content is grouped.",
                "BY_MOD_SECTION: one final page named 'Mods', with one section per mod.",
                "BY_MOD_PAGE: one final page per mod.",
                "This option only applies when enableFallbackPages is true."
        ).defineEnum("fallbackMode", CtlFallbackMode.BY_MOD_SECTION);

        SPEC = BUILDER.build();
    }
}