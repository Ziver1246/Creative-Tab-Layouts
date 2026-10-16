package com.ziver.tab_layouts.api.client;

/**
 * Preferred dimensions for a CTL visual.
 *
 * @param width preferred width in GUI pixels
 * @param height preferred height in GUI pixels
 */
public record CtlVisualSize(int width, int height) {

    public CtlVisualSize {
        if (width <= 0) throw new IllegalArgumentException("Width must be greater than zero: " + width);
        if (height <= 0) throw new IllegalArgumentException("Height must be greater than zero: " + height);
    }
}