# Creative Tab Layouts Extensions

This document describes `CtlApiExtensions`, the public API for querying resolved CTL layouts and reusing their visuals in other interfaces.

> Layout API: [API.md](./API.md)  
> Visuals: [VISUALS.md](./VISUALS.md)  
> Fallback: [FALLBACK.md](./FALLBACK.md)

## Purpose

The API returns immutable semantic snapshots. An integration receives resolved pages, sections, and entries, while remaining responsible for arranging and displaying them in its own interface.

It does not need access to CTL's creative-inventory representation.

## General queries

```java
boolean isTabControlled(ResourceLocation tabId);
boolean areBuiltinVanillaLayoutsEnabled();
boolean areSubtabsEnabled();
boolean isCreativeConfigButtonEnabled();
boolean hasPages(ResourceLocation tabId);
```

`isTabControlled` reflects current state. A built-in vanilla layout disabled through configuration is not considered controlled while that option remains disabled.

## Subtab queries

```java
boolean isSubtab(ResourceLocation tabId);
boolean hasSubtabs(ResourceLocation tabId);
Optional<ResourceLocation> getParentTab(ResourceLocation tabId);
List<ResourceLocation> getSubtabs(ResourceLocation tabId);
Optional<SubtabGroupView> getSubtabGroup(ResourceLocation tabId);
```

`SubtabGroupView` contains the parent tab ID and an immutable list of its subtabs.

An integration should respect `areSubtabsEnabled()` before representing grouped tabs.

## Layout snapshots

```java
Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries);
Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries, List<ItemStack> originalItems);
Optional<PageView> getPageView(ResourceLocation tabId, int pageIndex, HolderLookup.Provider registries);
```

The first `getTabView` overload returns declared CTL pages only. The second may also build fallback pages from the original item list.

They return `Optional.empty()` when the tab is not controlled or has no active pages.

Before returning the snapshot, CTL resolves:

```text
- priorities
- insertion order
- addFirst / addLast
- addBefore / addAfter
- dynamic entries
- section order
- fallback, when originalItems are provided
```

Returned `ItemStack` values are copies and the lists must not be modified.

## TabView

Exposes the tab ID and its ordered list of `PageView` values.

## PageView

Exposes:

```text
index
id
type
title
direct entries
sections
```

`PageType` distinguishes `OVERVIEW`, `BASE`, `ADDON`, and `FALLBACK`.

An overview contains no entries or sections.

## SectionView

Exposes:

```text
id
type
title
entries
```

`SectionType` distinguishes `BASE`, `ADDON`, and `FALLBACK`.

## Client API

`CtlApiExtensions.Client` depends on client classes and must only be used from client code.

```java
Size getPreferredHeaderSize();
Size getPreferredBannerSize();
boolean hasHeaderVisual(ResourceLocation sectionId);
boolean hasBannerVisual(ResourceLocation pageId);
```

The sizes are recommendations intended to preserve the designed appearance. An integration may use other bounds, but should retain a reasonable height and aspect ratio.

## Rendering headers

```java
boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY);
```

Overloads with `animationContext` and `allowDebug` are also available.

Always use the index returned by `PageView#index()`.

The method returns `true` when CTL rendered a custom visual. When it returns `false`, the integration must render its own frame or fallback.

## Rendering banners

```java
boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY);
```

Only an overview page with a valid banner can render successfully. As with headers, a `false` result requires a consumer-provided fallback.

## Animation contexts

Overloads receiving `animationContext` maintain independent animation states.

```java
private static final ResourceLocation ANIMATION_CONTEXT = ResourceLocation.fromNamespaceAndPath("examplemod", "my_screen");
```

Use different contexts when the same visual may appear simultaneously in different interfaces. This prevents hover, pause, and current-frame state from being shared accidentally.

## Visual debug

Overloads with `allowDebug` let an integration suppress CTL's debug helpers without changing global configuration.

```java
CtlApiExtensions.Client.renderSectionHeader(graphics, ANIMATION_CONTEXT, tabId, page.index(), section.id(), x, y, width, height, hovered, mouseX, mouseY, false);
```

`allowDebug = true` does not force debug output. It only permits CTL to show it when the environment and configuration also allow it.

## Optional integration

Before calling this API from a compatibility integration, check that CTL is loaded through the normal mechanism provided by your loader. Keep client references isolated so graphical classes are never loaded on a dedicated server.
