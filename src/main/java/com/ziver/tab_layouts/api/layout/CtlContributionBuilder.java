package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * Contribution-only view of a controlled tab layout.
 *
 * <p>This interface intentionally excludes owner operations such as creating base pages
 * or an overview page.</p>
 */
public interface CtlContributionBuilder {

    /** Adds an addon page using the default priority. */
    CtlContributionBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    /** Adds an addon page; addon pages are ordered after base pages. */
    CtlContributionBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

    /** Contributes addon sections or section content to an existing non-overview page. */
    CtlContributionBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageContributionBuilder> builder);

    /** Adds entries to an existing section of an existing page. */
    CtlContributionBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}