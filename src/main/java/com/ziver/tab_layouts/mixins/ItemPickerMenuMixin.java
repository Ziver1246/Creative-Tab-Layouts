package com.ziver.tab_layouts.mixins;

import com.ziver.tab_layouts.client.render.CtlPageRenderState;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.ItemPickerMenu.class)
public abstract class ItemPickerMenuMixin {

    @Shadow
    protected abstract int getRowIndexForScroll(float scrollOffs);

    @Inject(method = "scrollTo", at = @At("HEAD"))
    private void ctl$trackCurrentRow(float scrollOffs, CallbackInfo ci) {
        CtlPageRenderState.currentRow = this.getRowIndexForScroll(scrollOffs);
    }
}