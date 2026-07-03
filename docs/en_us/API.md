# Creative Tab Layouts API

This document describes the public API of **Creative Tab Layouts** (CTL) according to the current implementation.

> Complete copy-paste examples: [EXAMPLES.md](./EXAMPLES.md)  
> Base concepts: [CONCEPTS.md](./CONCEPTS.md)  
> JSON visuals and datagen: [VISUALS.md](./VISUALS.md)

## Public packages

The public API is in:

```text
com.ziver.tab_layouts.api
com.ziver.tab_layouts.api.layout
com.ziver.tab_layouts.api.plugin
com.ziver.tab_layouts.api.datagen
```

Do not depend on `internal` packages, built-in vanilla classes, mixins, renderers, internal registries, or screen classes.

## General flow

```text
@CtlPlugin + ICtlPlugin
└─ register(CtlPluginContext ctx)
   ├─ ctx.controlTab(tabId)
   │  ├─ overview(pageId)
   │  ├─ page(pageId, ...)
   │  ├─ addonPage(pageId, ...)
   │  ├─ contributePage(pageId, ...)
   │  └─ contributeSection(pageId, sectionId, ...)
   └─ ctx.contributeTab(tabId)
      └─ Optional<CtlTabBuilder>
```

Inside a page:

```text
page
├─ add / stack / dynamic / empty
├─ addFirst / addLast
├─ addBefore / addAfter
├─ section(...)
├─ addonSection(...)
└─ contributeSection(...)
```

Inside a section:

```text
section
├─ add / stack / dynamic / empty
├─ addFirst / addLast
└─ addBefore / addAfter
```

## Creating a CTL plugin

A CTL plugin is a class that implements `ICtlPlugin` and is annotated with `@CtlPlugin`.

```java
package com.example.examplemod;

import com.ziver.tab_layouts.api.plugin.CtlPlugin;
import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.api.plugin.ICtlPlugin;
import net.minecraft.resources.ResourceLocation;

@CtlPlugin
public final class ExampleCtlPlugin implements ICtlPlugin {

    public ExampleCtlPlugin() {
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("examplemod", "ctl_plugin");
    }

    @Override
    public void register(CtlPluginContext ctx) {
        ctx.info("Example CTL plugin loaded");
    }
}
```

Rules:

```text
- The class must implement ICtlPlugin.
- The class must be annotated with @CtlPlugin.
- It must have a no-args constructor.
- getPluginUid() must return a unique ResourceLocation.
- It must not depend on internal packages.
```

## ICtlPlugin

```java
ResourceLocation getPluginUid();

void register(CtlPluginContext ctx);
```

### getPluginUid

Identifies your CTL plugin.

It must be unique. Use your mod namespace.

```java
@Override
public ResourceLocation getPluginUid() {
    return ResourceLocation.fromNamespaceAndPath("examplemod", "ctl_plugin");
}
```

### register

Registers pages, sections, and contributions.

```java
@Override
public void register(CtlPluginContext ctx) {
    ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
            .addonPage(id("materials"), page -> {
                page.add(Items.DIAMOND);
            });
}
```

## CtlPluginContext

Public methods:

```java
CtlTabBuilder controlTab(ResourceLocation tabId);

Optional<CtlTabBuilder> contributeTab(ResourceLocation tabId);

void info(String message);
void warn(String message);
void error(String message);
```

### controlTab

Creates or retrieves a CTL layout for a creative tab.

```java
ctx.controlTab(CtlVanillaTabs.INGREDIENTS)
        .page(id("materials"), page -> {
            page.add(Items.DIAMOND, Items.EMERALD);
        });
```

When a tab is controlled by CTL, its visible content is built from the CTL layout.

Minecraft's original creative tab still exists.

### contributeTab

Returns the tab only if it is already controlled.

```java
ctx.contributeTab(CtlVanillaTabs.INGREDIENTS).ifPresent(tab -> {
    tab.addonPage(id("extra_materials"), page -> {
        page.add(Items.AMETHYST_SHARD);
    });
});
```

