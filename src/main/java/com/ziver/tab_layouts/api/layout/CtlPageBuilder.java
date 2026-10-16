package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Builds the ordered content of a CTL page.
 *
 * <p>Normal entries preserve registration order. Placement methods can insert entries
 * at the beginning, end, or relative to an occurrence of another item.</p>
 */
public interface CtlPageBuilder extends CtlPageContributionBuilder {

    /** Adds one or more normal item entries. */
    CtlPageBuilder add(ItemLike... items);

    /** Adds a lazily supplied normal item entry. */
    CtlPageBuilder add(Supplier<? extends ItemLike> item);

    /** Adds a lazily supplied item stack. */
    CtlPageBuilder stack(Supplier<ItemStack> stack);

    /** Adds registry-aware dynamic entries resolved when the layout is built. */
    CtlPageBuilder dynamic(CtlDynamicEntries entries);

    /** Adds one intentional empty slot. */
    CtlPageBuilder empty();

    /** Adds intentional empty slots. A negative count is rejected. */
    CtlPageBuilder empty(int count);

    /** Adds entries at the beginning using the default priority. */
    CtlPageBuilder addFirst(ItemLike... items);

    /** Adds entries at the beginning using the supplied placement priority. */
    CtlPageBuilder addFirst(long priority, ItemLike... items);

    /** Adds entries at the end using the default priority. */
    CtlPageBuilder addLast(ItemLike... items);

    /** Adds entries at the end using the supplied placement priority. */
    CtlPageBuilder addLast(long priority, ItemLike... items);

    /** Inserts entries before the first occurrence of {@code target}. */
    CtlPageBuilder addBefore(ItemLike target, ItemLike... items);

    /** Inserts entries before the first occurrence of {@code target} with a placement priority. */
    CtlPageBuilder addBefore(ItemLike target, long priority, ItemLike... items);

    /** Inserts entries before a one-based occurrence of {@code target}; requests past the end clamp to the last occurrence. */
    CtlPageBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);

    /** Inserts entries before a one-based occurrence of {@code target} using a placement priority. */
    CtlPageBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

    /** Inserts entries after the first occurrence of {@code target}. */
    CtlPageBuilder addAfter(ItemLike target, ItemLike... items);

    /** Inserts entries after the first occurrence of {@code target} with a placement priority. */
    CtlPageBuilder addAfter(ItemLike target, long priority, ItemLike... items);

    /** Inserts entries after a one-based occurrence of {@code target}; requests past the end clamp to the last occurrence. */
    CtlPageBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);

    /** Inserts entries after a one-based occurrence of {@code target} using a placement priority. */
    CtlPageBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);

    /** Adds a base section using the default priority. */
    CtlPageBuilder section(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);

    /** Adds a base section; lower priority values are ordered first. */
    CtlPageBuilder section(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

    @Override
    CtlPageBuilder addonSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);

    @Override
    CtlPageBuilder addonSection(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

    @Override
    CtlPageBuilder contributeSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}