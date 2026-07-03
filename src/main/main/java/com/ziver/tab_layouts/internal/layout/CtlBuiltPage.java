package com.ziver.tab_layouts.internal.layout;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CtlBuiltPage(List<ItemStack> items, List<CtlRenderedSection> sections, boolean overview) {}