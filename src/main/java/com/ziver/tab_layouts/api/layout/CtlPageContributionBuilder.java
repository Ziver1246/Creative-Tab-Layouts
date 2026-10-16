package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/** Contribution-only view of a single existing CTL page. */
public interface CtlPageContributionBuilder {

    /** Adds an addon section using the default priority. */
    CtlPageContributionBuilder addonSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);

    /** Adds an addon section; addon sections are ordered after base sections. */
    CtlPageContributionBuilder addonSection(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

    /** Adds entries to an existing section. */
    CtlPageContributionBuilder contributeSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}