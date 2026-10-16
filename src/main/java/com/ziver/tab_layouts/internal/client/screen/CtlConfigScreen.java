package com.ziver.tab_layouts.internal.client.screen;

import com.ziver.tab_layouts.CtlConfig;
import com.ziver.tab_layouts.internal.layout.fallback.CtlFallbackMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public final class CtlConfigScreen extends Screen {
    private static final Component TITLE = Component.translatable("screen.tab_layouts.config.title");

    private static final int SCREEN_MARGIN = 8;
    private static final int PANEL_PADDING = 14;
    private static final int COLUMN_GAP = 32;

    private static final int ROW_HEIGHT = 24;
    private static final int CONTROL_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int LABEL_BUTTON_GAP = 12;

    private static final int SECTION_TITLE_HEIGHT = 12;
    private static final int SECTION_SEPARATOR_GAP = 3;
    private static final int SECTION_SEPARATOR_HEIGHT = 3;
    private static final int SECTION_CONTENT_GAP = 5;
    private static final int SECTION_GAP = 12;

    private static final int TITLE_GAP = 12;
    private static final int DONE_WIDTH = 200;

    private static final int CORNER_SIZE = 2;
    private static final int SEPARATOR_END_SIZE = 1;

    private static final int PANEL_BACKGROUND = 0xB014171C;
    private static final int PANEL_BORDER = 0xFF8A96A6;
    private static final int SEPARATOR_COLOR = 0xFF697482;

    private static final int TEXT_PRIMARY = 0xFFF2F2F2;
    private static final int TEXT_SECONDARY = 0xFFA5ACB7;

    private final Screen parent;
    private final List<Separator> sectionSeparators = new ArrayList<>();

    private Button builtinVanillaLayoutsButton;
    private Button subtabsButton;
    private Button creativeConfigButton;
    private Button survivalConfigButton;
    private Button fallbackPagesButton;
    private CycleButton<CtlFallbackMode> fallbackModeButton;
    private Button devVisualDebugButton;

    private int panelX;
    private int panelY;
    private int panelWidth;
    private int panelHeight;

    public CtlConfigScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.sectionSeparators.clear();

        this.panelX = SCREEN_MARGIN;
        this.panelY = SCREEN_MARGIN;
        this.panelWidth = this.width - SCREEN_MARGIN * 2;
        this.panelHeight = this.height - SCREEN_MARGIN * 2;

        addRenderableOnly(this::renderPanel);

        int titleY = this.panelY + PANEL_PADDING;
        addRenderableWidget(new StringWidget(this.panelX + PANEL_PADDING, titleY, this.panelWidth - PANEL_PADDING * 2, this.font.lineHeight, TITLE, this.font)
                .alignCenter()
                .setColor(TEXT_PRIMARY));

        int contentX = this.panelX + PANEL_PADDING;
        int contentY = titleY + this.font.lineHeight + TITLE_GAP;
        int contentWidth = this.panelWidth - PANEL_PADDING * 2;
        int columnWidth = (contentWidth - COLUMN_GAP) / 2;

        int leftX = contentX;
        int rightX = contentX + columnWidth + COLUMN_GAP;

        int leftY = addGeneralSection(leftX, contentY, columnWidth);
        leftY += SECTION_GAP;
        addInterfaceSection(leftX, leftY, columnWidth);

        int rightY = addFallbackSection(rightX, contentY, columnWidth);

        if (isDevActive()) {
            rightY += SECTION_GAP;
            addDeveloperSection(rightX, rightY, columnWidth);
        }

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(this.width / 2 - DONE_WIDTH / 2, this.panelY + this.panelHeight - PANEL_PADDING - BUTTON_HEIGHT, DONE_WIDTH, BUTTON_HEIGHT)
                .build());

        updateButtonStates();
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
    }

    private int addGeneralSection(int x, int y, int width) {
        y = addSection(x, y, width, Component.translatable("screen.tab_layouts.config.general_section"));

        Component builtinVanillaLayoutsLabel = Component.translatable("screen.tab_layouts.config.builtin_vanilla_layouts");
        Component builtinVanillaLayoutsTooltip = Component.translatable("screen.tab_layouts.config.builtin_vanilla_layouts.tooltip");

        Component subtabsLabel = Component.translatable("screen.tab_layouts.config.subtabs");
        Component subtabsTooltip = Component.translatable("screen.tab_layouts.config.subtabs.tooltip");

        this.builtinVanillaLayoutsButton = addToggleRow(x, y, width, builtinVanillaLayoutsLabel, builtinVanillaLayoutsTooltip, CtlConfig.ENABLE_BUILTIN_VANILLA_LAYOUTS.get(), button -> {
            CtlConfig.ENABLE_BUILTIN_VANILLA_LAYOUTS.set(!CtlConfig.ENABLE_BUILTIN_VANILLA_LAYOUTS.get());
            saveConfig();
            updateButtonStates();
        });
        y += ROW_HEIGHT;

        this.subtabsButton = addToggleRow(x, y, width, subtabsLabel, subtabsTooltip, CtlConfig.ENABLE_SUBTABS.get(), button -> {
            CtlConfig.ENABLE_SUBTABS.set(!CtlConfig.ENABLE_SUBTABS.get());
            saveConfig();
            updateButtonStates();
        });

        return y + ROW_HEIGHT;
    }

    private int addInterfaceSection(int x, int y, int width) {
        y = addSection(x, y, width, Component.translatable("screen.tab_layouts.config.interface_section"));

        Component creativeConfigButtonLabel = Component.translatable("screen.tab_layouts.config.creative_button");
        Component creativeConfigButtonTooltip = Component.translatable("screen.tab_layouts.config.creative_button.tooltip");

        Component survivalConfigButtonLabel = Component.translatable("screen.tab_layouts.config.survival_button");
        Component survivalConfigButtonTooltip = Component.translatable("screen.tab_layouts.config.survival_button.tooltip");

        this.creativeConfigButton = addToggleRow(x, y, width, creativeConfigButtonLabel, creativeConfigButtonTooltip, CtlConfig.SHOW_CREATIVE_CONFIG_BUTTON.get(), button -> {
            CtlConfig.SHOW_CREATIVE_CONFIG_BUTTON.set(!CtlConfig.SHOW_CREATIVE_CONFIG_BUTTON.get());
            saveConfig();
            updateButtonStates();
        });
        y += ROW_HEIGHT;

        this.survivalConfigButton = addToggleRow(x, y, width, survivalConfigButtonLabel, survivalConfigButtonTooltip, CtlConfig.SHOW_SURVIVAL_CONFIG_BUTTON.get(), button -> {
            CtlConfig.SHOW_SURVIVAL_CONFIG_BUTTON.set(!CtlConfig.SHOW_SURVIVAL_CONFIG_BUTTON.get());
            saveConfig();
            updateButtonStates();
        });

        return y + ROW_HEIGHT;
    }

    private int addFallbackSection(int x, int y, int width) {
        y = addSection(x, y, width, Component.translatable("screen.tab_layouts.config.fallback_section"));

        Component fallbackPagesLabel = Component.translatable("screen.tab_layouts.config.fallback_pages");
        Component fallbackPagesTooltip = Component.translatable("screen.tab_layouts.config.fallback_pages.tooltip");

        Component fallbackModeLabel = Component.translatable("screen.tab_layouts.config.fallback_mode");
        Component fallbackModeTooltip = Component.translatable("screen.tab_layouts.config.fallback_mode.tooltip");

        this.fallbackPagesButton = addToggleRow(x, y, width, fallbackPagesLabel, fallbackPagesTooltip, CtlConfig.ENABLE_FALLBACK_PAGES.get(), button -> {
            CtlConfig.ENABLE_FALLBACK_PAGES.set(!CtlConfig.ENABLE_FALLBACK_PAGES.get());
            saveConfig();
            updateButtonStates();
        });
        y += ROW_HEIGHT;

        this.fallbackModeButton = addCycleRow(x, y, width, fallbackModeLabel, fallbackModeTooltip);

        return y + ROW_HEIGHT;
    }

    private int addDeveloperSection(int x, int y, int width) {
        y = addSection(x, y, width, Component.translatable("screen.tab_layouts.config.developer_section"));

        Component devVisualDebugLabel = Component.translatable("screen.tab_layouts.config.dev_visual_debug");
        Component devVisualDebugTooltip = Component.translatable("screen.tab_layouts.config.dev_visual_debug.tooltip");

        this.devVisualDebugButton = addToggleRow(x, y, width, devVisualDebugLabel, devVisualDebugTooltip, CtlConfig.ENABLE_DEVELOPER_VISUAL_DEBUG.get(), button -> {
            CtlConfig.ENABLE_DEVELOPER_VISUAL_DEBUG.set(!CtlConfig.ENABLE_DEVELOPER_VISUAL_DEBUG.get());
            saveConfig();
            updateButtonStates();
        });

        return y + ROW_HEIGHT;
    }

    private int addSection(int x, int y, int width, Component title) {
        addRenderableWidget(new StringWidget(x, y, width, SECTION_TITLE_HEIGHT, title, this.font)
                .alignLeft()
                .setColor(TEXT_SECONDARY));

        int separatorY = y + SECTION_TITLE_HEIGHT + SECTION_SEPARATOR_GAP;
        this.sectionSeparators.add(new Separator(x, separatorY, width));

        return separatorY + SECTION_SEPARATOR_HEIGHT + SECTION_CONTENT_GAP;
    }

    private Button addToggleRow(int x, int y, int width, Component label, Component tooltip, boolean value, Button.OnPress onPress) {
        int buttonX = x + width - CONTROL_WIDTH;
        int labelWidth = width - CONTROL_WIDTH - LABEL_BUTTON_GAP;

        StringWidget labelWidget = new StringWidget(x, y, labelWidth, BUTTON_HEIGHT, label, this.font).alignLeft();
        labelWidget.setTooltip(Tooltip.create(tooltip));
        addRenderableWidget(labelWidget);

        return addRenderableWidget(Button.builder(getToggleText(value), onPress)
                .bounds(buttonX, y, CONTROL_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.create(tooltip))
                .build());
    }

    private CycleButton<CtlFallbackMode> addCycleRow(int x, int y, int width, Component label, Component tooltip) {
        int buttonX = x + width - CONTROL_WIDTH;
        int labelWidth = width - CONTROL_WIDTH - LABEL_BUTTON_GAP;

        StringWidget labelWidget = new StringWidget(x, y, labelWidth, BUTTON_HEIGHT, label, this.font).alignLeft();
        labelWidget.setTooltip(Tooltip.create(tooltip));
        addRenderableWidget(labelWidget);

        CycleButton<CtlFallbackMode> button = CycleButton.builder(CtlFallbackMode::displayName)
                .withValues(CtlFallbackMode.values())
                .withInitialValue(CtlConfig.FALLBACK_MODE.get())
                .displayOnlyValue()
                .create(buttonX, y, CONTROL_WIDTH, BUTTON_HEIGHT, Component.empty(), (cycleButton, value) -> {
                    CtlConfig.FALLBACK_MODE.set(value);
                    saveConfig();
                    updateButtonStates();
                });

        button.setTooltip(Tooltip.create(tooltip));

        return addRenderableWidget(button);
    }

    private void updateButtonStates() {
        if (this.builtinVanillaLayoutsButton != null)
            this.builtinVanillaLayoutsButton.setMessage(getToggleText(CtlConfig.ENABLE_BUILTIN_VANILLA_LAYOUTS.get()));

        if (this.subtabsButton != null)
            this.subtabsButton.setMessage(getToggleText(CtlConfig.ENABLE_SUBTABS.get()));

        if (this.creativeConfigButton != null)
            this.creativeConfigButton.setMessage(getToggleText(CtlConfig.SHOW_CREATIVE_CONFIG_BUTTON.get()));

        if (this.survivalConfigButton != null)
            this.survivalConfigButton.setMessage(getToggleText(CtlConfig.SHOW_SURVIVAL_CONFIG_BUTTON.get()));

        if (this.fallbackPagesButton != null)
            this.fallbackPagesButton.setMessage(getToggleText(CtlConfig.ENABLE_FALLBACK_PAGES.get()));

        if (this.fallbackModeButton != null) {
            CtlFallbackMode mode = CtlConfig.FALLBACK_MODE.get();

            this.fallbackModeButton.active = CtlConfig.ENABLE_FALLBACK_PAGES.get();
            this.fallbackModeButton.setValue(mode);
            this.fallbackModeButton.setTooltip(Tooltip.create(getFallbackModeTooltip(mode)));
        }

        if (this.devVisualDebugButton != null)
            this.devVisualDebugButton.setMessage(getToggleText(CtlConfig.ENABLE_DEVELOPER_VISUAL_DEBUG.get()));
    }

    private void renderPanel(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        drawPixelPanel(graphics, this.panelX, this.panelY, this.panelWidth, this.panelHeight, PANEL_BACKGROUND, PANEL_BORDER);

        for (Separator separator : this.sectionSeparators)
            renderPixelSeparator(graphics, separator.x(), separator.y(), separator.width(), SEPARATOR_COLOR);
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
        CtlConfig.SPEC.save();
    }

    private static boolean isDevActive() {
        return !FMLEnvironment.production;
    }

    private static void renderPixelSeparator(GuiGraphics graphics, int x, int y, int width, int color) {
        if (width < SEPARATOR_END_SIZE * 2 + 1) return;

        int centerY = y + SECTION_SEPARATOR_HEIGHT / 2;

        graphics.fill(x, centerY, x + width, centerY + 1, color);
        graphics.fill(x, y, x + SEPARATOR_END_SIZE, y + SECTION_SEPARATOR_HEIGHT, color);
        graphics.fill(x + width - SEPARATOR_END_SIZE, y, x + width, y + SECTION_SEPARATOR_HEIGHT, color);
    }

    private static void drawPixelPanel(GuiGraphics graphics, int x, int y, int width, int height, int background, int border) {
        int right = x + width;
        int bottom = y + height;

        graphics.fill(x + 1, y + 1, right - 1, bottom - 1, background);
        graphics.fill(x + CORNER_SIZE, y, right - CORNER_SIZE, y + 1, border);
        graphics.fill(x + CORNER_SIZE, bottom - 1, right - CORNER_SIZE, bottom, border);
        graphics.fill(x, y + CORNER_SIZE, x + 1, bottom - CORNER_SIZE, border);
        graphics.fill(right - 1, y + CORNER_SIZE, right, bottom - CORNER_SIZE, border);

        graphics.fill(x + 1, y + 1, x + CORNER_SIZE, y + CORNER_SIZE, border);
        graphics.fill(right - CORNER_SIZE, y + 1, right - 1, y + CORNER_SIZE, border);
        graphics.fill(x + 1, bottom - CORNER_SIZE, x + CORNER_SIZE, bottom - 1, border);
        graphics.fill(right - CORNER_SIZE, bottom - CORNER_SIZE, right - 1, bottom - 1, border);
    }

    private record Separator(int x, int y, int width) {}
}