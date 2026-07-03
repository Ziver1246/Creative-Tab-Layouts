# Creative Tab Layouts configuration

This document describes the public configuration options of **Creative Tab Layouts** (CTL).

> Examples: [EXAMPLES.md](./EXAMPLES.md)  
> Fallback: [FALLBACK.md](./FALLBACK.md)  
> Visual debug: [VISUALS.md#visual-debug](./VISUALS.md#visual-debug)

## Config file

CTL uses a NeoForge client config.

The current options are:

```text
enableBuiltinVanillaLayouts
enableDeveloperVisualDebug
enableFallbackPages
fallbackMode
```

## enableBuiltinVanillaLayouts

Default:

```text
true
```

Enables or disables CTL's built-in vanilla layouts.

```text
true  → CTL organizes the supported vanilla tabs.
false → CTL does not apply its built-in vanilla layouts.
```

This does not disable the CTL API.

External plugins can still control their own tabs or contribute to tabs controlled by other plugins.

Changes:

```text
Apply at runtime.
Reopen the creative inventory if you need to refresh the view.
```

## enableDeveloperVisualDebug

Current default:

```text
true
```

Enables visual debug tooltips for headers and banners.

To see the debug tooltip, all of these conditions must be met:

```text
1. Development environment.
2. enableDeveloperVisualDebug = true.
3. Hold Left Alt.
4. Hover the header or banner.
```

Normal hover is not enough.

Visual debug can show:

```text
- visual id
- JSON path
- pretty-printed JSON
- missing, empty, invalid, or incomplete JSON errors
```

In production, visual debug is not shown even if the config exists.

Developer note: if you are actively working on visuals, it is usually convenient to keep this option set to `true` during development.

## enableFallbackPages

Default:

```text
true
```

Enables or disables fallback pages.

```text
true  → CTL preserves unclaimed external items.
false → CTL does not generate fallback.
```

If disabled, items from mods without CTL integration may stop appearing in controlled tabs.

## fallbackMode

Default:

```text
BY_MOD_SECTION
```

Values:

```text
BY_MOD_SECTION
BY_MOD_PAGE
```

### BY_MOD_SECTION

Creates a final page named `Mods` and groups items by mod inside sections.

```text
Mods
├─ Example Mod
├─ Another Mod
└─ Third Mod
```

### BY_MOD_PAGE

Creates one final page per mod.

```text
Example Mod
Another Mod
Third Mod
```

`fallbackMode` only has an effect when `enableFallbackPages` is enabled.

## Runtime changes

The main options apply at runtime.

Practical recommendation:

```text
1. Change the config.
2. Close the creative inventory if it was open.
3. Open it again.
```

## What config does not change

Config does not change:

```text
- Original creative tab registration.
- Item registration.
- CTL plugin code.
- Visual JSON files.
```

It only changes how CTL applies or shows layouts and helpers.