Use it for compat integrations that should not force a tab to become controlled.

## CtlVanillaTabs

Public constants for common vanilla tabs:

```java
CtlVanillaTabs.BUILDING_BLOCKS
CtlVanillaTabs.COLORED_BLOCKS
CtlVanillaTabs.NATURAL_BLOCKS
CtlVanillaTabs.FUNCTIONAL_BLOCKS
CtlVanillaTabs.REDSTONE_BLOCKS

CtlVanillaTabs.TOOLS_AND_UTILITIES
CtlVanillaTabs.COMBAT
CtlVanillaTabs.FOOD_AND_DRINKS
CtlVanillaTabs.INGREDIENTS
CtlVanillaTabs.SPAWN_EGGS
CtlVanillaTabs.OP_BLOCKS

CtlVanillaTabs.INVENTORY
CtlVanillaTabs.HOTBAR
CtlVanillaTabs.SEARCH
```

You can also use:

```java
CtlVanillaTabs.vanilla("ingredients");
```

## CtlTabBuilder

Methods:

```java
CtlTabBuilder overview(ResourceLocation pageId);

CtlTabBuilder page(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);
CtlTabBuilder page(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

CtlTabBuilder addonPage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);
CtlTabBuilder addonPage(ResourceLocation pageId, long priority, Consumer<CtlPageBuilder> builder);

CtlTabBuilder contributePage(ResourceLocation pageId, Consumer<CtlPageBuilder> builder);

CtlTabBuilder contributeSection(ResourceLocation pageId, ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
```

### overview

Declares an overview page.

```java
tab.overview(id("overview/ingredients"));
```

The overview uses a banner visual associated with `pageId`.

Path:

```text
assets/<namespace>/ctl/banners/<path>.json
```

Restrictions:

```text
- Only one overview can exist per tab.
- It does not receive CtlPageBuilder.
- It does not accept entries, sections, or layout content.
- It cannot be modified with contributePage.
```

### page

Creates a base page.

```java
tab.page(id("materials"), page -> {
    page.add(Items.DIAMOND);
});
```

With priority:

```java
tab.page(id("materials"), 100L, page -> {
    page.add(Items.DIAMOND);
});
```

### addonPage

Creates an addon page.

```java
tab.addonPage(id("addon_materials"), page -> {
    page.add(Items.AMETHYST_SHARD);
});
```

It appears after base pages and before fallback pages.

### contributePage

Adds content to an existing page.

```java
tab.contributePage(id("materials"), page -> {
    page.add(Items.NETHERITE_INGOT);
});
```

Rules:

```text
- The page must exist.
- It cannot be the overview.
- It can add direct entries.
- It can add new sections.
- It cannot redefine an existing section; use contributeSection for that.
```

### contributeSection

Adds entries to an existing section.

```java
tab.contributeSection(id("materials"), id("gems"), section -> {
    section.add(Items.AMETHYST_SHARD);
});
```

Rules:

```text
- The page must exist.
- The section must exist inside that page.
- It keeps the original section position, type, and priority.
- It adds entries to the logical end of that section.
```

## CtlPageBuilder

Entry methods:

```java
CtlPageBuilder add(ItemLike... items);
CtlPageBuilder add(Supplier<? extends ItemLike> item);
CtlPageBuilder stack(Supplier<ItemStack> stack);
CtlPageBuilder dynamic(CtlDynamicEntries entries);
CtlPageBuilder empty();
CtlPageBuilder empty(int count);

CtlPageBuilder addFirst(ItemLike... items);
CtlPageBuilder addFirst(long priority, ItemLike... items);
CtlPageBuilder addLast(ItemLike... items);
CtlPageBuilder addLast(long priority, ItemLike... items);

CtlPageBuilder addBefore(ItemLike target, ItemLike... items);
CtlPageBuilder addBefore(ItemLike target, long priority, ItemLike... items);
CtlPageBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);
CtlPageBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

CtlPageBuilder addAfter(ItemLike target, ItemLike... items);
CtlPageBuilder addAfter(ItemLike target, long priority, ItemLike... items);
CtlPageBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);
CtlPageBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);
```

