package com.ziver.tab_layouts.client.render;

import com.ziver.tab_layouts.client.render.animation.CtlAnimationStateRegistry;
import com.ziver.tab_layouts.client.util.CtlUiLayouts;
import com.ziver.tab_layouts.internal.layout.*;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.List;

public final class CtlSimpleFrameRenderer {
    private CtlSimpleFrameRenderer() {}

    public static void renderHeaders(GuiGraphics graphics, int left, int top, CreativeModeTab tab, int mouseX, int mouseY) {
        ResourceLocation tabId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (tabId == null) return;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null) return;

        int pageIndex = CtlPageState.page(tabId);
        CtlPage page = layout.page(pageIndex);
        List<CtlRenderedSection> sections = CtlPageRenderState.get(tabId, pageIndex);

        if (sections.isEmpty()) return;

        int baseX = left + CtlUiLayouts.HEADER_BASE_X_OFFSET;
        int baseY = top + CtlUiLayouts.HEADER_BASE_Y_OFFSET;
        boolean collapsible = page != null && page.sections().size() > 1;

        for (CtlRenderedSection rendered : sections) {
            int visibleRow = rendered.row() - CtlPageRenderState.currentRow;

            if (visibleRow < 0 || visibleRow >= CtlUiLayouts.GRID_VISIBLE_ROWS) continue;

            int y = baseY + visibleRow * CtlUiLayouts.HEADER_ROW_HEIGHT;
            boolean hovering = mouseX >= baseX && mouseX < baseX + CtlUiLayouts.HEADER_ROW_WIDTH && mouseY >= y && mouseY < y + CtlUiLayouts.HEADER_ROW_HEIGHT;

            boolean visualRendered = CtlVisualRenderer.renderHeader(graphics, Minecraft.getInstance().font, CtlAnimationStateRegistry.CREATIVE_CONTEXT, tabId, pageIndex, rendered.section(), baseX, y, CtlUiLayouts.HEADER_ROW_WIDTH, CtlUiLayouts.HEADER_ROW_HEIGHT, hovering, mouseX, mouseY, true);
            if (visualRendered) continue;

            renderFallbackHeader(graphics, baseX, y, rendered);

            if (hovering && collapsible)
                renderCollapseIndicator(graphics, baseX, y, CtlUiLayouts.HEADER_ROW_WIDTH, CtlUiLayouts.HEADER_ROW_HEIGHT, CtlSectionState.isCollapsed(tabId, page.id(), rendered.section().id()));
        }
    }

    private static void renderCollapseIndicator(GuiGraphics graphics, int x, int y, int width, int height, boolean collapsed) {
        int centerX = x + width - 7;
        int centerY = y + height / 2;

        if (!collapsed) {
            graphics.fill(centerX - 2, centerY, centerX + 3, centerY + 1, 0xFFFFFFFF);
            return;
        }

        graphics.fill(centerX - 2, centerY - 2, centerX + 3, centerY - 1, 0xFFFFFFFF);
        graphics.fill(centerX - 1, centerY - 1, centerX + 2, centerY, 0xFFFFFFFF);
        graphics.fill(centerX, centerY, centerX + 1, centerY + 1, 0xFFFFFFFF);
    }

    public static void renderBannerFrame(GuiGraphics graphics, int left, int top) {
        int x = left + CtlUiLayouts.BANNER_X_OFFSET;
        int y = top + CtlUiLayouts.BANNER_Y_OFFSET;

        graphics.blit(CtlUiLayouts.BANNER_FRAME_TEXTURE, x, y, 0.0F, 0.0F, CtlUiLayouts.BANNER_WIDTH, CtlUiLayouts.BANNER_HEIGHT, CtlUiLayouts.BANNER_WIDTH, CtlUiLayouts.BANNER_HEIGHT);
    }

    private static void renderFallbackHeader(GuiGraphics graphics, int x, int y, CtlRenderedSection rendered) {
        graphics.blit(CtlUiLayouts.HEADER_FRAME_TEXTURE, x, y, 0.0F, 0.0F, CtlUiLayouts.HEADER_ROW_WIDTH, CtlUiLayouts.HEADER_ROW_HEIGHT, CtlUiLayouts.HEADER_ROW_WIDTH, CtlUiLayouts.HEADER_ROW_HEIGHT);
        graphics.drawString(Minecraft.getInstance().font, rendered.section().title(), x + CtlUiLayouts.HEADER_TEXT_X_PADDING, y + CtlUiLayouts.HEADER_TEXT_Y_OFFSET, 0xFFFFFFFF, false);
    }
}