package com.ziver.tab_layouts.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a discoverable CTL plugin and declares its static integration metadata.
 *
 * <p>Annotated classes must implement {@link ICtlPlugin} and provide a no-argument
 * constructor. Plugin identity is the namespaced ID formed from {@link #modId()} and
 * {@link #id()}.</p>
 *
 * <p>Empty {@code modId} and {@code id} values exist only for legacy 1.x compatibility.
 * New plugins should always declare both fields.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CtlPlugin {
    /** Mod ID of the plugin provider. */
    String modId() default "";

    /** Plugin ID local to the provider mod. */
    String id() default "";

    /** Semantic role of this integration. */
    CtlPluginType type() default CtlPluginType.NATIVE;

    /**
     * Mod IDs targeted or represented by this integration.
     *
     * <p>Plugins are skipped when one or more declared targets are not loaded. Presets
     * are also skipped when an eligible native integration exists for the same target.</p>
     */
    String[] targets() default {};
}