# Plugins de CTL

Los plugins son el punto de entrada de una integración con Creative Tab Layouts.

En 1.3.0 la identidad y la intención de un plugin se declaran mediante `@CtlPlugin`.

## Identidad

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

Los campos principales son:

- `modId`: mod que provee el plugin.
- `id`: identificador local del plugin dentro de ese proveedor.
- `type`: intención semántica del plugin.
- `targets`: mods a los que aplica la integración.

CTL combina `modId` e `id` como un ID namespaced. Por ejemplo:

```text
examplemod:layout
```

Los IDs de plugins deben ser únicos.

## Tipos de plugin

### NATIVE

Usa `NATIVE` cuando la integración pertenece al propio mod que se está organizando.

```java
type = CtlPluginType.NATIVE,
targets = ExampleMod.MOD_ID
```

Un `NATIVE` representa la integración oficial del target.

### PRESET

Usa `PRESET` para proporcionar una integración de respaldo para otro mod que todavía no tenga integración CTL propia.

```java
@CtlPlugin(
        modId = MyIntegrationMod.MOD_ID,
        id = "example_preset",
        type = CtlPluginType.PRESET,
        targets = "examplemod"
)
```

Si existe un plugin `NATIVE` elegible para el mismo target, CTL omite automáticamente el `PRESET`.

Esto permite distribuir presets sin competir con una integración oficial futura.

CTL 1.3.0 incluye presets internos de subtabs para algunos mods compatibles. Esos presets siguen la misma regla: una integración `NATIVE` moderna para el target tiene precedencia.

### COMPAT

Usa `COMPAT` cuando la integración conecta varios mods y normalmente contribuye a layouts ya existentes.

```java
@CtlPlugin(
        modId = MyCompatMod.MOD_ID,
        id = "example_create_compat",
        type = CtlPluginType.COMPAT,
        targets = {"examplemod", "create"}
)
```

CTL carga `COMPAT` después de `NATIVE` y `PRESET`.

Un `COMPAT` normalmente debería usar `contribute`, `addonPage`, `contributePage` o `contributeSection`. CTL avisa en log si un plugin `COMPAT` llama a operaciones de ownership como `controlTab` o `controlSubtab`.

## Targets

CTL solo activa un plugin cuando sus targets están cargados.

Por ejemplo:

```java
targets = {"examplemod", "create"}
```

requiere ambos mods.

`minecraft` se considera un target válido para integraciones vanilla.

Los targets representan a qué mods aplica la integración. No son un sustituto general del sistema de dependencias de NeoForge.

## Orden de carga

La resolución general es:

```text
1. Descubrir plugins @CtlPlugin.
2. Validar identidad y clases.
3. Descartar plugins cuyos targets no estén cargados.
4. Detectar targets con integración NATIVE.
5. Omitir PRESET reemplazados por NATIVE.
6. Cargar NATIVE/PRESET.
7. Cargar COMPAT.
```

No dependas del orden relativo entre dos plugins del mismo nivel como mecanismo de diseño.

## Contexto del plugin

`register(CtlPluginContext ctx)` recibe las operaciones públicas principales:

```text
controlTab
controlSubtab
subtab
subtabs
contribute
```

También existen helpers simples de log:

```text
info
warn
error
```

## Ownership vs contribution

Usa:

```java
ctx.controlTab(tabId)
```

cuando tu plugin define el layout base de una tab.

Usa:

```java
ctx.contribute(tabId)
```

cuando quieres extender un layout que debe existir previamente.

`contribute` no crea una tab controlada y devuelve un `Optional`.

## API legacy

El sistema anterior identificaba plugins mediante:

```java
getPluginUid()
```

Ese método sigue soportado temporalmente en 1.x, pero está deprecated y será eliminado en 2.0.0.

Los plugins legacy no tienen metadata de `targets`, por lo que CTL no puede inferir automáticamente qué preset deberían reemplazar. La solución es migrar a `@CtlPlugin`.

También está deprecated:

```java
ctx.contributeTab(tabId)
```

Usa `ctx.contribute(tabId)` en código nuevo.

Consulta [MIGRATION.md](./MIGRATION.md) para la transición completa.
