package com.ziver.tab_layouts.internal.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class CtlPageOrderConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("creative_tab_layouts").resolve("page_order.json");
    private static final Map<ResourceLocation, TabOrder> ORDERS = new HashMap<>();

    static {
        load();
    }

    private CtlPageOrderConfig() {}

    public static boolean hasOrder(ResourceLocation tabId) {
        TabOrder order = ORDERS.get(tabId);
        return order != null && (!order.base().isEmpty() || !order.addon().isEmpty());
    }

    public static List<ResourceLocation> baseOrder(ResourceLocation tabId) {
        TabOrder order = ORDERS.get(tabId);
        return order == null ? List.of() : order.base();
    }

    public static List<ResourceLocation> addonOrder(ResourceLocation tabId) {
        TabOrder order = ORDERS.get(tabId);
        return order == null ? List.of() : order.addon();
    }

    public static void setOrder(ResourceLocation tabId, List<ResourceLocation> base, List<ResourceLocation> addon) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(base, "base");
        Objects.requireNonNull(addon, "addon");

        ORDERS.put(tabId, new TabOrder(List.copyOf(base), List.copyOf(addon)));

        save();
    }

    public static void reset(ResourceLocation tabId) {
        ORDERS.remove(tabId);
        save();
    }

    public static void load() {
        ORDERS.clear();

        if (!Files.exists(CONFIG_PATH)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);

            if (root == null || !root.has("tabs") || !root.get("tabs").isJsonObject()) return;

            JsonObject tabs = root.getAsJsonObject("tabs");

            for (Map.Entry<String, JsonElement> entry : tabs.entrySet()) {
                ResourceLocation tabId = ResourceLocation.parse(entry.getKey());

                if (!entry.getValue().isJsonObject()) continue;

                JsonObject tabJson = entry.getValue().getAsJsonObject();

                List<ResourceLocation> base = readIdArray(tabJson, "base");
                List<ResourceLocation> addon = readIdArray(tabJson, "addon");

                ORDERS.put(tabId, new TabOrder(base, addon));
            }
        } catch (Exception ignored) {
            ORDERS.clear();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            JsonObject root = new JsonObject();
            JsonObject tabs = new JsonObject();

            for (Map.Entry<ResourceLocation, TabOrder> entry : ORDERS.entrySet()) {
                JsonObject tab = new JsonObject();

                tab.add("base", writeIdArray(entry.getValue().base()));
                tab.add("addon", writeIdArray(entry.getValue().addon()));

                tabs.add(entry.getKey().toString(), tab);
            }

            root.add("tabs", tabs);

            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(root, writer);
            }
        } catch (IOException ignored) {
        }
    }

    private static List<ResourceLocation> readIdArray(JsonObject json, String key) {
        if (!json.has(key) || !json.get(key).isJsonArray()) {
            return List.of();
        }

        List<ResourceLocation> result = new ArrayList<>();
        JsonArray array = json.getAsJsonArray(key);

        for (JsonElement element : array) {
            if (!element.isJsonPrimitive()) continue;

            result.add(ResourceLocation.parse(element.getAsString()));
        }

        return List.copyOf(result);
    }

    private static JsonArray writeIdArray(List<ResourceLocation> ids) {
        JsonArray array = new JsonArray();

        for (ResourceLocation id : ids) {
            array.add(id.toString());
        }

        return array;
    }

    private record TabOrder(List<ResourceLocation> base, List<ResourceLocation> addon) {}
}