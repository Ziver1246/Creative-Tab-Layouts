package com.ziver.tab_layouts.internal.client.util;

import net.minecraft.client.gui.components.Button;

public final class OffsetButton extends Button {
        public final int xOffset;

        public OffsetButton(Builder builder, int xOffset) {
            super(builder);
            this.xOffset = xOffset;
        }

        @Override
        public void setX(int x) {
            super.setX(x + this.xOffset);
        }
    }