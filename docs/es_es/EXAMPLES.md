# Ejemplos de Creative Tab Layouts

Este archivo contiene ejemplos correctos y copiables para usar **Creative Tab Layouts** (CTL).

> Referencia completa: [API.md](./API.md)  
> Visuales JSON: [VISUALS.md](./VISUALS.md)  
> Fallback: [FALLBACK.md](./FALLBACK.md)

## Helper id

En todos los ejemplos se usa este helper:

```java
private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath("examplemod", path);
}
```

## Plugin mínimo

```java
package com.example.examplemod;

import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;

@CtlPlugin
public final class ExampleCtlPlugin implements ICtlPlugin {

    public ExampleCtlPlugin() {
    }

    @Override
    public ResourceLocation getPluginUid() {
        return id("ctl_plugin");
    }

    @Override
    public void register(CtlPluginContext ctx) {
        ctx.info("Example CTL plugin loaded");
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("examplemod", path);
    }
}
```

## Controlar una vanilla tab

```java
@Override
public void register(CtlPluginContext ctx) {
    ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
            .page(id("materials"), page -> {
                page.add(Items.DIAMOND, Items.EMERALD);
            });
}
```

Imports principales:

```java
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import net.minecraft.world.item.Items;
```

## Integración opcional con contributeTab

Usa `contributeTab` si tu mod no debería forzar una tab a quedar controlada.

```java
@Override
public void register(CtlPluginContext ctx) {
    ctx.contributeTab(CtlVanillaTabs.INGREDIENTS).ifPresent(tab -> {
        tab.addonPage(id("example_extra_materials"), page -> {
            page.add(Items.AMETHYST_SHARD);
        });
    });
}
```

Si la tab no está controlada, no hace nada.

## Crear una page con entries directas

```java
tab.page(id("direct_entries"), page -> {
    page.add(
            Items.IRON_INGOT,
            Items.GOLD_INGOT,
            Items.DIAMOND,
            Items.EMERALD
    );
});
```

Las direct entries aparecen antes de cualquier section de esa page.

## Crear sections

```java
tab.page(id("materials"), page -> {
    page.section(id("ores"), section -> {
        section.add(
                Items.COAL,
                Items.RAW_IRON,
                Items.RAW_GOLD,
                Items.RAW_COPPER
        );
    });

    page.section(id("gems"), section -> {
        section.add(
                Items.DIAMOND,
                Items.EMERALD,
                Items.AMETHYST_SHARD
        );
    });
});
```

## add con Supplier

Útil para items de tu propio mod.

```java
page.section(id("mod_items"), section -> {
    section.add(() -> ModItems.RUBY.get());
});
```

## stack con ItemStack custom

`stack` recibe `Supplier<ItemStack>`.

```java
page.section(id("custom_stacks"), section -> {
    section.stack(() -> {
        ItemStack stack = new ItemStack(Items.DIAMOND, 4);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Custom Diamond Stack"));
        return stack;
    });
});
```

Imports:

```java
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
```

## dynamic con registries

`dynamic` recibe una función con `HolderLookup.Provider` y devuelve `List<ItemStack>`.

Ejemplo con ominous banner:

```java
page.section(id("dynamic_entries"), section -> {
    section.dynamic(registries -> List.of(
            Raid.getLeaderBannerInstance(
                    registries.lookupOrThrow(Registries.BANNER_PATTERN)
            )
    ));
});
```

Imports:

```java
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.raid.Raid;
import java.util.List;
```

## empty para alinear

```java
page.section(id("aligned"), section -> {
    section.add(Items.DIAMOND);
    section.empty(2);
    section.add(Items.EMERALD);
});
```

`empty(2)` añade dos slots vacíos visibles.

## addFirst y addLast

```java
page.section(id("ordered"), section -> {
    section.add(Items.IRON_INGOT, Items.GOLD_INGOT);

    section.addFirst(Items.NETHER_STAR);
    section.addLast(Items.BARRIER);
});
```

Resultado conceptual:

```text
NETHER_STAR
IRON_INGOT
GOLD_INGOT
BARRIER
```

