package com.ziver.tab_layouts.data.client;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.api.datagen.CtlVisualProvider;
import net.minecraft.data.PackOutput;

public final class CtlVanillaVisualProvider extends CtlVisualProvider {
    public CtlVanillaVisualProvider(PackOutput output) {
        super(output, CreativeTabLayouts.MOD_ID);
    }

    @Override
    protected void addVisuals() {
        CtlColoredBlocksVisuals.register(this);
    }
}