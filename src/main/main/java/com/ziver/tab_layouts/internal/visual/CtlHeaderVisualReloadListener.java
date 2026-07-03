package com.ziver.tab_layouts.internal.visual;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ziver.tab_layouts.internal.registry.CtlVisualDebugRegistry;
import com.ziver.tab_layouts.internal.registry.CtlVisualRegistry;
import com.ziver.tab_layouts.internal.util.CtlVisualPaths;
import com.ziver.tab_layouts.internal.visual.debug.CtlVisualDebugInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class CtlHeaderVisualReloadListener extends SimplePreparableReloadListener<CtlHeaderVisualReloadListener.PreparedHeaders> {

    private static final String DIRECTORY = "ctl/headers";
    private static final String JSON_SUFFIX = ".json";

    @Override
    protected @NotNull PreparedHeaders prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, RawVisualFile> files = new HashMap<>();

        Map<ResourceLocation, Resource> resources = resourceManager.listResources(DIRECTORY, location -> location.getPath().endsWith(JSON_SUFFIX));

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation fileLocation = entry.getKey();
            Resource resource = entry.getValue();
            ResourceLocation visualId = visualId(fileLocation);

            try {
                files.put(visualId, new RawVisualFile(visualId, CtlVisualPaths.headerPath(visualId), readRaw(resource), null));
            } catch (Exception exception) {
                files.put(visualId, new RawVisualFile(visualId, CtlVisualPaths.headerPath(visualId), "", Component.literal(exception.getMessage())));
            }
        }

        return new PreparedHeaders(files);
    }

    @Override
    protected void apply(@NotNull PreparedHeaders prepared, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, CtlHeaderVisual> headers = new HashMap<>();
        Map<ResourceLocation, CtlVisualDebugInfo> debug = new HashMap<>();

        for (RawVisualFile file : prepared.files().values()) {
            Component readError = file.readError();

            if (readError != null) {
                debug.put(file.id(), debugInfo("CTL Header Visual", file, file.raw(), readError));
                continue;
            }

            String raw = file.raw();

            if (raw.isBlank()) {
                debug.put(file.id(), debugInfo("CTL Header Visual", file, raw, Component.translatable("screen.tab_layouts.empty_json_config")));
                continue;
            }

            JsonElement element;

            try {
                element = JsonParser.parseString(raw);
            } catch (Exception exception) {
                debug.put(file.id(), debugInfo("CTL Header Visual", file, raw, Component.translatable("screen.tab_layouts.invalid_json_config")));
                continue;
            }

            if (!element.isJsonObject()) {
                debug.put(file.id(), debugInfo("CTL Header Visual", file, CtlVisualJson.prettyJson(element), Component.translatable("screen.tab_layouts.invalid_json_config")));
                continue;
            }

            JsonObject json = element.getAsJsonObject();
            String prettyJson = CtlVisualJson.prettyJson(element);

            if (!json.has("texture")) {
                debug.put(file.id(), debugInfo("CTL Header Visual", file, prettyJson, Component.translatable("screen.tab_layouts.incomplete_json_config")));
                continue;
            }

            try {
                CtlHeaderVisual visual = CtlVisualJson.parseHeader(json);

                headers.put(file.id(), visual);
                debug.put(file.id(), debugInfo("CTL Header Visual", file, prettyJson, Component.empty()));
            } catch (Exception exception) {
                debug.put(file.id(), debugInfo("CTL Header Visual", file, prettyJson, Component.literal(exception.getMessage())));
            }
        }

        CtlVisualRegistry.replaceHeaders(headers);
        CtlVisualDebugRegistry.replaceHeaders(debug);
    }

    private static CtlVisualDebugInfo debugInfo(String type, RawVisualFile file, String source, Component message) {
        return new CtlVisualDebugInfo(type, file.id(), file.virtualPath(), source, message);
    }

    private static String readRaw(Resource resource) throws IOException {
        try (InputStream stream = resource.open()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static ResourceLocation visualId(ResourceLocation fileLocation) {
        String path = fileLocation.getPath();

        if (!path.startsWith(DIRECTORY + "/")) throw new IllegalArgumentException("Invalid CTL header path: " + fileLocation);
        path = path.substring((DIRECTORY + "/").length());

        if (!path.endsWith(JSON_SUFFIX)) throw new IllegalArgumentException("Invalid CTL header extension: " + fileLocation);
        path = path.substring(0, path.length() - JSON_SUFFIX.length());

        return ResourceLocation.fromNamespaceAndPath(fileLocation.getNamespace(), path);
    }

    public record PreparedHeaders(Map<ResourceLocation, RawVisualFile> files) {}

    public record RawVisualFile(ResourceLocation id, String virtualPath, String raw, Component readError) {}
}