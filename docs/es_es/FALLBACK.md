# Fallback

El fallback evita que contenido original de una creative tab desaparezca cuando CTL controla su layout pero algunos items no fueron asignados explícitamente.

## Idea

CTL compara:

```text
items originales de la creative tab
-
items reclamados por las páginas CTL
=
items no reclamados
```

Los items no reclamados pueden aparecer al final mediante contenido fallback.

Desde 1.3.0 esto incluye tanto contenido vanilla como contenido de mods. Minecraft ya no se excluye del fallback.

## Qué significa "reclamado"

CTL construye las páginas declaradas del layout y considera reclamados los `Item` presentes en esas páginas.

El fallback trabaja a nivel de `Item`, no como un sistema completo de identidad de `ItemStack` con components. Por ello no debe usarse como sustituto de una integración CTL intencional.

## Modos

La configuración actual ofrece dos modos.

### BY_MOD_SECTION

Crea una página final `Mods` y agrupa el contenido no reclamado en secciones por namespace/mod.

```text
Mods
├─ Minecraft
├─ Create
├─ Example Mod
└─ ...
```

### BY_MOD_PAGE

Crea una página fallback independiente por mod/namespace.

```text
Minecraft
Create
Example Mod
...
```

## Orden

El fallback se añade después de las páginas CTL declaradas.

No participa como una página base o addon creada manualmente por plugins.

## Fallback y layouts reales

Fallback es una red de seguridad, no una API para diseñar contenido.

No ofrece operaciones como:

```text
add
stack
dynamic
section
priority
addBefore
addAfter
```

Si conoces el contenido que quieres integrar, crea o contribuye a un layout CTL normal.

## Views externas

`CtlViews.getTabView(tabId, registries)` devuelve las páginas CTL declaradas.

Para que el snapshot incluya fallback, usa la sobrecarga que también recibe los items originales de la creative tab:

```java
CtlViews.getTabView(tabId, registries, originalItems);
```

Las páginas fallback aparecen como:

```text
CtlPageView.Type.FALLBACK
```

y sus secciones como:

```text
CtlSectionView.Type.FALLBACK
```

## Limitaciones

Fallback no conoce la intención del mod que añadió el item.

Por diseño:

- prioriza no perder contenido;
- agrupa automáticamente por namespace/mod;
- no garantiza un layout visual ideal;
- no sustituye `NATIVE`, `PRESET` o `COMPAT`;
- puede perder distinción entre variantes si el mismo `Item` ya fue reclamado por el layout.

## Recomendación

Para una integración real:

```text
NATIVE → layout oficial del propio mod
PRESET → integración de respaldo para un mod sin NATIVE
COMPAT → contribución entre varios mods
```

Usa fallback únicamente como protección para contenido no asignado.
