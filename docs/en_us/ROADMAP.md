# Creative Tab Layouts roadmap

This document lists improvement lines for **Creative Tab Layouts**.

> Available API: [API.md](./API.md)  
> Examples: [EXAMPLES.md](./EXAMPLES.md)

## Available in the current version

The current version includes:

```text
- CTL plugin API.
- @CtlPlugin and ICtlPlugin.
- controlTab and contributeTab.
- Layouts with pages and sections.
- Overview pages with banners.
- Headers for sections.
- Base pages and addon pages.
- Base sections and addon sections.
- contributePage.
- contributeSection.
- add, stack, dynamic, and empty.
- addFirst, addLast, addBefore, and addAfter.
- 1-based occurrence for positioning.
- Fallback pages.
- Fallback modes BY_MOD_SECTION and BY_MOD_PAGE.
- Runtime config.
- Visual JSON reload.
- Visual debug with Left Alt + hover.
- Datagen for visuals.
- Vertical, horizontal, and grid sprite animation.
```

## Additional vanilla visuals

CTL already supports headers and banners through JSON and datagen.

The natural improvement line is expanding the built-in visual set for vanilla tabs:

```text
Building Blocks
Natural Blocks
Functional Blocks
Redstone Blocks
Tools & Utilities
Combat
Ingredients
Food & Drinks
Spawn Eggs
```

## High-resolution visual sources

CTL renders headers and banners at fixed sizes:

```text
Header: 162 x 18 px
Banner: 162 x 90 px
```

A future improvement is accepting source textures with higher density than the rendered size.

Example:

```text
Rendered header: 162 x 18
2x source:       324 x 36

Rendered banner: 162 x 90
2x source:       324 x 180
```

This improvement requires additional JSON fields for source size or frame size.

## Expanded visual validation

Current visual debug shows JSON, paths, ids, and missing, empty, invalid, or incomplete file errors.

Useful improvements for visual debug:

```text
- Explicit warning if the referenced texture does not exist.
- Warning if a spritesheet has unexpected dimensions.
- Show current frame in animations.
- Show expected texture size.
- Copy ids/paths from the UI.
```

## Manual page organization

One improvement line for modpacks is exposing a public workflow for manual page order.

Goals:

```text
- Reorder pages per tab.
- Reset order per tab.
- Save manual order.
- Make adjustments easier for modpack creators.
```

## Advanced fallback

The current implementation supports:

```text
BY_MOD_SECTION
BY_MOD_PAGE
```

Future improvements for fallback:

```text
- Filters by mod id.
- Strict mode per tab.
- Whitelist/blacklist.
- Better reporting of unclaimed items.
- Diagnostic tools for modpack compatibility.
```

## API extensions

The public API stays small and oriented around real cases.

Useful extensions for future versions:

```text
- Additional metadata for pages/sections.
- More precise claiming hooks.
- Optional matching by ItemStack or components.
- Positioning helpers for stacks if a real need appears.
- More datagen utilities.
- More detailed validation/reporting for missing targets.
```

## Minecraft and NeoForge compatibility

CTL follows the areas that most affect creative tabs and UI:

```text
creative tabs
ItemStack components
registries
datagen
GUI rendering
```

## Priority criteria

Improvements are evaluated with these criteria:

```text
1. Stability.
2. Compatibility.
3. Clean API.
4. Predictable behavior.
5. Real usefulness for modders and modpack creators.
```

## Not guaranteed

The proposals and ideas in this document are not fully guaranteed and are subject to removal and/or changes.
