# Configuración

CTL 1.3.0 incluye una pantalla de configuración cliente accesible desde los inventarios creativo y de supervivencia cuando sus botones están habilitados.

Las opciones actuales son las siguientes.

## Built-in vanilla layouts

```text
enableBuiltinVanillaLayouts = true
```

Activa los layouts vanilla incluidos con CTL.

Desactivarlos no desactiva la API de CTL. Plugins externos pueden seguir controlando sus propias tabs.

El cambio se consulta en runtime; si la interfaz ya está abierta puede ser necesario reabrir el inventario creativo.

## Subtabs

```text
enableSubtabs = true
```

Activa la agrupación visual de subtabs en la Creative Inventory.

Cuando está desactivado, las creative tabs registradas siguen existiendo y aparecen como tabs independientes.

## Botón de configuración en creativo

```text
showCreativeConfigButton = true
```

Muestra el acceso a configuración de CTL desde el inventario creativo.

## Botón de configuración en supervivencia

```text
showSurvivalConfigButton = true
```

Muestra el acceso a configuración de CTL desde el inventario de supervivencia.

## Debug visual

```text
enableDeveloperVisualDebug = true
```

Habilita ayudas de desarrollo relacionadas con headers y banners, como metadata o rutas de recursos cuando el renderer lo permite.

No forma parte del contrato de layout y puede cambiar como herramienta de desarrollo.

## Fallback pages

```text
enableFallbackPages = true
```

Activa el contenido fallback para items presentes en la creative tab original que no fueron reclamados por páginas CTL.

Esto aplica tanto a vanilla como a mods.

Consulta [FALLBACK.md](./FALLBACK.md) para el comportamiento completo.

## Fallback mode

```text
fallbackMode = BY_MOD_SECTION
```

Valores actuales:

```text
BY_MOD_SECTION
BY_MOD_PAGE
```

`BY_MOD_SECTION` crea una página `Mods` con una sección por mod.

`BY_MOD_PAGE` crea una página final independiente por mod.

Solo tiene efecto cuando las fallback pages están activadas.

## Configuración y API pública

La configuración interna de CTL no forma parte de la nueva API pública estructurada.

Las integraciones deberían consultar comportamiento efectivo mediante APIs como `CtlViews` cuando corresponda, en vez de depender de campos internos de configuración.

`CtlApiExtensions` conserva temporalmente algunos accesos antiguos por compatibilidad 1.x, pero está deprecated y será eliminado en 2.0.0.
