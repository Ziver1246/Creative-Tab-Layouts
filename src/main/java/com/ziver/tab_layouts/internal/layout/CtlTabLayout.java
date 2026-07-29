package com.ziver.tab_layouts.internal.layout;

import com.ziver.tab_layouts.api.layout.CtlPageBuilder;
import com.ziver.tab_layouts.api.layout.CtlPageContributionBuilder;
import com.ziver.tab_layouts.api.layout.CtlSectionBuilder;
import com.ziver.tab_layouts.api.layout.CtlTabBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class CtlTabLayout implements CtlTabBuilder {
    private final ResourceLocation tabId;
    private final List<CtlPage> pages = new ArrayList<>();

    private long nextInsertionOrder = 0L;
    private CtlPageOrderMode pageOrderMode = CtlPageOrderMode.DEFAULT;

    public CtlTabLayout(ResourceLocation tabId) {
        this.tabId = Objects.requireNonNull(tabId, "tabId");
    }

    @Override
    public CtlTabBuilder overview(ResourceLocation pageId) {
        Objects.requireNonNull(pageId, "pageId");

        validateNoOverview();
        validateUniquePageId(pageId);

        CtlPageBuilderImpl pageBuilder = new CtlPageBuilderImpl(pageId, CtlPageType.OVERVIEW, 0L, nextInsertionOrder++);

        pages.add(pageBuilder.build());

        return this;
    }

    @Override
    public CtlTabBuilder page(ResourceLocation pageId, Consumer<CtlPageBuilder> builder) {
        return page(pageId, 0L, builder);
    }

    @Override
    public CtlTabBuilder page(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder) {
        return addPage(pageId, CtlPageType.BASE, priority, builder);
    }

    @Override
    public CtlTabBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder) {
        return addonPage(pageId, 0L, builder);
    }

    @Override
    public CtlTabBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder) {
        return addPage(pageId, CtlPageType.ADDON, priority, builder);
    }

    private CtlTabBuilder addPage(ResourceLocation pageId, CtlPageType type, long priority, Consumer<CtlPageBuilder> builder) {
        Objects.requireNonNull(pageId, "pageId");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(builder, "builder");

        validateUniquePageId(pageId);

        CtlPageBuilderImpl pageBuilder = new CtlPageBuilderImpl(pageId, type, priority, nextInsertionOrder++);

        builder.accept(pageBuilder);
        pages.add(pageBuilder.build());

        return this;
    }

    @Override
    public CtlTabBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageContributionBuilder> builder) {
        Objects.requireNonNull(pageId, "pageId");
        Objects.requireNonNull(builder, "builder");

        int index = findPageIndex(pageId);

        if (index < 0) {
            throw new IllegalArgumentException("Cannot contribute to missing CTL page '" + pageId + "' in tab '" + tabId + "'");
        }

        CtlPage existing = pages.get(index);

        if (existing.type() == CtlPageType.OVERVIEW) throw new IllegalArgumentException("Cannot contribute layout content to overview page '" + pageId + "' in tab '" + tabId + "'");

        CtlPageBuilderImpl pageBuilder = new CtlPageBuilderImpl(existing.id(), existing.type(), existing.priority(), existing.insertionOrder(), existing.nextEntryInsertionOrder(), existing.nextSectionInsertionOrder());
        builder.accept(pageBuilder);

        pages.set(index, existing.withAdditionalContent(pageBuilder.build()));

        return this;
    }

    @Override
    public CtlTabBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder) {
        Objects.requireNonNull(pageId, "pageId");
        Objects.requireNonNull(sectionId, "sectionId");
        Objects.requireNonNull(builder, "builder");

        int pageIndex = findPageIndex(pageId);

        if (pageIndex < 0) throw new IllegalArgumentException("Cannot contribute to missing CTL page '" + pageId + "' in tab '" + tabId + "'");

        CtlPage page = pages.get(pageIndex);

        CtlSection existingSection = page.section(sectionId);

        if (existingSection == null) throw new IllegalArgumentException("Cannot contribute to missing CTL section '" + sectionId + "' in page '" + pageId + "'");

        CtlSectionBuilderImpl sectionBuilder = new CtlSectionBuilderImpl(existingSection.id(), existingSection.type(), existingSection.priority(), existingSection.insertionOrder(), existingSection.nextEntryInsertionOrder());
        builder.accept(sectionBuilder);

        pages.set(pageIndex, page.withAdditionalSectionContent(sectionBuilder.build()));

        return this;
    }

    private int findPageIndex(ResourceLocation pageId) {
        for (int i = 0; i < pages.size(); i++) {
            if (pages.get(i).id().equals(pageId)) return i;
        }

        return -1;
    }

    private void validateNoOverview() {
        for (CtlPage page : pages) {
            if (page.type() == CtlPageType.OVERVIEW) {
                throw new IllegalStateException("Tab '" + tabId + "' already has an overview page");
            }
        }
    }

    private void validateUniquePageId(ResourceLocation pageId) {
        for (CtlPage page : pages) {
            if (page.id().equals(pageId)) {
                throw new IllegalArgumentException("Duplicate CTL page id '" + pageId + "' in tab '" + tabId + "'");
            }
        }
    }

    public ResourceLocation tabId() {
        return tabId;
    }

    public int pageCount() {
        return pages.size();
    }

    public boolean isEmpty() {
        return pages.isEmpty();
    }

    public List<CtlPage> orderedPages() {
        return CtlPageOrderResolver.order(tabId, pages, pageOrderMode);
    }

    public List<CtlPage> pagesOfType(CtlPageType type) {
        return orderedPages().stream().filter(page -> page.type() == type).toList();
    }

    public CtlPage overviewPage() {
        for (CtlPage page : orderedPages()) {
            if (page.type() == CtlPageType.OVERVIEW) return page;
        }

        return null;
    }

    public CtlPage page(int index) {
        List<CtlPage> ordered = orderedPages();
        if (ordered.isEmpty()) {
            return null;
        }

        return ordered.get(Math.floorMod(index, ordered.size()));
    }

    public CtlPageOrderMode pageOrderMode() {
        return pageOrderMode;
    }

    public void setPageOrderMode(CtlPageOrderMode pageOrderMode) {
        this.pageOrderMode = Objects.requireNonNull(pageOrderMode, "pageOrderMode");
    }
}