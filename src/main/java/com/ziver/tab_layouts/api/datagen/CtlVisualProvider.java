package com.ziver.tab_layouts.api.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Base NeoForge data provider for generating CTL header and banner JSON resources.
 * Generated resources are written under the CTL visual asset directories.
 */
public abstract class CtlVisualProvider implements DataProvider {

    private final PackOutput output;
    private final String modId;

    private final Map<ResourceLocation, CtlGeneratedVisual> headers = new LinkedHashMap<>();
    private final Map<ResourceLocation, CtlGeneratedVisual> banners = new LinkedHashMap<>();

    protected CtlVisualProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    /** Called on each datagen run to register this provider's visuals. */
    protected abstract void addVisuals();

    /** Creates a resource location in this provider's mod namespace. */
    public ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    /** Creates a resource location in the Minecraft namespace. */
    public ResourceLocation mcLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", path);
    }

    /** Registers a generated header visual; duplicate IDs are rejected. */
    public void header(ResourceLocation id, CtlHeaderVisualBuilder builder) {
        if (headers.put(id, builder) != null) throw new IllegalStateException("Duplicate CTL header visual: " + id);
    }

    /** Registers a generated banner visual; duplicate IDs are rejected. */
    public void banner(ResourceLocation id, CtlBannerVisualBuilder builder) {
        if (banners.put(id, builder) != null) throw new IllegalStateException("Duplicate CTL banner visual: " + id);
    }

    public void header(String path, CtlHeaderVisualBuilder builder) {
        header(modLoc(path), builder);
    }

    public void banner(String path, CtlBannerVisualBuilder builder) {
        banner(modLoc(path), builder);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        headers.clear();
        banners.clear();

        addVisuals();

        CompletableFuture<?>[] headerTasks = headers.entrySet()
                .stream()
                .map(entry -> save(cache, "ctl/headers", entry.getKey(), entry.getValue().toJson()))
                .toArray(CompletableFuture[]::new);

        CompletableFuture<?>[] bannerTasks = banners.entrySet()
                .stream()
                .map(entry -> save(cache, "ctl/banners", entry.getKey(), entry.getValue().toJson()))
                .toArray(CompletableFuture[]::new);

        CompletableFuture<?> allHeaders = CompletableFuture.allOf(headerTasks);
        CompletableFuture<?> allBanners = CompletableFuture.allOf(bannerTasks);

        return CompletableFuture.allOf(allHeaders, allBanners);
    }

    private CompletableFuture<?> save(CachedOutput cache, String folder, ResourceLocation id, JsonObject json) {
        Path path = output.getOutputFolder().resolve("assets").resolve(id.getNamespace()).resolve(folder).resolve(id.getPath() + ".json");
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public @NotNull String getName() {
        return "CTL Visuals: " + modId;
    }
}