# Creative Tab Layouts

Creative Tab Layouts, or CTL, is a Minecraft modding utility that organizes creative mode tabs using structured layouts.

CTL does **not** replace Minecraft's creative tab registration system. The original creative tabs still exist. Instead, CTL controls how items are displayed inside selected creative tabs by rebuilding their visible content with pages, sections, headers, banners, and fallback pages.

It is designed for mods, addons, and modpacks that need cleaner creative inventory organization, especially when many items end up inside the same tab.

## What CTL Solves

Minecraft creative tabs are simple item lists. This works well for small mods, but large mods, addons, and modpacks can quickly make tabs difficult to navigate.

Creative Tab Layouts can organize a tab into:

* Pages
* Subtabs for grouping related creative tabs
* Sections
* Visual headers
* Page banners
* Ordered item groups
* Addon pages for compatibility
* Fallback pages for external items

This makes creative tabs easier to browse without forcing every item into custom tabs.

## Core Idea

CTL controls the **visible layout** of a selected creative tab.

The original Minecraft creative tab still exists, but CTL replaces its visible item list with CTL-built pages.

This means:

* Mods can keep using normal Minecraft creative tabs.
* CTL plugins can organize those tabs into pages and sections.
* Addons can contribute additional pages, sections, or entries.
* Fallback pages can preserve items added by mods that do not use CTL directly.

## Features

* Page-based creative tab organization
* Subtab groups with a scrollable side panel
* Section-based item grouping
* Collapsible section headers
* Visual headers for sections
* Visual banners for pages
* JSON-driven visuals
* Optional sprite animations
* Addon pages for compatibility integrations
* Fallback pages for external mod items
* Priority-based ordering
* Public API for mods and addons
* Resolved layout snapshots and reusable visuals for external interfaces
* In-game client configuration screen
* Datagen support for CTL visuals
* Built-in vanilla tab layouts
* Developer visual debug tools

## Project Status

Creative Tab Layouts provides a documented public API for creating layouts, registering pages, adding sections, contributing content, and defining custom visuals.

Documentation focuses on the public contract: how to use CTL, how to structure layouts, how to contribute content safely, and how to generate visuals through JSON or datagen.

Internal implementation details are not part of the public API contract. This includes mixins, controllers, caches, render internals, internal classes, and built-in vanilla layouts.

## Installation

Install Creative Tab Layouts like any other Minecraft mod.

For players and modpacks, CTL can be installed as a dependency when required by a mod or modpack.

For developers, add CTL as a dependency and use the documented public API.

## Documentation

Extended documentation is available in the `docs/` directory:

* [Documentation contents](docs/en_us/DOCS_CONTENT.md)
* [Concepts](docs/en_us/CONCEPTS.md)
* [API](docs/en_us/API.md)
* [Extensions](docs/en_us/EXTENSIONS.md)
* [Visuals](docs/en_us/VISUALS.md)
* [Fallback Pages](docs/en_us/FALLBACK.md)
* [Config](docs/en_us/CONFIG.md)
* [Examples](docs/en_us/EXAMPLES.md)
* [AI Preset](docs/en_us/AI_PRESET.md)
* [Roadmap](docs/en_us/ROADMAP.md)

Spanish documentation is also available:

* [Documentación en español](docs/es_es/DOCS_CONTENT.md)

## Public API Scope

The `api` packages are part of CTL's public contract and may be used by other mods and addons.

The following are not part of the public API:

```text
internal.*
client.*
mixins.*
built-in vanilla layout classes
render internals
screen controllers
fallback implementation internals
```

These details may change without being considered a public API break.
