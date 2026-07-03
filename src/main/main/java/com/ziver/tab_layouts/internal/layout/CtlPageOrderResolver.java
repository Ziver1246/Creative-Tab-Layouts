package com.ziver.tab_layouts.internal.layout;

import com.ziver.tab_layouts.internal.config.CtlPageOrderConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CtlPageOrderResolver {
    private CtlPageOrderResolver() {}

    public static List<CtlPage> order(ResourceLocation tabId, List<CtlPage> pages, CtlPageOrderMode orderMode) {
        List<CtlPage> overview = pages.stream()
                .filter(page -> page.type() == CtlPageType.OVERVIEW)
                .sorted(Comparator.comparingLong(CtlPage::insertionOrder))
                .toList();

        List<CtlPage> base = pages.stream().filter(page -> page.type() == CtlPageType.BASE).toList();
        List<CtlPage> addon = pages.stream().filter(page -> page.type() == CtlPageType.ADDON).toList();

        if (CtlPageOrderConfig.hasOrder(tabId)) {
            base = orderByCustomList(base, CtlPageOrderConfig.baseOrder(tabId));
            addon = orderByCustomList(addon, CtlPageOrderConfig.addonOrder(tabId));
        } else {
            base = base.stream().sorted(pageComparator(orderMode)).toList();
            addon = addon.stream().sorted(pageComparator(orderMode)).toList();
        }

        List<CtlPage> ordered = new ArrayList<>(pages.size());
        ordered.addAll(overview);
        ordered.addAll(base);
        ordered.addAll(addon);

        return ordered;
    }

    private static List<CtlPage> orderByCustomList(List<CtlPage> pages, List<ResourceLocation> customOrder) {
        List<CtlPage> ordered = new ArrayList<>();
        List<CtlPage> remaining = new ArrayList<>(pages);

        for (ResourceLocation pageId : customOrder) {
            CtlPage page = findById(remaining, pageId);

            if (page != null) {
                ordered.add(page);
                remaining.remove(page);
            }
        }

        remaining.sort(pageComparator(CtlPageOrderMode.DEFAULT));
        ordered.addAll(remaining);

        return ordered;
    }

    private static CtlPage findById(List<CtlPage> pages, ResourceLocation pageId) {
        for (CtlPage page : pages) {
            if (page.id().equals(pageId)) {
                return page;
            }
        }

        return null;
    }

    private static Comparator<CtlPage> pageComparator(CtlPageOrderMode orderMode) {
        return switch (orderMode) {
            case DEFAULT, CUSTOM -> Comparator.comparingLong(CtlPage::priority).thenComparingLong(CtlPage::insertionOrder);
            case ALPHABETICAL_ASC -> Comparator.comparing(page -> page.title().getString(), String.CASE_INSENSITIVE_ORDER);
            case ALPHABETICAL_DESC -> Comparator.comparing((CtlPage page) -> page.title().getString(), String.CASE_INSENSITIVE_ORDER).reversed();
        };
    }
}