## Priority en entries

Menor priority aparece antes dentro del mismo grupo.

```java
section.addFirst(100L, Items.EMERALD);
section.addFirst(-100L, Items.DIAMOND);
```

Resultado conceptual dentro de FIRST:

```text
DIAMOND
EMERALD
```

## addBefore y addAfter

```java
page.section(id("positioned"), section -> {
    section.add(Items.IRON_INGOT, Items.DIAMOND, Items.GOLD_INGOT);

    section.addBefore(Items.DIAMOND, Items.EMERALD);
    section.addAfter(Items.DIAMOND, Items.AMETHYST_SHARD);
});
```

Resultado conceptual:

```text
IRON_INGOT
EMERALD
DIAMOND
AMETHYST_SHARD
GOLD_INGOT
```

## occurrence correcto

`occurrence` es 1-based.

```java
page.section(id("occurrence"), section -> {
    section.add(
            Items.DIAMOND,
            Items.GOLD_INGOT,
            Items.DIAMOND
    );

    section.addAfter(Items.DIAMOND, 1, Items.EMERALD);
    section.addAfter(Items.DIAMOND, 2, Items.AMETHYST_SHARD);
});
```

Resultado conceptual:

```text
DIAMOND
EMERALD
GOLD_INGOT
DIAMOND
AMETHYST_SHARD
```

No uses `0`:

```java
section.addAfter(Items.DIAMOND, 0, Items.EMERALD); // inválido
```

## occurrence mayor al número real de targets

```java
section.add(Items.DIAMOND);
section.addAfter(Items.DIAMOND, 5, Items.EMERALD);
```

CTL usa la última aparición disponible.

Resultado conceptual:

```text
DIAMOND
EMERALD
```

## target inexistente

```java
section.add(Items.IRON_INGOT);
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

Si `DIAMOND` no existe como anchor normal en esa section, la entry queda no resuelta y se conserva después de las normal entries, antes de LAST entries.

Recomendación: evita targets inexistentes salvo que estés coordinando contributions entre plugins.

## Base page y addonPage

```java
tab.page(id("base_materials"), page -> {
    page.add(Items.IRON_INGOT);
});

tab.addonPage(id("addon_materials"), page -> {
    page.add(Items.AMETHYST_SHARD);
});
```

La addon page aparece después de las base pages.

## contributePage

```java
tab.page(id("materials"), page -> {
    page.section(id("gems"), section -> {
        section.add(Items.DIAMOND);
    });
});

tab.contributePage(id("materials"), page -> {
    page.add(Items.NETHERITE_INGOT);

    page.section(id("extra_materials"), section -> {
        section.add(Items.AMETHYST_SHARD);
    });
});
```

No uses `contributePage` para redefinir una section existente. Usa `contributeSection`.

## contributeSection desde tab

```java
tab.page(id("materials"), page -> {
    page.section(id("gems"), section -> {
        section.add(Items.DIAMOND);
    });
});

tab.contributeSection(id("materials"), id("gems"), section -> {
    section.add(Items.EMERALD);
});
```

## contributeSection dentro de una page

```java
tab.page(id("materials"), page -> {
    page.section(id("gems"), section -> {
        section.add(Items.DIAMOND);
    });

    page.contributeSection(id("gems"), section -> {
        section.add(Items.EMERALD);
    });
});
```

La section debe existir antes dentro del mismo builder.

## Overview con banner

```java
tab.overview(id("overview/ingredients"));
```

Archivo esperado:

```text
assets/examplemod/ctl/banners/overview/ingredients.json
```

Ejemplo JSON:

```json
{
  "texture": "examplemod:textures/gui/ctl/banners/ingredients.png"
}
```

## Header JSON básico

Section:

```java
page.section(id("materials/gems"), section -> {
    section.add(Items.DIAMOND, Items.EMERALD);
});
```

Archivo esperado:

```text
assets/examplemod/ctl/headers/materials/gems.json
```

JSON:

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/gems.png",
  "text_top_color": "#FFFFFFFF",
  "text_bottom_color": "#FFB8B8B8",
  "label_color": "#99000000",
  "text_shadow": true,
  "text_align": "left"
}
```

