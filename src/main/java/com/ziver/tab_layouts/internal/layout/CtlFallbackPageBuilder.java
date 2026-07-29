package com.ziver.tab_layouts.internal.layout;

import com.ziver.tab_layouts.Config;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.*;
import java.util.function.Predicate;

public final class CtlFallbackPageBuilder {
    private static final int SECTION_HEADER_SIZE = 9;

    private CtlFallbackPageBuilder() {}

    public static int pageCount(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> vanillaItems, HolderLookup.Provider registries) {
        Map<String, List<ItemStack>> itemsByMod = fallbackItemsByMod(layout, vanillaItems, registries);

        if (itemsByMod.isEmpty()) return 0;

        return switch (Config.FALLBACK_MODE.get()) {
            case BY_MOD_SECTION -> 1;
            case BY_MOD_PAGE -> itemsByMod.size();
        };
    }

    public static boolean hasFallback(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> vanillaItems, HolderLookup.Provider registries) {
        return pageCount(tabId, layout, vanillaItems, registries) > 0;
    }

    public static Component title(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> vanillaItems, HolderLookup.Provider registries, int fallbackIndex) {
        Map<String, List<ItemStack>> itemsByMod = fallbackItemsByMod(layout, vanillaItems, registries);

        if (itemsByMod.isEmpty()) {
            return Component.translatable("screen.tab_layouts.fallback.mods");
        }

        return switch (Config.FALLBACK_MODE.get()) {
            case BY_MOD_SECTION -> Component.translatable("screen.tab_layouts.fallback.mods");
            case BY_MOD_PAGE -> {
                String modId = modIdAt(itemsByMod, fallbackIndex);
                yield modDisplayName(modId);
            }
        };
    }

    public static CtlBuiltPage build(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> vanillaItems, HolderLookup.Provider registries, int fallbackIndex) {
        return build(tabId, layout, vanillaItems, registries, fallbackIndex, sectionId -> false);
    }

    public static CtlBuiltPage build(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> vanillaItems, HolderLookup.Provider registries, int fallbackIndex, Predicate<ResourceLocation> collapsedSections) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(layout, "layout");
        Objects.requireNonNull(vanillaItems, "vanillaItems");
        Objects.requireNonNull(registries, "registries");
        Objects.requireNonNull(collapsedSections, "collapsedSections");

        Map<String, List<ItemStack>> itemsByMod = fallbackItemsByMod(layout, vanillaItems, registries);

        if (itemsByMod.isEmpty()) return emptyPage();

