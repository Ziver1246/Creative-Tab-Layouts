package com.ziver.tab_layouts.mixins;

import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeTab.class)
public abstract class CreativeModeTabMixin {

    @Inject(method = "hasAnyItems", at = @At("HEAD"), cancellable = true)
    private void ctl$hasAnyItemsIfControlledByCtl(CallbackInfoReturnable<Boolean> cir) {
        CreativeModeTab self = (CreativeModeTab) (Object) this;
        ResourceLocation tabId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(self);

        if (tabId != null && CtlTabRegistry.hasPages(tabId)) {
            cir.setReturnValue(true);
        }
    }
}