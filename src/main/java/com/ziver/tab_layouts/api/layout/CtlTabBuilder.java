package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface CtlTabBuilder extends CtlContributionBuilder {

    CtlTabBuilder overview(ResourceLocation pageId);

    CtlTabBuilder page(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    CtlTabBuilder page(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

    @Override
    CtlTabBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    @Override
    CtlTabBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

    @Override
    CtlTabBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageContributionBuilder> builder);

    @Override
    CtlTabBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}