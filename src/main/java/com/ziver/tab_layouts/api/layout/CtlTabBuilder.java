package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface CtlTabBuilder {

    CtlTabBuilder overview(ResourceLocation pageId);

    CtlTabBuilder page(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    CtlTabBuilder page(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

    CtlTabBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    CtlTabBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

    CtlTabBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    CtlTabBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}