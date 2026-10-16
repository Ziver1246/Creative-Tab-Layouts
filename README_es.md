# Creative Tab Layouts

Creative Tab Layouts, o CTL, es una utilidad de modding para Minecraft que permite organizar las pestañas del inventario creativo mediante layouts estructurados.

CTL **no reemplaza el sistema de registro de creative tabs de Minecraft**. Las pestañas siguen registrándose normalmente; CTL únicamente controla cómo se presenta el contenido de las pestañas seleccionadas.

Los layouts pueden usar páginas, secciones, subtabs, headers visuales, banners, entradas ordenadas y contenido fallback para mantener inventarios creativos grandes más fáciles de navegar.

## Qué problema resuelve

Las creative tabs de Minecraft son listas simples de items. Esto funciona bien para mods pequeños, pero en mods grandes, addons o modpacks pueden volverse difíciles de navegar.

CTL permite organizar esas pestañas mediante:

- Páginas
- Subtabs
- Secciones
- Grupos ordenados de items
- Headers de sección colapsables
- Headers visuales
- Banners de página
- Contenido de addons
- Contenido fallback para items no asignados

Esto permite seguir usando creative tabs normales de Minecraft mientras su contenido se presenta de forma más estructurada.

## Idea principal

CTL controla el **layout visible** de una creative tab.

La creative tab original sigue existiendo y permanece registrada normalmente. CTL reconstruye su contenido visible a partir de una definición de layout.

Esto significa que:

- Los mods pueden seguir usando creative tabs estándar de Minecraft.
- Los plugins de CTL pueden organizar pestañas en páginas y secciones.
- Otras integraciones pueden contribuir páginas, secciones o entradas.
- Los subtabs pueden agrupar creative tabs relacionadas sin reemplazarlas.
- El contenido fallback puede preservar items que no fueron asignados explícitamente a un layout de CTL.

## Características principales

- Organización de creative tabs por páginas
- Grupos de subtabs con panel lateral y scroll
- Agrupación de items por secciones
- Headers de sección colapsables
- Headers visuales para secciones
- Banners visuales para páginas
- Visuales definidos mediante JSON
- Animaciones opcionales por sprites
- Addon pages e integraciones de compatibilidad
- Fallback pages para items no asignados
- Ordenamiento por prioridad
- Sistema semántico de plugins con integraciones `NATIVE`, `PRESET` y `COMPAT`
- Presets integrados para mods compatibles
- API pública para mods y addons
- Snapshots de layouts resueltos para interfaces externas
- Visuales de CTL reutilizables
- Soporte de datagen para visuales de CTL
- Configuración integrada dentro del juego
- Layouts vanilla integrados
- Herramientas de debug visual para desarrollo

## Estado del proyecto

Creative Tab Layouts ofrece una API pública documentada para crear layouts, registrar integraciones, contribuir contenido, consultar layouts resueltos y utilizar visuales de CTL desde interfaces externas.

La API pública está organizada bajo:

```text
com.ziver.tab_layouts.api.*
```

Los detalles de implementación están organizados bajo:

```text
com.ziver.tab_layouts.internal.*
```

Cualquier clase dentro de `internal.*` **no forma parte del contrato público de la API** y puede cambiar sin previo aviso entre versiones.

## Instalación

Instala Creative Tab Layouts como cualquier otro mod de NeoForge.

Para jugadores y modpacks, CTL puede instalarse directamente o utilizarse como dependencia de otros mods.

Para desarrolladores, agrega CTL como dependencia y utiliza únicamente la API pública documentada bajo `com.ziver.tab_layouts.api.*`.

## Documentación

La documentación para desarrolladores está disponible en [`docs/es_es/`](docs/es_es/):

- [Primeros pasos](docs/es_es/GETTING_STARTED.md)
- [Sistema de plugins](docs/es_es/PLUGINS.md)
- [Layouts](docs/es_es/LAYOUTS.md)
- [Visuales](docs/es_es/VISUALS.md)
- [Fallback](docs/es_es/FALLBACK.md)
- [Configuración](docs/es_es/CONFIG.md)
- [Migración y planificación para 2.0](docs/es_es/MIGRATION.md)

También existe documentación en inglés:

- [English documentation](docs/en_us/GETTING_STARTED.md)

## Alcance de la API pública

Solo los paquetes bajo:

```text
com.ziver.tab_layouts.api.*
```

se consideran parte del contrato público de CTL.

Los paquetes bajo:

```text
com.ziver.tab_layouts.internal.*
```

son detalles de implementación y no deben ser utilizados por mods o addons externos.

Las clases internas pueden cambiar, moverse o eliminarse sin considerarse una ruptura de la API pública.