package com.github.ruediste.jreact.elements;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.antepod.taffy.geometry.Size;
import com.antepod.taffy.style.AvailableSpace;
import com.github.ruediste.jreact.TextLayout;
import com.github.ruediste.jreact.style.StyleProperties;

public class TextElement extends ElementNode {

    public String text;

    public final List<Line> lines = new ArrayList<>();

    public TextElement(String text) {
        this(null, text);
    }

    public TextElement(Object key, String text) {
        super(key);
        this.text = Objects.requireNonNull(text);
    }

    @Override
    public Size<Float> measure(Size<Optional<Float>> knownDimensions, Size<AvailableSpace> availableSpace) {
        AvailableSpace knownWidth = AvailableSpace.fromOptional(knownDimensions.width.orElse(null));
        return TextLayout.measure(text, style, knownWidth, availableSpace.width,
                knownDimensions.width.orElse(0f));
    }

    @Override
    protected void paintContent(Graphics2D graphics) {
        graphics.setFont(StyleProperties.font(style));
        StyleProperties.Shadow shadow = StyleProperties.TEXT_SHADOW.get(style);
        for (Line line : lines) {
            float x = (float) bounds.x + line.x();
            float y = (float) bounds.y + line.baseline();
            if (shadow != null) {
                graphics.setColor(shadow.color());
                graphics.drawString(line.text(), x + (float) shadow.offsetX(), y + (float) shadow.offsetY());
            }
            graphics.setColor(StyleProperties.COLOR.get(style).toColor());
            graphics.drawString(line.text(), x, y);
        }
    }
}
