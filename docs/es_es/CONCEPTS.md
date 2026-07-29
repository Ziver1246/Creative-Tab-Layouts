# Conceptos de Creative Tab Layouts

Este documento explica los conceptos principales de **Creative Tab Layouts** (CTL).

> Código listo para copiar: [EXAMPLES.md](./EXAMPLES.md)  
> Referencia de métodos: [API.md](./API.md)

## Qué es CTL

**Creative Tab Layouts** es una API y sistema de layout para organizar creative tabs usando páginas, secciones, headers, banners y fallback pages.

CTL no reemplaza el registro original de creative tabs de Minecraft. La tab sigue existiendo. CTL controla cómo se muestra su contenido dentro del creative inventory.

```text
Minecraft mantiene la creative tab.
CTL reconstruye el contenido visible de una tab controlada.
```

## Controlled tab

Una **controlled tab** es una creative tab cuyo contenido visible es administrado por CTL.

Una tab se vuelve controlada cuando un plugin llama:

```java
ctx.controlTab(tabId)
```

También puede recibir contribuciones si otro plugin usa:

```java
ctx.contributeTab(tabId)
```

`contributeTab` solo devuelve una tab si ya estaba controlada. No fuerza una tab a quedar controlada.

## Subtab

Una subtab es una creative tab existente agrupada bajo otra creative tab. La parent permanece como tab superior visible y CTL muestra sus hijas directas mediante un panel lateral.

Una subtab puede conservar su layout original de Minecraft o estar controlada por CTL. La parent no necesita estar controlada. Los grupos de subtabs no se anidan.

## Layout

Un **layout** es la estructura completa de una controlled tab.

Puede contener:

```text
overview page
base pages
addon pages
fallback pages
sections
entries
headers
banners
```

Orden general visible:

```text
Overview Page → Base Pages → Addon Pages → Fallback Pages
```

## Page

Una **page** es una página dentro de una creative tab.

Sirve para dividir tabs grandes sin crear tabs nuevas de Minecraft.

Ejemplo conceptual:

```text
Building Blocks
├─ Wood
├─ Stone
├─ Deepslate & Tuff
├─ Nether & End
├─ Resource Blocks
└─ Copper
```

## Overview page

Una **overview page** es una página visual inicial.

Se declara con:

```java
tab.overview(pageId);
```

Características:

```text
- Puede existir una por tab.
- Aparece antes de base pages, addon pages y fallback pages.
- No recibe CtlPageBuilder.
- No contiene entries normales.
- Usa un banner asociado a pageId.
```

Ruta del banner:

```text
assets/<namespace>/ctl/banners/<path>.json
```

## Base page

Una **base page** es una página principal del layout.

Se declara con:

```java
tab.page(pageId, page -> {
});
```

Las base pages aparecen después de la overview y antes de addon pages.

## Addon page

Una **addon page** es una página adicional pensada para addons, compat integrations o contenido externo.

Se declara con:

```java
tab.addonPage(pageId, page -> {
});
```

Las addon pages aparecen después de base pages y antes de fallback pages.

## Fallback page

Una **fallback page** es una página automática de compatibilidad.

Sirve para preservar items externos añadidos a una tab vanilla por mods que no usan CTL.

Modos disponibles:

```text
BY_MOD_SECTION → una página final "Mods", una section por mod.
BY_MOD_PAGE    → una página final por mod.
```

Las fallback pages siempre aparecen al final.

Fallback no es una API de layout manual. Si un mod quiere control completo, debe usar `addonPage`, `contributePage` o `contributeSection`.

## Section

Una **section** es un grupo dentro de una page.

Se declara con:

```java
page.section(sectionId, section -> {
    section.add(Items.DIAMOND);
});
```

Una section puede tener un header visual asociado a `sectionId`.

Ruta del header:

```text
assets/<namespace>/ctl/headers/<path>.json
```

## Base section

Una **base section** es una section normal creada con:

