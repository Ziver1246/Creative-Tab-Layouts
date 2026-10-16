# Migration and the path to 2.0.0

This document tracks API changes that require attention and small decisions currently expected during the transition toward CTL 2.0.0.

It is not a rigid roadmap promise: future plans may change before 2.0.0.

## 1.3.0: new plugin system

### Before

Older plugins could declare their identity by implementing:

```java
@Override
public ResourceLocation getPluginUid() {
    return ResourceLocation.fromNamespaceAndPath("examplemod", "ctl_plugin");
}
```

### Now

Declare metadata through `@CtlPlugin`:

```java
@CtlPlugin(
        modId = ExampleMod.MOD_ID,
        id = "layout",
        type = CtlPluginType.NATIVE,
        targets = ExampleMod.MOD_ID
)
public final class ExampleCtlPlugin implements ICtlPlugin {
    @Override
    public void register(CtlPluginContext ctx) {
    }
}
```

`getPluginUid()` still works for legacy plugins during 1.x, but is marked `forRemoval` and will be removed in 2.0.0.

### Why migrate

The new metadata lets CTL understand:

```text
provider
logical ID
integration type
targets
```

It also enables automatic precedence:

```text
NATIVE > PRESET for the same target
```

A legacy plugin does not declare targets, so CTL cannot infer which preset it should replace. That limitation is intentional during the transition.

## 1.2.x → 1.3.0: contributions

`CtlPluginContext#contributeTab` is deprecated.

### Before

```java
ctx.contributeTab(tabId).ifPresent(tab -> {
    // The builder also exposed owner operations.
});
```

### Now

```java
ctx.contribute(tabId).ifPresent(tab -> {
    tab.addonPage(id("compat"), page -> {
        // ...
    });
});
```

`contribute` returns `CtlContributionBuilder`, which limits the surface to contribution operations.

`contributeTab` is planned for removal in 2.0.0.

## 1.3.0: CtlApiExtensions

`CtlApiExtensions` is deprecated.

The new API is separated by responsibility:

```text
api.view    → resolved read-only layouts
api.client  → public visual rendering
api.layout  → layout construction and contribution
api.plugin  → plugins and integration
api.datagen → visual generation
```

### Layout queries

Before:

```java
CtlApiExtensions.getTabView(...)
```

Now:

```java
CtlViews.getTabView(...)
```

Public snapshots now have their own classes:

```text
CtlTabView
CtlPageView
CtlSectionView
CtlSubtabGroupView
```

### External visuals

Before:

```java
CtlApiExtensions.Client.renderSectionHeader(...)
CtlApiExtensions.Client.renderPageBanner(...)
```

Now:

```java
CtlVisuals.renderSectionHeader(...)
CtlVisuals.renderPageBanner(...)
```

Preferred dimensions use:

```text
CtlVisualSize
```

### Configuration

The new API no longer exposes internal configuration state as part of its contract.

Compatibility shims in `CtlApiExtensions` remain temporarily so 1.x integrations are not broken.

## 1.3.0: CtlVanillaTabs removed

The `CtlVanillaTabs` wrapper was removed.

For vanilla tabs, use Minecraft's keys directly:

```java
CreativeModeTabs.INGREDIENTS.location()
CreativeModeTabs.BUILDING_BLOCKS.location()
```

This avoids duplicating IDs that vanilla already exposes.

## 1.3.0: simplified ordering

Unused infrastructure for a configurable page-ordering system was removed.

The current order is deterministic:

```text
OVERVIEW
BASE    → priority → registration order
ADDON   → priority → registration order
```

Sections follow the same principle within BASE and ADDON.

External integrations are not expected to depend on a separate custom ordering system.

## 1.3.0: fallback

Fallback now preserves all unclaimed items from the original creative tab, including vanilla items.

Integrations that assumed `minecraft` was excluded from fallback should no longer depend on that previous behavior.

## Planned for 2.0.0

Changes currently expected:

- remove `ICtlPlugin#getPluginUid()`;
- remove `CtlPluginContext#contributeTab()`;
- remove `CtlApiExtensions` and its legacy types;
- make the new `@CtlPlugin` system the only supported way to declare plugin identity;
- continue removing deprecated or provisional API when a stable replacement exists.

The goal of 2.0.0 is to reduce historical compatibility baggage, not to unnecessarily redesign stable parts of the API.

## Practical rule

For new code aligned with the direction of 2.0.0:

```text
1. Use @CtlPlugin with modId, id, type, and targets.
2. Use contribute(), not contributeTab().
3. Use CtlViews and CtlVisuals, not CtlApiExtensions.
4. Use CreativeModeTabs.<TAB>.location() for vanilla IDs.
5. Do not depend on com.ziver.tab_layouts.internal.*.
```
