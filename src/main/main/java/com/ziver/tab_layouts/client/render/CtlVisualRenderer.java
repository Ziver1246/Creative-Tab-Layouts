package com.ziver.tab_layouts.client.render;

import com.mojang.blaze3d.platform.InputConstants;
import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.client.render.animation.CtlAnimationState;
import com.ziver.tab_layouts.client.render.animation.CtlAnimationStateRegistry;
import com.ziver.tab_layouts.client.util.CtlUiLayouts;
import com.ziver.tab_layouts.internal.layout.CtlPage;
import com.ziver.tab_layouts.internal.layout.CtlSection;
import com.ziver.tab_layouts.internal.registry.CtlVisualDebugRegistry;
import com.ziver.tab_layouts.internal.registry.CtlVisualRegistry;
import com.ziver.tab_layouts.internal.visual.*;
import com.ziver.tab_layouts.internal.visual.debug.CtlVisualDebugInfo;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;

public final class CtlVisualRenderer {
    private CtlVisualRenderer() {}

    public static boolean renderHeader(GuiGraphics graphics, Font font, ResourceLocation tabId, int pageIndex, CtlSection section, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
        CtlHeaderVisual visual = CtlVisualRegistry.header(section.id());

        if (visual == null) {
            renderHeaderDebugTooltip(graphics, font, section.id(), hovered, mouseX, mouseY);

            if (CtlVisualDebugRegistry.header(section.id()) == null) {
                renderMissingHeaderDebugTooltip(graphics, font, section.id(), hovered, mouseX, mouseY);
            }

            return false;
        }

        blitAnimatedHeader(graphics, tabId, pageIndex, section.id(), visual.texture(), visual.animation(), x, y, width, height, hovered);

        if (!visual.hideText()) {
            if (visual.labelColor() != 0) {
                drawDynamicLabelBackground(graphics, font, section.title(), x, y, width, height, visual);
            }

            renderHeaderText(graphics, font, section.title(), x, y, width, height, visual);
        }

        renderHeaderDebugTooltip(graphics, font, section.id(), hovered, mouseX, mouseY);

        return true;
    }

    public static boolean renderBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, CtlPage page, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
        CtlBannerVisual visual = CtlVisualRegistry.banner(page.id());

        if (visual == null) {
            renderBannerDebugTooltip(graphics, page.id(), hovered, mouseX, mouseY);

            if (CtlVisualDebugRegistry.banner(page.id()) == null) {
                renderMissingBannerDebugTooltip(graphics, page.id(), hovered, mouseX, mouseY);
            }

            return false;
        }

        blitAnimatedBanner(graphics, tabId, pageIndex, page.id(), visual.texture(), visual.animation(), x, y, width, height, hovered);
        renderBannerDebugTooltip(graphics, page.id(), hovered, mouseX, mouseY);

