# Creative Tab Layouts

Creative Tab Layouts, o CTL, es un mod/utilidad para Minecraft que permite organizar las pestañas del inventario creativo mediante layouts estructurados.

CTL **no reemplaza el sistema de registro de creative tabs de Minecraft**. Las pestañas originales siguen existiendo. Lo que hace CTL es controlar cómo se muestran los items dentro de pestañas seleccionadas, reconstruyendo su contenido visible mediante páginas, secciones, headers, banners y páginas fallback.

Está pensado para mods, addons y modpacks que necesitan una organización más clara del inventario creativo, especialmente cuando muchos items terminan dentro de una misma pestaña.

## Qué problema resuelve

Las creative tabs de Minecraft funcionan como listas simples de items. Eso es suficiente para mods pequeños, pero en mods grandes, addons o modpacks, esas listas pueden volverse difíciles de navegar.

Creative Tab Layouts permite organizar el contenido de una pestaña en:

* Páginas
* Secciones
* Headers visuales
* Banners de página
* Grupos ordenados de items
* Addon pages para compatibilidad
* Fallback pages para items externos

Esto permite que las pestañas sean más fáciles de leer y navegar sin obligar a mover todos los items a pestañas personalizadas.

## Idea principal

CTL controla el **layout visible** de una creative tab seleccionada.

La creative tab original de Minecraft sigue existiendo, pero CTL reemplaza la lista visible de items por páginas construidas por CTL.

Esto significa que:

* Los mods pueden seguir usando creative tabs normales de Minecraft.
* Los plugins de CTL pueden organizar esas tabs en páginas y secciones.
* Los addons pueden contribuir páginas, secciones o entradas adicionales.
* Las fallback pages pueden preservar items añadidos por mods que no usan CTL directamente.

## Características principales

* Organización de creative tabs por páginas
* Agrupación de items por secciones
* Headers visuales para secciones
* Banners visuales para páginas
* Visuales definidos mediante JSON
* Soporte para animaciones por sprites
* Addon pages para integraciones de compatibilidad
* Fallback pages para items añadidos por otros mods
* Ordenamiento por prioridad
* API pública para mods y addons
* Soporte de datagen para visuales de CTL
* Layouts vanilla integrados
* Herramientas de debug visual para desarrollo

## Estado del proyecto

Creative Tab Layouts cuenta con una API pública documentada para crear layouts, registrar páginas, agregar secciones, contribuir contenido y definir visuales personalizados.

La documentación se enfoca en explicar cómo usar CTL desde fuera: cómo crear layouts, cómo registrar páginas, cómo agregar secciones, cómo contribuir contenido de forma segura y cómo generar visuales mediante JSON o datagen.

Los detalles internos de implementación no forman parte del contrato público del mod. Esto incluye mixins, controllers, caches, render internals, clases internas y layouts vanilla integrados.

## Instalación

Instala Creative Tab Layouts como cualquier otro mod de Minecraft.

Para jugadores y modpacks, CTL puede instalarse como dependencia cuando un mod o modpack lo requiera.

Para desarrolladores, agrega CTL como dependencia y usa la API pública documentada.

## Documentación

La documentación completa está en la carpeta `docs/`:

* [Contenido de la documentación](docs/es_es/DOCS_CONTENT.md)
* [Conceptos](docs/es_es/CONCEPTS.md)
* [API](docs/es_es/API.md)
* [Visuales](docs/es_es/VISUALS.md)
* [Fallback Pages](docs/es_es/FALLBACK.md)
* [Configuración](docs/es_es/CONFIG.md)
* [Ejemplos](docs/es_es/EXAMPLES.md)
* [Preset para IA](docs/es_es/AI_PRESET.md)
* [Roadmap](docs/es_es/ROADMAP.md)

También existe documentación en inglés:

* [English documentation](docs/en_us/DOCS_CONTENT.md)

## Alcance de la API pública

Los paquetes `api` forman parte del contrato público de CTL y pueden ser usados por otros mods y addons.

En cambio, lo siguiente no forma parte de la API pública:

```text
internal.*
client.*
mixins.*
clases builtin de layouts vanilla
render internals
screen controllers
implementación interna del fallback
```

Estas partes pueden cambiar sin considerarse una ruptura de API pública.
