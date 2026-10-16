package com.ziver.tab_layouts.internal.mixins.client;

import com.ziver.tab_layouts.CtlConfig;
import com.ziver.tab_layouts.internal.client.screen.CtlConfigScreen;
import com.ziver.tab_layouts.internal.client.util.DrawerRect;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractContainerScreen<InventoryMenu> {
    @Unique
    private static final int CTL$ACTION_WIDTH = 28;
    @Unique
    private static final int CTL$ACTION_HEIGHT = 18;
    @Unique
    private static final int CTL$CENTER_GAP = 5;

    @Unique
    private static final int CTL$OFFSET_X = 0;
    @Unique
    private static final int CTL$OFFSET_Y = 0;

    @Unique
    private static final int CTL$ACTION_BACKGROUND = 0xA020252C;
    @Unique
    private static final int CTL$ACTION_HOVER_BACKGROUND = 0xD0353D48;
    @Unique
    private static final int CTL$ACTION_BORDER = 0xFF59616D;
    @Unique
    private static final int CTL$ACTION_HOVER_BORDER = 0xFF7B8796;
    @Unique
    private static final int CTL$TEXT_COLOR = 0xFFF2F2F2;

    protected InventoryScreenMixin(InventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void ctl$renderConfigButton(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!CtlConfig.SHOW_SURVIVAL_CONFIG_BUTTON.get()) return;

        DrawerRect bounds = ctl$getActionBounds();
        boolean hovered = bounds.contains(mouseX, mouseY);

        ctl$renderAction(graphics, bounds, hovered);

        if (hovered) graphics.renderTooltip(this.font, Component.translatable("screen.tab_layouts.config.title"), mouseX, mouseY);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ctl$openConfig(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != 0 || this.minecraft == null || !CtlConfig.SHOW_SURVIVAL_CONFIG_BUTTON.get()) return;
        if (!ctl$getActionBounds().contains(mouseX, mouseY)) return;

        Screen self = this;
        this.minecraft.setScreen(new CtlConfigScreen(self));
        cir.setReturnValue(true);
    }

    @Unique
    private DrawerRect ctl$getActionBounds() {
        int centerX = this.leftPos + this.imageWidth / 2;
        int x = centerX + CTL$CENTER_GAP + CTL$OFFSET_X;
        int y = this.topPos - CTL$ACTION_HEIGHT - 5 + CTL$OFFSET_Y;

        return new DrawerRect(x, y, CTL$ACTION_WIDTH, CTL$ACTION_HEIGHT);
    }

    @Unique
    private void ctl$renderAction(GuiGraphics graphics, DrawerRect bounds, boolean hovered) {
        int background = hovered ? CTL$ACTION_HOVER_BACKGROUND : CTL$ACTION_BACKGROUND;
        int border = hovered ? CTL$ACTION_HOVER_BORDER : CTL$ACTION_BORDER;

        ctl$drawAction(graphics, bounds.x(), bounds.y(), bounds.width(), bounds.height(), background, border);
        graphics.drawCenteredString(this.font, Component.literal("CTL"), bounds.x() + bounds.width() / 2, bounds.y() + (bounds.height() - this.font.lineHeight) / 2 + 1, CTL$TEXT_COLOR);
    }

    @Unique
    private void ctl$drawAction(GuiGraphics graphics, int x, int y, int width, int height, int background, int border) {
        int right = x + width;
        int bottom = y + height;

        graphics.fill(x + 1, y + 1, right - 1, bottom - 1, background);
        graphics.fill(x + 2, y, right - 2, y + 1, border);
        graphics.fill(x + 2, bottom - 1, right - 2, bottom, border);
        graphics.fill(x, y + 2, x + 1, bottom - 2, border);
        graphics.fill(right - 1, y + 2, right, bottom - 2, border);
        graphics.fill(x + 1, y + 1, x + 2, y + 2, border);
        graphics.fill(right - 2, y + 1, right - 1, y + 2, border);
        graphics.fill(x + 1, bottom - 2, x + 2, bottom - 1, border);
        graphics.fill(right - 2, bottom - 2, right - 1, bottom - 1, border);
    }
}