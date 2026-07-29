package com.ziver.tab_layouts.mixins;

import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.client.render.CtlSubtabPanel;
import com.ziver.tab_layouts.client.render.DrawerRect;
import com.ziver.tab_layouts.client.screen.CtlConfigScreen;
import com.ziver.tab_layouts.client.screen.CtlPageScreenController;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.CreativeTabsScreenPage;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Unique
    private static final boolean CTL$DEBUG = false;

    @Shadow
    private float scrollOffs;

    @Unique
    private static final ResourceLocation CTL$GEAR_TEXTURE = CreativeTabLayouts.id("textures/gui/config_gear.png");

    @Unique
    private static final int CTL$TEXTURE_SIZE = 28;
    @Unique
    private static final int CTL$ICON_SIZE = 14;
    @Unique
    private static final int CTL$DRAWER_COLLAPSED_WIDTH = 22;
    @Unique
    private static final int CTL$DRAWER_HEIGHT = 24;
    @Unique
    private static final int CTL$DRAWER_OVERLAP = 4;
    @Unique
    private static final int CTL$DRAWER_SLOT_Y = 31;
    @Unique
    private static final int CTL$DRAWER_PADDING = 3;
    @Unique
    private static final int CTL$ACTION_WIDTH = 28;
    @Unique
    private static final int CTL$ACTION_HEIGHT = 18;
    @Unique
    private static final float CTL$DRAWER_SPEED = 3F;

    @Unique
    private static final int CTL$DRAWER_BACKGROUND = 0xF014171C;
    @Unique
    private static final int CTL$DRAWER_HOVER_BACKGROUND = 0xF020252C;
    @Unique
    private static final int CTL$DRAWER_BORDER = 0xFF59616D;
    @Unique
    private static final int CTL$DRAWER_HOVER_BORDER = 0xFF7B8796;
    @Unique
    private static final int CTL$ACTION_BACKGROUND = 0xA020252C;
    @Unique
    private static final int CTL$ACTION_HOVER_BACKGROUND = 0xD0353D48;
    @Unique
    private static final int CTL$TEXT_COLOR = 0xFFF2F2F2;
    @Unique
    private static final int CTL$CORNER_SIZE = 2;

    @Unique
    private float ctl$configDrawerProgress;
    @Unique
    private long ctl$lastConfigDrawerUpdateNanos;

    protected CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Shadow
    private static CreativeModeTab selectedTab;

    @Shadow
    @Final
    private List<CreativeTabsScreenPage> pages;

    @Unique
    private final CtlPageScreenController ctl$pageController = new CtlPageScreenController();

    @Unique
    private final CtlSubtabPanel ctl$subtabPanel = new CtlSubtabPanel();

    @Unique
    private CreativeModeTab ctl$selectedTabBeforeInit;

    @Inject(method = "init", at = @At("HEAD"))
    private void ctl$captureSelectedTab(CallbackInfo ci) {
        this.ctl$selectedTabBeforeInit = selectedTab;
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 0, shift = At.Shift.BEFORE))
    private void ctl$hideRegisteredSubtabs(CallbackInfo ci) {
        if (!Config.ENABLE_SUBTABS.get()) return;
        List<CreativeModeTab> defaultTabs = CreativeModeTabRegistry.getDefaultTabs();

        List<CreativeModeTab> visibleTabs = this.pages.stream()
                .flatMap(page -> page.getVisibleTabs().stream())
                .filter(tab -> !defaultTabs.contains(tab))
                .filter(tab -> !CtlSubtabPanel.isSubtab(tab))
                .distinct()
                .toList();

        this.pages.clear();

        for (int start = 0; start < visibleTabs.size(); start += 10) {
            int end = Math.min(start + 10, visibleTabs.size());
            this.pages.add(new CreativeTabsScreenPage(new ArrayList<>(visibleTabs.subList(start, end))));
        }
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;selectTab(Lnet/minecraft/world/item/CreativeModeTab;)V", ordinal = 0), index = 0)
    private CreativeModeTab ctl$restoreHiddenSelectedTab(CreativeModeTab tab) {
        if (!Config.ENABLE_SUBTABS.get()) return tab;
        if (CtlSubtabPanel.belongsToGroup(this.ctl$selectedTabBeforeInit)) return this.ctl$selectedTabBeforeInit;
        return tab;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void ctl$initControls(CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        ctl$pageController.init(self, selectedTab);
        ctl$subtabPanel.init(selectedTab);
        this.scrollOffs = 0.0F;
        this.ctl$selectedTabBeforeInit = null;
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void ctl$onSelectTab(CreativeModeTab tab, CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if(CTL$DEBUG) ctl$logRegisteredTabs(tab);

        ctl$pageController.applyCurrentPage(self, tab);
        ctl$pageController.updateButtons(self, tab);
        this.scrollOffs = 0.0F;
    }

    @Unique
    private static void ctl$logRegisteredTabs(CreativeModeTab selectedTab) {
        ResourceLocation selectedId = CreativeModeTabRegistry.getName(selectedTab);

        CreativeTabLayouts.LOGGER.info("Selected creative tab: {}", selectedId);

        List<CreativeModeTab> tabs = CreativeModeTabRegistry.getSortedCreativeModeTabs();

        for (int index = 0; index < tabs.size(); index++) {
            CreativeModeTab tab = tabs.get(index);
            ResourceLocation tabId = CreativeModeTabRegistry.getName(tab);

            CreativeTabLayouts.LOGGER.info("Creative tab [{}]: id={}, name=\"{}\", visible={}, items={}", index, tabId, tab.getDisplayName().getString(), tab.shouldDisplay(), tab.getDisplayItems().size());
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void ctl$keyPageNavigation(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (keyCode == GLFW.GLFW_KEY_LEFT && ctl$pageController.previous(self, selectedTab)) {
            cir.setReturnValue(true);
            return;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT && ctl$pageController.next(self, selectedTab)) cir.setReturnValue(true);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ctl$mouseNavigation(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (ctl$subtabPanel.mouseClicked(self, selectedTab, mouseX, mouseY, button)) {
            cir.setReturnValue(true);
            return;
        }

        if (ctl$pageController.mouseClicked(self, selectedTab, mouseX, mouseY, button)) cir.setReturnValue(true);
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void ctl$mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (ctl$subtabPanel.mouseScrolled(self, selectedTab, mouseX, mouseY, scrollY)) cir.setReturnValue(true);
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void ctl$mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (ctl$subtabPanel.mouseDragged(self, selectedTab, mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void ctl$mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (ctl$subtabPanel.mouseReleased(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderLabels", at = @At("HEAD"), cancellable = true)
    private void ctl$renderPageTitle(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        if (ctl$pageController.renderPageTitle(self, graphics, selectedTab)) ci.cancel();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void ctl$renderCtlOverlay(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

        ctl$pageController.renderOverlay(self, graphics, selectedTab, mouseX, mouseY);
        ctl$pageController.renderPageCounter(self, graphics, selectedTab);
        ctl$subtabPanel.render(self, graphics, selectedTab, mouseX, mouseY);
    }

    @Inject(method = "renderBg", at = @At("HEAD"))
    private void ctl$renderStandaloneConfigDrawer(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (!ctl$shouldRenderStandaloneDrawer()) {
            ctl$configDrawerProgress = 0.0F;
            ctl$lastConfigDrawerUpdateNanos = 0L;
            return;
        }

        boolean hovered = ctl$isConfigDrawerHovered(mouseX, mouseY);
        ctl$updateConfigDrawerAnimation(hovered);

        int expandedWidth = ctl$getExpandedDrawerWidth();
        int width = Mth.floor(Mth.lerp(ctl$configDrawerProgress, CTL$DRAWER_COLLAPSED_WIDTH, expandedWidth));

        int x = ctl$getConfigDrawerX();
        int y = ctl$getConfigDrawerY();

        ctl$drawDrawer(graphics, x, y, width, CTL$DRAWER_HEIGHT, hovered ? CTL$DRAWER_HOVER_BACKGROUND : CTL$DRAWER_BACKGROUND, hovered ? CTL$DRAWER_HOVER_BORDER : CTL$DRAWER_BORDER);

        int iconX = x + width - CTL$DRAWER_COLLAPSED_WIDTH + (CTL$DRAWER_COLLAPSED_WIDTH - CTL$ICON_SIZE) / 2;
        int iconY = y + (CTL$DRAWER_HEIGHT - CTL$ICON_SIZE) / 2;

        graphics.blit(CTL$GEAR_TEXTURE, iconX, iconY, CTL$ICON_SIZE, CTL$ICON_SIZE, 0, 0, CTL$TEXTURE_SIZE, CTL$TEXTURE_SIZE, CTL$TEXTURE_SIZE, CTL$TEXTURE_SIZE);

        if (width >= expandedWidth - 1) {
            DrawerRect action = ctl$getConfigActionBounds();
            ctl$renderAction(graphics, action, action.contains(mouseX, mouseY));
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void ctl$renderStandaloneConfigDrawerTooltip(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!ctl$shouldRenderStandaloneDrawer() || ctl$configDrawerProgress < 0.99F) return;
        if (!ctl$getConfigActionBounds().contains(mouseX, mouseY)) return;

        graphics.renderTooltip(this.font, Component.translatable("screen.tab_layouts.config.title"), mouseX, mouseY);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ctl$openStandaloneConfigDrawer(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ctl$shouldRenderStandaloneDrawer() || button != 0 || this.minecraft == null || ctl$configDrawerProgress < 0.99F) return;
        if (!ctl$getConfigActionBounds().contains(mouseX, mouseY)) return;

        Screen self = this;
        this.minecraft.setScreen(new CtlConfigScreen(self));
        cir.setReturnValue(true);
    }

    @Unique
    private boolean ctl$shouldRenderStandaloneDrawer() {
        return Config.SHOW_CREATIVE_CONFIG_BUTTON.get() && !ModList.get().isLoaded(CreativeTabLayouts.CTO_MOD_ID);
    }

    @Unique
    private int ctl$getExpandedDrawerWidth() {
        return CTL$DRAWER_OVERLAP + CTL$DRAWER_PADDING + CTL$ACTION_WIDTH + CTL$DRAWER_PADDING + CTL$DRAWER_COLLAPSED_WIDTH;
    }

    @Unique
    private int ctl$getConfigDrawerX() {
        return this.leftPos + this.imageWidth - CTL$DRAWER_OVERLAP;
    }

    @Unique
    private int ctl$getConfigDrawerY() {
        return this.topPos + CTL$DRAWER_SLOT_Y;
    }

    @Unique
    private int ctl$getCurrentConfigDrawerWidth() {
        return Mth.floor(Mth.lerp(ctl$configDrawerProgress, CTL$DRAWER_COLLAPSED_WIDTH, ctl$getExpandedDrawerWidth()));
    }

    @Unique
    private boolean ctl$isConfigDrawerHovered(double mouseX, double mouseY) {
        int x = ctl$getConfigDrawerX();
        int y = ctl$getConfigDrawerY();
        int currentWidth = ctl$getCurrentConfigDrawerWidth();

        if (ctl$isInside(mouseX, mouseY, x, y, currentWidth, CTL$DRAWER_HEIGHT)) return true;
        if (ctl$configDrawerProgress <= 0.0F) return false;

        return ctl$isInside(mouseX, mouseY, x, y, ctl$getExpandedDrawerWidth(), CTL$DRAWER_HEIGHT);
    }

    @Unique
    private DrawerRect ctl$getConfigActionBounds() {
        int x = ctl$getConfigDrawerX() + CTL$DRAWER_OVERLAP + CTL$DRAWER_PADDING;
        int y = ctl$getConfigDrawerY() + (CTL$DRAWER_HEIGHT - CTL$ACTION_HEIGHT) / 2;
        return new DrawerRect(x, y, CTL$ACTION_WIDTH, CTL$ACTION_HEIGHT);
    }

    @Unique
    private void ctl$updateConfigDrawerAnimation(boolean hovered) {
        long now = System.nanoTime();
        if (ctl$lastConfigDrawerUpdateNanos == 0L) ctl$lastConfigDrawerUpdateNanos = now;

        float deltaSeconds = Math.min(0.05F, (now - ctl$lastConfigDrawerUpdateNanos) / 1_000_000_000.0F);
        ctl$lastConfigDrawerUpdateNanos = now;

        float target = hovered ? 1.0F : 0.0F;
        float step = CTL$DRAWER_SPEED * deltaSeconds;
        ctl$configDrawerProgress = Mth.clamp(ctl$configDrawerProgress + Mth.clamp(target - ctl$configDrawerProgress, -step, step), 0.0F, 1.0F);
    }

    @Unique
    private void ctl$renderAction(GuiGraphics graphics, DrawerRect bounds, boolean hovered) {
        int background = hovered ? CTL$ACTION_HOVER_BACKGROUND : CTL$ACTION_BACKGROUND;
        int border = hovered ? CTL$DRAWER_HOVER_BORDER : CTL$DRAWER_BORDER;

        ctl$drawAction(graphics, bounds.x(), bounds.y(), bounds.width(), bounds.height(), background, border);
        graphics.drawCenteredString(this.font, Component.literal("CTL"), bounds.x() + bounds.width() / 2, bounds.y() + (bounds.height() - this.font.lineHeight) / 2 + 1, CTL$TEXT_COLOR);
    }

    @Unique
    private boolean ctl$isInside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Unique
    private void ctl$drawDrawer(GuiGraphics graphics, int x, int y, int width, int height, int background, int border) {
        int right = x + width;
        int bottom = y + height;

        graphics.fill(x, y + 1, right - 1, bottom - 1, background);
        graphics.fill(x, y, right - CTL$CORNER_SIZE, y + 1, border);
        graphics.fill(x, bottom - 1, right - CTL$CORNER_SIZE, bottom, border);
        graphics.fill(right - 1, y + CTL$CORNER_SIZE, right, bottom - CTL$CORNER_SIZE, border);
        graphics.fill(right - CTL$CORNER_SIZE, y + 1, right - 1, y + CTL$CORNER_SIZE, border);
        graphics.fill(right - CTL$CORNER_SIZE, bottom - CTL$CORNER_SIZE, right - 1, bottom - 1, border);
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