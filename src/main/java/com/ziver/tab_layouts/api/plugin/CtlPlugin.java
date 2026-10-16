package com.ziver.tab_layouts.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CtlPlugin {
    String modId() default "";

    String id() default "";

    CtlPluginType type() default CtlPluginType.NATIVE;

    String[] targets() default {};
}