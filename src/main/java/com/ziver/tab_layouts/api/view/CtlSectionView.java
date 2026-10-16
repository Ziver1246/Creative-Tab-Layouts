package com.ziver.tab_layouts.api.view;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;

public record CtlSectionView(ResourceLocation id, Component title, Type type, List<ItemStack> items, boolean customHeader) {

    public CtlSectionView {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(type, "type");
        items = items.stream().map(ItemStack::copy).toList();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public enum Type {
        BASE,
        ADDON,
        FALLBACK
    }
}