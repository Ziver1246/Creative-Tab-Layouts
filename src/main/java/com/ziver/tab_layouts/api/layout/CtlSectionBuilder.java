package com.ziver.tab_layouts.api.layout;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/**
 * Builds the ordered entries of a CTL section.
 *
 * <p>Normal entries preserve registration order. Placement methods can insert entries
 * at the beginning, end, or relative to an occurrence of another item.</p>
 */
public interface CtlSectionBuilder {

    /** Adds one or more normal item entries. */
    CtlSectionBuilder add(ItemLike... items);

    /** Adds a lazily supplied normal item entry. */
    CtlSectionBuilder add(Supplier<? extends ItemLike> item);

    /** Adds a lazily supplied item stack. */
    CtlSectionBuilder stack(Supplier<ItemStack> stack);

    /** Adds registry-aware dynamic entries resolved when the layout is built. */
    CtlSectionBuilder dynamic(CtlDynamicEntries entries);

    /** Adds one intentional empty slot. */
    CtlSectionBuilder empty();

    /** Adds intentional empty slots. A negative count is rejected. */
    CtlSectionBuilder empty(int count);

    /** Adds entries at the beginning using the default priority. */
    CtlSectionBuilder addFirst(ItemLike... items);

    /** Adds entries at the beginning using the supplied placement priority. */
    CtlSectionBuilder addFirst(long priority, ItemLike... items);

    /** Adds entries at the end using the default priority. */
    CtlSectionBuilder addLast(ItemLike... items);

    /** Adds entries at the end using the supplied placement priority. */
    CtlSectionBuilder addLast(long priority, ItemLike... items);

    /** Inserts entries before the first occurrence of {@code target}. */
    CtlSectionBuilder addBefore(ItemLike target, ItemLike... items);

    /** Inserts entries before the first occurrence of {@code target} with a placement priority. */
    CtlSectionBuilder addBefore(ItemLike target, long priority, ItemLike... items);

    /** Inserts entries before a one-based occurrence of {@code target}; requests past the end clamp to the last occurrence. */
    CtlSectionBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);

    /** Inserts entries before a one-based occurrence of {@code target} using a placement priority. */
    CtlSectionBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

    /** Inserts entries after the first occurrence of {@code target}. */
    CtlSectionBuilder addAfter(ItemLike target, ItemLike... items);

    /** Inserts entries after the first occurrence of {@code target} with a placement priority. */
    CtlSectionBuilder addAfter(ItemLike target, long priority, ItemLike... items);

    /** Inserts entries after a one-based occurrence of {@code target}; requests past the end clamp to the last occurrence. */
    CtlSectionBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);

    /** Inserts entries after a one-based occurrence of {@code target} using a placement priority. */
    CtlSectionBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);
}