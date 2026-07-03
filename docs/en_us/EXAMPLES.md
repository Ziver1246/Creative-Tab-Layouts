# Creative Tab Layouts examples

This file contains correct copy-paste examples for using **Creative Tab Layouts** (CTL).

> Complete reference: [API.md](./API.md)  
> JSON visuals: [VISUALS.md](./VISUALS.md)  
> Fallback: [FALLBACK.md](./FALLBACK.md)

## id helper

All examples use this helper:

```java
private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath("examplemod", path);
}
```

## Minimal plugin

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

## Controlling a vanilla tab

```java
@Override
public void register(CtlPluginContext ctx) {
    ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
            .page(id("materials"), page -> {
                page.add(Items.DIAMOND, Items.EMERALD);
            });
}
```

Main imports:

```java
import com.ziver.tab_layouts.api.CtlVanillaTabs;
import net.minecraft.world.item.Items;
```

## Optional integration with contributeTab

Use `contributeTab` if your mod should not force a tab to become controlled.

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

If the tab is not controlled, it does nothing.

## Creating a page with direct entries

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

Direct entries appear before any section in that page.

## Creating sections

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

## add with Supplier

Useful for your own mod items.

```java
page.section(id("mod_items"), section -> {
    section.add(() -> ModItems.RUBY.get());
});
```

## stack with custom ItemStack

`stack` receives `Supplier<ItemStack>`.

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

## dynamic with registries

`dynamic` receives a function with `HolderLookup.Provider` and returns `List<ItemStack>`.

Example with ominous banner:

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

## empty for alignment

```java
page.section(id("aligned"), section -> {
    section.add(Items.DIAMOND);
    section.empty(2);
    section.add(Items.EMERALD);
});
```

`empty(2)` adds two visible empty slots.

## addFirst and addLast

```java
page.section(id("ordered"), section -> {
    section.add(Items.IRON_INGOT, Items.GOLD_INGOT);

    section.addFirst(Items.NETHER_STAR);
    section.addLast(Items.BARRIER);
});
```

Conceptual result:

```text
NETHER_STAR
IRON_INGOT
GOLD_INGOT
BARRIER
```

## Entry priority

Lower priority appears earlier inside the same group.

```java
section.addFirst(100L, Items.EMERALD);
section.addFirst(-100L, Items.DIAMOND);
```

Conceptual result inside FIRST:

```text
DIAMOND
EMERALD
```

## addBefore and addAfter

```java
page.section(id("positioned"), section -> {
    section.add(Items.IRON_INGOT, Items.DIAMOND, Items.GOLD_INGOT);

    section.addBefore(Items.DIAMOND, Items.EMERALD);
    section.addAfter(Items.DIAMOND, Items.AMETHYST_SHARD);
});
```

Conceptual result:

```text
IRON_INGOT
EMERALD
DIAMOND
AMETHYST_SHARD
GOLD_INGOT
```

## Correct occurrence

`occurrence` is 1-based.

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

Conceptual result:

```text
DIAMOND
EMERALD
GOLD_INGOT
DIAMOND
AMETHYST_SHARD
```

Do not use `0`:

```java
section.addAfter(Items.DIAMOND, 0, Items.EMERALD); // invalid
```

## occurrence greater than the real target count

```java
section.add(Items.DIAMOND);
section.addAfter(Items.DIAMOND, 5, Items.EMERALD);
```

CTL uses the last available appearance.

Conceptual result:

```text
DIAMOND
EMERALD
```

## Missing target

```java
section.add(Items.IRON_INGOT);
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

If `DIAMOND` does not exist as a normal anchor in that section, the entry remains unresolved and is kept after normal entries, before LAST entries.

Recommendation: avoid missing targets unless you are coordinating contributions between plugins.

## Base page and addonPage

```java
tab.page(id("base_materials"), page -> {
    page.add(Items.IRON_INGOT);
});

tab.addonPage(id("addon_materials"), page -> {
    page.add(Items.AMETHYST_SHARD);
});
```

The addon page appears after base pages.

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

Do not use `contributePage` to redefine an existing section. Use `contributeSection`.

## contributeSection from tab

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

## contributeSection inside a page

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

The section must exist earlier inside the same builder.

## Overview with banner

```java
tab.overview(id("overview/ingredients"));
```

Expected file:

```text
assets/examplemod/ctl/banners/overview/ingredients.json
```

JSON example:

```json
{
  "texture": "examplemod:textures/gui/ctl/banners/ingredients.png"
}
```

## Basic header JSON

Section:

```java
page.section(id("materials/gems"), section -> {
    section.add(Items.DIAMOND, Items.EMERALD);
});
```

Expected file:

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

## Header without text

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/logo.png",
  "hide_text": true
}
```

## Vertical animated header

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

## Grid animated header

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

Do not use `rows`. CTL calculates rows automatically.

## Minimal visual datagen

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

## Banner datagen

```java
banner(
        "overview/ingredients",
        CtlBannerVisualBuilder.banner(modLoc("textures/gui/ctl/banners/ingredients.png"))
);
```

## Datagen with sprite

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

## Datagen split into classes

Main provider:

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

Separate class:

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

## Registering the provider in GatherDataEvent

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

For:

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
