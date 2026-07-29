# Configuración de Creative Tab Layouts

Este documento describe las opciones de configuración públicas de **Creative Tab Layouts** (CTL).

> Ejemplos: [EXAMPLES.md](./EXAMPLES.md)  
> Fallback: [FALLBACK.md](./FALLBACK.md)  
> Debug visual: [VISUALS.md#debug-visual](./VISUALS.md#debug-visual)

## Archivo de config

CTL usa una config cliente de NeoForge.

Las opciones actuales son:

```text
enableBuiltinVanillaLayouts
enableSubtabs
showCreativeConfigButton
enableDeveloperVisualDebug
enableFallbackPages
fallbackMode
```

## enableBuiltinVanillaLayouts

Default:

```text
true
```

Activa o desactiva los layouts vanilla integrados de CTL.

```text
true  → CTL organiza las tabs vanilla soportadas.
false → CTL no aplica sus layouts vanilla builtin.
```

Esto no desactiva la API CTL.

Los plugins externos pueden seguir controlando tabs propias o contribuir a tabs controladas por otros plugins.

Cambios:

```text
Aplican en runtime.
Reabre el creative inventory si necesitas refrescar la vista.
```

## enableSubtabs

Default:

```text
true
```

Activa el panel lateral de subtabs y la navegación agrupada. Cuando se desactiva, las subtabs registradas vuelven a mostrarse como creative tabs independientes normales.

## showCreativeConfigButton

Default:

```text
true
```

Muestra el botón de configuración de CTL en el creative inventory. Ocultarlo no desactiva CTL ni reinicia sus opciones. Las mismas opciones cliente siguen disponibles en el archivo de config.

## Pantalla de configuración integrada

El botón del creative inventory abre la pantalla de configuración cliente de CTL. Los cambios se guardan al modificar una opción. Las opciones que dependen de otra, como `fallbackMode`, quedan deshabilitadas visualmente mientras su función principal está desactivada.

## enableDeveloperVisualDebug

Default actual:

```text
true
```

Activa tooltips de debug visual para headers y banners.

Para ver el debug deben cumplirse todas estas condiciones:

```text
1. Entorno de desarrollo.
2. enableDeveloperVisualDebug = true.
3. Mantener Left Alt presionado.
4. Hacer hover sobre el header o banner.
```

No basta con hover normal.

El debug visual puede mostrar:

```text
- id del visual
- ruta del JSON
- JSON pretty-printed
- errores de JSON faltante, vacío, inválido o incompleto
```

En producción, el debug visual no se muestra aunque la config exista.

Nota para developers: si estás trabajando activamente en visuals, suele ser cómodo poner esta opción en `true` durante desarrollo.

## enableFallbackPages

Default:

```text
true
```

Activa o desactiva fallback pages.

```text
true  → CTL preserva items externos no reclamados.
false → CTL no genera fallback.
```

Si se desactiva, items de mods sin integración CTL podrían dejar de verse en tabs controladas.

## fallbackMode

Default:

```text
BY_MOD_SECTION
```

Valores:

```text
BY_MOD_SECTION
BY_MOD_PAGE
```

### BY_MOD_SECTION

Crea una página final llamada `Mods` y agrupa items por mod dentro de sections.

```text
Mods
├─ Example Mod
├─ Another Mod
└─ Third Mod
```

### BY_MOD_PAGE

Crea una página final por mod.

```text
Example Mod
Another Mod
Third Mod
```

`fallbackMode` solo tiene efecto si `enableFallbackPages` está activado.

## Cambios en runtime

Las opciones principales aplican en runtime.

Recomendación práctica:

```text
1. Cambia la config.
2. Cierra el creative inventory si estaba abierto.
3. Ábrelo de nuevo.
```

## Qué no cambia la config

La config no cambia:

```text
- Registro original de creative tabs.
- Registro de items.
- Código de plugins CTL.
- Archivos JSON de visuales.
```

Solo cambia cómo CTL aplica o muestra layouts y helpers.
