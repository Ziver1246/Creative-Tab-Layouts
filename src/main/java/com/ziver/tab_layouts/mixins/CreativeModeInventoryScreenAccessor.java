package com.ziver.tab_layouts.mixins;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreativeModeInventoryScreen.class)
public interface CreativeModeInventoryScreenAccessor {

    @Invoker("selectTab")
    void ctl$selectTab(CreativeModeTab tab);

    @Accessor("scrollOffs")
    float ctl$getScrollOffs();

    @Accessor("scrollOffs")
    void ctl$setScrollOffs(float scrollOffs);
}