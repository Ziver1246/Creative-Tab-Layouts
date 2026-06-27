# Creative Tab Layouts

Creative Tab Layouts, or CTL, is a Minecraft modding utility that adds structured layouts to creative mode tabs.

CTL does **not** replace Minecraft's creative tab registration system. The original creative tabs still exist. Instead, CTL takes control of how items are displayed inside selected creative tabs and rebuilds their visible content using pages, sections, headers, banners, and fallback pages.

It is designed for mods and modpacks that need cleaner creative inventory organization, especially when many items are added to the same tab.

## What CTL Solves

Minecraft creative tabs are simple item lists. This works well for small mods, but large mods, addons, and modpacks can quickly make tabs difficult to navigate.

Creative Tab Layouts helps solve this by allowing controlled tabs to be organized into:

* Pages
* Sections
* Visual headers
* Page banners
* Ordered item groups
* Addon pages
* Fallback pages for external mod items

This makes creative tabs easier to browse without requiring every item to be moved into custom tabs.

## Core Idea

CTL controls the **displayed layout** of selected creative tabs.

The original Minecraft creative tab still exists, but CTL replaces its visible item list with CTL-built pages.

This means:

* Mods can keep using normal Minecraft creative tabs.
* CTL plugins can organize those tabs into pages and sections.
* Addons can contribute additional pages, sections, or entries.
* Fallback pages can preserve items added by mods that do not use CTL directly.

## Features

* Page-based creative tab organization
* Section-based item grouping
* Visual headers for sections
* Visual banners for pages
* JSON-driven visuals
* Optional sprite animations
* Addon pages for compatibility integrations
* Fallback pages for external mod items
* Priority-based ordering
* Public API for mods and addons
* Built-in vanilla tab layouts
* Developer visual debug tools

## Project Status

Creative Tab Layouts is under active development.

The public API is intended to be stable enough for external use, but some features may still evolve before future releases. Internal implementation details such as mixins, render internals, controllers, caches, and built-in vanilla layout classes are not part of the public API.

Documentation focuses on the public contract: how to use CTL, how to structure layouts, and how addons should contribute safely.

## Installation

Install Creative Tab Layouts like a normal Minecraft mod.

For players and modpack users, CTL can be installed as a client-side utility when required by a mod or modpack.

For developers, add CTL as a dependency and use its public API packages:

```text
com.ziver.tab_layouts.api
com.ziver.tab_layouts.api.layout
com.ziver.tab_layouts.api.plugin
```

The recommended mod metadata license value is:

```toml
license="LGPL-3.0-or-later"
```

## Minimal Plugin Example

```java
package com.example.examplemod;

import com.ziver.tab_layouts.CreativeTabLayouts;
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

This example controls the vanilla Ingredients tab and adds a page with a single section.

## Documentation

Extended documentation is available in the `docs/` directory:

* [Concepts](docs/CONCEPTS.md)
  Explains CTL terminology such as controlled tabs, pages, sections, entries, banners, headers, priorities, and fallback pages.

* [API](docs/API.md)
  Developer guide for creating CTL plugins and contributing pages, sections, and entries.

* [Visuals](docs/VISUALS.md)
  Guide for JSON-driven headers, banners, textures, colors, text alignment, and sprite animations.

* [Fallback Pages](docs/FALLBACK.md)
  Explains how CTL preserves external mod items added to controlled vanilla tabs.

* [Config](docs/CONFIG.md)
  Documents user-facing configuration options.

* [Examples](docs/EXAMPLES.md)
  Copy-ready snippets for common CTL use cases.

## Public API Scope

The documented public API is limited to:

```text
com.ziver.tab_layouts.api
com.ziver.tab_layouts.api.layout
com.ziver.tab_layouts.api.plugin
```

The following are not part of the public API and should not be relied on by external mods:

```text
internal.*
client.*
mixins.*
built-in vanilla layout classes
render internals
screen controllers
fallback implementation internals
```

These internal details may change without being considered a public API break.

## License

Creative Tab Layouts is licensed under the GNU Lesser General Public License v3.0 or later.

```text
SPDX-License-Identifier: LGPL-3.0-or-later
```

Mods may use Creative Tab Layouts as a dependency without being required to use the LGPL license themselves.

Modified versions of Creative Tab Layouts itself must comply with the LGPL license terms.
