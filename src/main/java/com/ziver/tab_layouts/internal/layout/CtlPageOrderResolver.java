package com.ziver.tab_layouts.internal.layout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Orders overview pages first, then base pages, then addon pages; priority precedes registration order. */
public final class CtlPageOrderResolver {
    private CtlPageOrderResolver() {}

    private static final Comparator<CtlPage> PAGE_ORDER = Comparator.comparingLong(CtlPage::priority).thenComparingLong(CtlPage::insertionOrder);

    public static List<CtlPage> order(List<CtlPage> pages) {
        List<CtlPage> overview = pages.stream().filter(page -> page.type() == CtlPageType.OVERVIEW).sorted(Comparator.comparingLong(CtlPage::insertionOrder)).toList();
        List<CtlPage> base = pages.stream().filter(page -> page.type() == CtlPageType.BASE).sorted(PAGE_ORDER).toList();
        List<CtlPage> addon = pages.stream().filter(page -> page.type() == CtlPageType.ADDON).sorted(PAGE_ORDER).toList();

        List<CtlPage> ordered = new ArrayList<>(pages.size());
        ordered.addAll(overview);
        ordered.addAll(base);
        ordered.addAll(addon);
        return ordered;
    }
}