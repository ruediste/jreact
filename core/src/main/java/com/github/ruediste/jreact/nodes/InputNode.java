package com.github.ruediste.jreact.nodes;

import java.awt.Dimension;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.antepod.taffy.geometry.Size;
import com.antepod.taffy.style.AvailableSpace;
import com.github.ruediste.jreact.JReactContext;
import com.github.ruediste.jreact.style.StyleProperties;

public class InputNode extends ElementNode {

    public String value;
    public Consumer<String> onChange;
    private Handle handle;

    public InputNode(String value, Consumer<String> onChange) {
        this(null, value, onChange);
    }

    public InputNode(Object key, String value, Consumer<String> onChange) {
        super(key);
        this.value = Objects.requireNonNull(value);
        this.onChange = Objects.requireNonNull(onChange);
    }

    @Override
    public Size<Float> measure(Size<Optional<Float>> knownDimensions, Size<AvailableSpace> availableSpace) {
        if (handle == null)
            return Size.zeroFloat();
        handle.field.setFont(StyleProperties.font(style));
        Dimension preferred = handle.field.getPreferredSize();
        return new Size<>((float) preferred.width, (float) preferred.height);
    }

    @Override
    public void created() {
        super.created();
        handle = new Handle(this);
        JReactContext.root().onComponentAdded.accept(handle.field);
    }

    @Override
    public void reconcile(Node oldNode) {
        super.reconcile(oldNode);
        handle = ((InputNode) oldNode).handle;
        handle.node = this;
    }

    @Override
    public void afterLayout() {
        super.afterLayout();
        handle.sync();
    }

    @Override
    public void dispose() {
        JReactContext.root().onComponentRemoved.accept(handle.field);
        super.dispose();
    }

    private static class Handle {
        InputNode node;
        final JTextField field;
        boolean syncing;

        private Handle(InputNode node) {
            this.node = node;
            field = new JTextField();
            field.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent event) {
                    textChanged();
                }

                @Override
                public void removeUpdate(DocumentEvent event) {
                    textChanged();
                }

                @Override
                public void changedUpdate(DocumentEvent event) {
                    textChanged();
                }
            });
        }

        private void textChanged() {
            if (syncing)
                return;
            String value = field.getText();
            node.value = value;
            node.onChange.accept(value);
        }

        private void sync() {
            syncing = true;
            try {
                if (!field.getText().equals(node.value))
                    field.setText(node.value);
                field.setBounds((int) Math.round(node.bounds.x), (int) Math.round(node.bounds.y),
                        (int) Math.round(node.bounds.width), (int) Math.round(node.bounds.height));
                field.setFont(StyleProperties.font(node.style));
                field.setForeground(StyleProperties.COLOR.get(node.style));
                field.setEnabled(!node.disabled);
                field.getAccessibleContext().setAccessibleName(node.accessibleName);
            } finally {
                syncing = false;
            }
        }
    }
}
