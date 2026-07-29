# Creative Tab Layouts visuals

This document explains the public visual system of **Creative Tab Layouts** (CTL).

> Examples: [EXAMPLES.md](./EXAMPLES.md)  
> Reusing visuals in external interfaces: [EXTENSIONS.md](./EXTENSIONS.md)  
> Datagen API: [API.md#datagen-api](./API.md#datagen-api)

CTL allows associating visual JSON files with layout elements:

```text
Overview Page → Banner
Section       → Header
```

Visuals are loaded from mod assets or resource packs. They are not registered from Java at runtime.

## Visual resolution

CTL uses the layout `ResourceLocation` to resolve the JSON.

```text
pageId    → banner JSON
sectionId → header JSON
```

Overview example:

```java
tab.overview(ResourceLocation.fromNamespaceAndPath("examplemod", "overview/main"));
```

Expected path:

```text
assets/examplemod/ctl/banners/overview/main.json
```

Section example:

```java
page.section(ResourceLocation.fromNamespaceAndPath("examplemod", "materials/gems"), section -> {
    section.add(Items.DIAMOND);
});
```

Expected path:

```text
assets/examplemod/ctl/headers/materials/gems.json
```

## Banner

A **banner** is the visual associated with an overview page.

Rendered size:

```text
162 x 90 px
```

Equivalent to:

```text
9 columns x 18 px = 162 px
5 rows    x 18 px = 90 px
```

## Header

A **header** is the visual associated with a section.

Rendered size:

```text
162 x 18 px
```

Equivalent to:

```text
9 columns x 18 px = 162 px
1 row     x 18 px = 18 px
```

## Paths

Headers:

```text
assets/<namespace>/ctl/headers/<path>.json
```

Banners:

```text
assets/<namespace>/ctl/banners/<path>.json
```

Example:

```text
tab_layouts:minecraft/colored_blocks/red_blocks
```

Expected header:

```text
assets/tab_layouts/ctl/headers/minecraft/colored_blocks/red_blocks.json
```

## texture field

Every valid visual needs `texture`.

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/gems.png"
}
```

The texture is a normal `ResourceLocation`.

Recommended sizes without animation:

```text
Header: 162 x 18 px
Banner: 162 x 90 px
```

The current implementation has no public fields for `source_width`, `source_height`, or high-resolution source size. If you want to use a non-animated texture, make it the expected rendered size.

## Header JSON

Supported fields:

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/gems.png",
  "sprite_animation": {
    "layout": "vertical",
    "frames": 4,
    "fps": 8,
    "columns": 2,
    "animate_just_on_hover": false
  },
  "hide_text": false,
  "text_color": "#FFFFFFFF",
  "text_top_color": "#FFFFFFFF",
  "text_bottom_color": "#FFB8B8B8",
  "label_color": "#99000000",
  "text_shadow": true,
  "text_align": "left"
}
```

All fields except `texture` are optional.

## Banner JSON

Supported fields:

```json
{
  "texture": "examplemod:textures/gui/ctl/banners/main.png",
  "sprite_animation": {
    "layout": "horizontal",
    "frames": 4,
    "fps": 8,
    "animate_just_on_hover": false
  }
}
```

Banners do not have dynamic text. They only use texture and optional sprite animation.

## Color format

CTL uses this format:

```text
#AARRGGBB
```

It also accepts:

```text
#RRGGBB
```

in which case CTL assumes alpha `FF`.

Examples:

```text
#FFFFFFFF → opaque white
#99000000 → black with alpha 99
#00000000 → fully transparent
```

If a tool gives you `RRGGBBAA`, convert it to `AARRGGBB`.

Example:

```text
RRGGBBAA: D429E8FF
AARRGGBB: FFD429E8
```

