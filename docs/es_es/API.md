# API de Creative Tab Layouts

Este documento describe la API pública de **Creative Tab Layouts** (CTL) según la implementación actual.

> Ejemplos completos y copiables: [EXAMPLES.md](./EXAMPLES.md)  
> Conceptos base: [CONCEPTS.md](./CONCEPTS.md)  
> Visuales JSON y datagen: [VISUALS.md](./VISUALS.md)  
> Layouts resueltos y render externo: [EXTENSIONS.md](./EXTENSIONS.md)

## Paquetes públicos

La API pública está en:

```text
com.ziver.tab_layouts.api
com.ziver.tab_layouts.api.layout
com.ziver.tab_layouts.api.plugin
com.ziver.tab_layouts.api.datagen
```

No dependas de paquetes `internal`, clases builtin vanilla, mixins, renderers, registries internos ni clases de pantalla.

## Flujo general

```text
@CtlPlugin + ICtlPlugin
└─ register(CtlPluginContext ctx)
   ├─ ctx.controlTab(tabId)
   │  ├─ overview(pageId)
   │  ├─ page(pageId, ...)
   │  ├─ addonPage(pageId, ...)
   │  ├─ contributePage(pageId, ...)
   │  └─ contributeSection(pageId, sectionId, ...)
   ├─ ctx.controlSubtab(tabId, parentTabId)
   ├─ ctx.subtab(tabId, parentTabId)
   ├─ ctx.subtabs(parentTabId, tabIds...)
   └─ ctx.contribute(tabId)
      └─ Optional<CtlContributionBuilder>
```

Dentro de una page:

```text
page
├─ add / stack / dynamic / empty
├─ addFirst / addLast
├─ addBefore / addAfter
├─ section(...)
├─ addonSection(...)
└─ contributeSection(...)
```

Dentro de una section:

```text
section
├─ add / stack / dynamic / empty
├─ addFirst / addLast
└─ addBefore / addAfter
```

## Crear un plugin CTL

Un plugin CTL es una clase que implementa `ICtlPlugin` y está anotada con `@CtlPlugin`.

```java
package com.example.examplemod;

import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;

@CtlPlugin
public final class ExampleCtlPlugin implements ICtlPlugin {

    public ExampleCtlPlugin() {
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("examplemod", "ctl_plugin");
    }

    @Override
    public void register(CtlPluginContext ctx) {
        ctx.info("Example CTL plugin loaded");
    }
}
```

Reglas:

```text
- La clase debe implementar ICtlPlugin.
- La clase debe estar anotada con @CtlPlugin.
- Debe tener constructor sin argumentos.
- getPluginUid() debe devolver un ResourceLocation único.
- No debe depender de paquetes internal.
```

## ICtlPlugin

```java
ResourceLocation getPluginUid();

void register(CtlPluginContext ctx);
```

### getPluginUid

Identifica tu plugin CTL.

Debe ser único. Usa el namespace de tu mod.

```java
@Override
public ResourceLocation getPluginUid() {
    return ResourceLocation.fromNamespaceAndPath("examplemod", "ctl_plugin");
}
```

### register

Registra pages, sections y contributions.

```java
@Override
public void register(CtlPluginContext ctx) {
    ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
            .addonPage(id("materials"), page -> {
                page.add(Items.DIAMOND);
            });
}
```

## CtlPluginContext

Métodos públicos:

```java
CtlTabBuilder controlTab(ResourceLocation tabId);
CtlTabBuilder controlSubtab(ResourceLocation tabId, ResourceLocation parentTabId);

void subtab(ResourceLocation tabId, ResourceLocation parentTabId);
void subtabs(ResourceLocation parentTabId, ResourceLocation... tabIds);

Optional<CtlContributionBuilder> contribute(ResourceLocation tabId);

@Deprecated(forRemoval = true)
Optional<CtlTabBuilder> contributeTab(ResourceLocation tabId);

void info(String message);
void warn(String message);
void error(String message);
```

### controlTab

Crea o recupera un layout CTL para una creative tab.

```java
ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
        .page(id("materials"), page -> {
            page.add(Items.DIAMOND, Items.EMERALD);
        });
```

Cuando una tab está controlada por CTL, su contenido visible se construye desde el layout CTL.

La creative tab original de Minecraft sigue existiendo.

### contribute

Devuelve un builder limitado a contribuciones solo cuando la tab ya está controlada. No fuerza que una tab no controlada pase a estar controlada.

