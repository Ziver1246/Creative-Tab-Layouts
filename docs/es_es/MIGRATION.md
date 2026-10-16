# Migración y camino hacia 2.0.0

Este documento registra cambios de API que requieren atención y pequeñas decisiones previstas para la transición hacia CTL 2.0.0.

No es una promesa rígida de roadmap: los planes futuros pueden cambiar antes de 2.0.0.

## 1.3.0: nuevo sistema de plugins

### Antes

Plugins anteriores podían declarar su identidad implementando:

```java
@Override
public ResourceLocation getPluginUid() {
    return ResourceLocation.fromNamespaceAndPath("examplemod", "ctl_plugin");
}
```

### Ahora

Declara la metadata en `@CtlPlugin`:

```java
@CtlPlugin(
        modId = ExampleMod.MOD_ID,
        id = "layout",
        type = CtlPluginType.NATIVE,
        targets = ExampleMod.MOD_ID
)
public final class ExampleCtlPlugin implements ICtlPlugin {
    @Override
    public void register(CtlPluginContext ctx) {
    }
}
```

`getPluginUid()` sigue funcionando para plugins legacy durante 1.x, pero está marcado `forRemoval` y será eliminado en 2.0.0.

### Por qué migrar

La nueva metadata permite a CTL conocer:

```text
proveedor
ID lógico
tipo de integración
targets
```

También permite la precedencia automática:

```text
NATIVE > PRESET para el mismo target
```

Un plugin legacy no declara targets, por lo que CTL no puede inferir qué preset debería reemplazar. Esa limitación es intencional durante la transición.

## 1.2.x → 1.3.0: contributions

`CtlPluginContext#contributeTab` está deprecated.

### Antes

```java
ctx.contributeTab(tabId).ifPresent(tab -> {
    // El builder exponía también operaciones de owner.
});
```

### Ahora

```java
ctx.contribute(tabId).ifPresent(tab -> {
    tab.addonPage(id("compat"), page -> {
        // ...
    });
});
```

`contribute` devuelve `CtlContributionBuilder`, que limita la superficie a operaciones de contribución.

`contributeTab` está previsto para eliminación en 2.0.0.

## 1.3.0: CtlApiExtensions

`CtlApiExtensions` está deprecated.

La API nueva está separada por responsabilidad:

```text
api.view   → layouts resueltos de solo lectura
api.client → render público de visuales
api.layout → construcción y contribución de layouts
api.plugin → plugins e integración
api.datagen → generación de visuales
```

### Consultas de layouts

Antes:

```java
CtlApiExtensions.getTabView(...)
```

Ahora:

```java
CtlViews.getTabView(...)
```

Los snapshots públicos ahora tienen clases propias:

```text
CtlTabView
CtlPageView
CtlSectionView
CtlSubtabGroupView
```

### Visuales externos

Antes:

```java
CtlApiExtensions.Client.renderSectionHeader(...)
CtlApiExtensions.Client.renderPageBanner(...)
```

Ahora:

```java
CtlVisuals.renderSectionHeader(...)
CtlVisuals.renderPageBanner(...)
```

Y los tamaños preferidos usan:

```text
CtlVisualSize
```

### Config

La nueva API ya no expone el estado interno de configuración como parte de su contrato.

Las compat shims de `CtlApiExtensions` permanecen temporalmente para no romper 1.x.

## 1.3.0: CtlVanillaTabs eliminado

El wrapper `CtlVanillaTabs` fue eliminado.

Para tabs vanilla usa las keys de Minecraft directamente:

```java
CreativeModeTabs.INGREDIENTS.location()
CreativeModeTabs.BUILDING_BLOCKS.location()
```

Esto evita duplicar IDs que vanilla ya expone.

## 1.3.0: orden simplificado

Se eliminó infraestructura no utilizada para un sistema de page ordering configurable.

El orden actual es determinista:

```text
OVERVIEW
BASE    → priority → orden de registro
ADDON   → priority → orden de registro
```

Las sections usan el mismo principio dentro de BASE y ADDON.

No se espera que integraciones externas dependan de un sistema de orden custom separado.

## 1.3.0: fallback

Fallback ahora conserva todos los items no reclamados de la creative tab original, incluidos items vanilla.

Integraciones que asumían que `minecraft` quedaba fuera del fallback no deben seguir dependiendo de ese comportamiento anterior.

## Previsto para 2.0.0

Cambios actualmente esperados:

- eliminar `ICtlPlugin#getPluginUid()`;
- eliminar `CtlPluginContext#contributeTab()`;
- eliminar `CtlApiExtensions` y sus tipos legacy;
- hacer del nuevo sistema `@CtlPlugin` la única forma soportada de declarar identidad de plugins;
- continuar limpiando API deprecated o provisional cuando exista un reemplazo estable.

La intención de 2.0.0 es reducir compatibilidad histórica, no rehacer innecesariamente las partes estables de la API.

## Regla práctica

Para escribir código nuevo compatible con la dirección de 2.0.0:

```text
1. Usa @CtlPlugin con modId, id, type y targets.
2. Usa contribute(), no contributeTab().
3. Usa CtlViews y CtlVisuals, no CtlApiExtensions.
4. Usa CreativeModeTabs.<TAB>.location() para IDs vanilla.
5. No dependas de com.ziver.tab_layouts.internal.*.
```
