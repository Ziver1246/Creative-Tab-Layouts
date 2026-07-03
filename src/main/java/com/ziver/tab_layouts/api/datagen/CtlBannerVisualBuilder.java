package com.ziver.tab_layouts.api.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class CtlBannerVisualBuilder implements CtlGeneratedVisual {
    private final ResourceLocation texture;

    private CtlSpriteAnimationBuilder spriteAnimation;

    private CtlBannerVisualBuilder(ResourceLocation texture) {
        this.texture = Objects.requireNonNull(texture, "texture");
    }

    public static CtlBannerVisualBuilder banner(ResourceLocation texture) {
        return new CtlBannerVisualBuilder(texture);
    }

    public CtlBannerVisualBuilder spriteAnimation(CtlSpriteAnimationBuilder spriteAnimation) {
        this.spriteAnimation = Objects.requireNonNull(spriteAnimation, "spriteAnimation");
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("texture", texture.toString());

        if (spriteAnimation != null) json.add("sprite_animation", spriteAnimation.toJson());

        return json;
    }
}