package com.ziver.tab_layouts.api.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.Objects;

public final class CtlHeaderVisualBuilder implements CtlGeneratedVisual {
    private final ResourceLocation texture;

    private CtlSpriteAnimationBuilder spriteAnimation;

    private String textColor;
    private String textTopColor;
    private String textBottomColor;
    private String labelColor;
    private Boolean textShadow;
    private String textAlign;
    private Boolean hideText;

    private CtlHeaderVisualBuilder(ResourceLocation texture) {
        this.texture = Objects.requireNonNull(texture, "texture");
    }

    public static CtlHeaderVisualBuilder header(ResourceLocation texture) {
        return new CtlHeaderVisualBuilder(texture);
    }

    public CtlHeaderVisualBuilder spriteAnimation(CtlSpriteAnimationBuilder spriteAnimation) {
        this.spriteAnimation = Objects.requireNonNull(spriteAnimation, "spriteAnimation");
        return this;
    }

    public CtlHeaderVisualBuilder textColor(String color) {
        this.textColor = Objects.requireNonNull(color, "color");
        return this;
    }

    public CtlHeaderVisualBuilder textTopColor(String color) {
        this.textTopColor = Objects.requireNonNull(color, "color");
        return this;
    }

    public CtlHeaderVisualBuilder textBottomColor(String color) {
        this.textBottomColor = Objects.requireNonNull(color, "color");
        return this;
    }

    public CtlHeaderVisualBuilder splitTextColor(String topColor, String bottomColor) {
        this.textTopColor = Objects.requireNonNull(topColor, "topColor");
        this.textBottomColor = Objects.requireNonNull(bottomColor, "bottomColor");
        return this;
    }

    public CtlHeaderVisualBuilder labelColor(String color) {
        this.labelColor = Objects.requireNonNull(color, "color");
        return this;
    }

    public CtlHeaderVisualBuilder textShadow(boolean shadow) {
        this.textShadow = shadow;
        return this;
    }

    public CtlHeaderVisualBuilder textAlign(String align) {
        this.textAlign = Objects.requireNonNull(align, "align").toLowerCase(Locale.ROOT);
        return this;
    }

    public CtlHeaderVisualBuilder hideText(boolean hideText) {
        this.hideText = hideText;
        return this;
    }

    public CtlHeaderVisualBuilder hideText() {
        return hideText(true);
    }

    public CtlHeaderVisualBuilder left() {
        return textAlign("left");
    }

    public CtlHeaderVisualBuilder center() {
        return textAlign("center");
    }

    public CtlHeaderVisualBuilder right() {
        return textAlign("right");
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("texture", texture.toString());

        if (spriteAnimation != null) json.add("sprite_animation", spriteAnimation.toJson());
        if (hideText != null) json.addProperty("hide_text", hideText);
        if (textColor != null) json.addProperty("text_color", textColor);
        if (textTopColor != null) json.addProperty("text_top_color", textTopColor);
        if (textBottomColor != null) json.addProperty("text_bottom_color", textBottomColor);
        if (labelColor != null) json.addProperty("label_color", labelColor);
        if (textShadow != null) json.addProperty("text_shadow", textShadow);
        if (textAlign != null) json.addProperty("text_align", textAlign);

        return json;
    }
}