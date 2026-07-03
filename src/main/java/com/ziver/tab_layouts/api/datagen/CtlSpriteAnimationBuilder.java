package com.ziver.tab_layouts.api.datagen;

import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.Objects;

public final class CtlSpriteAnimationBuilder {

    private String layout = "vertical";
    private int frames = 1;
    private int fps = 8;
    private Integer columns;
    private Boolean animateJustOnHover;

    private CtlSpriteAnimationBuilder() {}

    public static CtlSpriteAnimationBuilder sprite() {
        return new CtlSpriteAnimationBuilder();
    }

    public static CtlSpriteAnimationBuilder horizontal(int frames, int fps) {
        return sprite().horizontal().frames(frames).fps(fps);
    }

    public static CtlSpriteAnimationBuilder vertical(int frames, int fps) {
        return sprite().vertical().frames(frames).fps(fps);
    }

    public static CtlSpriteAnimationBuilder grid(int frames, int fps, int columns) {
        return sprite().grid().frames(frames).fps(fps).columns(columns);
    }

    public CtlSpriteAnimationBuilder layout(String layout) {
        this.layout = Objects.requireNonNull(layout, "layout").toLowerCase(Locale.ROOT);
        return this;
    }

    public CtlSpriteAnimationBuilder horizontal() {
        return layout("horizontal");
    }

    public CtlSpriteAnimationBuilder vertical() {
        return layout("vertical");
    }

    public CtlSpriteAnimationBuilder grid() {
        return layout("grid");
    }

    public CtlSpriteAnimationBuilder frames(int frames) {
        if (frames <= 0) throw new IllegalArgumentException("Sprite animation frames must be greater than 0");

        this.frames = frames;
        return this;
    }

    public CtlSpriteAnimationBuilder fps(int fps) {
        if (fps <= 0) throw new IllegalArgumentException("Sprite animation FPS must be greater than 0");

        this.fps = fps;
        return this;
    }

    public CtlSpriteAnimationBuilder columns(int columns) {
        if (columns <= 0) throw new IllegalArgumentException("Sprite animation columns must be greater than 0");

        this.columns = columns;
        return this;
    }

    public CtlSpriteAnimationBuilder animateJustOnHover(boolean value) {
        this.animateJustOnHover = value;
        return this;
    }

    public CtlSpriteAnimationBuilder alwaysAnimate() {
        return animateJustOnHover(false);
    }

    public CtlSpriteAnimationBuilder animateOnHover() {
        return animateJustOnHover(true);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("layout", layout);
        json.addProperty("frames", frames);
        json.addProperty("fps", fps);

        if (columns != null) json.addProperty("columns", columns);
        if (animateJustOnHover != null) json.addProperty("animate_just_on_hover", animateJustOnHover);

        return json;
    }
}