package com.github.ruediste.jreact.nodes;

import java.awt.Graphics2D;
import java.util.Objects;

import com.github.ruediste.jreact.style.StyleProperties;

public class TextNode extends VisualNode {

    public String text;

    public TextNode(String text) {
        this(null, text);
    }

    public TextNode(Object key, String text) {
        super(key);
        this.text = Objects.requireNonNull(text);
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
            graphics.setColor(StyleProperties.COLOR.get(style));
            graphics.drawString(line.text(), x, y);
        }
    }
}