## Header sin texto

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/logo.png",
  "hide_text": true
}
```

## Header animado vertical

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/animated.png",
  "sprite_animation": {
    "layout": "vertical",
    "frames": 4,
    "fps": 8,
    "animate_just_on_hover": false
  },
  "text_color": "#FFFFFFFF",
  "label_color": "#99000000",
  "text_shadow": true,
  "text_align": "left"
}
```

## Header animado grid

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/grid.png",
  "sprite_animation": {
    "layout": "grid",
    "frames": 8,
    "fps": 10,
    "columns": 4
  },
  "hide_text": true
}
```

No uses `rows`. CTL calcula las filas automáticamente.

## Datagen visual mínimo

```java
package com.example.examplemod.data;

import com.ziver.tab_layouts.api.datagen.CtlHeaderVisualBuilder;
import com.ziver.tab_layouts.api.datagen.CtlVisualProvider;
import net.minecraft.data.PackOutput;

public final class ExampleVisualProvider extends CtlVisualProvider {

    public ExampleVisualProvider(PackOutput output) {
        super(output, "examplemod");
    }

    @Override
    protected void addVisuals() {
        header(
                "materials/gems",
                CtlHeaderVisualBuilder.header(modLoc("textures/gui/ctl/headers/gems.png"))
                        .splitTextColor("#FFFFFFFF", "#FFB8B8B8")
                        .labelColor("#99000000")
                        .textShadow(true)
                        .left()
        );
    }
}
```

## Datagen de banner

```java
banner(
        "overview/ingredients",
        CtlBannerVisualBuilder.banner(modLoc("textures/gui/ctl/banners/ingredients.png"))
);
```

## Datagen con sprite

```java
header(
        "materials/animated",
        CtlHeaderVisualBuilder.header(modLoc("textures/gui/ctl/headers/animated.png"))
                .spriteAnimation(CtlSpriteAnimationBuilder.vertical(4, 8))
                .textColor("#FFFFFFFF")
                .labelColor("#99000000")
                .textShadow(true)
                .left()
);
```

Imports:

```java
import com.ziver.tab_layouts.api.datagen.CtlBannerVisualBuilder;
import com.ziver.tab_layouts.api.datagen.CtlHeaderVisualBuilder;
import com.ziver.tab_layouts.api.datagen.CtlSpriteAnimationBuilder;
import com.ziver.tab_layouts.api.datagen.CtlVisualProvider;
```

## Datagen dividido en clases

Provider principal:

```java
public final class ExampleVisualProvider extends CtlVisualProvider {
    public ExampleVisualProvider(PackOutput output) {
        super(output, "examplemod");
    }

    @Override
    protected void addVisuals() {
        ExampleMaterialVisuals.register(this);
        ExampleMachineVisuals.register(this);
    }
}
```

Clase separada:

```java
public final class ExampleMaterialVisuals {
    private ExampleMaterialVisuals() {}

    public static void register(CtlVisualProvider provider) {
        provider.header(
                "materials/gems",
                CtlHeaderVisualBuilder.header(provider.modLoc("textures/gui/ctl/headers/gems.png"))
                        .textColor("#FFFFFFFF")
                        .labelColor("#99000000")
                        .textShadow(true)
                        .left()
        );
    }
}
```

## Registrar el provider en GatherDataEvent

```java
@EventBusSubscriber(modid = ExampleMod.MOD_ID)
public final class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        generator.addProvider(event.includeClient(), new ExampleVisualProvider(output));
    }
}
```

## Lang keys

Para:

```java
tab.page(id("materials"), page -> {
    page.section(id("materials/gems"), section -> {
        section.add(Items.DIAMOND);
    });
});
```

`en_us.json`:

```json
{
  "tabpage.examplemod.materials": "Materials",
  "tabsection.examplemod.materials.gems": "Gems"
}
```

`es_es.json`:

```json
{
  "tabpage.examplemod.materials": "Materiales",
  "tabsection.examplemod.materials.gems": "Gemas"
}
```
