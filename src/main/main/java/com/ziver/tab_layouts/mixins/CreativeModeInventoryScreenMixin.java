package com.ziver.tab_layouts.mixins;

import com.ziver.tab_layouts.client.screen.CtlPageScreenController;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {

    @Shadow
    private float scrollOffs;

    @Shadow
    private static CreativeModeTab selectedTab;

    @Unique
    private final CtlPageScreenController ctl$pageController = new CtlPageScreenController();

    @Inject(method = "init", at = @At("TAIL"))
    private void ctl$initPageControls(CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        ctl$pageController.init(self, selectedTab);
        this.scrollOffs = 0.0F;
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void ctl$onSelectTab(CreativeModeTab tab, CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        ctl$pageController.applyCurrentPage(self, tab);
        ctl$pageController.updateButtons(self, tab);
        this.scrollOffs = 0.0F;
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void ctl$keyPageNavigation(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (keyCode == GLFW.GLFW_KEY_LEFT && ctl$pageController.previous(self, selectedTab)) {
            cir.setReturnValue(true);
            return;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT && ctl$pageController.next(self, selectedTab)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ctl$mousePageNavigation(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (ctl$pageController.mouseClicked(self, selectedTab, mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderLabels", at = @At("HEAD"), cancellable = true)
    private void ctl$renderPageTitle(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (ctl$pageController.renderPageTitle(self, graphics, selectedTab)) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void ctl$renderCtlOverlay(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        ctl$pageController.renderOverlay(self, graphics, selectedTab, mouseX, mouseY);
        ctl$pageController.renderPageCounter(self, graphics, selectedTab);
    }
}