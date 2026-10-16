package com.ziver.tab_layouts.internal.mixins.client;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.CtlConfig;
import com.ziver.tab_layouts.internal.client.render.CtlSubtabPanel;
import com.ziver.tab_layouts.internal.client.screen.CtlConfigScreen;
import com.ziver.tab_layouts.internal.client.screen.CtlPageScreenController;
import com.ziver.tab_layouts.internal.client.util.DrawerRect;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
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
    private static final int CTL$ACTION_WIDTH = 28;
    @Unique
    private static final int CTL$ACTION_HEIGHT = 18;
    @Unique
    private static final int CTL$PAGE_GAP = 5;
    @Unique
    private static final int CTL$OFFSET_X = 10;
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

    protected CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Shadow
    private static CreativeModeTab selectedTab;

    @Shadow
    @Final
    private List<CreativeTabsScreenPage> pages;

    @Shadow
    private CreativeTabsScreenPage currentPage;

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
        if (!CtlConfig.ENABLE_SUBTABS.get()) return;

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
        if (!CtlConfig.ENABLE_SUBTABS.get()) return tab;
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

    @Inject(method = "render", at = @At("TAIL"))
    private void ctl$renderStandaloneConfigButton(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!ctl$shouldRenderStandaloneButton()) return;

        DrawerRect bounds = ctl$getConfigActionBounds();
        boolean hovered = bounds.contains(mouseX, mouseY);

        ctl$renderAction(graphics, bounds, hovered);

        if (hovered) graphics.renderTooltip(this.font, Component.translatable("screen.tab_layouts.config.title"), mouseX, mouseY);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ctl$openStandaloneConfigButton(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ctl$shouldRenderStandaloneButton() || button != 0 || this.minecraft == null) return;
        if (!ctl$getConfigActionBounds().contains(mouseX, mouseY)) return;

        Screen self = this;
        this.minecraft.setScreen(new CtlConfigScreen(self));
        cir.setReturnValue(true);
    }

    @Unique
    private boolean ctl$shouldRenderStandaloneButton() {
        return CtlConfig.SHOW_CREATIVE_CONFIG_BUTTON.get();
    }

    @Unique
    private DrawerRect ctl$getConfigActionBounds() {
        int pageCenter = this.leftPos + this.imageWidth / 2;
        int pageWidth = 0;

        if (this.pages.size() > 1) {
            Component page = Component.literal(String.format("%d / %d", this.pages.indexOf(this.currentPage) + 1, this.pages.size()));
            pageWidth = this.font.width(page);
        }

        int pageRight = pageCenter + pageWidth / 2;
        int x = pageRight + CTL$PAGE_GAP + CTL$OFFSET_X;
        int y = this.topPos - 49 + CTL$OFFSET_Y;

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