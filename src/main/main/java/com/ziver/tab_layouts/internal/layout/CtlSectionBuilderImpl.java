package com.ziver.tab_layouts.internal.layout;

import com.ziver.tab_layouts.api.layout.CtlDynamicEntries;
import com.ziver.tab_layouts.api.layout.CtlSectionBuilder;
import com.ziver.tab_layouts.internal.util.CtlLangKeys;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class CtlSectionBuilderImpl implements CtlSectionBuilder {
    private final ResourceLocation id;
    private final Component title;
    private final long priority;
    private final long insertionOrder;
    private final CtlSectionType type;
    private final List<CtlEntry> entries = new ArrayList<>();

    private long entryInsertionOrder;

    public CtlSectionBuilderImpl(ResourceLocation id, CtlSectionType type, long priority, long insertionOrder, long entryInsertionOrder) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Component.translatable(CtlLangKeys.section(id));
        this.type = Objects.requireNonNull(type, "type");
        this.priority = priority;
        this.insertionOrder = insertionOrder;
        this.entryInsertionOrder = entryInsertionOrder;
    }

    public CtlSectionBuilderImpl(ResourceLocation id, CtlSectionType type, long priority, long insertionOrder) {
        this(id, type, priority, insertionOrder, 0L);
    }

    @Override
    public CtlSectionBuilder add(ItemLike... items) {
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.normal(() -> item, 0L, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlSectionBuilder add(Supplier<? extends ItemLike> item) {
        Objects.requireNonNull(item, "item");
        entries.add(CtlEntry.normal(item, 0L, nextEntryInsertionOrder()));
        return this;
    }

    @Override
    public CtlSectionBuilder stack(Supplier<ItemStack> stack) {
        Objects.requireNonNull(stack, "stack");
        entries.add(CtlEntry.stack(stack, 0L, nextEntryInsertionOrder()));
        return this;
    }

    @Override
    public CtlSectionBuilder dynamic(CtlDynamicEntries dynamicEntries) {
        Objects.requireNonNull(dynamicEntries, "dynamicEntries");
        entries.add(CtlEntry.dynamic(dynamicEntries, 0L, nextEntryInsertionOrder()));
        return this;
    }

    @Override
    public CtlSectionBuilder empty() {
        return empty(1);
    }

    @Override
    public CtlSectionBuilder empty(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Empty slot count cannot be negative: " + count);
        }

        for (int i = 0; i < count; i++) {
            entries.add(CtlEntry.empty(0L, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlSectionBuilder addFirst(ItemLike... items) {
        return addFirst(0L, items);
    }

    @Override
    public CtlSectionBuilder addFirst(long priority, ItemLike... items) {
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.first(() -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlSectionBuilder addLast(ItemLike... items) {
        return addLast(0L, items);
    }

    @Override
    public CtlSectionBuilder addLast(long priority, ItemLike... items) {
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.last(() -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlSectionBuilder addBefore(ItemLike target, ItemLike... items) {
        return addBefore(target, 1, 0L, items);
    }

    @Override
    public CtlSectionBuilder addBefore(ItemLike target, long priority, ItemLike... items) {
        return addBefore(target, 1, priority, items);
    }

    @Override
    public CtlSectionBuilder addBefore(ItemLike target, int occurrence, ItemLike... items) {
        return addBefore(target, occurrence, 0L, items);
    }

    @Override
    public CtlSectionBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.before(target, occurrence, () -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    @Override
    public CtlSectionBuilder addAfter(ItemLike target, ItemLike... items) {
        return addAfter(target, 1, 0L, items);
    }

    @Override
    public CtlSectionBuilder addAfter(ItemLike target, long priority, ItemLike... items) {
        return addAfter(target, 1, priority, items);
    }

    @Override
    public CtlSectionBuilder addAfter(ItemLike target, int occurrence, ItemLike... items) {
        return addAfter(target, occurrence, 0L, items);
    }

    @Override
    public CtlSectionBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(items, "items");

        for (ItemLike item : items) {
            Objects.requireNonNull(item, "item");
            entries.add(CtlEntry.after(target, occurrence, () -> item, priority, nextEntryInsertionOrder()));
        }

        return this;
    }

    CtlSection build() {
        return new CtlSection(id, title, type, priority, insertionOrder, List.copyOf(entries));
    }

    private long nextEntryInsertionOrder() {
        return entryInsertionOrder++;
    }
}