```java
ctx.contribute(CtlVanillaTabs.INGREDIENTS).ifPresent(tab -> {
    tab.addonPage(id("extra_materials"), page -> {
        page.add(Items.AMETHYST_SHARD);
    });
});
```

`CtlContributionBuilder` expone únicamente operaciones destinadas a integraciones de compatibilidad: `addonPage`, `contributePage` y `contributeSection`. No expone operaciones del propietario como `overview` o la creación de una `page` base.

`contributeTab` está deprecated desde 1.2.0 y se eliminará en 2.0.0. Las integraciones nuevas deben usar `contribute`.

### controlSubtab

Controla una creative tab con CTL y la registra como subtab de otra creative tab.

```java
ctx.controlSubtab(EXAMPLE_ADDON_TAB, EXAMPLE_PARENT_TAB)
        .page(id("machines"), page -> {
            page.add(ModItems.MACHINE.get());
        });
```

La parent tab no necesita estar controlada por CTL.

### subtab y subtabs

Registran creative tabs existentes como subtabs conservando sus layouts originales.

```java
ctx.subtab(EXAMPLE_ADDON_TAB, EXAMPLE_PARENT_TAB);

ctx.subtabs(EXAMPLE_PARENT_TAB, FIRST_ADDON_TAB, SECOND_ADDON_TAB);
```

Usa `controlSubtab` cuando CTL también deba construir el layout de la tab hija. Usa `subtab` o `subtabs` cuando solo necesites la agrupación visual. Las subtabs son hijas directas de una sola parent; no se soportan grupos de subtabs anidados.

## CtlContributionBuilder

Lo devuelve `CtlPluginContext#contribute`.

```java
CtlContributionBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);
CtlContributionBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);
CtlContributionBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageContributionBuilder> builder);
CtlContributionBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
```

`CtlPageContributionBuilder` se usa al contribuir a una página existente y solo expone `addonSection` y `contributeSection`. Una overview page no puede recibir contribuciones de página.

## CtlVanillaTabs

Constantes públicas para tabs vanilla comunes:

```java
CtlVanillaTabs.BUILDING_BLOCKS
CtlVanillaTabs.COLORED_BLOCKS
CtlVanillaTabs.NATURAL_BLOCKS
CtlVanillaTabs.FUNCTIONAL_BLOCKS
CtlVanillaTabs.REDSTONE_BLOCKS

CtlVanillaTabs.TOOLS_AND_UTILITIES
CtlVanillaTabs.COMBAT
CtlVanillaTabs.FOOD_AND_DRINKS
CtlVanillaTabs.INGREDIENTS
CtlVanillaTabs.SPAWN_EGGS
CtlVanillaTabs.OP_BLOCKS

CtlVanillaTabs.INVENTORY
CtlVanillaTabs.HOTBAR
CtlVanillaTabs.SEARCH
```

También puedes usar:

```java
CtlVanillaTabs.vanilla("ingredients");
```

## CtlTabBuilder

Métodos:

```java
CtlTabBuilder overview(ResourceLocation pageId);

CtlTabBuilder page(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);
CtlTabBuilder page(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

CtlTabBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);
CtlTabBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

CtlTabBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

CtlTabBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
```

### overview

Declara una overview page.

```java
tab.overview(id("overview/ingredients"));
```

La overview usa un banner visual asociado a `pageId`.

Ruta:

```text
assets/<namespace>/ctl/banners/<path>.json
```

Restricciones:

```text
- Solo puede existir una overview por tab.
- No recibe CtlPageBuilder.
- No acepta entries, sections ni layout content.
- No se puede modificar con contributePage.
```

### page

Crea una base page.

```java
tab.page(id("materials"), page -> {
    page.add(Items.DIAMOND);
});
```

Con priority:

```java
tab.page(id("materials"), 100L, page -> {
    page.add(Items.DIAMOND);
});
```

### addonPage

Crea una addon page.

```java
tab.addonPage(id("addon_materials"), page -> {
    page.add(Items.AMETHYST_SHARD);
});
```

Aparece después de base pages y antes de fallback pages.

### contributePage

Añade contenido a una page existente.

```java
tab.contributePage(id("materials"), page -> {
    page.add(Items.NETHERITE_INGOT);
});
```

Reglas:

```text
- La page debe existir.
- No puede ser overview.
- Puede añadir direct entries.
- Puede añadir nuevas sections.
- No puede redefinir una section existente; para eso usa contributeSection.
```

### contributeSection

Añade entries a una section existente.

