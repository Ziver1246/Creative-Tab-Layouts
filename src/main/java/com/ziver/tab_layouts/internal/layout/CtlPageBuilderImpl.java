package com.ziver.tab_layouts.internal.layout;

import com.ziver.tab_layouts.api.layout.CtlDynamicEntries;
import com.ziver.tab_layouts.api.layout.CtlPageBuilder;
import com.ziver.tab_layouts.api.layout.CtlSectionBuilder;
import com.ziver.tab_layouts.internal.util.CtlLangKeys;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class CtlPageBuilderImpl implements CtlPageBuilder {
    private final ResourceLocation id;
    private final Component title;
    private final CtlPageType type;
    private final long priority;
    private final long insertionOrder;

    private final List<CtlEntry> entries = new ArrayList<>();
    private final List<CtlSection> sections = new ArrayList<>();

    private long entryInsertionOrder;
    private long sectionInsertionOrder;

    public CtlPageBuilderImpl(ResourceLocation id, CtlPageType type, long priority, long insertionOrder, long entryInsertionOrder, long sectionInsertionOrder) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Component.translatable(CtlLangKeys.page(id));
        this.type = Objects.requireNonNull(type, "type");
        this.priority = priority;
        this.insertionOrder = insertionOrder;
        this.entryInsertionOrder = entryInsertionOrder;
        this.sectionInsertionOrder = sectionInsertionOrder;
    }

    public CtlPageBuilderImpl(ResourceLocation id, CtlPageType type, long priority, long insertionOrder) {
        this(id, type, priority, insertionOrder, 0L, 0L);
    }

    @Override
    public CtlPageBuilder add(ItemLike... items) {
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.normal(() -> item, 0L, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlPageBuilder add(Supplier<? extends ItemLike> item) {
        Objects.requireNonNull(item, "item");
        entries.add(CtlEntry.normal(item, 0L, nextEntryInsertionOrder()));
        return this;
    }

    @Override
    public CtlPageBuilder stack(Supplier<ItemStack> stack) {
        Objects.requireNonNull(stack, "stack");
        entries.add(CtlEntry.stack(stack, 0L, nextEntryInsertionOrder()));
        return this;
    }

    @Override
    public CtlPageBuilder dynamic(CtlDynamicEntries dynamicEntries) {
        Objects.requireNonNull(dynamicEntries, "dynamicEntries");
        entries.add(CtlEntry.dynamic(dynamicEntries, 0L, nextEntryInsertionOrder()));
        return this;
    }

    @Override
    public CtlPageBuilder empty() {
        return empty(1);
    }

    @Override
    public CtlPageBuilder empty(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Empty slot count cannot be negative: " + count);
        }

        for (int i = 0; i < count; i++) {
            entries.add(CtlEntry.empty(0L, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlPageBuilder addFirst(ItemLike... items) {
        return addFirst(0L, items);
    }

    @Override
    public CtlPageBuilder addFirst(long priority, ItemLike... items) {
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.first(() -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlPageBuilder addLast(ItemLike... items) {
        return addLast(0L, items);
    }

    @Override
    public CtlPageBuilder addLast(long priority, ItemLike... items) {
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.last(() -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlPageBuilder addBefore(ItemLike target, ItemLike... items) {
        return addBefore(target, 1, 0L, items);
    }

    @Override
    public CtlPageBuilder addBefore(ItemLike target, long priority, ItemLike... items) {
        return addBefore(target, 1, priority, items);
    }

    @Override
    public CtlPageBuilder addBefore(ItemLike target, int occurrence, ItemLike... items) {
        return addBefore(target, occurrence, 0L, items);
    }

    @Override
    public CtlPageBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.before(target, occurrence, () -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlPageBuilder addAfter(ItemLike target, ItemLike... items) {
        return addAfter(target, 1, 0L, items);
    }

    @Override
    public CtlPageBuilder addAfter(ItemLike target, long priority, ItemLike... items) {
        return addAfter(target, 1, priority, items);
    }

    @Override
    public CtlPageBuilder addAfter(ItemLike target, int occurrence, ItemLike... items) {
        return addAfter(target, occurrence, 0L, items);
    }

    @Override
    public CtlPageBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.after(target, occurrence, () -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlPageBuilder section(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder) {
        return section(sectionId, 0L, builder);
    }

    @Override
    public CtlPageBuilder section(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder) {
        return addSection(sectionId, CtlSectionType.BASE, priority, builder);
    }

    @Override
    public CtlPageBuilder addonSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder) {
        return addonSection(sectionId, 0L, builder);
    }

    @Override
    public CtlPageBuilder addonSection(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder) {
        return addSection(sectionId, CtlSectionType.ADDON, priority, builder);
    }

    private CtlPageBuilder addSection(ResourceLocation sectionId, CtlSectionType type, long priority, Consumer<CtlSectionBuilder> builder) {
        Objects.requireNonNull(sectionId, "sectionId");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(builder, "builder");

        validateUniqueSectionId(sectionId);

        CtlSectionBuilderImpl sectionBuilder = new CtlSectionBuilderImpl(sectionId, type, priority, nextSectionInsertionOrder());

        builder.accept(sectionBuilder);
        sections.add(sectionBuilder.build());

        return this;
    }

    @Override
    public CtlPageBuilder contributeSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder) {
        Objects.requireNonNull(sectionId, "sectionId");
        Objects.requireNonNull(builder, "builder");

        int index = findSectionIndex(sectionId);

        if (index < 0) throw new IllegalArgumentException("Cannot contribute to missing CTL section '" + sectionId + "' in page '" + id + "'");

        CtlSection existing = sections.get(index);
        CtlSectionBuilderImpl sectionBuilder = new CtlSectionBuilderImpl(existing.id(), existing.type(), existing.priority(), existing.insertionOrder(), existing.nextEntryInsertionOrder());
        builder.accept(sectionBuilder);

        sections.set(index, existing.withAdditionalEntries(sectionBuilder.build().entries()));

        return this;
    }

    private int findSectionIndex(ResourceLocation sectionId) {
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).id().equals(sectionId)) return i;
        }

        return -1;
    }


    public CtlPage build() {
        List<CtlSection> orderedSections = orderedSections();

        return new CtlPage(id, title, type, priority, insertionOrder, List.copyOf(entries), orderedSections);
    }

    private List<CtlSection> orderedSections() {
        List<CtlSection> base = sections.stream()
                .filter(section -> section.type() == CtlSectionType.BASE)
                .sorted(Comparator.comparingLong(CtlSection::priority).thenComparingLong(CtlSection::insertionOrder))
                .toList();

        List<CtlSection> addon = sections.stream()
                .filter(section -> section.type() == CtlSectionType.ADDON)
                .sorted(Comparator.comparingLong(CtlSection::priority).thenComparingLong(CtlSection::insertionOrder))
                .toList();

        List<CtlSection> ordered = new ArrayList<>(sections.size());
        ordered.addAll(base);
        ordered.addAll(addon);

        return ordered;
    }

    private void validateUniqueSectionId(ResourceLocation sectionId) {
        for (CtlSection section : sections) {
            if (section.id().equals(sectionId)) {
                throw new IllegalArgumentException("Duplicate CTL section id '" + sectionId + "' in page '" + id + "'. " + "Section ids must be unique within the same page."
                );
            }
        }
    }

    private long nextEntryInsertionOrder() {
        return entryInsertionOrder++;
    }

    private long nextSectionInsertionOrder() {
        return sectionInsertionOrder++;
    }
}