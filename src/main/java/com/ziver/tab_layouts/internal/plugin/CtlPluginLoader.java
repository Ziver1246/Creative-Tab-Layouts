package com.ziver.tab_layouts.internal.plugin;

import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.slf4j.Logger;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Discovers annotated plugins, validates metadata, resolves target availability and
 * PRESET-vs-NATIVE precedence, then registers the selected integrations.
 */
public final class CtlPluginLoader {
    private CtlPluginLoader() {}

    private static boolean loaded = false;

    public static void loadPlugins(Logger logger) {
        Objects.requireNonNull(logger, "logger");

        if (loaded) throw new IllegalStateException("CTL plugins were already loaded");
        loaded = true;

        List<CtlPluginDescriptor> plugins = findPlugins(logger);
        logger.info("[CTL] Found {} plugin(s)", plugins.size());

        validateUniquePluginIds(plugins);

        List<CtlPluginDescriptor> activePlugins = resolvePlugins(plugins, logger);
        CtlPluginContext ctx = new CtlPluginContext(logger);

        for (CtlPluginDescriptor descriptor : activePlugins) {
            ICtlPlugin plugin = createPlugin(descriptor.pluginClass());

            try {
                logger.info("[CTL] Loading plugin: {}", descriptor.id());

                CtlPluginValidation.begin(descriptor);

                try {
                    plugin.register(ctx);
                } finally {
                    CtlPluginValidation.end();
                }
            } catch (RuntimeException | LinkageError exception) {
                throw new IllegalStateException("Failed to load CTL plugin '" + descriptor.id() + "' (" + descriptor.pluginClass().getName() + ")", exception);
            }
        }
    }

    private static List<CtlPluginDescriptor> findPlugins(Logger logger) {
        Map<String, ModFileScanData.AnnotationData> discoveredPlugins = new LinkedHashMap<>();

        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            scanData.getAnnotatedBy(CtlPlugin.class, ElementType.TYPE)
                    .forEach(annotation -> discoveredPlugins.putIfAbsent(annotation.memberName(), annotation));
        }

        List<CtlPluginDescriptor> plugins = new ArrayList<>();

        for (Map.Entry<String, ModFileScanData.AnnotationData> entry : discoveredPlugins.entrySet()) {
            String className = entry.getKey();
            Class<?> discoveredClass = loadClass(className);
            Class<? extends ICtlPlugin> pluginClass = validatePluginClass(className, discoveredClass);

            plugins.add(createDescriptor(pluginClass, entry.getValue(), logger));
        }

