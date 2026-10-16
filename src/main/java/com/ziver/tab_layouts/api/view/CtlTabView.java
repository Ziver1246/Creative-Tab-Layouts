package com.ziver.tab_layouts.api.view;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Immutable resolved snapshot of a CTL-controlled tab and its pages. */
public record CtlTabView(ResourceLocation id, List<CtlPageView> pages) {

    public CtlTabView {
        Objects.requireNonNull(id, "id");
        pages = List.copyOf(pages);
    }

    public int pageCount() {
        return pages.size();
    }

    public boolean isEmpty() {
        return pages.isEmpty();
    }

    public Optional<CtlPageView> page(int index) {
        if (index < 0 || index >= pages.size()) return Optional.empty();
        return Optional.of(pages.get(index));
    }

    public Optional<CtlPageView> page(ResourceLocation pageId) {
        Objects.requireNonNull(pageId, "pageId");

        for (CtlPageView page : pages) {
            if (page.id().equals(pageId)) return Optional.of(page);
        }

        return Optional.empty();
    }
}