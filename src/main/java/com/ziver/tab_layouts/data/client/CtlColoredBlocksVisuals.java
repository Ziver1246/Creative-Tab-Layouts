package com.ziver.tab_layouts.data.client;

import com.ziver.tab_layouts.api.datagen.CtlHeaderVisualBuilder;
import com.ziver.tab_layouts.api.datagen.CtlVisualProvider;

public final class CtlColoredBlocksVisuals {
    private CtlColoredBlocksVisuals() {}

    public static void register(CtlVisualProvider provider) {
        colored(provider, "light_gray", "#FFB1B1AB", "#FF94948C");
        colored(provider, "lime", "#FF9ECB74", "#FF7BB842");
        colored(provider, "magenta", "#FFC36EBC", "#FF982B8F");
        colored(provider, "orange", "#FFECA067", "#FFE67D2F");
        colored(provider, "pink", "#FFE293B1", "#FFC15B81");
        colored(provider, "black", "#FF909193", "#FF57585C");
        colored(provider, "blue", "#FF8182BC", "#FF5354A3");
        colored(provider, "brown", "#FFA08A79", "#FF7D5F48");
        colored(provider, "cyan", "#FF73ADB8", "#FF3F8F9D");
        colored(provider, "gray", "#FFA5A6A8", "#FF77797C");
        colored(provider, "light_blue", "#FF7CB8DD", "#FF4B9ED1");
        colored(provider, "purple", "#FFA279C4", "#FF8048AE");
        colored(provider, "red", "#FFBB7A7A", "#FFA24949");
        colored(provider, "white", "#FFDADEDF", "#FFC5CACA");
        colored(provider, "yellow", "#FFF5C75B", "#FFD99E13");
        colored(provider, "green", "#FF929D7C", "#FF6A794B");

        provider.header(
                "minecraft/colored_blocks/neutral_blocks",
                CtlHeaderVisualBuilder.header(provider.modLoc("textures/gui/vanilla/colored_blocks/neutral.png"))
                        .splitTextColor("#FFC8BDA5", "#FFA99672")
                        .labelColor("#99000000")
                        .textShadow(true)
                        .left()
        );
    }

    private static void colored(CtlVisualProvider provider, String color, String topColor, String bottomColor) {
        provider.header(
                "minecraft/colored_blocks/" + color + "_blocks",
                CtlHeaderVisualBuilder.header(provider.modLoc("textures/gui/vanilla/colored_blocks/" + color + ".png"))
                        .splitTextColor(topColor, bottomColor)
                        .labelColor("#99000000")
                        .textShadow(true)
                        .left()
        );
    }
}