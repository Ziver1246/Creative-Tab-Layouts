# Configuration

CTL 1.3.0 includes a client configuration screen accessible from both the creative and survival inventories when their buttons are enabled.

The current options are listed below.

## Built-in vanilla layouts

```text
enableBuiltinVanillaLayouts = true
```

Enables the vanilla layouts bundled with CTL.

Disabling them does not disable the CTL API. External plugins can still control their own tabs.

The setting is checked at runtime; if the interface is already open, reopening the creative inventory may be necessary.

## Subtabs

```text
enableSubtabs = true
```

Enables visual subtab grouping in the Creative Inventory.

When disabled, registered creative tabs still exist and are shown as independent tabs.

## Creative inventory config button

```text
showCreativeConfigButton = true
```

Shows the CTL configuration entry point in the creative inventory.

## Survival inventory config button

```text
showSurvivalConfigButton = true
```

Shows the CTL configuration entry point in the survival inventory.

## Visual debug

```text
enableDeveloperVisualDebug = true
```

Enables development helpers related to headers and banners, such as metadata or resource paths when supported by the renderer.

This is not part of the layout contract and may change as a development tool.

## Fallback pages

```text
enableFallbackPages = true
```

Enables fallback content for items present in the original creative tab that were not claimed by CTL pages.

This applies to both vanilla and mod content.

See [FALLBACK.md](./FALLBACK.md) for the complete behavior.

## Fallback mode

```text
fallbackMode = BY_MOD_SECTION
```

Current values:

```text
BY_MOD_SECTION
BY_MOD_PAGE
```

`BY_MOD_SECTION` creates a `Mods` page with one section per mod.

`BY_MOD_PAGE` creates one final fallback page per mod.

This setting only has an effect when fallback pages are enabled.

## Configuration and public API

CTL's internal configuration is not part of the new structured public API.

Integrations should query effective behavior through APIs such as `CtlViews` when appropriate instead of depending on internal configuration fields.

`CtlApiExtensions` temporarily preserves some older configuration accessors for 1.x compatibility, but it is deprecated and will be removed in 2.0.0.
