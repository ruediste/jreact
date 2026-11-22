package com.github.ruediste.jreact.elements;

import java.awt.Graphics2D;
import java.util.Objects;
import java.util.Optional;

import javax.swing.Icon;

import com.antepod.taffy.geometry.Size;
import com.antepod.taffy.style.AvailableSpace;
import com.github.ruediste.jreact.style.StyleProperties;

public class IconElement extends ElementNode {

    public Icon icon;

    public IconElement(Icon icon) {
        this(null, icon);
    }

    public IconElement(Object key, Icon icon) {
        super(key);
        this.icon = Objects.requireNonNull(icon);
    }

    @Override
    public Size<Float> measure(Size<Optional<Float>> knownDimensions, Size<AvailableSpace> availableSpace) {
        return new Size<>((float) icon.getIconWidth(), (float) icon.getIconHeight());
    }

    @Override
    protected void paintContent(Graphics2D graphics) {
        graphics.setColor(StyleProperties.COLOR.get(this).toColor());
        icon.paintIcon(null, graphics, (int) Math.round(bounds.x), (int) Math.round(bounds.y));
    }
}
