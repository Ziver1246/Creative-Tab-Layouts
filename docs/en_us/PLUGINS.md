# CTL Plugins

Plugins are the entry point for integrating with Creative Tab Layouts.

In 1.3.0, plugin identity and intent are declared through `@CtlPlugin`.

## Identity

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

The main fields are:

- `modId`: the mod providing the plugin.
- `id`: the plugin's local identifier within that provider.
- `type`: the semantic purpose of the plugin.
- `targets`: mods the integration applies to.

CTL combines `modId` and `id` into a namespaced ID. For example:

```text
examplemod:layout
```

Plugin IDs must be unique.

## Plugin types

### NATIVE

Use `NATIVE` when the integration belongs to the mod being organized.

```java
type = CtlPluginType.NATIVE,
targets = ExampleMod.MOD_ID
```

A `NATIVE` plugin represents the target's official integration.

### PRESET

Use `PRESET` to provide a fallback integration for another mod that does not yet provide its own CTL integration.

```java
@CtlPlugin(
        modId = MyIntegrationMod.MOD_ID,
        id = "example_preset",
        type = CtlPluginType.PRESET,
        targets = "examplemod"
)
```

If an eligible `NATIVE` plugin exists for the same target, CTL automatically skips the `PRESET`.

This allows presets to be distributed without competing with a future official integration.

CTL 1.3.0 includes internal subtab presets for some supported mods. Those presets follow the same rule: a modern `NATIVE` integration for the target takes precedence.

### COMPAT

Use `COMPAT` when an integration connects multiple mods and normally contributes to layouts that already exist.

```java
@CtlPlugin(
        modId = MyCompatMod.MOD_ID,
        id = "example_create_compat",
        type = CtlPluginType.COMPAT,
        targets = {"examplemod", "create"}
)
```

CTL loads `COMPAT` after `NATIVE` and `PRESET`.

A `COMPAT` plugin should normally use `contribute`, `addonPage`, `contributePage`, or `contributeSection`. CTL logs a warning if a `COMPAT` plugin calls ownership operations such as `controlTab` or `controlSubtab`.

## Targets

CTL only activates a plugin when all of its targets are loaded.

For example:

```java
targets = {"examplemod", "create"}
```

requires both mods.

`minecraft` is treated as a valid target for vanilla integrations.

Targets describe which mods the integration applies to. They are not a general replacement for NeoForge dependencies.

## Load order

The general resolution flow is:

```text
1. Discover @CtlPlugin classes.
2. Validate identity and plugin classes.
3. Discard plugins whose targets are not loaded.
4. Detect targets with a NATIVE integration.
5. Skip PRESET plugins replaced by NATIVE.
6. Load NATIVE/PRESET.
7. Load COMPAT.
```

Do not rely on the relative order of two plugins at the same level as a design mechanism.

## Plugin context

`register(CtlPluginContext ctx)` exposes the main public operations:

```text
controlTab
controlSubtab
subtab
subtabs
contribute
```

Simple logging helpers are also available:

```text
info
warn
error
```

## Ownership vs contribution

Use:

```java
ctx.controlTab(tabId)
```

when your plugin defines the base layout of a tab.

Use:

```java
ctx.contribute(tabId)
```

when you want to extend a layout that must already exist.

`contribute` does not create a controlled tab and returns an `Optional`.

## Legacy API

The previous system identified plugins through:

```java
getPluginUid()
```

That method remains temporarily supported in 1.x, but is deprecated and will be removed in 2.0.0.

Legacy plugins do not declare `targets` metadata, so CTL cannot automatically infer which preset they should replace. Migrating to `@CtlPlugin` is the intended solution.

Also deprecated:

```java
ctx.contributeTab(tabId)
```

Use `ctx.contribute(tabId)` in new code.

See [MIGRATION.md](./MIGRATION.md) for the complete transition.
