# Fallback pages en Creative Tab Layouts

Este documento explica el sistema fallback de **Creative Tab Layouts** (CTL).

> Ejemplos de integración: [EXAMPLES.md](./EXAMPLES.md)  
> Config relacionada: [CONFIG.md](./CONFIG.md)

## Por qué existe fallback

Cuando CTL controla una creative tab, el contenido visible se reconstruye desde páginas y secciones CTL.

Sin fallback, items añadidos por otros mods mediante el sistema vanilla podrían dejar de verse en esa tab.

Fallback existe como red de seguridad para preservar contenido de mods sin integración CTL.

## Qué hace fallback

Fallback detecta items externos presentes en el contenido original de la tab y los coloca al final del layout CTL.

Orden general:

```text
Overview Page
Base Pages
Addon Pages
Fallback Pages
```

## Cuándo aparece fallback

Fallback puede aparecer cuando:

```text
- La tab está controlada por CTL.
- enableFallbackPages está activado.
- Existen items externos en el contenido original de la tab.
- Esos items no fueron reclamados por páginas CTL.
- Esos items no pertenecen al namespace minecraft.
```

Si no hay contenido externo que preservar, no aparece fallback visible.

## Qué items toma

Toma items externos añadidos a la tab original mediante mecanismos vanilla o eventos normales de creative tabs.

Ejemplo:

```text
examplemod:ruby aparece en minecraft:ingredients.
CTL controla minecraft:ingredients.
examplemod:ruby no está en ninguna página CTL.
→ fallback puede mostrar examplemod:ruby.
```

## Qué items ignora

Ignora:

```text
- Items del namespace minecraft.
- Items ya reclamados por CTL.
- Duplicados por Item.
- Stacks vacíos.
```

## Claimed items

Un item se considera reclamado si aparece en páginas normales o addon pages de CTL.

La comparación se hace por `Item`, no por `ItemStack` exacto.

Consecuencia:

```text
Si CTL ya muestra examplemod:ruby una vez,
fallback no añade otro examplemod:ruby aunque el stack original tenga otro nombre, count o components.
```

Esto evita duplicados, pero significa que fallback no es un sistema de preservación exacta de stacks complejos.

## Agrupación por mod

Los items fallback se agrupan por namespace del item.

```text
examplemod:ruby       → examplemod
anothermod:copper_rod → anothermod
```

CTL intenta mostrar el nombre público del mod mediante `ModList`. Si no está disponible, usa el mod id.

## Modos de fallback

La implementación actual soporta dos modos.

### BY_MOD_SECTION

Una sola página final llamada `Mods`.

Dentro, CTL crea una section por mod.

```text
Fallback Page: Mods
├─ Section: Example Mod
├─ Section: Another Mod
└─ Section: Third Mod
```

Es el modo recomendado por defecto porque ocupa menos páginas.

### BY_MOD_PAGE

Una página final por mod.

```text
Fallback Page: Example Mod
Fallback Page: Another Mod
Fallback Page: Third Mod
```

Puede ser más claro cuando un mod tiene muchos items.

## Config

Opciones relevantes:

```text
enableFallbackPages = true / false
fallbackMode = BY_MOD_SECTION / BY_MOD_PAGE
```

`fallbackMode` solo tiene efecto si `enableFallbackPages` está activado.

Los cambios aplican en runtime. Reabre el creative inventory si necesitas refrescar la vista.

## Headers visuales para fallback

Las fallback sections también pueden tener headers visuales.

Para una fallback section con id:

```text
examplemod:fallback/minecraft/ingredients
```

CTL buscará:

```text
assets/examplemod/ctl/headers/fallback/minecraft/ingredients.json
```

Esto permite que un mod personalice su sección fallback en una tab vanilla concreta.

## Fallback no es una API de layout

Fallback es automático.

No expone:

```text
add
stack
dynamic
empty
section
addonSection
priority
positioning
```

Si un mod quiere control completo, debe usar la API CTL:

```java
ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
        .addonPage(id("materials"), page -> {
            page.section(id("special"), section -> {
                section.add(Items.DIAMOND);
                section.stack(() -> customStack());
                section.dynamic(registries -> createDynamicStacks(registries));
            });
        });
```

Regla:

```text
Fallback no define layout.
Fallback recupera contenido sin integración CTL.
```

## Por qué fallback no tiene stack ni dynamic

`stack` y `dynamic` son APIs declarativas. Requieren intención del mod:

```text
qué stack crear
dónde ponerlo
con qué prioridad
si necesita registries
cómo deduplicarlo
```

Fallback no tiene esa intención. Solo observa el contenido original de la tab y preserva items no reclamados.

Si necesitas `stack` o `dynamic`, escribe un plugin CTL.

## Limitaciones importantes

Fallback:

```text
- No garantiza layout bonito.
- No conoce intención del mod.
- No preserva necesariamente variantes exactas si el mismo Item ya fue reclamado.
- No sustituye una integración real.
- Siempre aparece al final.
```

## Recomendación para mods

Si tu mod quiere integrarse bien:

```text
- Usa @CtlPlugin.
- Usa contributeTab si la integración debe ser opcional.
- Usa addonPage para contenido propio.
- Usa contributePage o contributeSection para integrarte en layouts existentes.
- No dependas del fallback como integración principal.
```

Fallback está para que nada importante desaparezca, no para diseñar el layout final.
