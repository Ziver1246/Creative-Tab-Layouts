# Preset para usar IA con Creative Tab Layouts

Este archivo contiene instrucciones listas para pegar en una IA junto con la documentación de **Creative Tab Layouts**.

El objetivo es que la IA ayude a crear plugins, ejemplos, compat integrations o visuales sin inventar API ni depender de paquetes internos.

> Documentación principal: [API.md](./API.md)  
> Ejemplos: [EXAMPLES.md](./EXAMPLES.md)

## Preset general

Copia este texto y luego pega los documentos relevantes.

```text
Vas a ayudarme a trabajar con Creative Tab Layouts (CTL), una API de Minecraft/NeoForge para organizar creative tabs con pages, sections, entries, headers, banners, fallback pages y datagen visual.

Reglas estrictas:
1. Usa únicamente la API documentada.
2. No inventes clases, métodos, campos JSON, paquetes ni comportamiento.
3. No uses paquetes internal, client, mixins ni registries internos como API pública.
4. Si algo no aparece en la documentación, dilo como duda en vez de asumir.
5. Mantén nombres de clases, métodos, paquetes y campos JSON exactamente como están.
6. Usa ResourceLocation.fromNamespaceAndPath(namespace, path) en ejemplos Java.
7. Para entries normales usa add(...).
8. Para ItemStack custom usa stack(() -> itemStack).
9. Para contenido que necesita registries usa dynamic(registries -> List<ItemStack>).
10. Para addBefore/addAfter recuerda que occurrence es 1-based: 1 = primera aparición. 0 es inválido.
11. No uses rows en sprite_animation; CTL calcula rows desde frames y columns.
12. El debug visual requiere entorno de desarrollo, config activa, Left Alt presionado y hover.
13. Los colores JSON usan #AARRGGBB o #RRGGBB.
14. Fallback no es una API de layout; no tiene add, stack, dynamic ni sections manuales.
```

## Prompt para crear un plugin CTL

```text
Usando solo la API pública documentada de CTL, crea un plugin CTL para mi mod.

Datos:
- modid: <modid>
- tab objetivo: <tab>
- páginas deseadas: <lista>
- secciones deseadas: <lista>
- items por sección: <lista>

Requisitos:
- Debe implementar ICtlPlugin.
- Debe usar @CtlPlugin.
- Debe tener getPluginUid único.
- No debe usar paquetes internal.
- Debe usar helper id(String path).
- Debe incluir imports.
- Si usas addBefore/addAfter, occurrence debe ser 1-based.
```

## Prompt para revisar un plugin CTL

```text
Revisa este plugin CTL contra la documentación.

Busca:
- uso de paquetes internal
- métodos inexistentes
- occurrence 0 o negativo
- sections duplicadas
- contributePage usado para redefinir sections existentes
- contributeSection sobre pages/sections que podrían no existir
- uso incorrecto de stack o dynamic
- imports faltantes
- lang keys necesarias
- rutas esperadas de headers y banners

No inventes soluciones fuera de la API documentada.
```

## Prompt para crear visuales JSON

```text
Crea JSON visuales para CTL usando la documentación.

Requisitos:
- Headers van en assets/<namespace>/ctl/headers/<path>.json.
- Banners van en assets/<namespace>/ctl/banners/<path>.json.
- texture es obligatorio.
- Colores en #AARRGGBB.
- No uses rows en sprite_animation.
- Si el header debe ser solo imagen, usa hide_text: true.
- Si hay animación grid, usa columns y deja que CTL calcule rows.
```

## Prompt para datagen visual

```text
Crea un CtlVisualProvider para generar visuales CTL.

Requisitos:
- Extender CtlVisualProvider.
- Usar super(output, "<modid>").
- Usar header(...) y banner(...).
- Usar CtlHeaderVisualBuilder y CtlBannerVisualBuilder.
- Usar CtlSpriteAnimationBuilder si hay sprites.
- Separar en clases register(provider) si hay muchos visuales.
- No usar CtlGeneratedVisual directamente.
```

## Documentos recomendados para pasar a la IA

Para generar código:

```text
API.md
EXAMPLES.md
CONCEPTS.md
```

Para visuales:

```text
VISUALS.md
EXAMPLES.md
API.md sección datagen
```

Para fallback:

```text
FALLBACK.md
CONFIG.md
CONCEPTS.md
```

Para revisar un plugin completo:

```text
API.md
EXAMPLES.md
VISUALS.md si usa headers, banners o datagen
FALLBACK.md si toca compatibilidad con tabs vanilla
```
