package com.ziver.tab_layouts.internal.plugin;

import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.util.*;

public final class CtlPluginLoader {
    private CtlPluginLoader() {}

    private static boolean loaded = false;

    public static void loadPlugins(Logger logger) {
        loadPlugins(logger, new CtlPluginContext(logger));
    }

    public static void loadPlugins(Logger logger, CtlPluginContext ctx) {
        Objects.requireNonNull(logger, "logger");
        Objects.requireNonNull(ctx, "ctx");

        if (loaded) throw new IllegalStateException("CTL plugins were already loaded");
        loaded = true;

        List<ICtlPlugin> plugins = findPlugins();
        logger.info("[CTL] Found {} plugin(s)", plugins.size());
        validateUniquePluginIds(plugins);

        for (ICtlPlugin plugin : plugins) {
            ResourceLocation pluginUid = Objects.requireNonNull(plugin.getPluginUid(), "CTL plugin '" + plugin.getClass().getName() + "' returned null plugin uid");

            try {
                logger.info("[CTL] Loading plugin: {}", pluginUid);
                plugin.register(ctx);
            } catch (RuntimeException | LinkageError exception) {
                throw new IllegalStateException("Failed to load CTL plugin '" + pluginUid + "' (" + plugin.getClass().getName() + ")", exception);
            }
        }
    }

    private static List<ICtlPlugin> findPlugins() {
        Type annotationType = Type.getType(CtlPlugin.class);

        Set<String> pluginClassNames = new LinkedHashSet<>();

        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData annotationData : scanData.getAnnotations()) {
                if (Objects.equals(annotationData.annotationType(), annotationType)) {
                    pluginClassNames.add(annotationData.memberName());
                }
            }
        }

        List<ICtlPlugin> plugins = new ArrayList<>();

        for (String className : pluginClassNames) {
            plugins.add(createPlugin(className));
        }

        return plugins;
    }

    private static ICtlPlugin createPlugin(String className) {
        Class<?> discoveredClass = loadClass(className);
        Class<? extends ICtlPlugin> pluginClass = validatePluginClass(className, discoveredClass);
        Constructor<? extends ICtlPlugin> constructor = getNoArgsConstructor(className, pluginClass);

        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException | LinkageError exception) {
            throw new IllegalStateException("Failed to instantiate CTL plugin class '" + className + "'", exception);
        }
    }

    private static Class<? extends ICtlPlugin> validatePluginClass(String className, Class<?> discoveredClass) {
        if (!ICtlPlugin.class.isAssignableFrom(discoveredClass)) {
            throw new IllegalStateException("Class '" + className + "' is annotated with @CtlPlugin but does not implement ICtlPlugin");
        }

        @SuppressWarnings("unchecked")
        Class<? extends ICtlPlugin> pluginClass = (Class<? extends ICtlPlugin>) discoveredClass;

        return pluginClass;
    }

    private static Constructor<? extends ICtlPlugin> getNoArgsConstructor(String className, Class<? extends ICtlPlugin> pluginClass) {
        try {
            Constructor<? extends ICtlPlugin> constructor = pluginClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("CTL plugin class '" + className + "' must have a no-args constructor", exception);
        }
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Failed to find CTL plugin class '" + className + "'", exception);
        }
    }

    private static void validateUniquePluginIds(List<ICtlPlugin> plugins) {
        Set<ResourceLocation> ids = new LinkedHashSet<>();

        for (ICtlPlugin plugin : plugins) {
            ResourceLocation pluginUid = Objects.requireNonNull(plugin.getPluginUid(), "CTL plugin '" + plugin.getClass().getName() + "' returned null plugin uid");
            if (!ids.add(pluginUid)) throw new IllegalStateException("Duplicate CTL plugin uid '" + pluginUid + "' from plugin class '" + plugin.getClass().getName() + "'");
        }
    }
}