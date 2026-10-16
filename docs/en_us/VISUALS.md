# Visuals

CTL can associate visual resources with layout elements:

```text
Overview page → Banner
Section       → Header
```

Visuals are loaded from assets and can also be generated through the public datagen API.

## Resolution by ID

The element ID determines the JSON path.

Page:

```java
tab.overview(ResourceLocation.fromNamespaceAndPath("examplemod", "overview/main"));
```

Looks for:

```text
assets/examplemod/ctl/banners/overview/main.json
```

Section:

```java
page.section(ResourceLocation.fromNamespaceAndPath("examplemod", "materials/gems"), section -> {
    section.add(Items.DIAMOND);
});
```

Looks for:

```text
assets/examplemod/ctl/headers/materials/gems.json
```

## Preferred sizes

CTL visuals are designed for:

```text
Header: 162 x 18
Banner: 162 x 90
```

The client API exposes these dimensions through `CtlVisuals`.

## Header JSON

`texture` is required.

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/gems.png",
  "hide_text": false,
  "text_color": "#FFFFFFFF",
  "text_top_color": "#FFFFFFFF",
  "text_bottom_color": "#FFB8B8B8",
  "label_color": "#99000000",
  "text_shadow": true,
  "text_align": "left"
}
```

Optional fields supported by the public builder:

```text
sprite_animation
hide_text
text_color
text_top_color
text_bottom_color
label_color
text_shadow
text_align
```

`text_align` can be `left`, `center`, or `right`.

## Banner JSON

```json
{
  "texture": "examplemod:textures/gui/ctl/banners/main.png"
}
```

Banners support a texture and optional `sprite_animation`.

## Sprite animation

Vertical example:

```json
{
  "layout": "vertical",
  "frames": 4,
  "fps": 8,
  "animate_just_on_hover": false
}
```

Layouts available from the public builder:

```text
vertical
horizontal
grid
```

For `grid`, specify `columns`.

```json
{
  "layout": "grid",
  "frames": 8,
  "fps": 8,
  "columns": 4
}
```

CTL derives the remaining layout from those values; there is no public `rows` field in the 1.3.0 builder.

## Colors

CTL accepts colors in visual properties as JSON strings. Existing documentation generally uses:

```text
#AARRGGBB
#RRGGBB
```

Keep the same format in your resources for compatibility with CTL's parser.

## Datagen

Create a provider by extending `CtlVisualProvider`:

```java
public final class ExampleCtlVisualProvider extends CtlVisualProvider {

    public ExampleCtlVisualProvider(PackOutput output) {
        super(output, ExampleMod.MOD_ID);
    }

    @Override
    protected void addVisuals() {
        header("materials/gems", CtlHeaderVisualBuilder.header(
                modLoc("textures/gui/ctl/headers/gems.png")
        ));

        banner("overview/main", CtlBannerVisualBuilder.banner(
                modLoc("textures/gui/ctl/banners/main.png")
        ));
    }
}
```

The provider generates:

```text
assets/<namespace>/ctl/headers/<path>.json
assets/<namespace>/ctl/banners/<path>.json
```

Registering the same header or banner ID twice in one provider produces an error.

### Header builder

```java
CtlHeaderVisualBuilder.header(texture)
        .textColor("#FFFFFFFF")
        .labelColor("#99000000")
        .textShadow(true)
        .left();
```

To hide text entirely:

```java
CtlHeaderVisualBuilder.header(texture)
        .hideText();
```

### Animation from datagen

```java
CtlSpriteAnimationBuilder.vertical(4, 8)
        .animateOnHover();
```

```java
CtlHeaderVisualBuilder.header(texture)
        .spriteAnimation(CtlSpriteAnimationBuilder.horizontal(4, 8).alwaysAnimate());
```

## Client API for external interfaces

`CtlVisuals` allows CTL visuals to be reused outside the Creative Inventory.

Basic queries:

```java
CtlVisuals.getPreferredHeaderSize();
CtlVisuals.getPreferredBannerSize();
CtlVisuals.hasHeaderVisual(sectionId);
CtlVisuals.hasBannerVisual(pageId);
```

It can also render headers and banners for a resolved `tabId` and `pageIndex`.

```java
boolean rendered = CtlVisuals.renderSectionHeader(
        graphics,
        tabId,
        pageIndex,
        sectionId,
        x, y, width, height,
        hovered,
        mouseX, mouseY
);
```

The method returns `false` when CTL cannot render the requested visual, allowing the consuming interface to use its own fallback.

Overloads are available with an independent `animationContext` and with `allowDebug` to control visual debug for that integration.

## Visual debug

CTL configuration includes a visual debug option for development. It may expose metadata and resource paths related to headers and banners.

Do not depend on debug information as stable API; it is a development tool.
