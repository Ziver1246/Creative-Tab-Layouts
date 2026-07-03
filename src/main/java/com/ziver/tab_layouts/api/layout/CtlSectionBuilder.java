package com.ziver.tab_layouts.api.layout;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public interface CtlSectionBuilder {

    CtlSectionBuilder add(ItemLike... items);

    CtlSectionBuilder add(Supplier<? extends ItemLike> item);

    CtlSectionBuilder stack(Supplier<ItemStack> stack);

    CtlSectionBuilder dynamic(CtlDynamicEntries entries);

    CtlSectionBuilder empty();

    CtlSectionBuilder empty(int count);

    CtlSectionBuilder addFirst(ItemLike... items);

    CtlSectionBuilder addFirst(long priority, ItemLike... items);

    CtlSectionBuilder addLast(ItemLike... items);

    CtlSectionBuilder addLast(long priority, ItemLike... items);

    CtlSectionBuilder addBefore(ItemLike target, ItemLike... items);

    CtlSectionBuilder addBefore(ItemLike target, long priority, ItemLike... items);

    CtlSectionBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);

    CtlSectionBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

    CtlSectionBuilder addAfter(ItemLike target, ItemLike... items);

    CtlSectionBuilder addAfter(ItemLike target, long priority, ItemLike... items);

    CtlSectionBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);

    CtlSectionBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);
}