Section methods:

```java
CtlPageBuilder section(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
CtlPageBuilder section(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

CtlPageBuilder addonSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
CtlPageBuilder addonSection(ResourceLocation sectionId, long priority, Consumer<CtlSectionBuilder> builder);

CtlPageBuilder contributeSection(ResourceLocation sectionId, Consumer<CtlSectionBuilder> builder);
```

## CtlSectionBuilder

It has the same entry methods as `CtlPageBuilder`, but it cannot create subsections.

```java
CtlSectionBuilder add(ItemLike... items);
CtlSectionBuilder add(Supplier<? extends ItemLike> item);
CtlSectionBuilder stack(Supplier<ItemStack> stack);
CtlSectionBuilder dynamic(CtlDynamicEntries entries);
CtlSectionBuilder empty();
CtlSectionBuilder empty(int count);

CtlSectionBuilder addFirst(ItemLike... items);
CtlSectionBuilder addFirst(long priority, ItemLike... items);
CtlSectionBuilder addLast(ItemLike... items);
CtlSectionBuilder addLast(long priority, ItemLike... items);

CtlSectionBuilder addBefore(ItemLike target, ItemLike... items);
CtlSectionBuilder addBefore(ItemLike target, long priority, ItemLike... items);
CtlSectionBuilder addBefore(ItemLike target, int occurrence, ItemLike... items);
CtlSectionBuilder addBefore(ItemLike target, int occurrence, long priority, ItemLike... items);

CtlSectionBuilder addAfter(ItemLike target, ItemLike... items);
CtlSectionBuilder addAfter(ItemLike target, long priority, ItemLike... items);
CtlSectionBuilder addAfter(ItemLike target, int occurrence, ItemLike... items);
CtlSectionBuilder addAfter(ItemLike target, int occurrence, long priority, ItemLike... items);
```

## Entry types

### add(ItemLike...)

Use `add` for simple items.

```java
section.add(
        Items.IRON_INGOT,
        Items.GOLD_INGOT,
        Items.DIAMOND
);
```

Each item becomes a normal count-1 `ItemStack` when the page is built.

### add(Supplier<? extends ItemLike>)

Useful for items registered by your mod.

```java
section.add(() -> ModItems.RUBY.get());
```

The supplier is evaluated when CTL builds the page.

If the supplier returns `null`, the entry becomes empty and is normally not shown.

### stack(Supplier<ItemStack>)

Use `stack` when you need a concrete `ItemStack`.

```java
section.stack(() -> {
    ItemStack stack = new ItemStack(Items.DIAMOND, 4);
    stack.set(DataComponents.CUSTOM_NAME, Component.literal("Custom Diamond Stack"));
    return stack;
});
```

`stack` is useful for:

```text
- custom count
- custom name
- components
- damage
- custom model data if your version uses it through components
- any manually built ItemStack
```

CTL copies the stack before inserting it.

If the supplier returns `null` or `ItemStack.EMPTY`, it is not shown.

### dynamic(CtlDynamicEntries)

Use `dynamic` when you need to generate entries with registry access.

```java
section.dynamic(registries -> {
    return List.of(
            Raid.getLeaderBannerInstance(
                    registries.lookupOrThrow(Registries.BANNER_PATTERN)
            )
    );
});
```

`dynamic` is useful for:

```text
- stacks that depend on registries
- datapack-sensitive content
- banner patterns
- enchanted books
- goat horns
- lists generated at build time
```

The function returns `List<ItemStack>`.

Rules:

```text
- If it returns null, nothing is added.
- If it returns an empty list, nothing is added.
- If it contains null, CTL treats it as ItemStack.EMPTY and it is normally not shown.
- CTL copies each stack before inserting it.
```

