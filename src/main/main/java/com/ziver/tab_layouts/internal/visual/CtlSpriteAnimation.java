package com.ziver.tab_layouts.internal.visual;

public record CtlSpriteAnimation(int fps, int frames, CtlSpriteLayout layout, int columns, boolean animateJustOnHover) {

    public static CtlSpriteAnimation none() {
        return new CtlSpriteAnimation(0, 1, CtlSpriteLayout.VERTICAL, 1, false);
    }

    public boolean animated() {
        return fps > 0 && frames > 1;
    }

    public int safeColumns() {
        return Math.max(1, columns);
    }

    public int ticksPerFrame() {
        if (!animated()) return 0;
        return Math.max(1, Math.round(20.0F / fps));
    }

    public int rows() {
        int columns = safeColumns();

        return switch (layout) {
            case VERTICAL -> frames;
            case HORIZONTAL -> 1;
            case GRID -> Math.max(1, (int) Math.ceil(frames / (double) columns));
        };
    }

    public int columnsForLayout() {
        return switch (layout) {
            case VERTICAL -> 1;
            case HORIZONTAL -> frames;
            case GRID -> safeColumns();
        };
    }
}