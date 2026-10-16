# Layouts

A CTL layout organizes the visible content of an existing creative tab.

The conceptual structure is:

```text
Creative Tab
└─ Pages
   ├─ direct entries
   └─ Sections
      └─ entries
```

Multiple creative tabs can also be grouped as subtabs without ceasing to exist as registered Minecraft tabs.

## Pages

A controlled tab is obtained with:

```java
CtlTabBuilder tab = ctx.controlTab(tabId);
```

### Base page

```java
tab.page(id("materials"), page -> {
    page.add(Items.IRON_INGOT, Items.GOLD_INGOT);
});
```

### Overview

```java
tab.overview(id("overview"));
```

A tab can have only one overview. Its main purpose is to act as a visual landing page and it may have an associated banner.

Layout content cannot be contributed to an overview through `contributePage`.

### Addon page

```java
tab.addonPage(id("compat"), page -> {
    page.add(ModItems.EXTRA.get());
});
```

Addon pages are intended for extra content or compatibility integrations.

## Page order

CTL groups pages in this order:

```text
OVERVIEW
BASE
ADDON
```

Within `BASE` and `ADDON`, ordering uses:

```text
priority → registration order
```

Lower numeric priorities appear first.

```java
tab.page(id("early"), -100, page -> {});
tab.page(id("normal"), 0, page -> {});
tab.page(id("late"), 100, page -> {});
```

If multiple pages have the same priority, their registration order is preserved.

Page IDs must be unique within the tab.

## Sections

```java
tab.page(id("materials"), page -> {
    page.section(id("metals"), section -> {
        section.add(Items.IRON_INGOT, Items.GOLD_INGOT);
    });
});
```

Addon sections are also available:

```java
page.addonSection(id("compat_metals"), section -> {
    section.add(ModItems.EXTRA_INGOT.get());
});
```

Sections are grouped as:

```text
BASE
ADDON
```

Within each group, ordering uses:

```text
priority → registration order
```

Section IDs must be unique within the same page.

## Language keys

CTL derives titles from layout IDs.

For:

```text
examplemod:machines/advanced
```

a page uses:

```text
tabpage.examplemod.machines.advanced
```

and a section uses:

```text
tabsection.examplemod.machines.advanced
```

## Normal entries

### add

For normal items:

```java
section.add(Items.DIAMOND, Items.EMERALD);
```

Suppliers are also supported:

```java
section.add(ModItems.MACHINE);
```

### stack

For a specific `ItemStack`:

```java
section.stack(() -> new ItemStack(Items.POTION));
```

### dynamic

For content that needs registry access:

```java
section.dynamic(registries -> createStacks(registries));
```

`CtlDynamicEntries` returns `List<ItemStack>`.

### empty

Reserve empty slots:

```java
section.empty();
section.empty(3);
```

## Entry positioning

CTL can express relative placement without manually rebuilding the whole list.

```java
section.addFirst(ModItems.FIRST.get());
section.addLast(ModItems.LAST.get());
section.addBefore(Items.DIAMOND, ModItems.BEFORE_DIAMOND.get());
section.addAfter(Items.DIAMOND, ModItems.AFTER_DIAMOND.get());
```

All of these variants also accept priority.

### occurrence

If a target appears multiple times, you can choose which occurrence to use:

```java
section.addAfter(Items.DIAMOND, 2, ModItems.EXTRA.get());
```

`occurrence` is 1-based:

```text
1 = first occurrence
2 = second occurrence
...
```

If the requested occurrence is greater than the number available, CTL clamps it to the last available occurrence.

If the target does not appear among normal entries, the relative entry remains unanchored and CTL places it after the corresponding normal block, before `addLast` entries.

## Contributions

### Contribute to a tab

```java
ctx.contribute(tabId).ifPresent(tab -> {
    tab.addonPage(id("my_addon"), page -> {
        page.add(ModItems.EXTRA.get());
    });
});
```

### Contribute to an existing page

```java
tab.contributePage(targetPageId, page -> {
    page.addonSection(id("my_section"), section -> {
        section.add(ModItems.EXTRA.get());
    });
});
```

`contributePage` requires the page to exist.

### Contribute to an existing section

```java
tab.contributeSection(targetPageId, targetSectionId, section -> {
    section.add(ModItems.EXTRA.get());
});
```

It can also be called from a `CtlPageBuilder`:

```java
page.contributeSection(targetSectionId, section -> {
    section.add(ModItems.EXTRA.get());
});
```

Contributing to a missing page or section produces an error. Make sure the target layout is part of the contract of the integration you are extending.

## Subtabs

Group tabs without changing their original layout:

```java
ctx.subtab(childTabId, parentTabId);
ctx.subtabs(parentTabId, firstChild, secondChild);
```

Group a child tab and control its layout:

```java
ctx.controlSubtab(childTabId, parentTabId)
        .page(id("main"), page -> {
            page.add(ModItems.ITEM.get());
        });
```

The parent tab does not need to be controlled by CTL.

Subtab presentation can be disabled from client configuration; the original creative tabs continue to exist.

## Read-only views

`api.view` allows resolved layouts to be queried without exposing CTL's internal objects.

Main classes:

```text
CtlViews
CtlTabView
CtlPageView
CtlSectionView
CtlSubtabGroupView
```

Example:

```java
Optional<CtlTabView> view = CtlViews.getTabView(tabId, registries);
```

The overload without `originalItems` exposes declared CTL pages. If you provide the original tab items, CTL can also include resolved fallback pages:

```java
CtlViews.getTabView(tabId, registries, originalItems);
```

Views are public, read-only snapshots. They are not builders for modifying layouts.