For positioning, `dynamic` is not the best anchor because it can generate multiple stacks. If you need a stable target for `addBefore` or `addAfter`, use a normal entry with `add` as the anchor.

### empty()

Adds a visible empty slot.

```java
section.empty();
section.empty(3);
```

Rules:

```text
empty() is equivalent to empty(1).
empty(0) adds nothing.
empty(count < 0) throws an exception.
```

`empty` is kept even if it is `ItemStack.EMPTY`. It is used to reserve space or align content.

## Entry positioning

CTL supports several placement groups:

```text
FIRST
NORMAL
BEFORE
AFTER
LAST
```

Conceptual order:

```text
FIRST entries
NORMAL entries with BEFORE/AFTER resolved around them
unresolved BEFORE entries
unresolved AFTER entries
LAST entries
```

Inside each group, CTL sorts by:

```text
ascending priority → insertion order
```

### addFirst

Places entries before normal entries.

```java
section.addFirst(Items.NETHER_STAR);
section.addFirst(-100L, Items.DRAGON_EGG);
```

Lower priority appears earlier inside the FIRST group.

### addLast

Places entries after normal entries and unresolved positioned entries.

```java
section.addLast(Items.BARRIER);
section.addLast(100L, Items.COMMAND_BLOCK);
```

Lower priority appears earlier inside the LAST group.

### addBefore

Adds items before a target.

```java
section.add(Items.IRON_INGOT, Items.DIAMOND);
section.addBefore(Items.DIAMOND, Items.EMERALD);
```

Conceptual result:

```text
IRON_INGOT
EMERALD
DIAMOND
```

### addAfter

Adds items after a target.

```java
section.add(Items.IRON_INGOT, Items.DIAMOND);
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

Conceptual result:

```text
IRON_INGOT
DIAMOND
EMERALD
```

### Positioning methods only accept ItemLike

The current positioning API accepts `ItemLike... items`.

There are no public methods such as:

```java
stackBefore(...)
dynamicBefore(...)
stackAfter(...)
dynamicAfter(...)
```

If you need to position something complex, use a simple item anchor and then adjust the layout with normal entries, sections, or visual separation.

## Occurrence in addBefore and addAfter

`occurrence` indicates which appearance of the target is used.

It is **1-based**:

```java
addBefore(target, 1, items); // before the first appearance of target
addBefore(target, 2, items); // before the second appearance of target

addAfter(target, 1, items);  // after the first appearance of target
addAfter(target, 2, items);  // after the second appearance of target
```

`0` or negative is invalid:

```java
section.addAfter(Items.DIAMOND, 0, Items.EMERALD);  // invalid
section.addAfter(Items.DIAMOND, -1, Items.EMERALD); // invalid
```

Overloads without `occurrence` use `1`:

```java
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

is equivalent to:

```java
section.addAfter(Items.DIAMOND, 1, Items.EMERALD);
```

### Example with occurrence

```java
section.add(
        Items.IRON_INGOT,
        Items.DIAMOND,
        Items.GOLD_INGOT,
        Items.DIAMOND
);

section.addAfter(Items.DIAMOND, 1, Items.EMERALD);
section.addAfter(Items.DIAMOND, 2, Items.AMETHYST_SHARD);
```

Conceptual result:

```text
IRON_INGOT
DIAMOND
EMERALD
GOLD_INGOT
DIAMOND
AMETHYST_SHARD
```

### If occurrence is greater than the real target count

The current implementation clamps the occurrence to the last existing target.

Example:

```java
section.add(Items.DIAMOND);
section.addAfter(Items.DIAMOND, 5, Items.EMERALD);
```

There is only one `DIAMOND`. CTL uses the last available appearance, which in this case is the first one.

Conceptual result:

```text
DIAMOND
EMERALD
```

### If the target does not exist

