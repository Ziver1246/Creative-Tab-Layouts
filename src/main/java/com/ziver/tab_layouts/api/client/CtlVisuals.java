package com.ziver.tab_layouts.api.client;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.internal.client.render.CtlVisualRenderer;
import com.ziver.tab_layouts.internal.client.util.CtlUiLayouts;
import com.ziver.tab_layouts.internal.layout.CtlPage;
import com.ziver.tab_layouts.internal.layout.CtlPageType;
import com.ziver.tab_layouts.internal.layout.CtlSection;
import com.ziver.tab_layouts.internal.layout.CtlTabLayout;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import com.ziver.tab_layouts.internal.registry.CtlVisualRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class CtlVisuals {
    private CtlVisuals() {}

    private static final ResourceLocation DEFAULT_ANIMATION_CONTEXT = CreativeTabLayouts.id("external");

    public static CtlVisualSize getPreferredHeaderSize() {
        return new CtlVisualSize(CtlUiLayouts.HEADER_ROW_WIDTH, CtlUiLayouts.HEADER_ROW_HEIGHT);
    }

    public static CtlVisualSize getPreferredBannerSize() {
        return new CtlVisualSize(CtlUiLayouts.BANNER_WIDTH, CtlUiLayouts.BANNER_HEIGHT);
    }

    public static boolean hasHeaderVisual(ResourceLocation sectionId) {
        Objects.requireNonNull(sectionId, "sectionId");
        return CtlVisualRegistry.hasHeader(sectionId);
    }

    public static boolean hasBannerVisual(ResourceLocation pageId) {
        Objects.requireNonNull(pageId, "pageId");
        return CtlVisualRegistry.hasBanner(pageId);
    }

    public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
        return renderSectionHeader(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, true);
    }

    public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
        return renderSectionHeader(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
    }

    public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
        return renderSectionHeader(graphics, animationContext, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, true);
    }

    public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
        Objects.requireNonNull(graphics, "graphics");
        Objects.requireNonNull(animationContext, "animationContext");
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(sectionId, "sectionId");

        CtlPage page = findPage(tabId, pageIndex);
        if (page == null || page.type() == CtlPageType.OVERVIEW) return false;

        CtlSection section = page.section(sectionId);
        if (section == null) return false;

        return CtlVisualRenderer.renderHeader(graphics, Minecraft.getInstance().font, animationContext, tabId, pageIndex, section, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
    }

    public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
        return renderPageBanner(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, true);
    }

    public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
        return renderPageBanner(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
    }

    public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
        return renderPageBanner(graphics, animationContext, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, true);
    }

    public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
        Objects.requireNonNull(graphics, "graphics");
        Objects.requireNonNull(animationContext, "animationContext");
        Objects.requireNonNull(tabId, "tabId");

        CtlPage page = findPage(tabId, pageIndex);
        if (page == null || page.type() != CtlPageType.OVERVIEW) return false;

        return CtlVisualRenderer.renderBanner(graphics, animationContext, tabId, pageIndex, page, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
    }

    @Nullable
    private static CtlPage findPage(ResourceLocation tabId, int pageIndex) {
        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || pageIndex < 0 || pageIndex >= layout.pageCount()) return null;

        return layout.orderedPages().get(pageIndex);
    }
}