        return true;
    }

    private static void drawDynamicLabelBackground(GuiGraphics graphics, Font font, Component title, int x, int y, int width, int height, CtlHeaderVisual visual) {
        int textWidth = font.width(title);

        int labelWidth = textWidth + CtlUiLayouts.HEADER_LABEL_RIGHT_PADDING;
        int labelX1 = labelX(x, width, labelWidth, visual.textAlign());
        int labelY1 = y + CtlUiLayouts.HEADER_LABEL_Y_OFFSET;
        int labelX2 = labelX1 + labelWidth;
        int labelY2 = y + height - CtlUiLayouts.HEADER_LABEL_BOTTOM_INSET;

        graphics.fill(labelX1, labelY1, labelX2, labelY2, visual.labelColor());
    }

    private static void renderHeaderText(GuiGraphics graphics, Font font, Component title, int x, int y, int width, int height, CtlHeaderVisual visual) {
        int textWidth = font.width(title);
        int textX = textX(x, width, textWidth, visual.textAlign());
        int textY = y + CtlUiLayouts.HEADER_TEXT_Y_OFFSET;

        if (visual.splitTextColor()) {
            drawSplitText(graphics, font, title, textX, textY, textWidth, font.lineHeight, visual.textTopColor(), visual.textBottomColor(), visual.textShadow());
            return;
        }

        graphics.drawString(font, title, textX, textY, visual.textColor(), visual.textShadow());
    }

    private static int textX(int x, int width, int textWidth, CtlTextAlign align) {
        return switch (align) {
            case LEFT -> x + CtlUiLayouts.HEADER_TEXT_X_PADDING;
            case CENTER -> x + width / 2 - textWidth / 2;
            case RIGHT -> x + width - CtlUiLayouts.HEADER_TEXT_X_PADDING - textWidth;
        };
    }

    private static void blitAnimatedHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, ResourceLocation texture, CtlSpriteAnimation animation, int x, int y, int width, int height, boolean hovered) {
        long nowMillis = Util.getMillis();
        CtlAnimationState state = CtlAnimationStateRegistry.header(tabId, pageIndex, sectionId);
        blitAnimated(graphics, state, nowMillis, texture, animation, x, y, width, height, hovered);
    }

    private static void blitAnimatedBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation pageId, ResourceLocation texture, CtlSpriteAnimation animation, int x, int y, int width, int height, boolean hovered) {
        long nowMillis = Util.getMillis();
        CtlAnimationState state = CtlAnimationStateRegistry.banner(tabId, pageIndex, pageId);
        blitAnimated(graphics, state, nowMillis, texture, animation, x, y, width, height, hovered);
    }

    private static void blitAnimated(GuiGraphics graphics, CtlAnimationState state, long nowMillis, ResourceLocation texture, CtlSpriteAnimation animation, int x, int y, int width, int height, boolean hovered) {
        if (animation.animateJustOnHover()) {
            state.setPaused(!hovered, nowMillis);
        } else {
            state.setPaused(false, nowMillis);
        }

        int frame = state.getFrame(nowMillis, animation.frames(), animation.fps());

        int columns = animation.columnsForLayout();
        int rows = animation.rows();

        int column = column(animation, frame);
        int row = row(animation, frame);

        int textureWidth = width * columns;
        int textureHeight = height * rows;

        int u = width * column;
        int v = height * row;

        graphics.blit(texture, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    private static int column(CtlSpriteAnimation animation, int frame) {
        CtlSpriteLayout layout = animation.layout();

        return switch (layout) {
            case VERTICAL -> 0;
            case HORIZONTAL -> frame;
            case GRID -> frame % animation.safeColumns();
        };
    }

    private static int row(CtlSpriteAnimation animation, int frame) {
        CtlSpriteLayout layout = animation.layout();

        return switch (layout) {
            case VERTICAL -> frame;
            case HORIZONTAL -> 0;
            case GRID -> frame / animation.safeColumns();
        };
    }

    private static void renderMissingHeaderDebugTooltip(GuiGraphics graphics, Font font, ResourceLocation sectionId, boolean hovered, int mouseX, int mouseY) {
        if (!shouldRenderDebugTooltip(hovered)) return;
        renderMissingDebugTooltip(graphics, font, "CTL Header Visual", sectionId, "assets/" + sectionId.getNamespace() + "/ctl/headers/" + sectionId.getPath() + ".json", mouseX, mouseY);
    }

    private static void renderMissingBannerDebugTooltip(GuiGraphics graphics, ResourceLocation pageId, boolean hovered, int mouseX, int mouseY) {
        if (!shouldRenderDebugTooltip(hovered)) return;
        renderMissingDebugTooltip(graphics, Minecraft.getInstance().font, "CTL Banner Visual", pageId, "assets/" + pageId.getNamespace() + "/ctl/banners/" + pageId.getPath() + ".json", mouseX, mouseY);
    }

    private static void renderMissingDebugTooltip(GuiGraphics graphics, Font font, String type, ResourceLocation id, String virtualPath, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();

        String source = type + "\n" + "Id: " + id + "\n" + "File: " + virtualPath;

        CtlJsonDebugTooltipRenderer.render(graphics, font, mouseX, mouseY, minecraft.getWindow().getGuiScaledWidth(),
                minecraft.getWindow().getGuiScaledHeight(), source, null, Component.translatable("screen.tab_layouts.missing_json_config"));
    }

    private static void renderHeaderDebugTooltip(GuiGraphics graphics, Font font, ResourceLocation sectionId, boolean hovered, int mouseX, int mouseY) {
        if (!shouldRenderDebugTooltip(hovered)) return;
        CtlVisualDebugInfo info = CtlVisualDebugRegistry.header(sectionId);
        if (info == null) return;

        renderDebugTooltip(graphics, font, info, mouseX, mouseY);
    }

    private static void renderBannerDebugTooltip(GuiGraphics graphics, ResourceLocation pageId, boolean hovered, int mouseX, int mouseY) {
        if (!shouldRenderDebugTooltip(hovered)) return;
        CtlVisualDebugInfo info = CtlVisualDebugRegistry.banner(pageId);
        if (info == null) return;

        renderDebugTooltip(graphics, Minecraft.getInstance().font, info, mouseX, mouseY);
    }

    private static boolean shouldRenderDebugTooltip(boolean hovered) {
        return hovered && isDevActive() && leftAltDown() && Config.ENABLE_DEVELOPER_VISUAL_DEBUG.get();
    }

    private static boolean leftAltDown() {
        Minecraft minecraft = Minecraft.getInstance();
        long window = minecraft.getWindow().getWindow();

        return InputConstants.isKeyDown(window, InputConstants.KEY_LALT);
    }

    private static void renderDebugTooltip(GuiGraphics graphics, Font font, CtlVisualDebugInfo info, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();

        String source = info.type() + "\n" + "ID: " + info.id() + "\n" + "File: " + info.virtualPath();
        CtlJsonDebugTooltipRenderer.render(graphics, font, mouseX, mouseY, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight(), source, info.prettyJson(), info.error());
    }

    private static void drawSplitText(GuiGraphics graphics, Font font, Component title, int x, int y, int textWidth, int textHeight, int topColor, int bottomColor, boolean shadow) {
        int splitY = y + (textHeight + 1) / 2;

        graphics.enableScissor(x, y, x + textWidth, splitY);
        graphics.drawString(font, title, x, y, topColor, shadow);
        graphics.disableScissor();
        graphics.enableScissor(x, splitY, x + textWidth, y + textHeight);
        graphics.drawString(font, title, x, y, bottomColor, shadow);
        graphics.disableScissor();
    }


    private static int labelX(int x, int width, int labelWidth, CtlTextAlign align) {
        return switch (align) {
            case LEFT -> x + CtlUiLayouts.HEADER_LABEL_X_OFFSET;
            case CENTER -> x + width / 2 - labelWidth / 2;
            case RIGHT -> x + width - CtlUiLayouts.HEADER_LABEL_X_OFFSET - labelWidth;
        };
    }

    private static boolean isDevActive() {
        return !FMLEnvironment.production;
    }
}