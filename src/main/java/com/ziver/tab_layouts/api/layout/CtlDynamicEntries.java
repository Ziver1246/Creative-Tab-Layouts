package com.ziver.tab_layouts.api.layout;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Produces item stacks dynamically when a CTL layout is resolved.
 *
 * <p>The registry provider is supplied at resolution time so integrations can build
 * registry-dependent stacks without resolving them during plugin registration.</p>
 */
@FunctionalInterface
public interface CtlDynamicEntries {
    /**
     * Builds the stacks represented by this dynamic entry.
     *
     * @param registries current registry provider
     * @return stacks to insert into the layout
     */
    List<ItemStack> build(HolderLookup.Provider registries);
}