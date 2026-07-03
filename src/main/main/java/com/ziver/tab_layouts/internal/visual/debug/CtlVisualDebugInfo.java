package com.ziver.tab_layouts.internal.visual.debug;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record CtlVisualDebugInfo(String type, ResourceLocation id, String virtualPath, String prettyJson, Component error) {}