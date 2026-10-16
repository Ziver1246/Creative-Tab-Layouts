# Creative Tab Layouts

Creative Tab Layouts, or CTL, is a Minecraft modding utility for organizing creative mode tabs with structured layouts.

CTL does **not** replace Minecraft's creative tab registration system. Creative tabs remain registered normally; CTL only controls how the contents of selected tabs are presented.

Layouts can use pages, sections, subtabs, visual headers, banners, ordered entries, and fallback content to keep large creative inventories easier to navigate.

## What CTL Solves

Minecraft creative tabs are simple item lists. This works well for small mods, but larger mods, addons, and modpacks can quickly produce tabs that are difficult to browse.

CTL allows those tabs to be organized into:

- Pages
- Subtabs
- Sections
- Ordered item groups
- Collapsible section headers
- Visual section headers
- Page banners
- Addon content
- Fallback content for unassigned items

This allows mods to keep using normal Minecraft creative tabs while presenting their contents in a more structured way.

## Core Idea

CTL controls the **visible layout** of a creative tab.

The original creative tab still exists and remains registered normally. CTL rebuilds its visible contents from a layout definition.

This means:

- Mods can continue using standard Minecraft creative tabs.
- CTL plugins can organize tabs into pages and sections.
- Other integrations can contribute pages, sections, or entries.
- Subtabs can group related creative tabs without replacing them.
- Fallback content can preserve items that were not explicitly assigned to a CTL layout.

## Features

- Page-based creative tab organization
- Scrollable subtab groups
- Section-based item grouping
- Collapsible section headers
- Visual section headers
- Page banners
- JSON-driven visuals
- Optional sprite animations
- Addon pages and compatibility contributions
- Fallback pages for unassigned items
- Priority-based ordering
- Semantic plugin system with `NATIVE`, `PRESET`, and `COMPAT` integrations
- Built-in presets for supported mods
- Public API for mods and addons
- Resolved layout snapshots for external interfaces
- Reusable CTL visuals
- Datagen support for CTL visuals
- In-game client configuration
- Built-in vanilla layouts
- Developer visual debug tools

## Project Status

Creative Tab Layouts provides a documented public API for creating layouts, registering integrations, contributing content, querying resolved layouts, and using CTL visuals from external interfaces.

The public API is organized under:

```text
com.ziver.tab_layouts.api.*
```

Implementation details are organized under:

```text
com.ziver.tab_layouts.internal.*
```

Anything under `internal.*` is **not part of the public API contract** and may change without notice between releases.

## Installation

Install Creative Tab Layouts like any other NeoForge mod.

For players and modpacks, CTL may be installed directly or used as a dependency by other mods.

For developers, add CTL as a dependency and use only the documented public API under `com.ziver.tab_layouts.api.*`.

## Documentation

Developer documentation is available under [`docs/en_us/`](docs/en_us/):

- [Getting Started](docs/en_us/GETTING_STARTED.md)
- [Plugin System](docs/en_us/PLUGINS.md)
- [Layouts](docs/en_us/LAYOUTS.md)
- [Visuals](docs/en_us/VISUALS.md)
- [Fallback](docs/en_us/FALLBACK.md)
- [Configuration](docs/en_us/CONFIG.md)
- [Migration and 2.0 Planning](docs/en_us/MIGRATION.md)

Spanish documentation is also available:

- [Documentación en español](docs/es_es/GETTING_STARTED.md)

## Public API Scope

Only packages under:

```text
com.ziver.tab_layouts.api.*
```

are considered part of CTL's public API contract.

Packages under:

```text
com.ziver.tab_layouts.internal.*
```

are implementation details and should not be used by external mods or addons.

Internal classes may change, move, or be removed without being considered a public API break.