```java
tab.contributeSection(id("materials"), id("gems"), section -> {
    section.add(Items.AMETHYST_SHARD);
});
```

Reglas:

```text
- La page debe existir.
- La section debe existir dentro de esa page.
- Mantiene la posición, tipo y priority original de la section.
- Añade entries al final lógico de esa section.
```

## CtlPageBuilder

Métodos de entries:

```java
CtlPageBuilder add(ItemLike... items);
CtlPageBuilder add(Supplier<? extends ItemLike> item);
CtlPageBuilder stack(Supplier<ItemStack> stack);
CtlPageBuilder dynamic(CtlDynamicEntries entries);
CtlPageBuilder empty();
CtlPageBuilder empty(int count);

CtlPageBuilder addFirst(ItemLike... items);
CtlPageBuilder addFirst(long priority, ItemLike... items);
CtlPageBuilder addLast(ItemLike... items);
CtlPageBuilder addLast(long priority, ItemLike... items);

CtlPageBuilder addBefore(ItemLike target, ItemLike... items);
CtlPageBuilder addBefore(ItemLike target, long priority, ItemLike... items);
CtlPageBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);
CtlPageBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

CtlPageBuilder addAfter(ItemLike target, ItemLike... items);
CtlPageBuilder addAfter(ItemLike target, long priority, ItemLike... items);
CtlPageBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);
CtlPageBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);
```

Métodos de sections:

```java
CtlPageBuilder section(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
CtlPageBuilder section(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

CtlPageBuilder addonSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
CtlPageBuilder addonSection(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

CtlPageBuilder contributeSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
```

## CtlSectionBuilder

Tiene los mismos métodos de entries que `CtlPageBuilder`, pero no puede crear subsections.

```java
CtlSectionBuilder add(ItemLike... items);
CtlSectionBuilder add(Supplier<? extends ItemLike> item);
CtlSectionBuilder stack(Supplier<ItemStack> stack);
CtlSectionBuilder dynamic(CtlDynamicEntries entries);
CtlSectionBuilder empty();
CtlSectionBuilder empty(int count);

CtlSectionBuilder addFirst(ItemLike... items);
CtlSectionBuilder addFirst(long priority, ItemLike... items);
CtlSectionBuilder addLast(ItemLike... items);
CtlSectionBuilder addLast(long priority, ItemLike... items);

CtlSectionBuilder addBefore(ItemLike target, ItemLike... items);
CtlSectionBuilder addBefore(ItemLike target, long priority, ItemLike... items);
CtlSectionBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);
CtlSectionBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

CtlSectionBuilder addAfter(ItemLike target, ItemLike... items);
CtlSectionBuilder addAfter(ItemLike target, long priority, ItemLike... items);
CtlSectionBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);
CtlSectionBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);
```

## Tipos de entry

### add(ItemLike...)

Usa `add` para items simples.

```java
section.add(
        Items.IRON_INGOT,
        Items.GOLD_INGOT,
        Items.DIAMOND
);
```

Cada item se convierte en un `ItemStack` normal de count 1 cuando la page se construye.

### add(Supplier<? extends ItemLike>)

Útil para items registrados por tu mod.

```java
section.add(() -> ModItems.RUBY.get());
```

El supplier se evalúa cuando CTL construye la page.

Si el supplier devuelve `null`, la entry se vuelve vacía y normalmente no se muestra.

### stack(Supplier<ItemStack>)

Usa `stack` cuando necesitas un `ItemStack` concreto.

```java
section.stack(() -> {
    ItemStack stack = new ItemStack(Items.DIAMOND, 4);
    stack.set(DataComponents.CUSTOM_NAME, Component.literal("Custom Diamond Stack"));
    return stack;
});
```

`stack` sirve para:

```text
- custom count
- custom name
- components
- damage
- custom model data si tu versión lo usa mediante components
- cualquier ItemStack construido manualmente
```

CTL copia el stack antes de insertarlo.

Si el supplier devuelve `null` o `ItemStack.EMPTY`, no se muestra.

### dynamic(CtlDynamicEntries)

Usa `dynamic` cuando necesitas generar entries con acceso a registries.

```java
section.dynamic(registries -> {
    return List.of(
            Raid.getLeaderBannerInstance(
                    registries.lookupOrThrow(Registries.BANNER_PATTERN)
            )
    );
});
```

`dynamic` sirve para:

```text
- stacks que dependen de registries
- contenido sensible a datapacks
- banner patterns
- enchanted books
- goat horns
- listas generadas en build time
```

