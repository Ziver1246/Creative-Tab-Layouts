package com.ziver.tab_layouts.client.render;

import com.ziver.tab_layouts.client.util.CtlUiLayouts;
import com.ziver.tab_layouts.internal.layout.CtlPageState;
import com.ziver.tab_layouts.internal.layout.CtlRenderedSection;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import com.ziver.tab_layouts.internal.layout.CtlTabLayout;
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
        List<CtlRenderedSection> sections = CtlPageRenderState.get(tabId, pageIndex);
        if (sections.isEmpty()) return;

        int baseX = left + CtlUiLayouts.HEADER_BASE_X_OFFSET;
        int baseY = top + CtlUiLayouts.HEADER_BASE_Y_OFFSET;

        for (CtlRenderedSection rendered : sections) {
            int visibleRow = rendered.row() - CtlPageRenderState.currentRow;

            if (visibleRow < 0 || visibleRow >= CtlUiLayouts.GRID_VISIBLE_ROWS) continue;

            int y = baseY + visibleRow * CtlUiLayouts.HEADER_ROW_HEIGHT;
            boolean hovering = mouseX >= baseX && mouseX < baseX + CtlUiLayouts.HEADER_ROW_WIDTH && mouseY >= y && mouseY < y + CtlUiLayouts.HEADER_ROW_HEIGHT;

            boolean visualRendered = CtlVisualRenderer.renderHeader(
                    graphics,
                    Minecraft.getInstance().font,
                    tabId,
                    pageIndex,
                    rendered.section(),
                    baseX,
                    y,
                    CtlUiLayouts.HEADER_ROW_WIDTH,
                    CtlUiLayouts.HEADER_ROW_HEIGHT,
                    hovering,
                    mouseX,
                    mouseY
            );

            if (!visualRendered) renderFallbackHeader(graphics, baseX, y, rendered);

        }
    }

    public static void renderBannerFrame(GuiGraphics graphics, int left, int top) {
        int x = left + CtlUiLayouts.BANNER_X_OFFSET;
        int y = top + CtlUiLayouts.BANNER_Y_OFFSET;

        graphics.blit(
                CtlUiLayouts.BANNER_FRAME_TEXTURE,
                x,
                y,
                0.0F,
                0.0F,
                CtlUiLayouts.BANNER_WIDTH,
                CtlUiLayouts.BANNER_HEIGHT,
                CtlUiLayouts.BANNER_WIDTH,
                CtlUiLayouts.BANNER_HEIGHT
        );
    }

    private static void renderFallbackHeader(GuiGraphics graphics, int x, int y, CtlRenderedSection rendered) {
        graphics.blit(
                CtlUiLayouts.HEADER_FRAME_TEXTURE,
                x,
                y,
                0.0F,
                0.0F,
                CtlUiLayouts.HEADER_ROW_WIDTH,
                CtlUiLayouts.HEADER_ROW_HEIGHT,
                CtlUiLayouts.HEADER_ROW_WIDTH,
                CtlUiLayouts.HEADER_ROW_HEIGHT
        );

        graphics.drawString(
                Minecraft.getInstance().font,
                rendered.section().title(),
                x + CtlUiLayouts.HEADER_TEXT_X_PADDING,
                y + CtlUiLayouts.HEADER_TEXT_Y_OFFSET,
                0xFFFFFFFF,
                false
        );
    }
}