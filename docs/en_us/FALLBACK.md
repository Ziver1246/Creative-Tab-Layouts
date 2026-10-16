# Fallback

Fallback prevents original creative-tab content from disappearing when CTL controls a layout but some items were not assigned explicitly.

## Idea

CTL compares:

```text
original creative-tab items
-
items claimed by CTL pages
=
unclaimed items
```

Unclaimed items can be appended through fallback content.

Since 1.3.0, this includes both vanilla and mod content. Minecraft is no longer excluded from fallback.

## What "claimed" means

CTL builds the declared pages in the layout and considers the `Item` instances present in those pages claimed.

Fallback operates at the `Item` level. It is not a complete `ItemStack` identity system with components, so it should not be used as a substitute for an intentional CTL integration.

## Modes

The current configuration provides two modes.

### BY_MOD_SECTION

Creates one final `Mods` page and groups unclaimed content into sections by namespace/mod.

```text
Mods
├─ Minecraft
├─ Create
├─ Example Mod
└─ ...
```

### BY_MOD_PAGE

Creates a separate fallback page for each mod/namespace.

```text
Minecraft
Create
Example Mod
...
```

## Order

Fallback is appended after declared CTL pages.

It does not participate as a manually declared base or addon page.

## Fallback vs intentional layouts

Fallback is a safety net, not an API for designing content.

It does not provide operations such as:

```text
add
stack
dynamic
section
priority
addBefore
addAfter
```

If you know the content you want to integrate, create or contribute to a normal CTL layout instead.

## External views

`CtlViews.getTabView(tabId, registries)` returns declared CTL pages.

To include fallback pages in the snapshot, use the overload that also receives the original creative-tab items:

```java
CtlViews.getTabView(tabId, registries, originalItems);
```

Fallback pages appear as:

```text
CtlPageView.Type.FALLBACK
```

and their sections as:

```text
CtlSectionView.Type.FALLBACK
```

## Limitations

Fallback does not know the intent of the mod that added an item.

By design, it:

- prioritizes preserving content;
- groups content automatically by namespace/mod;
- does not guarantee an ideal visual layout;
- does not replace `NATIVE`, `PRESET`, or `COMPAT`;
- may lose distinctions between variants if the same `Item` was already claimed by the layout.

## Recommendation

For a real integration:

```text
NATIVE → official layout provided by the mod itself
PRESET → fallback integration for a mod without NATIVE
COMPAT → contribution between multiple mods
```

Use fallback only as protection for unassigned content.
