package com.ziver.tab_layouts.internal.visual;

import net.minecraft.resources.ResourceLocation;

public record CtlHeaderVisual(ResourceLocation texture, int textColor, int textTopColor, int textBottomColor, boolean splitTextColor, int labelColor, boolean textShadow, CtlTextAlign textAlign, CtlSpriteAnimation animation, boolean hideText) {}