# Getting Started with Creative Tab Layouts

This guide shows the minimum flow for integrating a mod with **Creative Tab Layouts (CTL) 1.3.0**.

CTL does not replace Minecraft creative tabs. A CTL plugin organizes the visible content of an existing tab using pages, sections, and entries.

## 1. Create a plugin

A CTL plugin implements `ICtlPlugin` and declares its identity with `@CtlPlugin`.

```java
package com.example.examplemod.compat.ctl;

import com.example.examplemod.ExampleMod;
import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;

@CtlPlugin(
        modId = ExampleMod.MOD_ID,
        id = "layout",
        type = CtlPluginType.NATIVE,
        targets = ExampleMod.MOD_ID
)
public final class ExampleCtlPlugin implements ICtlPlugin {

    @Override
    public void register(CtlPluginContext ctx) {
        // Register layouts here.
    }
}
```

CTL discovers annotated classes automatically. The class must be constructible with a no-argument constructor.

`getPluginUid()` belongs to the previous plugin system and is deprecated. Do not implement it in new integrations.

## 2. Control a creative tab

Use `controlTab` when your integration owns the layout of that tab.

```java
@Override
public void register(CtlPluginContext ctx) {
    ctx.controlTab(ExampleCreativeTabs.MAIN.location())
            .page(id("main"), page -> {
                page.add(ExampleItems.FIRST.get(), ExampleItems.SECOND.get());
            });
}
```

A common helper:

```java
private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(ExampleMod.MOD_ID, path);
}
```

## 3. Add sections

```java
ctx.controlTab(ExampleCreativeTabs.MAIN.location())
        .page(id("materials"), page -> {
            page.section(id("metals"), section -> {
                section.add(ExampleItems.COPPER_PART.get());
                section.add(ExampleItems.STEEL_PART.get());
            });

            page.section(id("gems"), section -> {
                section.add(ExampleItems.RUBY.get());
            });
        });
```

Titles are resolved from language keys generated from the IDs:

```text
Page    examplemod:materials → tabpage.examplemod.materials
Section examplemod:metals    → tabsection.examplemod.metals
```

If the path contains `/`, CTL converts it to `.` for the language key.

## 4. Contribute to another layout

If another integration controls the tab, use `contribute` instead of `controlTab`.

```java
ctx.contribute(TargetTabs.MAIN.location()).ifPresent(tab -> {
    tab.addonPage(id("example_addon"), page -> {
        page.add(ExampleItems.EXTRA.get());
    });
});
```

`contribute` returns `Optional.empty()` when the tab is not controlled by CTL. It does not force a layout to be created.

## 5. Group tabs as subtabs

If you only want to group existing creative tabs while keeping their original content:

```java
ctx.subtabs(
        ExampleCreativeTabs.MAIN.location(),
        ExampleCreativeTabs.MACHINES.location(),
        ExampleCreativeTabs.DECORATION.location()
);
```

If you also want CTL to control the child layout:

```java
ctx.controlSubtab(ExampleCreativeTabs.MACHINES.location(), ExampleCreativeTabs.MAIN.location())
        .page(id("machines"), page -> {
            page.add(ExampleItems.MACHINE.get());
        });
```

## 6. Next steps

- [Plugins](./PLUGINS.md): `NATIVE`, `PRESET`, `COMPAT`, targets, and preset replacement.
- [Layouts](./LAYOUTS.md): pages, sections, entries, priorities, and contributions.
- [Visuals](./VISUALS.md): headers, banners, JSON, datagen, and external rendering.
- [Fallback](./FALLBACK.md): how CTL preserves unassigned items.
- [Configuration](./CONFIG.md): options available in 1.3.0.
- [Migration](./MIGRATION.md): deprecated APIs and expected changes for 2.0.0.

## Public scope

For external integrations, use only:

```text
com.ziver.tab_layouts.api.*
```

Everything under:

```text
com.ziver.tab_layouts.internal.*
```

is internal implementation and may change without being considered a public API break.