```java
page.section(sectionId, builder);
```

Dentro de una page, las base sections se ordenan antes que addon sections.

## Addon section

Una **addon section** es una section adicional creada con:

```java
page.addonSection(sectionId, builder);
```

Aparece después de base sections.

## Contribuir vs crear

CTL diferencia entre crear contenido nuevo y contribuir a contenido existente.

Crear:

```java
.page(...)
.section(...)
```

Contribuir:

```java
.contributePage(...)
.contributeSection(...)
```

Regla práctica:

```text
page(...) / section(...) crean.
contributePage(...) / contributeSection(...) modifican algo que ya existe.
```

Si intentas crear dos pages con el mismo id, CTL lo considera error. Si quieres añadir contenido a una page existente, usa `contributePage`.

Si intentas crear dos sections con el mismo id dentro de la misma page, CTL lo considera error. Si quieres añadir entries a una section existente, usa `contributeSection`.

## Entry

Una **entry** es una unidad de contenido visible o reservada.

Tipos públicos:

```text
add(...)     → item simple.
stack(...)   → ItemStack generado por Supplier.
dynamic(...) → lista dinámica de ItemStack usando registries.
empty(...)   → slot vacío reservado.
```

Las entries pueden estar directamente en una page o dentro de una section.

## Entries directas vs entries en section

Una page puede tener entries directas:

```java
page.add(Items.DIAMOND, Items.EMERALD);
```

También puede tener sections:

```java
page.section(id("gems"), section -> {
    section.add(Items.DIAMOND, Items.EMERALD);
});
```

Las entries directas se renderizan antes de las sections.

Orden visual dentro de una page normal:

```text
Direct entries
padding hasta completar fila
Section header
Section entries
padding hasta completar fila
Next section header
...
```

## Section colapsable

Los headers de sección pueden colapsarse desde el creative inventory. Al colapsar se ocultan las entries de la sección y su header permanece visible. Es un estado de navegación de la sesión actual, no información del layout registrada por un plugin.

## Header

Un **header** es el visual asociado a una section.

Tamaño renderizado:

```text
162 x 18 px
```

Si no existe visual JSON, CTL usa el frame simple de header.

## Banner

Un **banner** es el visual asociado a una overview page.

Tamaño renderizado:

```text
162 x 90 px
```

Si no existe visual JSON, CTL usa el frame simple de banner.

## Priority

`priority` controla el orden dentro del mismo grupo.

Regla:

```text
menor priority → aparece antes
misma priority → gana insertion order
```

Esto aplica a:

```text
base pages dentro de base pages
addon pages dentro de addon pages
base sections dentro de base sections
addon sections dentro de addon sections
entries dentro de su grupo de placement
```

`priority` no mezcla grupos. Una addon page no aparece antes que una base page solo por tener menor priority.

## Occurrence

`occurrence` significa qué aparición de un target item se usará para `addBefore` o `addAfter`.

Es 1-based:

```text
1 = primera aparición
2 = segunda aparición
3 = tercera aparición
```

`0` o negativo es inválido.

Más detalles: [API.md#occurrence-en-addbefore-y-addafter](./API.md#occurrence-en-addbefore-y-addafter)

## Datagen visual

CTL incluye una API de datagen para generar JSON de headers y banners.

Paquete:

```text
com.ziver.tab_layouts.api.datagen
```

Se usa para generar archivos como:

```text
assets/<namespace>/ctl/headers/<path>.json
assets/<namespace>/ctl/banners/<path>.json
```

## Paquetes públicos vs internos

Usa solo:

```text
com.ziver.tab_layouts.api
com.ziver.tab_layouts.api.layout
com.ziver.tab_layouts.api.plugin
com.ziver.tab_layouts.api.datagen
```

No uses:

```text
com.ziver.tab_layouts.internal
com.ziver.tab_layouts.client
com.ziver.tab_layouts.mixins
```

Esos paquetes no son API estable.
