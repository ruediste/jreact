package com.github.ruediste.jreact;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.antepod.taffy.style.Display;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties;

public final class JReactPanel extends JPanel {

    private final JReactRoot root;

    private RefreshResult prepared;
    private boolean refreshScheduled;
    private boolean paintPending;
    private boolean passiveScheduled;

    public JReactPanel(Runnable rootRender) {
        if (!SwingUtilities.isEventDispatchThread())
            throw new IllegalStateException("JReactPanel must be created on the event dispatch thread");
        root = new JReactRoot(Objects.requireNonNull(rootRender));
        root.invalidationCallback = this::requestRefresh;
        root.onComponentAdded = this::add;
        root.onComponentRemoved = this::remove;
        setLayout(null);
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
                root.pointerPressed(event.getX(), event.getY());
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                root.pointerReleased(event.getX(), event.getY());
            }

            @Override
            public void mouseExited(MouseEvent event) {
                root.pointerExited();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                root.pointerMoved(event.getX(), event.getY());
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                root.pointerMoved(event.getX(), event.getY());
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                root.keyPressed(event.getKeyCode());
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                requestRefresh();
            }
        });
    }

    public JReactRoot root() {
        return root;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestRefresh();
    }

    public void requestRefresh() {
        if (refreshScheduled)
            return;
        refreshScheduled = true;
        SwingUtilities.invokeLater(() -> {
            refreshScheduled = false;
            refreshNow();
        });
    }

    private void refreshNow() {
        if (getWidth() <= 0 || getHeight() <= 0)
            return;
        prepared = root.refresh(getWidth(), getHeight());
        paintPending = true;

        java.awt.geom.Rectangle2D dirty = prepared.dirtyRegion();
        if (dirty == null || dirty.getWidth() <= 0 || dirty.getHeight() <= 0) {
            repaint();
        } else {
            repaint((int) Math.floor(dirty.getX()), (int) Math.floor(dirty.getY()),
                    (int) Math.ceil(dirty.getWidth()), (int) Math.ceil(dirty.getHeight()));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (prepared == null) {
            requestRefresh();
            return;
        }

        Graphics2D copy = (Graphics2D) graphics.create();
        try {
            copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            copy.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            paintTree(prepared.tree(), copy, graphics.getClipBounds());
        } finally {
            copy.dispose();
        }

        if (paintPending) {
            paintPending = false;
            schedulePaintCompleted();
        }
    }

    private void schedulePaintCompleted() {
        if (passiveScheduled)
            return;
        passiveScheduled = true;
        SwingUtilities.invokeLater(() -> {
            passiveScheduled = false;
            root.paintCompleted();
            if (root.needsRender())
                requestRefresh();
        });
    }

    private static void paintTree(Node node, Graphics2D graphics, Rectangle clip) {
        if (node instanceof ElementNode visual) {
            if (StyleProperties.DISPLAY.get(visual.style) == Display.NONE)
                return;
            visual.paint(graphics, clip);
        }
        for (Node child : node.children())
            paintTree(child, graphics, clip);
    }

    public void close() {
        root.close();
        prepared = null;
    }
}
