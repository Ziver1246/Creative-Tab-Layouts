# Visuales de Creative Tab Layouts

Este documento explica el sistema visual público de **Creative Tab Layouts** (CTL).

> Ejemplos: [EXAMPLES.md](./EXAMPLES.md)  
> Reutilizar visuales en interfaces externas: [EXTENSIONS.md](./EXTENSIONS.md)  
> API de datagen: [API.md#datagen-api](./API.md#datagen-api)

CTL permite asociar archivos JSON visuales a elementos del layout:

```text
Overview Page → Banner
Section       → Header
```

Los visuales se cargan desde assets del mod o resource pack. No se registran desde Java en runtime.

## Resolución de visuales

CTL usa el `ResourceLocation` del layout para resolver el JSON.

```text
pageId    → banner JSON
sectionId → header JSON
```

Ejemplo de overview:

```java
tab.overview(ResourceLocation.fromNamespaceAndPath("examplemod", "overview/main"));
```

Ruta esperada:

```text
assets/examplemod/ctl/banners/overview/main.json
```

Ejemplo de section:

```java
page.section(ResourceLocation.fromNamespaceAndPath("examplemod", "materials/gems"), section -> {
    section.add(Items.DIAMOND);
});
```

Ruta esperada:

```text
assets/examplemod/ctl/headers/materials/gems.json
```

## Banner

Un **banner** es el visual asociado a una overview page.

Tamaño renderizado:

```text
162 x 90 px
```

Equivale a:

```text
9 columnas x 18 px = 162 px
5 filas    x 18 px = 90 px
```

## Header

Un **header** es el visual asociado a una section.

Tamaño renderizado:

```text
162 x 18 px
```

Equivale a:

```text
9 columnas x 18 px = 162 px
1 fila     x 18 px = 18 px
```

## Rutas

Headers:

```text
assets/<namespace>/ctl/headers/<path>.json
```

Banners:

```text
assets/<namespace>/ctl/banners/<path>.json
```

Ejemplo:

```text
tab_layouts:minecraft/colored_blocks/red_blocks
```

Header esperado:

```text
assets/tab_layouts/ctl/headers/minecraft/colored_blocks/red_blocks.json
```

## Campo texture

Todo visual válido necesita `texture`.

```json
{
  "texture": "examplemod:textures/gui/ctl/headers/gems.png"
}
```

La textura es un `ResourceLocation` normal.

Tamaños recomendados sin animación:

```text
Header: 162 x 18 px
Banner: 162 x 90 px
```

La implementación actual no tiene campos públicos para `source_width`, `source_height` ni high-resolution source size. Si quieres usar una textura sin animación, hazla del tamaño renderizado esperado.

## Header JSON

Campos soportados:

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

Todos los campos excepto `texture` son opcionales.

## Banner JSON

Campos soportados:

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

Los banners no tienen texto dinámico. Solo usan textura y sprite animation opcional.

## Formato de color

CTL usa formato:

```text
#AARRGGBB
```

También acepta:

```text
#RRGGBB
```

en cuyo caso CTL asume alpha `FF`.

Ejemplos:

```text
#FFFFFFFF → blanco opaco
#99000000 → negro con alpha 99
#00000000 → totalmente transparente
```

Si una herramienta te da `RRGGBBAA`, debes convertirlo a `AARRGGBB`.

Ejemplo:

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

Cuando `hide_text` es `true`:

```text
- No se dibuja el texto de la section.
- No se dibuja el label background.
- Solo se dibuja la textura o sprite.
```

Default:

```text
hide_text = false
```

`hide_text` solo aplica a headers.

## text_color

Color único para el texto.

```json
{
  "text_color": "#FFFFFFFF"
}
```

Default:

```text
text_color = #FFFFFFFF
```

## text_top_color y text_bottom_color

Permiten un split vertical de color en el texto.

```json
{
  "text_top_color": "#FFFFFFFF",
  "text_bottom_color": "#FFB8B8B8"
}
```

Solo se usan si ambos campos están presentes.

Si solo defines uno, CTL no activa el split y usa `text_color`.

## label_color

Color del fondo dinámico detrás del texto.

```json
{
  "label_color": "#99000000"
}
```

Default:

```text
label_color = #00000000
```

Si `label_color` es transparente o no está presente, CTL no dibuja label background.

## text_shadow

Activa sombra vanilla del texto.

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

Alineación del texto y del label background.

Valores:

```text
left
center
right
```

Default:

```text
left
```

Ejemplo:

```json
{
  "text_align": "center"
}
```

## sprite_animation

`sprite_animation` permite usar una spritesheet.

Campos:

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

Valores:

```text
vertical
horizontal
grid
```

Default runtime:

```text
vertical
```

### frames

Cantidad total de frames.

Default:

```text
1
```

Si `frames < 1`, CTL lo normaliza a `1`.

### fps

Frames por segundo.

Default runtime si se parsea JSON manual:

```text
0
```

En datagen, el builder usa default:

```text
8
```

Si `fps <= 0` o `frames <= 1`, la animación no avanza.

### columns

Solo es necesario para `layout = grid`.

```json
{
  "layout": "grid",
  "frames": 8,
  "fps": 10,
  "columns": 4
}
```

CTL calcula las filas automáticamente:

```text
rows = ceil(frames / columns)
```

No existe campo público `rows`.

### animate_just_on_hover

Si es `true`, la animación solo avanza mientras el mouse está sobre el header/banner.

```json
{
  "animate_just_on_hover": true
}
```

Default:

```text
false
```

## Layouts de spritesheet

### vertical

Frames apilados verticalmente.

```text
frame 0
frame 1
frame 2
frame 3
```

Para un header de `162 x 18` con 4 frames:

```text
textura total: 162 x 72
```

### horizontal

Frames en una fila.

```text
frame 0 | frame 1 | frame 2 | frame 3
```

Para un header de `162 x 18` con 4 frames:

```text
textura total: 648 x 18
```

### grid

Frames distribuidos en columnas.

Para `frames = 8`, `columns = 4`:

```text
frame 0 | frame 1 | frame 2 | frame 3
frame 4 | frame 5 | frame 6 | frame 7
```

Para un header de `162 x 18`:

```text
textura total: 648 x 36
```

## Datagen de visuales

`CtlVisualProvider` genera JSON en:

```text
assets/<namespace>/ctl/headers/<path>.json
assets/<namespace>/ctl/banners/<path>.json
```

Ejemplo:

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

## Datagen con sprite

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

## Debug visual

CTL tiene debug visual para headers y banners.

Condiciones necesarias:

```text
1. Estar en entorno de desarrollo.
2. enableDeveloperVisualDebug = true.
3. Mantener Left Alt presionado.
4. Hacer hover sobre el header o banner.
```

No basta con hover. Requiere **Left Alt + hover**.

El debug puede mostrar:

```text
- tipo de visual
- id
- ruta esperada
- JSON pretty-printed
- error de JSON faltante, vacío, inválido o incompleto
```

Estados importantes:

```text
JSON faltante     → ERROR: Missing JSON source
JSON vacío        → INFO: JSON source is empty
JSON inválido     → ERROR: Invalid JSON source
JSON sin texture  → INFO: JSON source has no texture field
JSON válido       → muestra JSON sin error
```

El debug visual está presente solo en entorno de desarrollo, por lo que no está presente para el usuario final.