## hide_text

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/logo.png",
  "hide_text": true
}
```

When `hide_text` is `true`:

```text
- The section text is not drawn.
- The label background is not drawn.
- Only the texture or sprite is drawn.
```

Default:

```text
hide_text = false
```

`hide_text` only applies to headers.

## text_color

Single color for the text.

```json
{
  "text_color": "#FFFFFFFF"
}
```

Default:

```text
text_color = #FFFFFFFF
```

## text_top_color and text_bottom_color

Enable a vertical color split in the text.

```json
{
  "text_top_color": "#FFFFFFFF",
  "text_bottom_color": "#FFB8B8B8"
}
```

They are only used if both fields are present.

If you only define one of them, CTL does not enable the split and uses `text_color`.

## label_color

Color of the dynamic background behind the text.

```json
{
  "label_color": "#99000000"
}
```

Default:

```text
label_color = #00000000
```

If `label_color` is transparent or absent, CTL does not draw the label background.

## text_shadow

Enables vanilla text shadow.

```json
{
  "text_shadow": true
}
```

Default:

```text
text_shadow = false
```

## text_align

Text and label background alignment.

Values:

```text
left
center
right
```

Default:

```text
left
```

Example:

```json
{
  "text_align": "center"
}
```

## sprite_animation

`sprite_animation` allows using a spritesheet.

Fields:

```json
{
  "layout": "vertical",
  "frames": 4,
  "fps": 8,
  "columns": 2,
  "animate_just_on_hover": false
}
```

### layout

Values:

```text
vertical
horizontal
grid
```

Runtime default:

```text
vertical
```

### frames

Total frame count.

Default:

```text
1
```

If `frames < 1`, CTL normalizes it to `1`.

### fps

Frames per second.

Runtime default when parsing manual JSON:

```text
0
```

In datagen, the builder uses default:

```text
8
```

If `fps <= 0` or `frames <= 1`, the animation does not advance.

### columns

Only needed for `layout = grid`.

```json
{
  "layout": "grid",
  "frames": 8,
  "fps": 10,
  "columns": 4
}
```

CTL calculates rows automatically:

```text
rows = ceil(frames / columns)
```

There is no public `rows` field.

### animate_just_on_hover

If `true`, the animation only advances while the mouse is over the header/banner.

```json
{
  "animate_just_on_hover": true
}
```

Default:

```text
false
```

## Spritesheet layouts

### vertical

Frames stacked vertically.

```text
frame 0
frame 1
frame 2
frame 3
```

For a `162 x 18` header with 4 frames:

```text
total texture: 162 x 72
```

### horizontal

Frames in one row.

```text
frame 0 | frame 1 | frame 2 | frame 3
```

For a `162 x 18` header with 4 frames:

```text
total texture: 648 x 18
```

### grid

Frames distributed in columns.

For `frames = 8`, `columns = 4`:

```text
frame 0 | frame 1 | frame 2 | frame 3
frame 4 | frame 5 | frame 6 | frame 7
```

For a `162 x 18` header:

```text
total texture: 648 x 36
```

## Visual datagen

`CtlVisualProvider` generates JSON in:

```text
assets/<namespace>/ctl/headers/<path>.json
assets/<namespace>/ctl/banners/<path>.json
```

Example:

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
                        .splitTextColor("#FFFFFFFF", "#FFB8B8B8")
                        .labelColor("#99000000")
                        .textShadow(true)
                        .left()
        );

        banner(
                "overview/main",
                CtlBannerVisualBuilder.banner(modLoc("textures/gui/ctl/banners/main.png"))
        );
    }
}
```

## Datagen with sprite

```java
header(
        "materials/animated",
        CtlHeaderVisualBuilder.header(modLoc("textures/gui/ctl/headers/animated.png"))
                .spriteAnimation(CtlSpriteAnimationBuilder.vertical(4, 8))
                .textColor("#FFFFFFFF")
                .labelColor("#99000000")
                .textShadow(true)
                .left()
);
```

Grid:

```java
header(
        "materials/grid_animation",
        CtlHeaderVisualBuilder.header(modLoc("textures/gui/ctl/headers/grid_animation.png"))
                .spriteAnimation(CtlSpriteAnimationBuilder.grid(8, 10, 4))
                .hideText()
);
```

## Visual debug

CTL has visual debug for headers and banners.

Required conditions:

```text
1. Development environment.
2. enableDeveloperVisualDebug = true.
3. Hold Left Alt.
4. Hover the header or banner.
```

Hover is not enough. It requires **Left Alt + hover**.

Debug can show:

```text
- visual type
- id
- expected path
- pretty-printed JSON
- missing, empty, invalid, or incomplete JSON error
```

Important states:

```text
Missing JSON     → ERROR: Missing JSON source
Empty JSON       → INFO: JSON source is empty
Invalid JSON     → ERROR: Invalid JSON source
JSON no texture  → INFO: JSON source has no texture field
Valid JSON       → shows JSON without error
```

Visual debug is only present in a development environment, so it is not present for the final user.
