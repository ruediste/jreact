package com.github.ruediste.jreact;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Shape;

import org.junit.jupiter.api.Test;

import com.antepod.taffy.style.LengthPercentage;
import com.github.ruediste.jreact.nodes.DivNode;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.StyleProperties.BorderRadius;

class PaintUtilsTest {

    /**
     * Verifies that percentage radii resolve against the width for the horizontal
     * component and against the height for the vertical component, producing an
     * elliptical corner.
     */
    @Test
    void percentageRadiiResolveAgainstBounds() {
        DivNode node = new DivNode();
        StyleProperties.BORDER_TOP_LEFT_RADIUS.set(node.style,
                new BorderRadius(LengthPercentage.percent(0.5f), LengthPercentage.percent(0.5f)));
        node.bounds.setRect(0, 0, 100, 50);

        Shape shape = PaintUtils.shape(node);

        assertFalse(shape.contains(1, 1), "point outside the elliptical corner");
        assertTrue(shape.contains(25, 12.5), "point inside the elliptical corner");
        assertTrue(shape.contains(99, 49), "sharp corners stay filled");
    }
}
