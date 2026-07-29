package com.ziver.tab_layouts.client.screen;

import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.internal.layout.CtlFallbackMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Objects;

public final class CtlConfigScreen extends OptionsSubScreen {

    private static final Component TITLE = Component.translatable("screen.tab_layouts.config.title");

    private static final int LABEL_WIDTH = 170;
    private static final int CONTROL_WIDTH = 100;

    private Button builtinVanillaLayoutsButton;
    private Button subtabsButton;
    private Button creativeConfigButton;
    private Button fallbackPagesButton;
    private CycleButton<CtlFallbackMode> fallbackModeButton;
    private Button devVisualDebugButton;

    public CtlConfigScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, TITLE);
    }

    @Override
    protected void addOptions() {
        Objects.requireNonNull(this.list, "list");

        Component builtinVanillaLayoutsLabel = Component.translatable("screen.tab_layouts.config.builtin_vanilla_layouts");
        Component builtinVanillaLayoutsTooltip = Component.translatable("screen.tab_layouts.config.builtin_vanilla_layouts.tooltip");

        Component subtabsLabel = Component.translatable("screen.tab_layouts.config.subtabs");
        Component subtabsTooltip = Component.translatable("screen.tab_layouts.config.subtabs.tooltip");

        Component creativeConfigButtonLabel = Component.translatable("screen.tab_layouts.config.creative_button");
        Component creativeConfigButtonTooltip = Component.translatable("screen.tab_layouts.config.creative_button.tooltip");

        Component fallbackPagesLabel = Component.translatable("screen.tab_layouts.config.fallback_pages");
        Component fallbackPagesTooltip = Component.translatable("screen.tab_layouts.config.fallback_pages.tooltip");

        Component fallbackModeLabel = Component.translatable("screen.tab_layouts.config.fallback_mode");
        Component fallbackModeTooltip = Component.translatable("screen.tab_layouts.config.fallback_mode.tooltip");

        this.builtinVanillaLayoutsButton = createToggleButton(Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.get(), builtinVanillaLayoutsTooltip, button -> {
            Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.set(!Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.get());
            saveConfig();
            updateButtonStates();
        });

        this.subtabsButton = createToggleButton(Config.ENABLE_SUBTABS.get(), subtabsTooltip, button -> {
            Config.ENABLE_SUBTABS.set(!Config.ENABLE_SUBTABS.get());
            saveConfig();
            updateButtonStates();
        });

        this.creativeConfigButton = createToggleButton(Config.SHOW_CREATIVE_CONFIG_BUTTON.get(), creativeConfigButtonTooltip, button -> {
            Config.SHOW_CREATIVE_CONFIG_BUTTON.set(!Config.SHOW_CREATIVE_CONFIG_BUTTON.get());
            saveConfig();
            updateButtonStates();
        });

        this.fallbackPagesButton = createToggleButton(Config.ENABLE_FALLBACK_PAGES.get(), fallbackPagesTooltip, button -> {
            Config.ENABLE_FALLBACK_PAGES.set(!Config.ENABLE_FALLBACK_PAGES.get());
            saveConfig();
            updateButtonStates();
        });

        this.fallbackModeButton = CycleButton.builder(CtlFallbackMode::displayName)
                .withValues(CtlFallbackMode.values())
                .withInitialValue(Config.FALLBACK_MODE.get())
                .displayOnlyValue()
                .create(0, 0, CONTROL_WIDTH, Button.DEFAULT_HEIGHT, Component.empty(), (button, value) -> {
                    Config.FALLBACK_MODE.set(value);
                    saveConfig();
                    updateButtonStates();
                });

        this.fallbackModeButton.setTooltip(Tooltip.create(fallbackModeTooltip));

        this.list.addSmall(createLabel(builtinVanillaLayoutsLabel, builtinVanillaLayoutsTooltip), this.builtinVanillaLayoutsButton);
        this.list.addSmall(createLabel(subtabsLabel, subtabsTooltip), this.subtabsButton);
        this.list.addSmall(createLabel(creativeConfigButtonLabel, creativeConfigButtonTooltip), this.creativeConfigButton);
        this.list.addSmall(createLabel(fallbackPagesLabel, fallbackPagesTooltip), this.fallbackPagesButton);
        this.list.addSmall(createLabel(fallbackModeLabel, fallbackModeTooltip), this.fallbackModeButton);

        if (isDevActive()) {
            Component devVisualDebugLabel = Component.translatable("screen.tab_layouts.config.dev_visual_debug");
            Component devVisualDebugTooltip = Component.translatable("screen.tab_layouts.config.dev_visual_debug.tooltip");

            this.devVisualDebugButton = createToggleButton(Config.ENABLE_DEVELOPER_VISUAL_DEBUG.get(), devVisualDebugTooltip, button -> {
                Config.ENABLE_DEVELOPER_VISUAL_DEBUG.set(!Config.ENABLE_DEVELOPER_VISUAL_DEBUG.get());
                saveConfig();
                updateButtonStates();
            });

            this.list.addSmall(createLabel(devVisualDebugLabel, devVisualDebugTooltip), this.devVisualDebugButton);
        } else {
            this.devVisualDebugButton = null;
        }

        updateButtonStates();
    }

    private StringWidget createLabel(Component label, Component tooltip) {
        StringWidget widget = new StringWidget(LABEL_WIDTH, Button.DEFAULT_HEIGHT, label, this.font).alignLeft();
        widget.setTooltip(Tooltip.create(tooltip));
        return widget;
    }

    private Button createToggleButton(boolean value, Component tooltip, Button.OnPress onPress) {
        Button button = Button.builder(getToggleText(value), onPress).width(CONTROL_WIDTH).build();
        button.setTooltip(Tooltip.create(tooltip));
        return button;
    }

    private void updateButtonStates() {
        if (this.builtinVanillaLayoutsButton != null) {
            this.builtinVanillaLayoutsButton.setMessage(getToggleText(Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.get()));
        }

        if (this.subtabsButton != null) {
            this.subtabsButton.setMessage(getToggleText(Config.ENABLE_SUBTABS.get()));
        }

        if (this.creativeConfigButton != null) {
            this.creativeConfigButton.setMessage(getToggleText(Config.SHOW_CREATIVE_CONFIG_BUTTON.get()));
        }

        if (this.fallbackPagesButton != null) {
            this.fallbackPagesButton.setMessage(getToggleText(Config.ENABLE_FALLBACK_PAGES.get()));
        }

        if (this.fallbackModeButton != null) {
            CtlFallbackMode mode = Config.FALLBACK_MODE.get();

            this.fallbackModeButton.active = Config.ENABLE_FALLBACK_PAGES.get();
            this.fallbackModeButton.setValue(mode);
            this.fallbackModeButton.setTooltip(Tooltip.create(getFallbackModeTooltip(mode)));
        }

        if (this.devVisualDebugButton != null) {
            this.devVisualDebugButton.setMessage(getToggleText(Config.ENABLE_DEVELOPER_VISUAL_DEBUG.get()));
        }
    }

    private static Component getFallbackModeTooltip(CtlFallbackMode mode) {
        return Component.translatable(switch (mode) {
            case BY_MOD_SECTION -> "screen.tab_layouts.config.fallback_mode.by_mod_section.tooltip";
            case BY_MOD_PAGE -> "screen.tab_layouts.config.fallback_mode.by_mod_page.tooltip";
        });
    }

    private static Component getToggleText(boolean value) {
        return Component.translatable(value ? "options.on" : "options.off");
    }

    private static void saveConfig() {
        Config.SPEC.save();
    }

    private static boolean isDevActive() {
        return !FMLEnvironment.production;
    }
}