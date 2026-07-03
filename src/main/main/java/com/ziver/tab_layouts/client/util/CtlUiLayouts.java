package com.ziver.tab_layouts.client.util;

import com.ziver.tab_layouts.CreativeTabLayouts;
import net.minecraft.resources.ResourceLocation;

public final class CtlUiLayouts {
    private CtlUiLayouts() {}

    // Vanilla creative inventory grid.
    public static final int GRID_X_OFFSET = 8;
    public static final int GRID_Y_OFFSET = 17;

    public static final int SLOT_SIZE = 18;
    public static final int GRID_COLUMNS = 9;
    public static final int GRID_VISIBLE_ROWS = 5;

    public static final int GRID_WIDTH = GRID_COLUMNS * SLOT_SIZE;
    public static final int GRID_HEIGHT = GRID_VISIBLE_ROWS * SLOT_SIZE;

    // Section/header rows.
    public static final int HEADER_ROW_WIDTH = GRID_WIDTH;
    public static final int HEADER_ROW_HEIGHT = SLOT_SIZE;

    public static final int HEADER_BASE_X_OFFSET = GRID_X_OFFSET;
    public static final int HEADER_BASE_Y_OFFSET = GRID_Y_OFFSET;

    // Header label/text.
    public static final int HEADER_LABEL_X_OFFSET = 2;
    public static final int HEADER_LABEL_Y_OFFSET = 2;
    public static final int HEADER_LABEL_RIGHT_PADDING = 8;
    public static final int HEADER_LABEL_BOTTOM_INSET = 2;

    public static final int HEADER_TEXT_X_PADDING = 5;
    public static final int HEADER_TEXT_Y_OFFSET = 5;

    // Overview/banner.
    public static final int BANNER_X_OFFSET = GRID_X_OFFSET;
    public static final int BANNER_Y_OFFSET = GRID_Y_OFFSET;
    public static final int BANNER_WIDTH = GRID_WIDTH;
    public static final int BANNER_HEIGHT = GRID_HEIGHT;

    // Fallback frame textures.
    public static final ResourceLocation HEADER_FRAME_TEXTURE = CreativeTabLayouts.id("textures/gui/ctl/header_frame.png");
    public static final ResourceLocation BANNER_FRAME_TEXTURE = CreativeTabLayouts.id("textures/gui/ctl/banner_frame.png");
}