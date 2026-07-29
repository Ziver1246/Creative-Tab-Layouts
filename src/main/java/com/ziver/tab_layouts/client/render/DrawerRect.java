package com.ziver.tab_layouts.client.render;

public record DrawerRect(int x, int y, int width, int height) {
    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}