La función devuelve `List<ItemStack>`.

Reglas:

```text
- Si devuelve null, no añade nada.
- Si devuelve lista vacía, no añade nada.
- Si contiene null, CTL lo trata como ItemStack.EMPTY y normalmente no se muestra.
- CTL copia cada stack antes de insertarlo.
```

Para posicionamiento, `dynamic` no es el mejor anchor porque puede generar varios stacks. Si necesitas un target estable para `addBefore` o `addAfter`, usa una entry normal con `add` como anchor.

### empty()

Añade un slot vacío visible.

```java
section.empty();
section.empty(3);
```

Reglas:

```text
empty() equivale a empty(1).
empty(0) no añade nada.
empty(count < 0) lanza excepción.
```

`empty` se mantiene aunque sea `ItemStack.EMPTY`. Sirve para reservar espacio o alinear contenido.

## Posicionamiento de entries

CTL soporta varios grupos de posicionamiento:

```text
FIRST
NORMAL
BEFORE
AFTER
LAST
```

Orden conceptual:

```text
FIRST entries
NORMAL entries con BEFORE/AFTER resueltos alrededor
unresolved BEFORE entries
unresolved AFTER entries
LAST entries
```

Dentro de cada grupo, CTL ordena por:

```text
priority ascendente → insertion order
```

### addFirst

Coloca entries antes de las normal entries.

```java
section.addFirst(Items.NETHER_STAR);
section.addFirst(-100L, Items.DRAGON_EGG);
```

Menor priority aparece antes dentro del grupo FIRST.

### addLast

Coloca entries después de normal entries y entries posicionadas no resueltas.

```java
section.addLast(Items.BARRIER);
section.addLast(100L, Items.COMMAND_BLOCK);
```

Menor priority aparece antes dentro del grupo LAST.

### addBefore

Añade items antes de un target.

```java
section.add(Items.IRON_INGOT, Items.DIAMOND);
section.addBefore(Items.DIAMOND, Items.EMERALD);
```

Resultado conceptual:

```text
IRON_INGOT
EMERALD
DIAMOND
```

### addAfter

Añade items después de un target.

```java
section.add(Items.IRON_INGOT, Items.DIAMOND);
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

Resultado conceptual:

```text
IRON_INGOT
DIAMOND
EMERALD
```

### Métodos de positioning solo aceptan ItemLike

La API actual de positioning acepta `ItemLike... items`.

No existen métodos públicos como:

```java
stackBefore(...)
dynamicBefore(...)
stackAfter(...)
dynamicAfter(...)
```

Si necesitas posicionar algo complejo, usa un item anchor simple y luego ajusta el layout con entries normales, sections o separación visual.

## Occurrence en addBefore y addAfter

`occurrence` indica qué aparición del target se usa.

Es **1-based**:

```java
addBefore(target, 1, items); // antes de la primera aparición del target
addBefore(target, 2, items); // antes de la segunda aparición del target

addAfter(target, 1, items);  // después de la primera aparición del target
addAfter(target, 2, items);  // después de la segunda aparición del target
```

`0` o negativo es inválido:

```java
section.addAfter(Items.DIAMOND, 0, Items.EMERALD);  // inválido
section.addAfter(Items.DIAMOND, -1, Items.EMERALD); // inválido
```

Las sobrecargas sin `occurrence` usan `1`:

```java
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

equivale a:

```java
section.addAfter(Items.DIAMOND, 1, Items.EMERALD);
```

### Ejemplo con occurrence

```java
section.add(
        Items.IRON_INGOT,
        Items.DIAMOND,
        Items.GOLD_INGOT,
        Items.DIAMOND
);

section.addAfter(Items.DIAMOND, 1, Items.EMERALD);
section.addAfter(Items.DIAMOND, 2, Items.AMETHYST_SHARD);
```

Resultado conceptual:

```text
IRON_INGOT
DIAMOND
EMERALD
GOLD_INGOT
DIAMOND
AMETHYST_SHARD
```

### Si occurrence es mayor que las apariciones reales

La implementación actual limita la ocurrencia al último target existente.

Ejemplo:

```java
section.add(Items.DIAMOND);
section.addAfter(Items.DIAMOND, 5, Items.EMERALD);
```

Solo hay un `DIAMOND`. CTL usa la última aparición disponible, que en este caso es la primera.

Resultado conceptual:

```text
DIAMOND
EMERALD
```

### Si el target no existe

