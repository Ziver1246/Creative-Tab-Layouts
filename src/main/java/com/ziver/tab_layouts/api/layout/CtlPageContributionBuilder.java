package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface CtlPageContributionBuilder {

    CtlPageContributionBuilder addonSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);

    CtlPageContributionBuilder addonSection(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

    CtlPageContributionBuilder contributeSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}