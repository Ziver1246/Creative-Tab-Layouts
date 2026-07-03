package com.ziver.tab_layouts.client.render;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class CtlJsonDebugTooltipRenderer {
    private CtlJsonDebugTooltipRenderer() {}

    private static final int LINE_HEIGHT = 10;
    private static final int PADDING = 4;
    private static final int MOUSE_OFFSET = 12;

    private static final int MAX_WIDTH = 360;
    private static final int MAX_VISIBLE_LINES = 28;

    private static final int COLOR_BOX_BACKGROUND = 0xDD000000;
    private static final int COLOR_BOX_BORDER = 0x66FFFFFF;

    private static final int COLOR_TITLE = 0xFFFFD76A;
    private static final int COLOR_SOURCE_LABEL = 0xFFB8A978;
    private static final int COLOR_SOURCE_VALUE = 0xFFE8DFC4;
    private static final int COLOR_ERROR = 0xFFFF5555;

    private static final int COLOR_JSON_DEFAULT = 0xFFA9B7C6;
    private static final int COLOR_JSON_BRACES = 0xFFFFFFFF;
    private static final int COLOR_JSON_PUNCTUATION = 0xFFFFFFFF;
    private static final int COLOR_JSON_KEY = 0xFFCC66CC;
    private static final int COLOR_JSON_STRING = 0xFF90E090;
    private static final int COLOR_JSON_NUMBER = 0xFFA0D0FF;
    private static final int COLOR_JSON_BOOLEAN = 0xFFFFAA33;
    private static final int COLOR_JSON_NULL = 0xFFFFAA33;

    public static void render(GuiGraphics graphics, Font font, int mouseX, int mouseY, int screenWidth, int screenHeight, String source, String prettyJson, Component error) {
        List<Line> lines = buildLines(source, prettyJson, error);

        if (lines.isEmpty()) return;

        lines = wrapLines(font, lines, MAX_WIDTH);

        if (lines.size() > MAX_VISIBLE_LINES) {
            lines = new ArrayList<>(lines.subList(0, MAX_VISIBLE_LINES));
            lines.add(Line.normal("...", COLOR_SOURCE_LABEL));
        }

        int contentWidth = 0;

        for (Line line : lines) {
            contentWidth = Math.max(contentWidth, line.width(font));
        }

        contentWidth = Math.min(contentWidth, MAX_WIDTH);

        int boxWidth = contentWidth + PADDING * 2;
        int boxHeight = lines.size() * LINE_HEIGHT + PADDING * 2;

        int x = mouseX + MOUSE_OFFSET;
        int y = mouseY + MOUSE_OFFSET;

        if (x + boxWidth > screenWidth) {
            x = mouseX - boxWidth - MOUSE_OFFSET;
        }

        if (y + boxHeight > screenHeight) y = screenHeight - boxHeight - 4;
        if (x < 4) x = 4;
        if (y < 4) y = 4;

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 500.0F);

        drawBox(graphics, x, y, boxWidth, boxHeight);

        int textY = y + PADDING;

        for (Line line : lines) {
            renderLine(graphics, font, line, x + PADDING, textY, boxWidth - PADDING * 2);
            textY += LINE_HEIGHT;
        }

        graphics.pose().popPose();
    }

    private static List<Line> buildLines(String source, String prettyJson, Component error) {
        List<Line> lines = new ArrayList<>();

        if (source != null && !source.isBlank()) {
            String[] sourceLines = source.replace("\r", "").split("\n");

            for (int i = 0; i < sourceLines.length; i++) {
                String sourceLine = sourceLines[i];

                if (i == 0) {
                    lines.add(Line.centered(sourceLine, COLOR_TITLE));
                    lines.add(Line.normal("", COLOR_JSON_DEFAULT));
                } else if (sourceLine.regionMatches(true, 0, "id: ", 0, "id: ".length())) {
                    lines.add(Line.parts(
                            new Part(sourceLine.substring(0, "id: ".length()), COLOR_SOURCE_LABEL),
                            new Part(sourceLine.substring("id: ".length()), COLOR_SOURCE_VALUE)
                    ));
                } else if (sourceLine.regionMatches(true, 0, "file: ", 0, "file: ".length())) {
                    lines.add(Line.parts(
                            new Part(sourceLine.substring(0, "file: ".length()), COLOR_SOURCE_LABEL),
                            new Part(sourceLine.substring("file: ".length()), COLOR_SOURCE_VALUE)
                    ));
                } else {
                    lines.add(Line.normal(sourceLine, COLOR_SOURCE_VALUE));
                }
            }

            lines.add(Line.normal("", COLOR_JSON_DEFAULT));
        }

        boolean hasError = error != null && !error.getString().isBlank();

        if (hasError) {
            lines.add(Line.normal(error.getString(), COLOR_ERROR));
            lines.add(Line.normal("", COLOR_JSON_DEFAULT));
        }

        if (prettyJson == null || prettyJson.isBlank()) {
            if (!hasError) {
                lines.add(Line.normal(Component.translatable("screen.tab_layouts.missing_json_config").getString(), COLOR_ERROR));
            }

            return lines;
        }

        String[] jsonLines = prettyJson.replace("\r", "").split("\n");

        for (String line : jsonLines) {
            lines.add(Line.json(line));
        }

        return lines;
    }

    private static List<Line> wrapLines(Font font, List<Line> lines, int maxWidth) {
        List<Line> wrapped = new ArrayList<>();

        for (Line line : lines) {
            if (line.width(font) <= maxWidth) {
                wrapped.add(line);
                continue;
            }

            if (line.highlightJson()) {
                wrapHighlightedJsonLine(font, line.text(), maxWidth, wrapped);
                continue;
            }

            if (line.parts().size() == 1) {
                Part part = line.parts().getFirst();
                wrapPlainLine(font, part.text(), part.color(), maxWidth, line.centered(), wrapped);
                continue;
            }

            wrapPartLine(font, line, maxWidth, wrapped);
        }

        return wrapped;
    }

    private static void wrapHighlightedJsonLine(Font font, String text, int maxWidth, List<Line> output) {
        List<Part> highlightedParts = highlightJsonLine(text);

        List<Part> currentParts = new ArrayList<>();
        int currentWidth = 0;

        for (Part part : highlightedParts) {
            String remaining = part.text();

            while (!remaining.isEmpty()) {
                int remainingWidth = font.width(remaining);

                if (currentWidth + remainingWidth <= maxWidth) {
                    currentParts.add(new Part(remaining, part.color()));
                    currentWidth += remainingWidth;
                    remaining = "";
                    continue;
                }

                int availableWidth = maxWidth - currentWidth;

                if (availableWidth <= 0) {
                    if (!currentParts.isEmpty()) {
                        output.add(new Line(List.copyOf(currentParts), false, false));
                        currentParts.clear();
                    }

                    currentWidth = 0;
                    continue;
                }

                int cut = findWrapPosition(font, remaining, availableWidth);
                String current = remaining.substring(0, cut);

                if (current.isEmpty()) {
                    current = remaining.substring(0, Math.min(remaining.length(), 1));
                    cut = current.length();
                }

                currentParts.add(new Part(current, part.color()));
                output.add(new Line(List.copyOf(currentParts), false, false));

                currentParts.clear();
                currentWidth = 0;

                remaining = remaining.substring(cut);
            }
        }

        if (!currentParts.isEmpty()) {
            output.add(new Line(List.copyOf(currentParts), false, false));
        }
    }

    private static void wrapPlainLine(Font font, String text, int color, int maxWidth, boolean centered, List<Line> output) {
        if (text == null || text.isEmpty()) {
            output.add(Line.normal("", color));
            return;
        }

        String remaining = text;

        while (!remaining.isEmpty()) {
            int cut = findWrapPosition(font, remaining, maxWidth);
            String current = remaining.substring(0, cut).stripTrailing();

            if (current.isEmpty()) {
                current = remaining.substring(0, Math.min(remaining.length(), 1));
                cut = current.length();
            }

            output.add(new Line(List.of(new Part(current, color)), centered, false));

            remaining = remaining.substring(cut).stripLeading();
        }
    }

    private static void wrapPartLine(Font font, Line line, int maxWidth, List<Line> output) {
        List<Part> currentParts = new ArrayList<>();
        int currentWidth = 0;

        for (Part part : line.parts()) {
            String remaining = part.text();

            while (!remaining.isEmpty()) {
                int remainingWidth = font.width(remaining);

                if (currentWidth + remainingWidth <= maxWidth) {
                    currentParts.add(new Part(remaining, part.color()));
                    currentWidth += remainingWidth;
                    remaining = "";
                    continue;
                }

                int availableWidth = maxWidth - currentWidth;

                if (availableWidth <= 0) {
                    output.add(new Line(List.copyOf(currentParts), line.centered(), false));
                    currentParts.clear();
                    currentWidth = 0;
                    continue;
                }

                int cut = findWrapPosition(font, remaining, availableWidth);
                String current = remaining.substring(0, cut).stripTrailing();

                if (current.isEmpty()) {
                    output.add(new Line(List.copyOf(currentParts), line.centered(), false));
                    currentParts.clear();
                    currentWidth = 0;
                    continue;
                }

                currentParts.add(new Part(current, part.color()));
                output.add(new Line(List.copyOf(currentParts), line.centered(), false));

                currentParts.clear();
                currentWidth = 0;

                remaining = remaining.substring(cut).stripLeading();
            }
        }

        if (!currentParts.isEmpty()) {
            output.add(new Line(List.copyOf(currentParts), line.centered(), false));
        }
    }

    private static int findWrapPosition(Font font, String text, int maxWidth) {
        int lastBreak = -1;

        for (int i = 1; i <= text.length(); i++) {
            char c = text.charAt(i - 1);

            if (Character.isWhitespace(c) || c == '/' || c == ',' || c == ':' || c == '_' || c == '-') {
                lastBreak = i;
            }

            if (font.width(text.substring(0, i)) > maxWidth) {
                if (lastBreak > 0) {
                    return lastBreak;
                }

                return Math.max(1, i - 1);
            }
        }

        return text.length();
    }

    private static void renderLine(GuiGraphics graphics, Font font, Line line, int x, int y, int contentWidth) {
        if (line.highlightJson()) {
            renderHighlightedJsonLine(graphics, font, line.text(), x, y);
            return;
        }

        if (line.centered()) {
            int lineWidth = line.width(font);
            int centeredX = x + contentWidth / 2 - lineWidth / 2;

            for (Part part : line.parts()) {
                graphics.drawString(font, part.text(), centeredX, y, part.color(), false);
                centeredX += font.width(part.text());
            }

            return;
        }

        int drawX = x;

        for (Part part : line.parts()) {
            graphics.drawString(font, part.text(), drawX, y, part.color(), false);
            drawX += font.width(part.text());
        }
    }

    private static void drawBox(GuiGraphics graphics, int x, int y, int width, int height) {
        int x2 = x + width;
        int y2 = y + height;

        graphics.fill(x, y, x2, y2, COLOR_BOX_BACKGROUND);
        graphics.fill(x, y, x2, y + 1, COLOR_BOX_BORDER);
        graphics.fill(x, y2 - 1, x2, y2, COLOR_BOX_BORDER);
        graphics.fill(x, y, x + 1, y2, COLOR_BOX_BORDER);
        graphics.fill(x2 - 1, y, x2, y2, COLOR_BOX_BORDER);
    }

    private static void renderHighlightedJsonLine(GuiGraphics graphics, Font font, String line, int x, int y) {
        List<Part> parts = highlightJsonLine(line);

        int drawX = x;

        for (Part part : parts) {
            if (!part.text().isEmpty()) {
                graphics.drawString(font, part.text(), drawX, y, part.color(), false);
                drawX += font.width(part.text());
            }
        }
    }

    private static List<Part> highlightJsonLine(String line) {
        List<Part> parts = new ArrayList<>();

        int i = 0;

        while (i < line.length()) {
            char c = line.charAt(i);

            if (Character.isWhitespace(c)) {
                int start = i;

                while (i < line.length() && Character.isWhitespace(line.charAt(i))) {
                    i++;
                }

                parts.add(new Part(line.substring(start, i), COLOR_JSON_DEFAULT));
                continue;
            }

            if (c == '{' || c == '}' || c == '[' || c == ']') {
                parts.add(new Part(String.valueOf(c), COLOR_JSON_BRACES));
                i++;
                continue;
            }

            if (c == ':' || c == ',') {
                parts.add(new Part(String.valueOf(c), COLOR_JSON_PUNCTUATION));
                i++;
                continue;
            }

            if (c == '"') {
                int start = i;
                i++;

                boolean escaped = false;

                while (i < line.length()) {
                    char current = line.charAt(i);

                    if (current == '"' && !escaped) {
                        i++;
                        break;
                    }

                    if (current == '\\' && !escaped) {
                        escaped = true;
                    } else {
                        escaped = false;
                    }

                    i++;
                }

                String quoted = line.substring(start, i);

                int j = i;

                while (j < line.length() && Character.isWhitespace(line.charAt(j))) {
                    j++;
                }

                boolean isKey = j < line.length() && line.charAt(j) == ':';
                parts.add(new Part(quoted, isKey ? COLOR_JSON_KEY : COLOR_JSON_STRING));
                continue;
            }

            if (Character.isDigit(c) || c == '-') {
                int start = i;
                i++;

                while (i < line.length()) {
                    char current = line.charAt(i);

                    if (Character.isDigit(current) || current == '.' || current == 'e' || current == 'E' || current == '+' || current == '-') {
                        i++;
                    } else {
                        break;
                    }
                }

                parts.add(new Part(line.substring(start, i), COLOR_JSON_NUMBER));
                continue;
            }

            if (line.startsWith("true", i)) {
                parts.add(new Part("true", COLOR_JSON_BOOLEAN));
                i += 4;
                continue;
            }

            if (line.startsWith("false", i)) {
                parts.add(new Part("false", COLOR_JSON_BOOLEAN));
                i += 5;
                continue;
            }

            if (line.startsWith("null", i)) {
                parts.add(new Part("null", COLOR_JSON_NULL));
                i += 4;
                continue;
            }

            parts.add(new Part(String.valueOf(c), COLOR_JSON_DEFAULT));
            i++;
        }

        return parts;
    }

    private record Part(String text, int color) {}

    private record Line(List<Part> parts, boolean centered, boolean highlightJson) {

        static Line normal(String text, int color) {
            return new Line(List.of(new Part(text, color)), false, false);
        }

        static Line centered(String text, int color) {
            return new Line(List.of(new Part(text, color)), true, false);
        }

        static Line parts(Part... parts) {
            return new Line(List.of(parts), false, false);
        }

        static Line json(String text) {
            return new Line(List.of(new Part(text, COLOR_JSON_DEFAULT)), false, true);
        }

        String text() {
            StringBuilder builder = new StringBuilder();

            for (Part part : parts) {
                builder.append(part.text());
            }

            return builder.toString();
        }

        int width(Font font) {
            int width = 0;

            for (Part part : parts) {
                width += font.width(part.text());
            }

            return width;
        }
    }
}