        return plugins;
    }

    private static CtlPluginDescriptor createDescriptor(Class<? extends ICtlPlugin> pluginClass, ModFileScanData.AnnotationData annotation, Logger logger) {
        Map<String, Object> data = annotation.annotationData();

        String modId = readString(data, "modId", "");
        String id = readString(data, "id", "");

        boolean hasModId = !modId.isBlank();
        boolean hasId = !id.isBlank();

        if (hasModId != hasId) throw new IllegalStateException("CTL plugin '" + pluginClass.getName() + "' must declare both modId and id");
        if (!hasModId) return createLegacyDescriptor(pluginClass, logger);

        ResourceLocation pluginId = ResourceLocation.tryBuild(modId, id);
        if (pluginId == null) throw new IllegalStateException("CTL plugin '" + pluginClass.getName() + "' declares invalid plugin id '" + modId + ":" + id + "'");

        CtlPluginType type = readEnum(data, "type", CtlPluginType.NATIVE);
        Set<String> targets = readStrings(data, "targets");

        for (String target : targets) {
            if (target == null || target.isBlank()) throw new IllegalStateException("CTL plugin '" + pluginId + "' declares an empty target mod id");
        }

        if (type == CtlPluginType.COMPAT && targets.size() < 2)
            logger.warn("[CTL] COMPAT plugin '{}' declares fewer than two target mods", pluginId);

        if (type == CtlPluginType.NATIVE && !targets.contains(modId))
            logger.warn("[CTL] NATIVE plugin '{}' does not include its provider mod '{}' among its targets", pluginId, modId);

        if (targets.isEmpty())
            logger.warn("[CTL] Plugin '{}' declares no target mods", pluginId);

        return new CtlPluginDescriptor(pluginId, type, Set.copyOf(targets), pluginClass, false);
    }

    private static String readString(Map<String, Object> data, String key, String fallback) {
        Object value = data.get(key);
        return value instanceof String string ? string : fallback;
    }

    private static <E extends Enum<E>> E readEnum(Map<String, Object> data, String key, E fallback) {
        Object value = data.get(key);
        if (value == null) return fallback;

        String name = readEnumName(value);
        if (name == null) throw new IllegalStateException("Invalid @" + CtlPlugin.class.getSimpleName() + " enum value for '" + key + "': " + value);

        try {
            @SuppressWarnings("unchecked")
            Class<E> enumClass = fallback.getDeclaringClass();
            return Enum.valueOf(enumClass, name);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Invalid @" + CtlPlugin.class.getSimpleName() + " enum value for '" + key + "': " + name, exception);
        }
    }

    private static String readEnumName(Object value) {
        try {
            var field = value.getClass().getDeclaredField("value");
            field.setAccessible(true);

            Object enumValue = field.get(value);
            return enumValue instanceof String string ? string : null;
        } catch (ReflectiveOperationException ignored) {
            return value instanceof Enum<?> enumValue ? enumValue.name() : value instanceof String string ? string : null;
        }
    }

    private static Set<String> readStrings(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) return Set.of();

        Set<String> values = new LinkedHashSet<>();

        if (value instanceof Collection<?> collection) {
            for (Object element : collection) {
                if (!(element instanceof String string))
                    throw new IllegalStateException("Invalid @" + CtlPlugin.class.getSimpleName() + " string value for '" + key + "': " + element);

                values.add(string);
            }
        } else if (value instanceof String string) {
            values.add(string);
        } else if (value instanceof String[] array) {
            values.addAll(Arrays.asList(array));
        } else {
            throw new IllegalStateException("Invalid @" + CtlPlugin.class.getSimpleName() + " value for '" + key + "': " + value);
        }

        return values;
    }

    @SuppressWarnings("removal")
    private static CtlPluginDescriptor createLegacyDescriptor(Class<? extends ICtlPlugin> pluginClass, Logger logger) {
        ICtlPlugin plugin = createPlugin(pluginClass);
        ResourceLocation pluginId = Objects.requireNonNull(plugin.getPluginUid(), "Legacy CTL plugin '" + pluginClass.getName() + "' returned null plugin uid");

        logger.warn("[CTL] Plugin '{}' uses deprecated getPluginUid(); migrate its identity to @CtlPlugin before CTL 2.0.0", pluginId);

        return new CtlPluginDescriptor(pluginId, CtlPluginType.NATIVE, Set.of(), pluginClass, true);
    }

    private static List<CtlPluginDescriptor> resolvePlugins(List<CtlPluginDescriptor> plugins, Logger logger) {
        List<CtlPluginDescriptor> eligible = new ArrayList<>();

        for (CtlPluginDescriptor plugin : plugins) {
            if (!areTargetsLoaded(plugin)) {
                logger.debug("[CTL] Skipping plugin '{}' because one or more target mods are not loaded", plugin.id());
                continue;
            }

            eligible.add(plugin);
        }

        Set<String> nativeTargets = new HashSet<>();

        for (CtlPluginDescriptor plugin : eligible) {
            if (plugin.type() == CtlPluginType.NATIVE)
                nativeTargets.addAll(plugin.targets());
        }

        List<CtlPluginDescriptor> active = new ArrayList<>();

        for (CtlPluginDescriptor plugin : eligible) {
            if (plugin.type() == CtlPluginType.PRESET && plugin.targets().stream().anyMatch(nativeTargets::contains)) {
                logger.info("[CTL] Skipping preset '{}' because a native plugin is available for its target", plugin.id());
                continue;
            }

            active.add(plugin);
        }

        active.sort(Comparator.comparingInt(CtlPluginLoader::loadOrder));
        return active;
    }

    private static boolean areTargetsLoaded(CtlPluginDescriptor plugin) {
        for (String target : plugin.targets()) {
            if (target.equals("minecraft")) continue;
            if (!ModList.get().isLoaded(target)) return false;
        }

        return true;
    }

    private static int loadOrder(CtlPluginDescriptor plugin) {
        return switch (plugin.type()) {
            case NATIVE -> 0;
            case PRESET -> 0;
            case COMPAT -> 1;
        };
    }

    private static ICtlPlugin createPlugin(Class<? extends ICtlPlugin> pluginClass) {
        Constructor<? extends ICtlPlugin> constructor = getNoArgsConstructor(pluginClass);

        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException | LinkageError exception) {
            throw new IllegalStateException("Failed to instantiate CTL plugin class '" + pluginClass.getName() + "'", exception);
        }
    }

    private static Class<? extends ICtlPlugin> validatePluginClass(String className, Class<?> discoveredClass) {
        if (!ICtlPlugin.class.isAssignableFrom(discoveredClass))
            throw new IllegalStateException("Class '" + className + "' is annotated with @CtlPlugin but does not implement ICtlPlugin");

        @SuppressWarnings("unchecked")
        Class<? extends ICtlPlugin> pluginClass = (Class<? extends ICtlPlugin>) discoveredClass;

        return pluginClass;
    }

    private static Constructor<? extends ICtlPlugin> getNoArgsConstructor(Class<? extends ICtlPlugin> pluginClass) {
        try {
            Constructor<? extends ICtlPlugin> constructor = pluginClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("CTL plugin class '" + pluginClass.getName() + "' must have a no-args constructor", exception);
        }
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className, false, CtlPluginLoader.class.getClassLoader());
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Failed to find CTL plugin class '" + className + "'", exception);
        }
    }

    private static void validateUniquePluginIds(List<CtlPluginDescriptor> plugins) {
        Map<ResourceLocation, CtlPluginDescriptor> ids = new LinkedHashMap<>();

        for (CtlPluginDescriptor plugin : plugins) {
            CtlPluginDescriptor previous = ids.putIfAbsent(plugin.id(), plugin);

            if (previous != null) throw new IllegalStateException("Duplicate CTL plugin id '" + plugin.id() + "' from '" + previous.pluginClass().getName() + "' and '" + plugin.pluginClass().getName() + "'");
        }
    }
}