If the target does not exist inside the normal entries, CTL cannot resolve the exact position.

The current implementation keeps those entries and places them after the normal entries, before LAST entries:

```text
normal entries
unresolved BEFORE entries
unresolved AFTER entries
LAST entries
```

Recommendation: do not depend on missing targets. Use them only when you know another plugin will add the anchor.

### Positioning scope

Positioning is resolved inside the builder where it is declared.

In a page:

```java
page.addAfter(Items.DIAMOND, Items.EMERALD);
```

it searches targets in that page's direct entries.

In a section:

```java
section.addAfter(Items.DIAMOND, Items.EMERALD);
```

it searches targets inside that section.

It does not cross between page entries and section entries.

## Sections

### section

Creates a base section.

```java
page.section(id("gems"), section -> {
    section.add(Items.DIAMOND, Items.EMERALD);
});
```

With priority:

```java
page.section(id("gems"), -100L, section -> {
    section.add(Items.DIAMOND);
});
```

### addonSection

Creates an addon section.

```java
page.addonSection(id("extra_gems"), section -> {
    section.add(Items.AMETHYST_SHARD);
});
```

Addon sections appear after base sections.

### contributeSection in page builder

Contributes to a section created earlier in the same page builder.

```java
page.section(id("gems"), section -> {
    section.add(Items.DIAMOND);
});

page.contributeSection(id("gems"), section -> {
    section.add(Items.EMERALD);
});
```

If the section does not exist yet in that builder, it throws an exception.

## Page and section order

Page order:

```text
overview pages
base pages
addon pages
fallback pages
```

Inside base pages and addon pages:

```text
ascending priority → insertion order
```

Section order inside a page:

```text
base sections
addon sections
```

Inside each group:

```text
ascending priority → insertion order
```

## Lang keys

CTL generates titles by convention.

For page id:

```text
examplemod:materials/gems
```

expected key:

```text
tabpage.examplemod.materials.gems
```

For section id:

```text
examplemod:materials/ores
```

expected key:

```text
tabsection.examplemod.materials.ores
```

Example `en_us.json`:

```json
{
  "tabpage.examplemod.materials.gems": "Gems",
  "tabsection.examplemod.materials.ores": "Ores"
}
```

## Datagen API

Package:

```text
com.ziver.tab_layouts.api.datagen
```

Public classes:

```java
CtlVisualProvider
CtlHeaderVisualBuilder
CtlBannerVisualBuilder
CtlSpriteAnimationBuilder
```

`CtlGeneratedVisual` is package-private and should not be used directly.

### CtlVisualProvider

Base provider for generating header and banner JSON.

Useful methods:

```java
public ResourceLocation modLoc(String path);
public ResourceLocation mcLoc(String path);

public void header(ResourceLocation id, CtlHeaderVisualBuilder builder);
public void header(String path, CtlHeaderVisualBuilder builder);

public void banner(ResourceLocation id, CtlBannerVisualBuilder builder);
public void banner(String path, CtlBannerVisualBuilder builder);
```

Minimal example:

```java
public final class ExampleVisualProvider extends CtlVisualProvider {
    public ExampleVisualProvider(PackOutput output) {
        super(output, "examplemod");
    }

    @Override
    protected void addVisuals() {
        header(
                "materials/gems",
                CtlHeaderVisualBuilder.header(modLoc("textures/gui/ctl/headers/gems.png"))
                        .textColor("#FFFFFFFF")
                        .labelColor("#99000000")
                        .textShadow(true)
                        .left()
        );
    }
}
```

More details: [VISUALS.md](./VISUALS.md)

## Compatibility and stability

Rules for addons:

```text
- Use only api.* packages.
- Do not use internal.*.
- Do not depend on the exact order of vanilla built-in layouts unless you are integrating with those ids directly.
- Use contributeTab for optional compatibility.
- Use addonPage for your own content.
- Use contributeSection to add entries to an existing section.
- Do not use fallback as your main integration.
```
