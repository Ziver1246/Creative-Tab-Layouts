package com.ziver.tab_layouts.internal.plugin;

import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

record CtlPluginDescriptor(ResourceLocation id, CtlPluginType type, Set<String> targets, Class<? extends ICtlPlugin> pluginClass, boolean legacy) {}