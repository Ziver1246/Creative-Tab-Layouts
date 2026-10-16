# Primeros pasos con Creative Tab Layouts

Esta guía muestra el flujo mínimo para integrar un mod con **Creative Tab Layouts (CTL) 1.3.0**.

CTL no reemplaza las creative tabs de Minecraft. Un plugin CTL organiza el contenido visible de una tab existente mediante páginas, secciones y entradas.

## 1. Crear un plugin

Un plugin CTL implementa `ICtlPlugin` y declara su identidad con `@CtlPlugin`.

```java
package com.example.examplemod.compat.ctl;

import com.example.examplemod.ExampleMod;
import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;

@CtlPlugin(
        modId = ExampleMod.MOD_ID,
        id = "layout",
        type = CtlPluginType.NATIVE,
        targets = ExampleMod.MOD_ID
)
public final class ExampleCtlPlugin implements ICtlPlugin {

    @Override
    public void register(CtlPluginContext ctx) {
        // Registrar layouts aquí.
    }
}
```

CTL descubre automáticamente las clases anotadas. La clase debe poder construirse con un constructor sin argumentos.

`getPluginUid()` pertenece al sistema anterior y está deprecated. No lo implementes en integraciones nuevas.

## 2. Controlar una creative tab

Usa `controlTab` cuando tu integración sea propietaria del layout de esa tab.

```java
@Override
public void register(CtlPluginContext ctx) {
    ctx.controlTab(ExampleCreativeTabs.MAIN.location())
            .page(id("main"), page -> {
                page.add(ExampleItems.FIRST.get(), ExampleItems.SECOND.get());
            });
}
```

Helper habitual:

```java
private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(ExampleMod.MOD_ID, path);
}
```

## 3. Añadir secciones

```java
ctx.controlTab(ExampleCreativeTabs.MAIN.location())
        .page(id("materials"), page -> {
            page.section(id("metals"), section -> {
                section.add(ExampleItems.COPPER_PART.get());
                section.add(ExampleItems.STEEL_PART.get());
            });

            page.section(id("gems"), section -> {
                section.add(ExampleItems.RUBY.get());
            });
        });
```

Los títulos se obtienen de lang keys generadas desde los IDs:

```text
Página  examplemod:materials → tabpage.examplemod.materials
Sección examplemod:metals    → tabsection.examplemod.metals
```

Si el path contiene `/`, CTL lo convierte en `.` para la lang key.

## 4. Contribuir a un layout ajeno

Si otra integración controla la tab, usa `contribute` en vez de `controlTab`.

```java
ctx.contribute(TargetTabs.MAIN.location()).ifPresent(tab -> {
    tab.addonPage(id("example_addon"), page -> {
        page.add(ExampleItems.EXTRA.get());
    });
});
```

`contribute` devuelve `Optional.empty()` si la tab no está controlada por CTL. No fuerza la creación de un layout.

## 5. Agrupar tabs como subtabs

Si solo quieres agrupar creative tabs existentes y conservar su contenido original:

```java
ctx.subtabs(
        ExampleCreativeTabs.MAIN.location(),
        ExampleCreativeTabs.MACHINES.location(),
        ExampleCreativeTabs.DECORATION.location()
);
```

Si también quieres que CTL controle el layout de la hija:

```java
ctx.controlSubtab(ExampleCreativeTabs.MACHINES.location(), ExampleCreativeTabs.MAIN.location())
        .page(id("machines"), page -> {
            page.add(ExampleItems.MACHINE.get());
        });
```

## 6. Siguiente paso

- [Plugins](./PLUGINS.md): `NATIVE`, `PRESET`, `COMPAT`, targets y reemplazo de presets.
- [Layouts](./LAYOUTS.md): pages, sections, entries, priorities y contributions.
- [Visuales](./VISUALS.md): headers, banners, JSON, datagen y render externo.
- [Fallback](./FALLBACK.md): cómo CTL conserva items no asignados.
- [Configuración](./CONFIG.md): opciones disponibles en 1.3.0.
- [Migración](./MIGRATION.md): APIs deprecated y cambios previstos para 2.0.0.

## Alcance público

Para integraciones externas usa únicamente:

```text
com.ziver.tab_layouts.api.*
```

Todo lo que esté bajo:

```text
com.ziver.tab_layouts.internal.*
```

es implementación interna y puede cambiar sin considerarse una ruptura de API pública.