        return switch (Config.FALLBACK_MODE.get()) {
            case BY_MOD_SECTION -> buildByModSection(tabId, itemsByMod, collapsedSections);
            case BY_MOD_PAGE -> buildByModPage(tabId, itemsByMod, fallbackIndex, collapsedSections);
        };
    }

    private static CtlBuiltPage buildByModSection(ResourceLocation tabId, Map<String, List<ItemStack>> itemsByMod, Predicate<ResourceLocation> collapsedSections) {
        List<ItemStack> items = new ArrayList<>();
        List<CtlRenderedSection> renderedSections = new ArrayList<>();

        for (Map.Entry<String, List<ItemStack>> entry : itemsByMod.entrySet()) {
            String modId = entry.getKey();
            List<ItemStack> modItems = entry.getValue();

            if (modItems.isEmpty()) continue;

            padToFullRow(items);

            ResourceLocation sectionId = fallbackSectionId(tabId, modId);
            CtlSection section = new CtlSection(sectionId, modDisplayName(modId), CtlSectionType.BASE, 0L, renderedSections.size(), List.of());

            renderedSections.add(new CtlRenderedSection(section, items.size() / 9));

            addHeaderRow(items);

            if (collapsedSections.test(sectionId)) continue;

            addCopies(items, modItems);
        }

        padToFullRow(items);
        padToMinimumPageSize(items);

        return new CtlBuiltPage(items, renderedSections, false);
    }

    private static CtlBuiltPage buildByModPage(ResourceLocation tabId, Map<String, List<ItemStack>> itemsByMod, int fallbackIndex, Predicate<ResourceLocation> collapsedSections) {
        String modId = modIdAt(itemsByMod, fallbackIndex);
        List<ItemStack> modItems = itemsByMod.getOrDefault(modId, List.of());

        List<ItemStack> items = new ArrayList<>();
        List<CtlRenderedSection> renderedSections = new ArrayList<>();

        ResourceLocation sectionId = fallbackSectionId(tabId, modId);
        CtlSection section = new CtlSection(sectionId, modDisplayName(modId), CtlSectionType.BASE, 0L, 0L, List.of());

        renderedSections.add(new CtlRenderedSection(section, 0));

        addHeaderRow(items);

        if (!collapsedSections.test(sectionId)) {
            addCopies(items, modItems);
        }

        padToFullRow(items);
        padToMinimumPageSize(items);

        return new CtlBuiltPage(items, renderedSections, false);
    }

    private static Map<String, List<ItemStack>> fallbackItemsByMod(CtlTabLayout layout, List<ItemStack> vanillaItems, HolderLookup.Provider registries) {
        Set<Item> claimedItems = claimedItems(layout, registries);
        Set<Item> addedItems = new HashSet<>();

        Map<String, List<ItemStack>> result = new LinkedHashMap<>();

        for (ItemStack stack : vanillaItems) {
            if (stack.isEmpty()) continue;

            Item item = stack.getItem();

            if (claimedItems.contains(item)) continue;
            if (addedItems.contains(item)) continue;

            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);

            String modId = itemId.getNamespace();

            if ("minecraft".equals(modId)) continue;

            addedItems.add(item);
            result.computeIfAbsent(modId, ignored -> new ArrayList<>()).add(stack.copy());
        }

        return result;
    }

    private static Set<Item> claimedItems(CtlTabLayout layout, HolderLookup.Provider registries) {
        Set<Item> claimed = new HashSet<>();

        for (CtlPage page : layout.orderedPages()) {
            CtlBuiltPage builtPage = page.build(registries);

            for (ItemStack stack : builtPage.items()) {
                if (!stack.isEmpty()) {
                    claimed.add(stack.getItem());
                }
            }
        }

        return claimed;
    }

    private static String modIdAt(Map<String, List<ItemStack>> itemsByMod, int index) {
        if (index < 0 || index >= itemsByMod.size()) {
            return itemsByMod.keySet().stream().findFirst().orElse("unknown");
        }

        int current = 0;

        for (String modId : itemsByMod.keySet()) {
            if (current == index) {
                return modId;
            }

            current++;
        }

        return "unknown";
    }

    private static Component modDisplayName(String modId) {
        return ModList.get()
                .getModContainerById(modId)
                .map(container -> Component.literal(container.getModInfo().getDisplayName()))
                .orElse(Component.literal(modId));
    }

    private static ResourceLocation fallbackSectionId(ResourceLocation tabId, String modId) {
        return ResourceLocation.fromNamespaceAndPath(modId,
                "fallback/" + sanitizePath(tabId.getNamespace()) + "/" + sanitizePath(tabId.getPath())
        );
    }

    private static void addHeaderRow(List<ItemStack> items) {
        for (int i = 0; i < SECTION_HEADER_SIZE; i++) {
            items.add(ItemStack.EMPTY);
        }
    }

    private static void addCopies(List<ItemStack> items, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            items.add(stack.copy());
        }
    }

    private static void padToFullRow(List<ItemStack> items) {
        int remainder = items.size() % 9;

        if (remainder == 0) return;

        int padding = 9 - remainder;

        for (int i = 0; i < padding; i++) {
            items.add(ItemStack.EMPTY);
        }
    }

    private static void padToMinimumPageSize(List<ItemStack> items) {
        while (items.size() < 45) {
            items.add(ItemStack.EMPTY);
        }
    }

    private static CtlBuiltPage emptyPage() {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < 45; i++) {
            items.add(ItemStack.EMPTY);
        }

        return new CtlBuiltPage(items, List.of(), false);
    }

    private static String sanitizePath(String value) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);

            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == '-' || c == '/' || c == '.') {
                builder.append(c);
            } else if (c >= 'A' && c <= 'Z') {
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append('_');
            }
        }

        if (builder.isEmpty()) return "unknown";

        return builder.toString();
    }

    public static List<CtlFallbackPage> resolve(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> originalItems, HolderLookup.Provider registries) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(layout, "layout");
        Objects.requireNonNull(originalItems, "originalItems");
        Objects.requireNonNull(registries, "registries");

        Map<String, List<ItemStack>> itemsByMod = fallbackItemsByMod(layout, originalItems, registries);
        if (itemsByMod.isEmpty()) return List.of();

        return switch (Config.FALLBACK_MODE.get()) {
            case BY_MOD_SECTION -> resolveByModSection(tabId, itemsByMod);
            case BY_MOD_PAGE -> resolveByModPage(tabId, itemsByMod);
        };
    }

    private static List<CtlFallbackPage> resolveByModSection(ResourceLocation tabId, Map<String, List<ItemStack>> itemsByMod) {
        List<CtlFallbackSection> sections = new ArrayList<>(itemsByMod.size());

        for (Map.Entry<String, List<ItemStack>> entry : itemsByMod.entrySet()) {
            String modId = entry.getKey();
            List<ItemStack> items = entry.getValue();

            if (items.isEmpty()) continue;

            sections.add(new CtlFallbackSection(fallbackSectionId(tabId, modId), modDisplayName(modId), items));
        }

        if (sections.isEmpty()) return List.of();
        ResourceLocation pageId = fallbackSectionPageId(tabId);

        return List.of(new CtlFallbackPage(pageId, Component.translatable("screen.tab_layouts.fallback.mods"), sections));
    }

    private static List<CtlFallbackPage> resolveByModPage(ResourceLocation tabId, Map<String, List<ItemStack>> itemsByMod) {
        List<CtlFallbackPage> pages = new ArrayList<>(itemsByMod.size());

        for (Map.Entry<String, List<ItemStack>> entry : itemsByMod.entrySet()) {
            String modId = entry.getKey();
            List<ItemStack> items = entry.getValue();

            if (items.isEmpty()) continue;

            Component title = modDisplayName(modId);
            ResourceLocation sectionId = fallbackSectionId(tabId, modId);
            ResourceLocation pageId = fallbackPageId(tabId, modId);

            CtlFallbackSection section = new CtlFallbackSection(sectionId, title, items);
            pages.add(new CtlFallbackPage(pageId, title, List.of(section)));
        }

        return List.copyOf(pages);
    }

    private static ResourceLocation fallbackPageId(ResourceLocation tabId, String modId) {
        return ResourceLocation.fromNamespaceAndPath(modId, "fallback/" + sanitizePath(tabId.getNamespace()) + "/" + sanitizePath(tabId.getPath()));
    }

    public static ResourceLocation pageId(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> vanillaItems, HolderLookup.Provider registries, int fallbackIndex) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(layout, "layout");
        Objects.requireNonNull(vanillaItems, "vanillaItems");
        Objects.requireNonNull(registries, "registries");

        if (Config.FALLBACK_MODE.get() == CtlFallbackMode.BY_MOD_SECTION) {
            return fallbackSectionPageId(tabId);
        }

        Map<String, List<ItemStack>> itemsByMod = fallbackItemsByMod(layout, vanillaItems, registries);
        String modId = modIdAt(itemsByMod, fallbackIndex);

        return fallbackPageId(tabId, modId);
    }

    private static ResourceLocation fallbackSectionPageId(ResourceLocation tabId) {
        return ResourceLocation.fromNamespaceAndPath(tabId.getNamespace(), "fallback/" + sanitizePath(tabId.getPath()));
    }

    public record CtlFallbackPage(ResourceLocation id, Component title, List<CtlFallbackSection> sections) {
        public CtlFallbackPage {
            Objects.requireNonNull(id, "id");
            Objects.requireNonNull(title, "title");
            sections = List.copyOf(sections);
        }
    }

    public record CtlFallbackSection(ResourceLocation id, Component title, List<ItemStack> items) {
        public CtlFallbackSection {
            Objects.requireNonNull(id, "id");
            Objects.requireNonNull(title, "title");
            items = items.stream().map(ItemStack::copy).toList();
        }
    }
}