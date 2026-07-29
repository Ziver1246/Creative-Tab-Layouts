package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface CtlContributionBuilder {

    CtlContributionBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    CtlContributionBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

    CtlContributionBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageContributionBuilder> builder);

    CtlContributionBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}