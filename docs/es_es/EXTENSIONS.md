# Extensiones de Creative Tab Layouts

Este documento describe `CtlApiExtensions`, la API pública para consultar layouts CTL resueltos y reutilizar sus visuales en otras interfaces.

> API de layouts: [API.md](./API.md)  
> Visuales: [VISUALS.md](./VISUALS.md)  
> Fallback: [FALLBACK.md](./FALLBACK.md)

## Objetivo

La API devuelve snapshots semánticos e inmutables. Una integración recibe pages, sections y entries ya resueltas, pero decide cómo organizarlas y mostrarlas en su propia interfaz.

No es necesario acceder a la representación usada por el inventario creativo de CTL.

## Consultas generales

```java
boolean isTabControlled(ResourceLocation tabId);
boolean areBuiltinVanillaLayoutsEnabled();
boolean areSubtabsEnabled();
boolean isCreativeConfigButtonEnabled();
boolean hasPages(ResourceLocation tabId);
```

`isTabControlled` refleja el estado actual. Un layout vanilla builtin desactivado por configuración no se considera controlado mientras esa opción permanezca apagada.

## Consultas de subtabs

```java
boolean isSubtab(ResourceLocation tabId);
boolean hasSubtabs(ResourceLocation tabId);
Optional<ResourceLocation> getParentTab(ResourceLocation tabId);
List<ResourceLocation> getSubtabs(ResourceLocation tabId);
Optional<SubtabGroupView> getSubtabGroup(ResourceLocation tabId);
```

`SubtabGroupView` contiene el ID de la tab padre y una lista inmutable de sus subtabs.

Una integración debería respetar `areSubtabsEnabled()` antes de representar el agrupamiento.

## Snapshots de layouts

```java
Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries);
Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries, List<ItemStack> originalItems);
Optional<PageView> getPageView(ResourceLocation tabId, int pageIndex, HolderLookup.Provider registries);
```

La primera sobrecarga de `getTabView` devuelve únicamente pages declaradas por CTL. La segunda también puede construir fallback pages usando la lista original de items.

Devuelven `Optional.empty()` cuando la tab no está controlada o no tiene pages activas.

Antes de devolver el snapshot, CTL resuelve:

```text
- priorities
- insertion order
- addFirst / addLast
- addBefore / addAfter
- dynamic entries
- orden de sections
- fallback, cuando se proporcionan originalItems
```

Los `ItemStack` entregados son copias y las listas no deben modificarse.

## TabView

Expone el ID de la tab y su lista ordenada de `PageView`.

## PageView

Expone:

```text
index
id
type
title
entries directas
sections
```

`PageType` distingue `OVERVIEW`, `BASE`, `ADDON` y `FALLBACK`.

Una overview no contiene entries ni sections.

## SectionView

Expone:

```text
id
type
title
entries
```

`SectionType` distingue `BASE`, `ADDON` y `FALLBACK`.

## API de cliente

`CtlApiExtensions.Client` depende de clases de cliente y solo debe usarse desde código cliente.

```java
Size getPreferredHeaderSize();
Size getPreferredBannerSize();
boolean hasHeaderVisual(ResourceLocation sectionId);
boolean hasBannerVisual(ResourceLocation pageId);
```

Los tamaños son recomendaciones para conservar la apariencia prevista. Una integración puede usar otras dimensiones, pero debería conservar una altura y proporción razonables.

## Renderizar headers

```java
boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY);
```

También existen sobrecargas con `animationContext` y `allowDebug`.

Usa siempre el índice recibido en `PageView#index()`.

El método devuelve `true` cuando CTL renderizó un visual personalizado. Si devuelve `false`, la integración debe renderizar su propio frame o fallback.

## Renderizar banners

```java
boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY);
```

Solo una overview page con un banner válido puede producir un render exitoso. Igual que con headers, un resultado `false` requiere un fallback del consumidor.

## Contextos de animación

Las sobrecargas que reciben `animationContext` permiten mantener estados de animación independientes.

```java
private static final ResourceLocation ANIMATION_CONTEXT = ResourceLocation.fromNamespaceAndPath("examplemod", "my_screen");
```

Usa contextos distintos cuando el mismo visual pueda aparecer simultáneamente en interfaces diferentes. Así el hover, pausa y frame actual no se comparten accidentalmente.

## Debug visual

Las sobrecargas con `allowDebug` permiten que una integración suprima los helpers de debug de CTL sin cambiar la configuración global.

```java
CtlApiExtensions.Client.renderSectionHeader(graphics, ANIMATION_CONTEXT, tabId, page.index(), section.id(), x, y, width, height, hovered, mouseX, mouseY, false);
```

`allowDebug = true` no fuerza el debug: solo permite que CTL lo muestre cuando el entorno y la configuración también lo permiten.

## Integración opcional

Antes de llamar esta API desde una compat integration, comprueba que CTL esté cargado mediante el mecanismo habitual de tu loader. Mantén las referencias cliente aisladas para evitar cargar clases gráficas en servidor dedicado.
