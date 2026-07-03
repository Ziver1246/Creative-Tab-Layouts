# Creative Tab Layouts documentation contents

This directory contains the English documentation for **Creative Tab Layouts** (CTL).

The documentation is written for modders, addon developers, and modpack creators who want to use CTL through its public API.

> Complete copy-paste examples: [EXAMPLES.md](./EXAMPLES.md)  
> AI preset for CTL: [AI_PRESET.md](./AI_PRESET.md)

## Recommended reading order

To get started quickly:

```text
1. CONCEPTS.md
2. EXAMPLES.md
3. API.md
4. VISUALS.md
5. FALLBACK.md
6. CONFIG.md
7. AI_PRESET.md
8. ROADMAP.md
```

To integrate a mod:

```text
1. EXAMPLES.md
2. API.md
3. VISUALS.md, if you need headers, banners, or datagen.
4. FALLBACK.md, if you need to understand compatibility with mods that do not integrate with CTL.
```

To create visuals:

```text
1. VISUALS.md
2. EXAMPLES.md
3. API.md, Datagen API section.
```

To use an AI with CTL:

```text
1. AI_PRESET.md
2. API.md
3. EXAMPLES.md
4. VISUALS.md, if you are working with JSON or datagen.
```

## Files

### CONCEPTS.md

Explains the CTL mental model:

```text
controlled tab
layout
page
overview page
base page
addon page
fallback page
section
base section
addon section
entry
header
banner
```

It is the entry point for understanding how CTL organizes a creative tab.

### API.md

Main reference for developers.

Covers:

```text
public packages
@CtlPlugin
ICtlPlugin
CtlPluginContext
CtlVanillaTabs
CtlTabBuilder
CtlPageBuilder
CtlSectionBuilder
add
stack
dynamic
empty
addFirst / addLast
addBefore / addAfter
occurrence
priority
contributePage
contributeSection
lang keys
datagen API
```

### EXAMPLES.md

Complete copy-paste examples.

Includes:

```text
minimal plugin
controlTab
contributeTab
page
addonPage
contributePage
contributeSection
sections
direct entries
add
stack
dynamic
empty
positioning
occurrence
overview + banner
headers JSON
banners JSON
hide_text
sprite animation
datagen
separate datagen classes
lang keys
integral test plugin
```

It is the most direct file for copying a functional base.

### VISUALS.md

Documents the visual system:

```text
headers
banners
JSON paths
texture
hide_text
text_color
text_top_color
text_bottom_color
label_color
text_shadow
text_align
sprite_animation
layout vertical/horizontal/grid
frames
fps
columns
animate_just_on_hover
visual debug with Left Alt
color format #AARRGGBB
```

It also explains missing, empty, invalid, and incomplete JSON states.

### FALLBACK.md

Explains fallback pages:

```text
why they exist
when they appear
which items they take
which items they ignore
claimed items
BY_MOD_SECTION
BY_MOD_PAGE
fallback headers
why fallback is not a layout API
important limitations
```

### CONFIG.md

Explains the public configuration options:

```text
enableBuiltinVanillaLayouts
enableDeveloperVisualDebug
enableFallbackPages
fallbackMode
```

It also explains which options apply at runtime and how to refresh the creative inventory.

### AI_PRESET.md

Contains ready-to-use prompts for working with CTL using an AI.

Useful for:

```text
creating CTL plugins
reviewing CTL plugins
creating visual JSON
generating visual datagen
answering questions without inventing API
```

### ROADMAP.md

Lists project improvement lines.

The roadmap does not replace `API.md`. The available and stable API is documented in `API.md`, `VISUALS.md`, `FALLBACK.md`, and `CONFIG.md`.