Si el target no existe dentro de las normal entries, CTL no puede resolver la posición exacta.

La implementación actual conserva esas entries y las coloca después de las normal entries, antes de las LAST entries:

```text
normal entries
unresolved BEFORE entries
unresolved AFTER entries
LAST entries
```

Recomendación: no dependas de targets inexistentes. Úsalos solo si sabes que otro plugin añadirá el anchor.

### Scope del positioning

El positioning se resuelve dentro del builder donde se declara.

En una page:

```java
page.addAfter(Items.DIAMOND, Items.EMERALD);
```

busca targets en las entries directas de esa page.

En una section:

```java
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

busca targets dentro de esa section.

No cruza entre page entries y section entries.

## Sections

### section

Crea una base section.

```java
page.section(id("gems"), section -> {
    section.add(Items.DIAMOND, Items.EMERALD);
});
```

Con priority:

```java
page.section(id("gems"), -100L, section -> {
    section.add(Items.DIAMOND);
});
```

### addonSection

Crea una addon section.

```java
page.addonSection(id("extra_gems"), section -> {
    section.add(Items.AMETHYST_SHARD);
});
```

Las addon sections aparecen después de base sections.

### contributeSection en page builder

Contribuye a una section creada antes en la misma page builder.

```java
page.section(id("gems"), section -> {
    section.add(Items.DIAMOND);
});

page.contributeSection(id("gems"), section -> {
    section.add(Items.EMERALD);
});
```

Si la section no existe todavía en ese builder, lanza excepción.

## Orden de pages y sections

Orden de pages:

```text
overview pages
base pages
addon pages
fallback pages
```

Dentro de base pages y addon pages:

```text
priority ascendente → insertion order
```

Orden de sections dentro de una page:

```text
base sections
addon sections
```

Dentro de cada grupo:

```text
priority ascendente → insertion order
```

## Lang keys

CTL genera títulos por convención.

Para page id:

```text
examplemod:materials/gems
```

key esperada:

```text
tabpage.examplemod.materials.gems
```

Para section id:

```text
examplemod:materials/ores
```

key esperada:

```text
tabsection.examplemod.materials.ores
```

Ejemplo `en_us.json`:

```json
{
  "tabpage.examplemod.materials.gems": "Gems",
  "tabsection.examplemod.materials.ores": "Ores"
}
```

## Datagen API

Paquete:

```text
com.ziver.tab_layouts.api.datagen
```

Clases públicas:

```java
CtlVisualProvider
CtlHeaderVisualBuilder
CtlBannerVisualBuilder
CtlSpriteAnimationBuilder
```

`CtlGeneratedVisual` es package-private y no debe usarse directamente.

### CtlVisualProvider

Provider base para generar JSON de headers y banners.

Métodos útiles:

```java
public ResourceLocation modLoc(String path);
public ResourceLocation mcLoc(String path);

public void header(ResourceLocation id, CtlHeaderVisualBuilder builder);
public void header(String path, CtlHeaderVisualBuilder builder);

public void banner(ResourceLocation id, CtlBannerVisualBuilder builder);
public void banner(String path, CtlBannerVisualBuilder builder);
```

Ejemplo mínimo:

```java
public final class ExampleVisualProvider extends CtlVisualProvider {
    public ExampleVisualProvider(PackOutput output) {
        super(output, "examplemod");
    }

    @Override
    protected void addVisuals() {
        header(
                "materials/gems",
                CtlHeaderVisualBuilder.header(modLoc("textures/gui/ctl/headers/gems.png"))
                        .textColor("#FFFFFFFF")
                        .labelColor("#99000000")
                        .textShadow(true)
                        .left()
        );
    }
}
```

Más detalles: [VISUALS.md](./VISUALS.md)

## API de extensiones

`CtlApiExtensions` expone vistas inmutables y resueltas de los layouts de CTL, además de helpers cliente para renderizar headers y banners de CTL en interfaces externas. Está pensada para integraciones como recipe viewers o navegadores personalizados de tabs.

Consulta [EXTENSIONS.md](./EXTENSIONS.md) para ver el contrato completo y ejemplos.

## Compatibilidad y estabilidad

Reglas para addons:

```text
- Usa solo paquetes api.*.
- No uses internal.*.
- No dependas del orden exacto de vanilla builtin layouts si no estás integrando con esos ids directamente.
- Usa contributeTab para compat opcional.
- Usa addonPage para contenido propio.
- Usa contributeSection para añadir entries a una section existente.
- No uses fallback como integración principal.
```
