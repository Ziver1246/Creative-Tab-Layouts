# Creative Tab Layouts

Creative Tab Layouts, o CTL, es un mod/utilidad para Minecraft que permite organizar las pestañas del inventario creativo mediante layouts estructurados.

CTL **no reemplaza el sistema de registro de creative tabs de Minecraft**. Las pestañas originales siguen existiendo. Lo que hace CTL es tomar control de cómo se muestran los items dentro de pestañas seleccionadas, reconstruyendo su contenido visible mediante páginas, secciones, headers, banners y páginas fallback.

Está pensado para mods, addons y modpacks que necesitan una organización más clara del inventario creativo, especialmente cuando muchos items terminan dentro de una misma pestaña.

## Qué problema resuelve

Las creative tabs de Minecraft funcionan como listas simples de items. Eso es suficiente para mods pequeños, pero en mods grandes, addons o modpacks, esas listas pueden volverse difíciles de navegar.

Creative Tab Layouts permite organizar el contenido de una pestaña en:

* Páginas.
* Secciones.
* Headers visuales.
* Banners de página.
* Grupos ordenados de items.
* Addon pages para compatibilidad.
* Fallback pages para items externos.

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

* Organización de creative tabs por páginas.
* Agrupación de items por secciones.
* Headers visuales para secciones.
* Banners visuales para páginas.
* Visuales definidos mediante JSON.
* Soporte para animaciones por sprites.
* Addon pages para integraciones de compatibilidad.
* Fallback pages para items añadidos por otros mods.
* Ordenamiento por prioridad.
* API pública para mods y addons.
* Layouts vanilla integrados.
* Herramientas de debug visual para desarrollo.

## Estado del proyecto

Creative Tab Layouts está en desarrollo activo.

La API pública está pensada para ser suficientemente estable para uso externo, pero algunas características pueden evolucionar en futuras versiones. Los detalles internos de implementación no forman parte del contrato público del mod.

La documentación se enfoca en explicar cómo usar CTL desde fuera: cómo crear layouts, cómo registrar páginas, cómo agregar secciones y cómo contribuir contenido de forma segura.

No se documentan detalles internos como mixins, controllers, caches, render internals o clases internas que puedan cambiar sin romper la API pública.

## Instalación

Instala Creative Tab Layouts como cualquier otro mod de Minecraft.

Para jugadores y modpacks, CTL puede instalarse como dependencia cuando un mod o modpack lo requiera.

Para desarrolladores, agrega CTL como dependencia y usa sus paquetes públicos de API:

```text id="cy3b9w"
com.ziver.tab_layouts.api
com.ziver.tab_layouts.api.layout
com.ziver.tab_layouts.api.plugin
```

La licencia recomendada en `mods.toml` es:

```toml id="s9r4a1"
license="LGPL-2.1-or-later"
```

## Ejemplo mínimo

```java id="gglqcb"
package com.example.examplemod;

import com.ziver.tab_layouts.api.CtlVanillaTabs;
import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

@CtlPlugin
public final class ExampleCtlPlugin implements ICtlPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("examplemod", "ctl_plugin");
    }

    @Override
    public void register(CtlPluginContext ctx) {
        ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
                .overview(ResourceLocation.fromNamespaceAndPath("examplemod", "materials"))
                .page(ResourceLocation.fromNamespaceAndPath("examplemod", "materials"), page -> {
                    page.section(ResourceLocation.fromNamespaceAndPath("examplemod", "gems"), section -> {
                        section.add(
                                Items.DIAMOND,
                                Items.EMERALD,
                                Items.AMETHYST_SHARD
                        );
                    });
                });
    }
}
```

Este ejemplo controla la pestaña vanilla de ingredientes y agrega una página con una sección simple.

## Documentación extendida

La documentación completa está en la carpeta `docs/`:

* [Conceptos](docs/CONCEPTS.md)
  Explica la terminología principal de CTL: controlled tabs, layouts, pages, sections, entries, headers, banners, prioridades y fallback pages.

* [API](docs/API.md)
  Guía para developers que quieren crear plugins de CTL, controlar tabs y agregar páginas, secciones o entries.

* [Visuales](docs/VISUALS.md)
  Explica headers, banners, archivos JSON visuales, texturas, colores, sombras y animaciones.

* [Fallback Pages](docs/FALLBACK.md)
  Explica cómo CTL preserva items añadidos a tabs vanilla por mods que no usan CTL directamente.

* [Configuración](docs/CONFIG.md)
  Documenta las opciones públicas de configuración.

* [Ejemplos](docs/EXAMPLES.md)
  Snippets listos para copiar, adaptar y usar.

## Alcance de la API pública

La API pública documentada se limita a:

```text id="zzv0dm"
com.ziver.tab_layouts.api
com.ziver.tab_layouts.api.layout
com.ziver.tab_layouts.api.plugin
```

Estas áreas sí pueden ser usadas por otros mods y addons.

En cambio, lo siguiente no forma parte de la API pública:

```text id="ixz8z4"
internal.*
client.*
mixins.*
clases builtin de layouts vanilla
render internals
screen controllers
implementación interna del fallback
```

Estas partes pueden cambiar sin considerarse una ruptura de API pública.

## Licencia

Creative Tab Layouts está licenciado bajo GNU Lesser General Public License v2.1 o posterior.

```text id="cbb9im"
SPDX-License-Identifier: LGPL-2.1-or-later
```

Otros mods pueden usar Creative Tab Layouts como dependencia sin estar obligados a usar la licencia LGPL.

Las versiones modificadas de Creative Tab Layouts deben cumplir con los términos de la licencia LGPL.
