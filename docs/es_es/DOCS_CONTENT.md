# Contenido de la documentación de Creative Tab Layouts

Este directorio contiene la documentación en español de **Creative Tab Layouts** (CTL).

La documentación está escrita para modders, addon developers y modpack creators que quieren usar CTL desde su API pública.

> Ejemplos completos y copiables: [EXAMPLES.md](./EXAMPLES.md)  
> Preset para usar IA con CTL: [AI_PRESET.md](./AI_PRESET.md)

## Orden recomendado de lectura

Para empezar rápido:

```text
1. CONCEPTS.md
2. EXAMPLES.md
3. API.md
4. VISUALS.md
5. FALLBACK.md
6. CONFIG.md
7. AI_PRESET.md
8. ROADMAP.md
```

Para integrar un mod:

```text
1. EXAMPLES.md
2. API.md
3. VISUALS.md, si necesitas headers, banners o datagen.
4. FALLBACK.md, si necesitas entender compatibilidad con mods sin integración CTL.
```

Para crear visuales:

```text
1. VISUALS.md
2. EXAMPLES.md
3. API.md, sección Datagen API.
```

Para usar una IA con CTL:

```text
1. AI_PRESET.md
2. API.md
3. EXAMPLES.md
4. VISUALS.md, si trabajas con JSON o datagen.
```

## Archivos

### CONCEPTS.md

Explica el modelo mental de CTL:

```text
controlled tab
layout
page
overview page
base page
addon page
fallback page
section
base section
addon section
entry
header
banner
```

Es el punto de entrada para entender cómo CTL organiza una creative tab.

### API.md

Referencia principal para developers.

Cubre:

```text
paquetes públicos
@CtlPlugin
ICtlPlugin
CtlPluginContext
CtlVanillaTabs
CtlTabBuilder
CtlPageBuilder
CtlSectionBuilder
add
stack
dynamic
empty
addFirst / addLast
addBefore / addAfter
occurrence
priority
contributePage
contributeSection
lang keys
datagen API
```

### EXAMPLES.md

Ejemplos completos y copiables.

Incluye:

```text
plugin mínimo
controlTab
contributeTab
page
addonPage
contributePage
contributeSection
sections
entries directas
add
stack
dynamic
empty
positioning
occurrence
overview + banner
headers JSON
banners JSON
hide_text
sprite animation
datagen
clases datagen separadas
lang keys
plugin de prueba integral
```

Es el archivo más directo para copiar una base funcional.

### VISUALS.md

Documenta el sistema visual:

```text
headers
banners
rutas de JSON
texture
hide_text
text_color
text_top_color
text_bottom_color
label_color
text_shadow
text_align
sprite_animation
layout vertical/horizontal/grid
frames
fps
columns
animate_just_on_hover
debug visual con Left Alt
formato de color #AARRGGBB
```

También explica los estados de JSON faltante, vacío, inválido e incompleto.

### FALLBACK.md

Explica fallback pages:

```text
por qué existen
cuándo aparecen
qué items toman
qué items ignoran
claimed items
BY_MOD_SECTION
BY_MOD_PAGE
headers fallback
por qué fallback no es una API de layout
limitaciones importantes
```

### CONFIG.md

Explica las opciones públicas de configuración:

```text
enableBuiltinVanillaLayouts
enableDeveloperVisualDebug
enableFallbackPages
fallbackMode
```

También explica qué opciones aplican en runtime y cómo refrescar el creative inventory.

### AI_PRESET.md

Contiene prompts listos para usar CTL con una IA.

Sirve para:

```text
crear plugins CTL
revisar plugins CTL
crear visuales JSON
generar datagen visual
resolver dudas sin inventar API
```

### ROADMAP.md

Lista líneas de mejora del proyecto.

El roadmap no reemplaza `API.md`. La API disponible y estable está documentada en `API.md`, `VISUALS.md`, `FALLBACK.md` y `CONFIG.md`.
