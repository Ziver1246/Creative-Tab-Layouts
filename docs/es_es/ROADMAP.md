# Roadmap de Creative Tab Layouts

Este documento lista líneas de mejora para **Creative Tab Layouts**.

> API disponible: [API.md](./API.md)  
> Ejemplos: [EXAMPLES.md](./EXAMPLES.md)

## Disponible en la versión actual

La versión actual incluye:

```text
- API de plugins CTL.
- @CtlPlugin e ICtlPlugin.
- controlTab, controlSubtab, subtab, subtabs y contribute.
- Layouts con pages y sections.
- Grupos de subtabs y navegación mediante panel lateral.
- Sections colapsables.
- API de extensiones con layouts resueltos para interfaces externas.
- Helpers de render externo para headers y banners.
- Pantalla de configuración cliente integrada.
- Overview pages con banners.
- Headers para sections.
- Base pages y addon pages.
- Base sections y addon sections.
- contributePage.
- contributeSection.
- add, stack, dynamic y empty.
- addFirst, addLast, addBefore y addAfter.
- occurrence 1-based para positioning.
- Fallback pages.
- Fallback modes BY_MOD_SECTION y BY_MOD_PAGE.
- Config runtime.
- Visual JSON reload.
- Debug visual con Left Alt + hover.
- Datagen para visuales.
- Sprite animation vertical, horizontal y grid.
```

## Visuales vanilla adicionales

CTL ya soporta headers y banners mediante JSON y datagen.

La línea de mejora natural es ampliar el set de visuales builtin para tabs vanilla:

```text
Building Blocks
Natural Blocks
Functional Blocks
Redstone Blocks
Tools & Utilities
Combat
Ingredients
Food & Drinks
Spawn Eggs
```

## High-resolution visual sources

CTL renderiza headers y banners en tamaños fijos:

```text
Header: 162 x 18 px
Banner: 162 x 90 px
```

Una mejora futura es aceptar texturas fuente con mayor densidad que el tamaño renderizado.

Ejemplo:

```text
Header renderizado: 162 x 18
Fuente 2x:          324 x 36

Banner renderizado: 162 x 90
Fuente 2x:          324 x 180
```

Esta mejora requiere campos JSON adicionales para tamaño fuente o tamaño de frame.

## Validación visual ampliada

El debug visual actual muestra JSON, rutas, ids y errores de archivo faltante, vacío, inválido o incompleto.

Mejoras útiles para el debug visual:

```text
- Aviso explícito si la textura referenciada no existe.
- Aviso si una spritesheet tiene dimensiones inesperadas.
- Mostrar frame actual en animaciones.
- Mostrar tamaño esperado de textura.
- Copiar ids/rutas desde la UI.
```

## Fallback avanzado

La implementación actual soporta:

```text
BY_MOD_SECTION
BY_MOD_PAGE
```

Mejoras futuras para fallback:

```text
- Filtros por mod id.
- Strict mode por tab.
- Whitelist/blacklist.
- Mejor reporting de items no reclamados.
- Herramientas de diagnóstico para compatibilidad de modpacks.
```

## Extensiones de API

La API pública se mantiene pequeña y orientada a casos reales.

Extensiones útiles para versiones futuras:

```text
- Metadata adicional para pages/sections.
- Hooks más precisos de claiming.
- Matching opcional por ItemStack o components.
- Helpers de positioning para stacks si aparece una necesidad real.
- Más utilidades de datagen.
- Validación/reporting más detallado de targets inexistentes.
```

## Compatibilidad con Minecraft y NeoForge

CTL sigue las áreas que más afectan creative tabs y UI:

```text
creative tabs
ItemStack components
registries
datagen
GUI rendering
```

## Criterios de prioridad

Las mejoras se evalúan con estos criterios:

```text
1. Estabilidad.
2. Compatibilidad.
3. API limpia.
4. Comportamiento predecible.
5. Utilidad real para modders y modpack creators.
```

## No Garantizado

Las propuestas e ideas presentes en este documento no están completamente garantizas y están sujetas a descarte y/o cambios.