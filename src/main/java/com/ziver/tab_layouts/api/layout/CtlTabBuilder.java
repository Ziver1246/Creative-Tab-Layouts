package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * Owner builder for a CTL-controlled creative tab.
 *
 * <p>Base pages are ordered before addon pages. Within each group, lower priority values
 * are ordered first and equal priorities preserve registration order.</p>
 */
public interface CtlTabBuilder extends CtlContributionBuilder {

    /** Adds the single overview page for this tab. Duplicate overview or page IDs are rejected. */
    CtlTabBuilder overview(ResourceLocation pageId);

    /** Adds a base page using the default priority. */
    CtlTabBuilder page(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

    /** Adds a base page; lower priority values are ordered first. */
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