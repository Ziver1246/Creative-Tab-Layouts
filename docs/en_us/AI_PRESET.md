# AI preset for Creative Tab Layouts

This file contains instructions ready to paste into an AI together with the **Creative Tab Layouts** documentation.

The goal is for the AI to help create plugins, examples, compat integrations, or visuals without inventing API or depending on internal packages.

> Main documentation: [API.md](./API.md)  
> Examples: [EXAMPLES.md](./EXAMPLES.md)

## General preset

Copy this text and then paste the relevant documents.

```text
You are going to help me work with Creative Tab Layouts (CTL), a Minecraft/NeoForge API for organizing creative tabs with pages, sections, entries, headers, banners, fallback pages, and visual datagen.

Strict rules:
1. Use only the documented API.
2. Do not invent classes, methods, JSON fields, packages, or behavior.
3. Do not use internal, client, mixins, or internal registries as public API.
4. If something does not appear in the documentation, state it as an uncertainty instead of assuming.
5. Keep class names, method names, packages, and JSON fields exactly as they are.
6. Use ResourceLocation.fromNamespaceAndPath(namespace, path) in Java examples.
7. For normal entries use add(...).
8. For custom ItemStack use stack(() -> itemStack).
9. For content that needs registries use dynamic(registries -> List<ItemStack>).
10. For addBefore/addAfter remember that occurrence is 1-based: 1 = first appearance. 0 is invalid.
11. Do not use rows in sprite_animation; CTL calculates rows from frames and columns.
12. Visual debug requires development environment, config enabled, Left Alt held, and hover.
13. JSON colors use #AARRGGBB or #RRGGBB.
14. Fallback is not a layout API; it does not have add, stack, dynamic, or manual sections.
```

## Prompt to create a CTL plugin

```text
Using only the documented public CTL API, create a CTL plugin for my mod.

Data:
- modid: <modid>
- target tab: <tab>
- desired pages: <list>
- desired sections: <list>
- items per section: <list>

Requirements:
- It must implement ICtlPlugin.
- It must use @CtlPlugin.
- It must have a unique getPluginUid.
- It must not use internal packages.
- It must use helper id(String path).
- It must include imports.
- If you use addBefore/addAfter, occurrence must be 1-based.
```

## Prompt to review a CTL plugin

```text
Review this CTL plugin against the documentation.

Look for:
- usage of internal packages
- nonexistent methods
- occurrence 0 or negative
- duplicated sections
- contributePage used to redefine existing sections
- contributeSection on pages/sections that might not exist
- incorrect use of stack or dynamic
- missing imports
- required lang keys
- expected header and banner paths

Do not invent solutions outside the documented API.
```

## Prompt to create visual JSON

```text
Create CTL visual JSON using the documentation.

Requirements:
- Headers go in assets/<namespace>/ctl/headers/<path>.json.
- Banners go in assets/<namespace>/ctl/banners/<path>.json.
- texture is required.
- Colors use #AARRGGBB.
- Do not use rows in sprite_animation.
- If the header should be image-only, use hide_text: true.
- If there is a grid animation, use columns and let CTL calculate rows.
```

## Prompt for visual datagen

```text
Create a CtlVisualProvider to generate CTL visuals.

Requirements:
- Extend CtlVisualProvider.
- Use super(output, "<modid>").
- Use header(...) and banner(...).
- Use CtlHeaderVisualBuilder and CtlBannerVisualBuilder.
- Use CtlSpriteAnimationBuilder if there are sprites.
- Split into register(provider) classes if there are many visuals.
- Do not use CtlGeneratedVisual directly.
```

## Recommended documents to pass to the AI

For code generation:

```text
API.md
EXAMPLES.md
CONCEPTS.md
```

For visuals:

```text
VISUALS.md
EXAMPLES.md
API.md datagen section
```

For fallback:

```text
FALLBACK.md
CONFIG.md
CONCEPTS.md
```

For reviewing a full plugin:

```text
API.md
EXAMPLES.md
VISUALS.md if it uses headers, banners, or datagen
FALLBACK.md if it touches compatibility with vanilla tabs
```
