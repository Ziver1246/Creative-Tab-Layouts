package com.ziver.tab_layouts;

import com.ziver.tab_layouts.internal.client.screen.CtlConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CreativeTabLayouts.MOD_ID, dist = Dist.CLIENT)
public final class CreativeTabLayoutsClient {
    public CreativeTabLayoutsClient(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) -> new CtlConfigScreen(parent));
    }
}
