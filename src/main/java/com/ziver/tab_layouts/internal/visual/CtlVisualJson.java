package com.ziver.tab_layouts.internal.visual;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public final class CtlVisualJson {
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    private CtlVisualJson() {}

    public static CtlHeaderVisual parseHeader(JsonObject json) {
        ResourceLocation texture = parseTexture(json);

        int textColor = parseColor(GsonHelper.getAsString(json, "text_color", "#FFFFFFFF"));

        boolean hasTopColor = json.has("text_top_color");
        boolean hasBottomColor = json.has("text_bottom_color");

        int textTopColor = hasTopColor ? parseColor(GsonHelper.getAsString(json, "text_top_color")) : 0x00000000;
        int textBottomColor = hasBottomColor ? parseColor(GsonHelper.getAsString(json, "text_bottom_color")) : 0x00000000;

        boolean splitTextColor = hasTopColor && hasBottomColor;

        int labelColor = parseColor(GsonHelper.getAsString(json, "label_color", "#00000000"));
        boolean textShadow = GsonHelper.getAsBoolean(json, "text_shadow", false);
        CtlTextAlign textAlign = CtlTextAlign.byName(GsonHelper.getAsString(json, "text_align", "left"));

        CtlSpriteAnimation animation = parseAnimation(json);
        boolean hideText = GsonHelper.getAsBoolean(json, "hide_text", false);

        return new CtlHeaderVisual(texture, textColor, textTopColor, textBottomColor, splitTextColor, labelColor, textShadow, textAlign, animation, hideText);
    }

    public static CtlBannerVisual parseBanner(JsonObject json) {
        ResourceLocation texture = parseTexture(json);
        CtlSpriteAnimation animation = parseAnimation(json);

        return new CtlBannerVisual(texture, animation);
    }

    public static String prettyJson(JsonElement json) {
        return PRETTY_GSON.toJson(json);
    }

    private static ResourceLocation parseTexture(JsonObject json) {
        String texture = GsonHelper.getAsString(json, "texture");
        return ResourceLocation.parse(texture);
    }

    private static CtlSpriteAnimation parseAnimation(JsonObject json) {
        if (!json.has("sprite_animation")) return CtlSpriteAnimation.none();
        JsonElement element = json.get("sprite_animation");
        if (!element.isJsonObject()) return CtlSpriteAnimation.none();


        JsonObject animation = element.getAsJsonObject();

        int fps = GsonHelper.getAsInt(animation, "fps", 0);
        int frames = GsonHelper.getAsInt(animation, "frames", 1);
        CtlSpriteLayout layout = CtlSpriteLayout.byName(GsonHelper.getAsString(animation, "layout", "vertical"));
        int columns = GsonHelper.getAsInt(animation, "columns", 1);
        boolean animateJustOnHover = GsonHelper.getAsBoolean(animation, "animate_just_on_hover", false);

        if (frames < 1) frames = 1;
        if (fps < 0) fps = 0;
        if (columns < 1) columns = 1;

        return new CtlSpriteAnimation(fps, frames, layout, columns, animateJustOnHover);
    }

    private static int parseColor(String value) {
        String color = value.trim();

        if (color.startsWith("#")) color = color.substring(1);
        if (color.length() == 6) color = "FF" + color;
        if (color.length() != 8) throw new IllegalArgumentException("Invalid ARGB color: " + value);

        return (int) Long.parseLong(color, 16);
    }
}