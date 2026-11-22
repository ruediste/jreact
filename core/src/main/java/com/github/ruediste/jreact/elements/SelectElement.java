package com.github.ruediste.jreact.elements;

import java.awt.Dimension;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.antepod.taffy.geometry.Size;
import com.antepod.taffy.style.AvailableSpace;
import com.github.ruediste.jreact.JReactContext;
import com.github.ruediste.jreact.Node;
import com.github.ruediste.jreact.style.StyleProperties;

public class SelectElement extends ElementNode {

    public String value;
    public final List<String> options;
    public Consumer<String> onChange;
    private Handle handle;

    public SelectElement(String value, List<String> options, Consumer<String> onChange) {
        this(null, value, options, onChange);
    }

    public SelectElement(Object key, String value, List<String> options, Consumer<String> onChange) {
        super(key);
        this.value = Objects.requireNonNull(value);
        this.options = List.copyOf(options);
        if (this.options.isEmpty())
            throw new IllegalArgumentException("Select options must not be empty");
        if (!this.options.contains(value))
            throw new IllegalArgumentException("Selected value must be one of the options");
        this.onChange = Objects.requireNonNull(onChange);
    }

    @Override
    public Size<Float> measure(Size<Optional<Float>> knownDimensions, Size<AvailableSpace> availableSpace) {
        if (handle == null)
            return Size.zeroFloat();
        handle.select.setFont(StyleProperties.font(style));
        Dimension preferred = handle.select.getPreferredSize();
        return new Size<>((float) preferred.width, (float) preferred.height);
    }

    @Override
    public void created() {
        super.created();
        handle = new Handle(this);
        JReactContext.root().onComponentAdded.accept(handle.select);
    }

    @Override
    public void reconcile(Node oldNode) {
        super.reconcile(oldNode);
        handle = ((SelectElement) oldNode).handle;
        handle.node = this;
    }

    @Override
    public void afterLayout() {
        super.afterLayout();
        handle.sync();
    }

    @Override
    public void dispose() {
        JReactContext.root().onComponentRemoved.accept(handle.select);
        super.dispose();
    }

    private static class Handle {
        SelectElement node;
        final JComboBox<String> select;
        boolean syncing;

        private Handle(SelectElement node) {
            this.node = node;
            select = new JComboBox<>();
            select.addActionListener(event -> selectChanged());
        }

        private void selectChanged() {
            if (syncing)
                return;
            Object selected = select.getSelectedItem();
            if (selected == null)
                return;
            String value = selected.toString();
            node.value = value;
            node.onChange.accept(value);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private void sync() {
            syncing = true;
            try {
                boolean optionsChanged = select.getItemCount() != node.options.size();
                if (!optionsChanged) {
                    for (int index = 0; index < node.options.size(); index++) {
                        if (!Objects.equals(select.getItemAt(index), node.options.get(index))) {
                            optionsChanged = true;
                            break;
                        }
                    }
                }
                if (optionsChanged)
                    select.setModel(new DefaultComboBoxModel(node.options.toArray()));
                if (!Objects.equals(select.getSelectedItem(), node.value))
                    select.setSelectedItem(node.value);
                select.setBounds((int) Math.round(node.bounds.x), (int) Math.round(node.bounds.y),
                        (int) Math.round(node.bounds.width), (int) Math.round(node.bounds.height));
                select.setFont(StyleProperties.font(node.style));
                select.setForeground(StyleProperties.COLOR.get(node.style).toColor());
                select.setEnabled(!node.disabled);
                select.getAccessibleContext().setAccessibleName(node.accessibleName);
            } finally {
                syncing = false;
            }
        }
    }
}
