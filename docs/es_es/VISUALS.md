# Visuales

CTL puede asociar recursos visuales a elementos del layout:

```text
Overview page → Banner
Section       → Header
```

Los visuales se cargan desde assets y pueden generarse mediante la API pública de datagen.

## Resolución por ID

El ID del elemento define la ruta del JSON.

Página:

```java
tab.overview(ResourceLocation.fromNamespaceAndPath("examplemod", "overview/main"));
```

Busca:

```text
assets/examplemod/ctl/banners/overview/main.json
```

Sección:

```java
page.section(ResourceLocation.fromNamespaceAndPath("examplemod", "materials/gems"), section -> {
    section.add(Items.DIAMOND);
});
```

Busca:

```text
assets/examplemod/ctl/headers/materials/gems.json
```

## Tamaños preferidos

Los visuales de CTL están diseñados para:

```text
Header: 162 x 18
Banner: 162 x 90
```

La API client expone estos tamaños mediante `CtlVisuals`.

## Header JSON

`texture` es obligatorio.

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

Campos opcionales soportados por el builder público:

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

`text_align` puede configurarse como `left`, `center` o `right`.

## Banner JSON

```json
{
  "texture": "examplemod:textures/gui/ctl/banners/main.png"
}
```

Los banners soportan textura y `sprite_animation` opcional.

## Sprite animation

Ejemplo vertical:

```json
{
  "layout": "vertical",
  "frames": 4,
  "fps": 8,
  "animate_just_on_hover": false
}
```

Layouts disponibles desde el builder público:

```text
vertical
horizontal
grid
```

Para `grid`, indica `columns`.

```json
{
  "layout": "grid",
  "frames": 8,
  "fps": 8,
  "columns": 4
}
```

CTL calcula el resto de la distribución a partir de esos datos; no existe un campo público `rows` en el builder 1.3.0.

## Colores

CTL acepta colores en las propiedades visuales como strings JSON. La documentación anterior usa normalmente:

```text
#AARRGGBB
#RRGGBB
```

Mantén el mismo formato en tus recursos para conservar compatibilidad con el parser de CTL.

## Datagen

Crea un provider extendiendo `CtlVisualProvider`:

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

El provider genera automáticamente:

```text
assets/<namespace>/ctl/headers/<path>.json
assets/<namespace>/ctl/banners/<path>.json
```

Registrar dos veces el mismo ID de header o banner en un provider produce error.

### Header builder

```java
CtlHeaderVisualBuilder.header(texture)
        .textColor("#FFFFFFFF")
        .labelColor("#99000000")
        .textShadow(true)
        .left();
```

Para ocultar completamente el texto:

```java
CtlHeaderVisualBuilder.header(texture)
        .hideText();
```

### Animación desde datagen

```java
CtlSpriteAnimationBuilder.vertical(4, 8)
        .animateOnHover();
```

```java
CtlHeaderVisualBuilder.header(texture)
        .spriteAnimation(CtlSpriteAnimationBuilder.horizontal(4, 8).alwaysAnimate());
```

## API client para interfaces externas

`CtlVisuals` permite reutilizar visuales CTL fuera de la Creative Inventory.

Consultas básicas:

```java
CtlVisuals.getPreferredHeaderSize();
CtlVisuals.getPreferredBannerSize();
CtlVisuals.hasHeaderVisual(sectionId);
CtlVisuals.hasBannerVisual(pageId);
```

También puede renderizar headers y banners para un `tabId` y `pageIndex` resueltos.

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

El método devuelve `false` si CTL no puede renderizar ese visual, permitiendo que la interfaz consumidora use su propio fallback.

Existen overloads con un `animationContext` independiente y con `allowDebug` para controlar el debug visual de esa integración.

## Debug visual

La configuración de CTL incluye una opción de debug visual para desarrollo. Puede mostrar metadata y rutas relacionadas con headers y banners.

No dependas de esa información de debug como API estable; es una herramienta de desarrollo.
