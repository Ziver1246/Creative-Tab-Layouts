package com.ziver.tab_layouts.api.layout;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface CtlPageBuilder extends CtlPageContributionBuilder {

    CtlPageBuilder add(ItemLike... items);

    CtlPageBuilder add(Supplier<? extends ItemLike> item);

    CtlPageBuilder stack(Supplier<ItemStack> stack);

    CtlPageBuilder dynamic(CtlDynamicEntries entries);

    CtlPageBuilder empty();

    CtlPageBuilder empty(int count);

    CtlPageBuilder addFirst(ItemLike... items);

    CtlPageBuilder addFirst(long priority, ItemLike... items);

    CtlPageBuilder addLast(ItemLike... items);

    CtlPageBuilder addLast(long priority, ItemLike... items);

    CtlPageBuilder addBefore(ItemLike target, ItemLike... items);

    CtlPageBuilder addBefore(ItemLike target, long priority, ItemLike... items);

    CtlPageBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);

    CtlPageBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

    CtlPageBuilder addAfter(ItemLike target, ItemLike... items);

    CtlPageBuilder addAfter(ItemLike target, long priority, ItemLike... items);

    CtlPageBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);

    CtlPageBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);

    CtlPageBuilder section(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);

    CtlPageBuilder section(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

    @Override
    CtlPageBuilder addonSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);

    @Override
    CtlPageBuilder addonSection(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

    @Override
    CtlPageBuilder contributeSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
}