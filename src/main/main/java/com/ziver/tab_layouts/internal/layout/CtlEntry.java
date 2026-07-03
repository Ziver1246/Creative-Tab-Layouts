package com.ziver.tab_layouts.internal.layout;

import com.ziver.tab_layouts.api.layout.CtlDynamicEntries;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class CtlEntry {
    private final Function<HolderLookup.Provider, List<ItemStack>> stackFactory;
    private final boolean keepIfEmpty;
    private final CtlEntryPlacementType placementType;
    private final ItemLike target;
    private final int targetOccurrence;
    private final long priority;
    private final long insertionOrder;

    private CtlEntry(Function<HolderLookup.Provider, List<ItemStack>> stackFactory, boolean keepIfEmpty, CtlEntryPlacementType placementType, ItemLike target, int targetOccurrence, long priority, long insertionOrder) {
        this.stackFactory = Objects.requireNonNull(stackFactory, "stackFactory");
        this.keepIfEmpty = keepIfEmpty;
        this.placementType = Objects.requireNonNull(placementType, "placementType");
        this.target = target;
        this.targetOccurrence = targetOccurrence;
        this.priority = priority;
        this.insertionOrder = insertionOrder;
    }

    public static CtlEntry normal(Supplier<? extends ItemLike> item, long priority, long insertionOrder) {
        return item(item, CtlEntryPlacementType.NORMAL, null, 1, priority, insertionOrder);
    }

    public static CtlEntry first(Supplier<? extends ItemLike> item, long priority, long insertionOrder) {
        return item(item, CtlEntryPlacementType.FIRST, null, 1, priority, insertionOrder);
    }

    public static CtlEntry last(Supplier<? extends ItemLike> item, long priority, long insertionOrder) {
        return item(item, CtlEntryPlacementType.LAST, null, 1, priority, insertionOrder);
    }

    public static CtlEntry before(ItemLike target, int targetOccurrence, Supplier<? extends ItemLike> item, long priority, long insertionOrder) {
        Objects.requireNonNull(target, "target");
        validateOccurrence(targetOccurrence);

        return item(item, CtlEntryPlacementType.BEFORE, target, targetOccurrence, priority, insertionOrder);
    }

    public static CtlEntry after(ItemLike target, int targetOccurrence, Supplier<? extends ItemLike> item, long priority, long insertionOrder) {
        Objects.requireNonNull(target, "target");
        validateOccurrence(targetOccurrence);

        return item(item, CtlEntryPlacementType.AFTER, target, targetOccurrence, priority, insertionOrder);
    }

    private static CtlEntry item(Supplier<? extends ItemLike> item, CtlEntryPlacementType placementType, ItemLike target, int targetOccurrence, long priority, long insertionOrder) {
        Objects.requireNonNull(item, "item");

        return new CtlEntry(registries -> {
            ItemLike resolved = item.get();
            return resolved == null ? List.of(ItemStack.EMPTY) : List.of(new ItemStack(resolved));
        }, false, placementType, target, targetOccurrence, priority, insertionOrder);
    }

    public static CtlEntry stack(Supplier<ItemStack> stack, long priority, long insertionOrder) {
        Objects.requireNonNull(stack, "stack");

        return new CtlEntry(registries -> {
            ItemStack resolved = stack.get();
            return resolved == null ? List.of(ItemStack.EMPTY) : List.of(resolved.copy());
        }, false, CtlEntryPlacementType.NORMAL, null, 1, priority, insertionOrder);
    }

    public static CtlEntry dynamic(CtlDynamicEntries entries, long priority, long insertionOrder) {
        Objects.requireNonNull(entries, "entries");

        return new CtlEntry(registries -> {
            List<ItemStack> stacks = entries.build(registries);

            if (stacks == null || stacks.isEmpty()) return List.of();


            return stacks.stream().map(stack -> stack == null ? ItemStack.EMPTY : stack.copy()).toList();
        }, false, CtlEntryPlacementType.NORMAL, null, 1, priority, insertionOrder);
    }

    public static CtlEntry empty(long priority, long insertionOrder) {
        return new CtlEntry(registries -> List.of(ItemStack.EMPTY), true, CtlEntryPlacementType.NORMAL, null, 1, priority, insertionOrder);
    }

    private static void validateOccurrence(int occurrence) {
        if (occurrence <= 0) throw new IllegalArgumentException("Target occurrence must be greater than zero: " + occurrence);
    }

    public List<ItemStack> buildStacks(HolderLookup.Provider registries) {
        List<ItemStack> stacks = stackFactory.apply(registries);

        if (stacks == null || stacks.isEmpty()) {
            return List.of();
        }

        return stacks.stream().map(stack -> stack == null ? ItemStack.EMPTY : stack.copy()).toList();
    }

    public ItemStack buildStack(HolderLookup.Provider registries) {
        List<ItemStack> stacks = buildStacks(registries);
        return stacks.isEmpty() ? ItemStack.EMPTY : stacks.getFirst();
    }

    public boolean keepIfEmpty() {
        return keepIfEmpty;
    }

    public CtlEntryPlacementType placementType() {
        return placementType;
    }

    public ItemLike target() {
        return target;
    }

    public int targetOccurrence() {
        return targetOccurrence;
    }

    public long priority() {
        return priority;
    }

    public long insertionOrder() {
        return insertionOrder;
    }
}