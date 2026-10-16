# Layouts

Un layout CTL organiza el contenido visible de una creative tab existente.

La estructura conceptual es:

```text
Creative Tab
└─ Pages
   ├─ direct entries
   └─ Sections
      └─ entries
```

Además, varias creative tabs pueden agruparse como subtabs sin dejar de existir como tabs registradas de Minecraft.

## Pages

Una tab controlada se obtiene con:

```java
CtlTabBuilder tab = ctx.controlTab(tabId);
```

### Página base

```java
tab.page(id("materials"), page -> {
    page.add(Items.IRON_INGOT, Items.GOLD_INGOT);
});
```

### Overview

```java
tab.overview(id("overview"));
```

Una tab solo puede tener una overview. Su función principal es servir como página visual de entrada y puede tener un banner asociado.

No se puede contribuir contenido de layout a una overview mediante `contributePage`.

### Addon page

```java
tab.addonPage(id("compat"), page -> {
    page.add(ModItems.EXTRA.get());
});
```

Las addon pages están pensadas para contenido adicional o compatibilidad.

## Orden de páginas

CTL agrupa las páginas en este orden:

```text
OVERVIEW
BASE
ADDON
```

Dentro de `BASE` y `ADDON`, el orden usa:

```text
priority → orden de registro
```

Menor prioridad numérica aparece antes.

```java
tab.page(id("early"), -100, page -> {});
tab.page(id("normal"), 0, page -> {});
tab.page(id("late"), 100, page -> {});
```

Si varias páginas tienen la misma prioridad, se conserva su orden de registro.

Los IDs de página deben ser únicos dentro de la tab.

## Sections

```java
tab.page(id("materials"), page -> {
    page.section(id("metals"), section -> {
        section.add(Items.IRON_INGOT, Items.GOLD_INGOT);
    });
});
```

También existen addon sections:

```java
page.addonSection(id("compat_metals"), section -> {
    section.add(ModItems.EXTRA_INGOT.get());
});
```

Las secciones se ordenan como:

```text
BASE
ADDON
```

Dentro de cada grupo se usa:

```text
priority → orden de registro
```

Los IDs de sección deben ser únicos dentro de una misma página.

## Lang keys

CTL genera los títulos desde el ID del layout.

Para:

```text
examplemod:machines/advanced
```

la página usa:

```text
tabpage.examplemod.machines.advanced
```

la sección usa:

```text
tabsection.examplemod.machines.advanced
```

## Entries normales

### add

Para items normales:

```java
section.add(Items.DIAMOND, Items.EMERALD);
```

También puedes usar suppliers:

```java
section.add(ModItems.MACHINE);
```

### stack

Para un `ItemStack` específico:

```java
section.stack(() -> new ItemStack(Items.POTION));
```

### dynamic

Para contenido que necesita acceso a registries:

```java
section.dynamic(registries -> createStacks(registries));
```

`CtlDynamicEntries` devuelve `List<ItemStack>`.

### empty

Reserva slots vacíos:

```java
section.empty();
section.empty(3);
```

## Posicionamiento de entries

CTL permite expresar posición relativa sin reconstruir manualmente toda la lista.

```java
section.addFirst(ModItems.FIRST.get());
section.addLast(ModItems.LAST.get());
section.addBefore(Items.DIAMOND, ModItems.BEFORE_DIAMOND.get());
section.addAfter(Items.DIAMOND, ModItems.AFTER_DIAMOND.get());
```

Todas estas variantes también aceptan prioridad.

### occurrence

Si el target aparece varias veces, puedes indicar qué aparición usar:

```java
section.addAfter(Items.DIAMOND, 2, ModItems.EXTRA.get());
```

`occurrence` es 1-based:

```text
1 = primera aparición
2 = segunda aparición
...
```

Si solicitas una occurrence mayor a las existentes, CTL la ajusta a la última aparición disponible.

Si el target no aparece entre las entries normales, la entrada relativa queda sin anchor y CTL la coloca después del bloque normal correspondiente, antes de `addLast`.

## Contributions

### Contribuir a una tab

```java
ctx.contribute(tabId).ifPresent(tab -> {
    tab.addonPage(id("my_addon"), page -> {
        page.add(ModItems.EXTRA.get());
    });
});
```

### Contribuir a una página existente

```java
tab.contributePage(targetPageId, page -> {
    page.addonSection(id("my_section"), section -> {
        section.add(ModItems.EXTRA.get());
    });
});
```

`contributePage` requiere que la página exista.

### Contribuir a una sección existente

```java
tab.contributeSection(targetPageId, targetSectionId, section -> {
    section.add(ModItems.EXTRA.get());
});
```

También puede hacerse desde un `CtlPageBuilder`:

```java
page.contributeSection(targetSectionId, section -> {
    section.add(ModItems.EXTRA.get());
});
```

Contribuir a una página o sección inexistente produce error; comprueba que el layout objetivo forme parte del contrato de la integración con la que trabajas.

## Subtabs

Agrupar sin cambiar el layout original:

```java
ctx.subtab(childTabId, parentTabId);
ctx.subtabs(parentTabId, firstChild, secondChild);
```

Agrupar y controlar la hija:

```java
ctx.controlSubtab(childTabId, parentTabId)
        .page(id("main"), page -> {
            page.add(ModItems.ITEM.get());
        });
```

La parent tab no necesita estar controlada por CTL.

La presentación de subtabs puede desactivarse desde la configuración cliente; las creative tabs originales siguen existiendo.

## Views de solo lectura

`api.view` permite consultar el layout resuelto sin exponer los objetos internos de CTL.

Clases principales:

```text
CtlViews
CtlTabView
CtlPageView
CtlSectionView
CtlSubtabGroupView
```

Ejemplo:

```java
Optional<CtlTabView> view = CtlViews.getTabView(tabId, registries);
```

La sobrecarga sin `originalItems` expone las páginas declaradas por CTL. Si proporcionas los items originales de la tab, CTL también puede incluir las páginas fallback resueltas:

```java
CtlViews.getTabView(tabId, registries, originalItems);
```

Los views son snapshots públicos de solo lectura; no son builders para modificar layouts.
