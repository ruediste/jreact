package com.github.ruediste.jreact;

import java.awt.geom.Rectangle2D;

public record RefreshResult(Node tree, Object focusKey,
        Rectangle2D dirtyRegion) {
}
