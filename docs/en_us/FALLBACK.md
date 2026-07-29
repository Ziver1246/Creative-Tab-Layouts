# Fallback pages in Creative Tab Layouts

This document explains the fallback system of **Creative Tab Layouts** (CTL).

> Integration examples: [EXAMPLES.md](./EXAMPLES.md)  
> Related config: [CONFIG.md](./CONFIG.md)

## Why fallback exists

When CTL controls a creative tab, the visible content is rebuilt from CTL pages and sections.

Without fallback, items added by other mods through the vanilla system could stop appearing in that tab.

Fallback exists as a safety net to preserve content from mods without CTL integration.

## What fallback does

Fallback detects external items present in the original tab content and places them at the end of the CTL layout.

General order:

```text
Overview Page
Base Pages
Addon Pages
Fallback Pages
```

## When fallback appears

Fallback can appear when:

```text
- The tab is controlled by CTL.
- enableFallbackPages is enabled.
- External items exist in the original tab content.
- Those items were not claimed by CTL pages.
- Those items do not belong to the minecraft namespace.
```

If there is no external content to preserve, no visible fallback appears.

## Which items it takes

It takes external items added to the original tab through vanilla mechanisms or normal creative tab events.

Example:

```text
examplemod:ruby appears in minecraft:ingredients.
CTL controls minecraft:ingredients.
examplemod:ruby is not in any CTL page.
→ fallback can show examplemod:ruby.
```

## Which items it ignores

It ignores:

```text
- Items from the minecraft namespace.
- Items already claimed by CTL.
- Duplicates by Item.
- Empty stacks.
```

## Claimed items

An item is considered claimed if it appears in normal pages or addon pages of CTL.

Comparison is done by `Item`, not by exact `ItemStack`.

Consequence:

```text
If CTL already shows examplemod:ruby once,
fallback does not add another examplemod:ruby even if the original stack has another name, count, or components.
```

This avoids duplicates, but it means fallback is not an exact preservation system for complex stacks.

## Grouping by mod

Fallback items are grouped by the item namespace.

```text
examplemod:ruby       → examplemod
anothermod:copper_rod → anothermod
```

CTL attempts to show the public mod name through `ModList`. If it is unavailable, it uses the mod id.

## Fallback modes

The current implementation supports two modes.

### BY_MOD_SECTION

One final page named `Mods`.

Inside it, CTL creates one section per mod.

```text
Fallback Page: Mods
├─ Section: Example Mod
├─ Section: Another Mod
└─ Section: Third Mod
```

This is the recommended default mode because it uses fewer pages.

### BY_MOD_PAGE

One final page per mod.

```text
Fallback Page: Example Mod
Fallback Page: Another Mod
Fallback Page: Third Mod
```

It can be clearer when a mod has many items.

## Config

Relevant options:

```text
enableFallbackPages = true / false
fallbackMode = BY_MOD_SECTION / BY_MOD_PAGE
```

`fallbackMode` only has an effect when `enableFallbackPages` is enabled.

Changes apply at runtime. Reopen the creative inventory if you need to refresh the view.

## Visual headers for fallback

Fallback sections can also have visual headers.

For a fallback section with id:

```text
examplemod:fallback/minecraft/ingredients
```

CTL will look for:

```text
assets/examplemod/ctl/headers/fallback/minecraft/ingredients.json
```

This allows a mod to customize its fallback section in a specific vanilla tab.

## Fallback is not a layout API

Fallback is automatic.

It does not expose:

```text
add
stack
dynamic
empty
section
addonSection
priority
positioning
```

If a mod wants full control, it must use the CTL API:

```java
ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
        .addonPage(id("materials"), page -> {
            page.section(id("special"), section -> {
                section.add(Items.DIAMOND);
                section.stack(() -> customStack());
                section.dynamic(registries -> createDynamicStacks(registries));
            });
        });
```

Rule:

```text
Fallback does not define layout.
Fallback recovers content without CTL integration.
```

## Why fallback does not have stack or dynamic

`stack` and `dynamic` are declarative APIs. They require intent from the mod:

```text
which stack to create
where to place it
with what priority
whether it needs registries
how to deduplicate it
```

Fallback does not have that intent. It only observes the original tab content and preserves unclaimed items.

If you need `stack` or `dynamic`, write a CTL plugin.

## Important limitations

Fallback:

```text
- Does not guarantee a pretty layout.
- Does not know the mod's intent.
- Does not necessarily preserve exact variants if the same Item was already claimed.
- Does not replace a real integration.
- Always appears at the end.
```

## External layout views

`CtlApiExtensions#getTabView(tabId, registries)` returns declared CTL pages only. The overload receiving `originalItems` may also append fallback pages and exposes them as `PageType.FALLBACK` with `SectionType.FALLBACK` sections.

## Recommendation for mods

If your mod wants to integrate well:

```text
- Use @CtlPlugin.
- Use contributeTab if the integration should be optional.
- Use addonPage for your own content.
- Use contributePage or contributeSection to integrate into existing layouts.
- Do not depend on fallback as the main integration.
```

Fallback exists so nothing important disappears, not to design the final layout.
