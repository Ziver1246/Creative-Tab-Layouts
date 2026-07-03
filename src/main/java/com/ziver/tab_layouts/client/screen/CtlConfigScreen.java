package com.ziver.tab_layouts.client.screen;

import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.internal.layout.CtlFallbackMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

public final class CtlConfigScreen extends Screen {

    private static final Component TITLE = Component.translatable("screen.tab_layouts.config.title");

    private final Screen parent;

    private Button toggleBuiltinVanillaLayoutsButton;
    private Button toggleFallbackPagesButton;
    private CycleButton<CtlFallbackMode> fallbackModeButton;
    private Button toggleDevVisualDebugButton;

    public CtlConfigScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 260;
        int buttonHeight = 20;
        int spacing = 24;
        int centerX = this.width / 2 - buttonWidth / 2;
        int startY = this.height / 2 - 48;

        int row = 0;

        this.toggleBuiltinVanillaLayoutsButton = this.addRenderableWidget(Button.builder(getToggleBuiltinVanillaLayoutsButtonText(),
                button -> toggleBuiltinVanillaLayouts()).bounds(centerX, startY + spacing * row++, buttonWidth, buttonHeight).build());

        this.toggleFallbackPagesButton = this.addRenderableWidget(Button.builder(getToggleFallbackPagesButtonText(),
                button -> toggleFallbackPages()).bounds(centerX, startY + spacing * row++, buttonWidth, buttonHeight).build());

        this.fallbackModeButton = this.addRenderableWidget(CycleButton
                .builder(CtlFallbackMode::displayName)
                .withValues(CtlFallbackMode.values())
                .withInitialValue(Config.FALLBACK_MODE.get())
                .create(centerX, startY + spacing * row++, buttonWidth, buttonHeight, Component.translatable("screen.tab_layouts.config.fallback_mode"),
                        (button, value) -> {
                            Config.FALLBACK_MODE.set(value);
                            Config.SPEC.save();
                        }
                ));

        this.fallbackModeButton.active = Config.ENABLE_FALLBACK_PAGES.get();

        if (isDevActive()) {
            this.toggleDevVisualDebugButton = this.addRenderableWidget(Button.builder(getToggleDevVisualDebugButtonText(),
                    button -> toggleDevVisualDebug()).bounds(centerX, startY + spacing * row++, buttonWidth, buttonHeight).build());
        } else {
            this.toggleDevVisualDebugButton = null;
        }

        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose()
        ).bounds(centerX, startY + spacing * (row + 1), buttonWidth, buttonHeight).build());
    }

    private void toggleBuiltinVanillaLayouts() {
        boolean next = !Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.get();

        Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.set(next);
        Config.SPEC.save();

        this.toggleBuiltinVanillaLayoutsButton.setMessage(getToggleBuiltinVanillaLayoutsButtonText());
    }

    private Component getToggleBuiltinVanillaLayoutsButtonText() {
        return Component.translatable(Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.get()
                ? "screen.tab_layouts.config.builtin_vanilla_layouts.enabled" : "screen.tab_layouts.config.builtin_vanilla_layouts.disabled");
    }

    private void toggleFallbackPages() {
        boolean next = !Config.ENABLE_FALLBACK_PAGES.get();

        Config.ENABLE_FALLBACK_PAGES.set(next);
        Config.SPEC.save();

        this.toggleFallbackPagesButton.setMessage(getToggleFallbackPagesButtonText());

        if (this.fallbackModeButton != null) {
            this.fallbackModeButton.active = next;
        }
    }

    private Component getToggleFallbackPagesButtonText() {
        return Component.translatable(Config.ENABLE_FALLBACK_PAGES.get()
                ? "screen.tab_layouts.config.fallback_pages.enabled" : "screen.tab_layouts.config.fallback_pages.disabled");
    }

    private void toggleDevVisualDebug() {
        boolean next = !Config.ENABLE_DEVELOPER_VISUAL_DEBUG.get();

        Config.ENABLE_DEVELOPER_VISUAL_DEBUG.set(next);
        Config.SPEC.save();

        if (this.toggleDevVisualDebugButton != null) this.toggleDevVisualDebugButton.setMessage(getToggleDevVisualDebugButtonText());
    }

    private Component getToggleDevVisualDebugButtonText() {
        return Component.translatable(Config.ENABLE_DEVELOPER_VISUAL_DEBUG.get()
                ? "screen.tab_layouts.config.dev_visual_debug.enabled" : "screen.tab_layouts.config.dev_visual_debug.disabled");
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        Component restartWarning = Component.translatable("screen.tab_layouts.config.restart_warning");
        graphics.drawCenteredString(this.font, restartWarning, this.width / 2, this.height / 2 + 95, 0xAAAAAA);
    }

    private static boolean isDevActive() {
        return !FMLEnvironment.production;
    }
}