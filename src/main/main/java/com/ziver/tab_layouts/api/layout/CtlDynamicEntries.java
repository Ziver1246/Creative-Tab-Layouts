package com.ziver.tab_layouts.api.layout;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@FunctionalInterface
public interface CtlDynamicEntries {
    List<ItemStack> build(HolderLookup.Provider registries);
}