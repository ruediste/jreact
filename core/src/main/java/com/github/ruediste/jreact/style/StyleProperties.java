package com.github.ruediste.jreact.style;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.antepod.taffy.geometry.Line;
import com.antepod.taffy.geometry.Point;
import com.antepod.taffy.geometry.Rect;
import com.antepod.taffy.geometry.Size;
import com.antepod.taffy.style.AlignContent;
import com.antepod.taffy.style.AlignItems;
import com.antepod.taffy.style.BoxSizing;
import com.antepod.taffy.style.Clear;
import com.antepod.taffy.style.Dimension;
import com.antepod.taffy.style.Direction;
import com.antepod.taffy.style.Display;
import com.antepod.taffy.style.FlexDirection;
import com.antepod.taffy.style.FlexWrap;
import com.antepod.taffy.style.Float;
import com.antepod.taffy.style.GridAutoFlow;
import com.antepod.taffy.style.GridPlacement;
import com.antepod.taffy.style.GridTemplateArea;
import com.antepod.taffy.style.GridTemplateAreas;
import com.antepod.taffy.style.GridTemplateComponent;
import com.antepod.taffy.style.LengthPercentage;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.antepod.taffy.style.Overflow;
import com.antepod.taffy.style.Position;
import com.antepod.taffy.style.TextAlign;
import com.antepod.taffy.style.TrackSizingFunction;
import com.github.ruediste.jreact.OptionalN;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.ElementNode;

/**
 * Typed declarations of the style properties supported by {@link Style}.
 *
 * <p>
 * Each property carries a stable name and stores its value in a {@link Style}'s
 * value map. {@link #properties} records every declared property in declaration
 * order.
 *
 * <p>
 * Properties marked as inherited resolve from the nearest ancestor element when
 * not declared on the node itself. Use the {@link Property#get(ElementNode)}
 * family of methods for resolution; the {@link Style} based variants only
 * inspect
 * declarations.
 */
public abstract class StyleProperties {
    /** All declared properties, in declaration order. */
    public static final List<Property<?>> properties = new ArrayList<>();

    /** Updates a Taffy style builder for one explicitly declared property. */
    @FunctionalInterface
    public interface TaffyUpdater<T> {
        void update(Style style, T value, com.antepod.taffy.style.Style.Builder builder);
    }

    public enum MultiPropertyTaffyUpdater {
        SIZE {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.size(new Size<>(WIDTH.get(style), HEIGHT.get(style)));
            }
        },
        MIN_SIZE {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.minSize(new Size<>(MIN_WIDTH.get(style), MIN_HEIGHT.get(style)));
            }
        },
        MAX_SIZE {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.maxSize(new Size<>(MAX_WIDTH.get(style), MAX_HEIGHT.get(style)));
            }
        },
        INSET {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.inset(new Rect<>(LEFT.get(style), RIGHT.get(style), TOP.get(style), BOTTOM.get(style)));
            }
        },
        MARGIN {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.margin(new Rect<>(MARGIN_LEFT.get(style), MARGIN_RIGHT.get(style),
                        MARGIN_TOP.get(style), MARGIN_BOTTOM.get(style)));
            }
        },
        PADDING {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.padding(new Rect<>(PADDING_LEFT.get(style), PADDING_RIGHT.get(style),
                        PADDING_TOP.get(style), PADDING_BOTTOM.get(style)));
            }
        },
        BORDER {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.border(new Rect<>(
                        LengthPercentage.length(BORDER_LEFT_WIDTH.get(style).floatValue()),
                        LengthPercentage.length(BORDER_RIGHT_WIDTH.get(style).floatValue()),
                        LengthPercentage.length(BORDER_TOP_WIDTH.get(style).floatValue()),
                        LengthPercentage.length(BORDER_BOTTOM_WIDTH.get(style).floatValue())));
            }
        },
        OVERFLOW {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.overflow(new Point<>(OVERFLOW_X.get(style), OVERFLOW_Y.get(style)));
            }
        },
        GAP {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.gap(new Size<>(COLUMN_GAP.get(style), ROW_GAP.get(style)));
            }
        },
        GRID_ROW {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.gridRow(new Line<>(GRID_ROW_START.get(style), GRID_ROW_END.get(style)));
            }
        },
        GRID_COLUMN {
            @Override
            public void update(Style style, com.antepod.taffy.style.Style.Builder builder) {
                builder.gridColumn(new Line<>(GRID_COLUMN_START.get(style), GRID_COLUMN_END.get(style)));
            }
        };

        public abstract void update(Style style, com.antepod.taffy.style.Style.Builder builder);
    }

    /** A single typed style property. */
    public static class Property<T> {
        public final String name;
        private final Optional<TaffyUpdater<T>> taffyUpdater;
        private final Optional<MultiPropertyTaffyUpdater> multiPropertyUpdater;
        private final T defaultValue;
        private final boolean inherited;

        Property(String name) {
            this(name, null, null, null, false);
        }

        Property(String name, TaffyUpdater<T> taffyUpdater) {
            this(name, taffyUpdater, null, null, false);
        }

        Property(String name, TaffyUpdater<T> taffyUpdater, T defaultValue) {
            this(name, taffyUpdater, defaultValue, null, false);
        }

        Property(String name, T defaultValue, MultiPropertyTaffyUpdater multiPropertyUpdater) {
            this(name, null, defaultValue, multiPropertyUpdater, false);
        }

        Property(String name, TaffyUpdater<T> taffyUpdater, T defaultValue,
                MultiPropertyTaffyUpdater multiPropertyUpdater, boolean inherited) {
            this.name = name;
            this.taffyUpdater = Optional.ofNullable(taffyUpdater);
            this.multiPropertyUpdater = Optional.ofNullable(multiPropertyUpdater);
            this.defaultValue = defaultValue;
            this.inherited = inherited;
            properties.add(this);
        }

        public boolean isInherited() {
            return inherited;
        }

        boolean hasTaffyUpdater() {
            return taffyUpdater.isPresent();
        }

        public String formatValue(T value) {
            return value != null ? value.toString() : "";
        }

        boolean isSet(Style style) {
            return style.values.containsKey(this);
        }

        @SuppressWarnings("unchecked")
        void applyTaffy(Style style, Object value, com.antepod.taffy.style.Style.Builder builder) {
            taffyUpdater.ifPresent(updater -> updater.update(style, (T) value, builder));
        }

        void registerTaffyUpdater(Style style) {
            multiPropertyUpdater.ifPresent(style.taffyUpdaters::add);
        }

        @SuppressWarnings("unchecked")
        private OptionalN<T> declaredValue(Style style) {
            if (style.values.containsKey(this))
                return OptionalN.of((T) style.values.get(this));
            if (style.defaultValues != null && style.defaultValues.containsKey(this))
                return OptionalN.of((T) style.defaultValues.get(this));
            return OptionalN.empty();
        }

        T get(Style style, T fallback) {
            OptionalN<T> value = declaredValue(style);
            return value.isPresent() ? value.get() : fallback;
        }

        T get(Style style) {
            return get(style, defaultValue);
        }

        OptionalN<T> getIfPresent(Style style) {
            return declaredValue(style);
        }

        /**
         * Resolves this property on a node, following inheritance when the property
         * is inherited and not declared on the node.
         */
        public T get(ElementNode node) {
            return get(node, defaultValue);
        }

        /**
         * Resolves this property on a node, following inheritance when the property
         * is inherited and not declared on the node.
         */
        public T get(ElementNode node, T fallback) {
            OptionalN<T> value = getIfPresent(node);
            return value.isPresent() ? value.get() : fallback;
        }

        /**
         * Returns the resolved value of an inherited property, or the value declared
         * on this node. Does not fall back to the property default.
         */
        public OptionalN<T> getIfPresent(ElementNode node) {
            OptionalN<T> value = declaredValue(node.style);
            if (value.isPresent())
                return value;
            if (inherited) {
                for (ElementNode ancestor = node.parentElement().orElse(null); ancestor != null; ancestor = ancestor
                        .parentElement().orElse(null)) {
                    value = declaredValue(ancestor.style);
                    if (value.isPresent())
                        return value;
                }
            }
            return OptionalN.empty();
        }

        void copyInheritedValue(ElementNode node, Style target) {
            T value = get(node, defaultValue);
            if (value != null)
                target.put(this, value);
        }

        public void set(Style style, T value) {
            style.put(this, value);
        }

        public void setNullable(Style style, T value) {
            if (value == null) {
                style.values.remove(this);
            } else {
                style.put(this, value);
            }
        }
    }

    private static <T> Property<T> inherited(String name) {
        return new Property<>(name, null, null, null, true);
    }

    private static <T> Property<T> inherited(String name, T defaultValue) {
        return new Property<>(name, null, defaultValue, null, true);
    }

    private static <T> Property<T> inherited(String name, TaffyUpdater<T> taffyUpdater, T defaultValue) {
        return new Property<>(name, taffyUpdater, defaultValue, null, true);
    }

    public static com.antepod.taffy.style.Style buildTaffyStyle(ElementNode node) {
        Style style = new Style();
        style.putAll(node.style);
        for (Property<?> property : properties) {
            if (property.isInherited() && property.hasTaffyUpdater() && !style.values.containsKey(property))
                property.copyInheritedValue(node, style);
        }

        var builder = com.antepod.taffy.style.Style.builder();
        for (var entry : style.values.entrySet())
            entry.getKey().applyTaffy(style, entry.getValue(), builder);
        for (var updater : style.taffyUpdaters)
            updater.update(style, builder);
        return builder.build();
    }

    static void finite(double value) {
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be finite");
    }

    static void nonNegative(double value) {
        finite(value);
        if (value < 0)
            throw new IllegalArgumentException("Value must be non-negative");
    }

    static void positive(double value) {
        finite(value);
        if (value <= 0)
            throw new IllegalArgumentException("Value must be positive");
    }

    /** One shadow with signed offsets, non-negative blur, and a solid color. */
    public record Shadow(double offsetX, double offsetY, double blur, Color color) {
        public Shadow {
            finite(offsetX);
            finite(offsetY);
            nonNegative(blur);
            Objects.requireNonNull(color);
        }
    }

    public enum BorderStyle {
        NONE, HIDDEN, SOLID, DOTTED, DASHED
    }

    public enum TextDecoration {
        NONE, UNDERLINE
    }

    public enum PointerEvents {
        AUTO, NONE
    }

    public record BorderRadius(LengthPercentage horizontal, LengthPercentage vertical) {
        public static final BorderRadius ZERO = new BorderRadius(LengthPercentage.ZERO, LengthPercentage.ZERO);

        public static BorderRadius of(double value) {
            LengthPercentage lp = LengthPercentage.length((float) value);
            return new BorderRadius(lp, lp);
        }
    }

    // Visual style properties
    public static final Property<JColor> BACKGROUND_COLOR = new Property<>("background");
    public static final Property<JColor> COLOR = inherited("color", JColor.rgb(28, 29, 31));
    public static final Property<String> FONT_FAMILY = inherited("font-family", java.awt.Font.DIALOG);
    public static final Property<Integer> FONT_SIZE = inherited("font-size", 14);
    public static final Property<Boolean> BOLD = inherited("bold", false);
    public static final Property<Boolean> ITALIC = inherited("italic", false);
    public static final Property<java.lang.Double> LINE_HEIGHT = inherited("line-height", null, Double.NaN);
    /**
     * CSS does not inherit text-decoration, it propagates to descendants. JReact
     * approximates that propagation with inheritance so a decoration declared on a
     * container reaches its text descendants.
     */
    public static final Property<TextDecoration> TEXT_DECORATION = inherited("text-decoration", TextDecoration.NONE);
    public static final Property<Shadow> BOX_SHADOW = new Property<>("box-shadow");
    public static final Property<Shadow> TEXT_SHADOW = inherited("text-shadow");
    public static final Property<java.lang.Double> OPACITY = new Property<>("opacity", null, 1.0);
    public static final Property<PointerEvents> POINTER_EVENTS = inherited("pointer-events", PointerEvents.AUTO);
    public static final Property<java.lang.Double> RING_WIDTH = new Property<>("ring-width", null, 0.0);
    public static final Property<JColor> RING_COLOR = new Property<>("ring-color");
    public static final Property<java.lang.Double> RING_OFFSET = new Property<>("ring-offset", null, 0.0);

    // CSS layout properties used by the layout engine.
    public static final Property<Display> DISPLAY = new Property<>("display",
            (style, value, builder) -> builder.display(value), Display.DEFAULT);
    public static final Property<Position> POSITION = new Property<>("position",
            (style, value, builder) -> builder.position(value), Position.RELATIVE);
    public static final Property<Float> FLOAT = new Property<>("float",
            (style, value, builder) -> builder.floatValue(value), Float.NONE);
    public static final Property<Clear> CLEAR = new Property<>("clear",
            (style, value, builder) -> builder.clear(value), Clear.NONE);
    public static final Property<Direction> DIRECTION = inherited("direction",
            (style, value, builder) -> builder.direction(value), Direction.LTR);
    public static final Property<BoxSizing> BOX_SIZING = new Property<>("box-sizing",
            (style, value, builder) -> builder.boxSizing(value), BoxSizing.BORDER_BOX);

    public static final Property<LengthPercentageAuto> TOP = multiProperty("top",
            LengthPercentageAuto.AUTO, MultiPropertyTaffyUpdater.INSET);
    public static final Property<LengthPercentageAuto> RIGHT = multiProperty("right",
            LengthPercentageAuto.AUTO, MultiPropertyTaffyUpdater.INSET);
    public static final Property<LengthPercentageAuto> BOTTOM = multiProperty("bottom",
            LengthPercentageAuto.AUTO, MultiPropertyTaffyUpdater.INSET);
    public static final Property<LengthPercentageAuto> LEFT = multiProperty("left",
            LengthPercentageAuto.AUTO, MultiPropertyTaffyUpdater.INSET);

    public static final Property<Dimension> WIDTH = multiProperty("width",
            Dimension.AUTO, MultiPropertyTaffyUpdater.SIZE);
    public static final Property<Dimension> HEIGHT = multiProperty("height",
            Dimension.AUTO, MultiPropertyTaffyUpdater.SIZE);
    public static final Property<Dimension> MIN_WIDTH = multiProperty("min-width",
            Dimension.AUTO, MultiPropertyTaffyUpdater.MIN_SIZE);
    public static final Property<Dimension> MIN_HEIGHT = multiProperty("min-height",
            Dimension.AUTO, MultiPropertyTaffyUpdater.MIN_SIZE);
    public static final Property<Dimension> MAX_WIDTH = multiProperty("max-width",
            Dimension.AUTO, MultiPropertyTaffyUpdater.MAX_SIZE);
    public static final Property<Dimension> MAX_HEIGHT = multiProperty("max-height",
            Dimension.AUTO, MultiPropertyTaffyUpdater.MAX_SIZE);
    public static final Property<java.lang.Float> ASPECT_RATIO = new Property<>("aspect-ratio",
            (style, value, builder) -> builder.aspectRatio(value));

    public static final Property<LengthPercentageAuto> MARGIN_TOP = multiProperty("margin-top",
            LengthPercentageAuto.ZERO, MultiPropertyTaffyUpdater.MARGIN);
    public static final Property<LengthPercentageAuto> MARGIN_RIGHT = multiProperty("margin-right",
            LengthPercentageAuto.ZERO, MultiPropertyTaffyUpdater.MARGIN);
    public static final Property<LengthPercentageAuto> MARGIN_BOTTOM = multiProperty("margin-bottom",
            LengthPercentageAuto.ZERO, MultiPropertyTaffyUpdater.MARGIN);
    public static final Property<LengthPercentageAuto> MARGIN_LEFT = multiProperty("margin-left",
            LengthPercentageAuto.ZERO, MultiPropertyTaffyUpdater.MARGIN);

    public static final Property<LengthPercentage> PADDING_TOP = multiProperty("padding-top",
            LengthPercentage.ZERO, MultiPropertyTaffyUpdater.PADDING);
    public static final Property<LengthPercentage> PADDING_RIGHT = multiProperty("padding-right",
            LengthPercentage.ZERO, MultiPropertyTaffyUpdater.PADDING);
    public static final Property<LengthPercentage> PADDING_BOTTOM = multiProperty("padding-bottom",
            LengthPercentage.ZERO, MultiPropertyTaffyUpdater.PADDING);
    public static final Property<LengthPercentage> PADDING_LEFT = multiProperty("padding-left",
            LengthPercentage.ZERO, MultiPropertyTaffyUpdater.PADDING);

    public static final Property<JColor> BORDER_TOP_COLOR = new Property<>("border-top-color", null,
            JColor.BLACK);
    public static final Property<BorderStyle> BORDER_TOP_STYLE = new Property<>("border-top-style", null,
            BorderStyle.NONE);
    public static final Property<Double> BORDER_TOP_WIDTH = multiProperty("border-top-width",
            1., MultiPropertyTaffyUpdater.BORDER);

    public static final Property<JColor> BORDER_RIGHT_COLOR = new Property<>("border-right-color", null,
            JColor.BLACK);
    public static final Property<BorderStyle> BORDER_RIGHT_STYLE = new Property<>("border-right-style", null,
            BorderStyle.NONE);
    public static final Property<Double> BORDER_RIGHT_WIDTH = multiProperty("border-right-width",
            0., MultiPropertyTaffyUpdater.BORDER);

    public static final Property<JColor> BORDER_BOTTOM_COLOR = new Property<>("border-bottom-color", null,
            JColor.BLACK);
    public static final Property<BorderStyle> BORDER_BOTTOM_STYLE = new Property<>("border-bottom-style", null,
            BorderStyle.NONE);
    public static final Property<Double> BORDER_BOTTOM_WIDTH = multiProperty("border-bottom-width",
            1., MultiPropertyTaffyUpdater.BORDER);

    public static final Property<JColor> BORDER_LEFT_COLOR = new Property<>("border-left-color", null,
            JColor.BLACK);
    public static final Property<BorderStyle> BORDER_LEFT_STYLE = new Property<>("border-left-style", null,
            BorderStyle.NONE);
    public static final Property<Double> BORDER_LEFT_WIDTH = multiProperty("border-left-width",
            0., MultiPropertyTaffyUpdater.BORDER);

    public static final Property<BorderRadius> BORDER_TOP_LEFT_RADIUS = new Property<>(
            "border-top-left-radius", null, BorderRadius.ZERO);
    public static final Property<BorderRadius> BORDER_TOP_RIGHT_RADIUS = new Property<>(
            "border-top-right-radius", null, BorderRadius.ZERO);
    public static final Property<BorderRadius> BORDER_BOTTOM_RIGHT_RADIUS = new Property<>(
            "border-bottom-right-radius", null, BorderRadius.ZERO);
    public static final Property<BorderRadius> BORDER_BOTTOM_LEFT_RADIUS = new Property<>(
            "border-bottom-left-radius", null, BorderRadius.ZERO);

    public static final Property<Overflow> OVERFLOW_X = multiProperty("overflow-x",
            Overflow.VISIBLE, MultiPropertyTaffyUpdater.OVERFLOW);
    public static final Property<Overflow> OVERFLOW_Y = multiProperty("overflow-y",
            Overflow.VISIBLE, MultiPropertyTaffyUpdater.OVERFLOW);
    public static final Property<java.lang.Float> SCROLLBAR_WIDTH = new Property<>(
            "scrollbar-width",
            (style, value, builder) -> builder.scrollbarWidth(value), 0.0f);

    public static final Property<AlignContent> ALIGN_CONTENT = new Property<>("align-content",
            (style, value, builder) -> builder.alignContent(Optional.of(value)));
    public static final Property<AlignItems> ALIGN_ITEMS = new Property<>("align-items",
            (style, value, builder) -> builder.alignItems(Optional.of(value)));
    public static final Property<AlignItems> ALIGN_SELF = new Property<>("align-self",
            (style, value, builder) -> builder.alignSelf(Optional.of(value)));
    public static final Property<AlignContent> JUSTIFY_CONTENT = new Property<>("justify-content",
            (style, value, builder) -> builder.justifyContent(Optional.of(value)));
    public static final Property<AlignItems> JUSTIFY_ITEMS = new Property<>("justify-items",
            (style, value, builder) -> builder.justifyItems(Optional.of(value)));
    public static final Property<AlignItems> JUSTIFY_SELF = new Property<>("justify-self",
            (style, value, builder) -> builder.justifySelf(Optional.of(value)));

    public static final Property<FlexDirection> FLEX_DIRECTION = new Property<>("flex-direction",
            (style, value, builder) -> builder.flexDirection(value), FlexDirection.ROW);
    public static final Property<FlexWrap> FLEX_WRAP = new Property<>("flex-wrap",
            (style, value, builder) -> builder.flexWrap(value), FlexWrap.NO_WRAP);
    public static final Property<Dimension> FLEX_BASIS = new Property<>("flex-basis",
            (style, value, builder) -> builder.flexBasis(value), Dimension.AUTO);
    public static final Property<java.lang.Double> FLEX_GROW = new Property<>("flex-grow",
            (style, value, builder) -> builder.flexGrow(value.floatValue()), 0.0);
    public static final Property<java.lang.Double> FLEX_SHRINK = new Property<>("flex-shrink",
            (style, value, builder) -> builder.flexShrink(value.floatValue()), 1.0);

    public static final Property<LengthPercentage> ROW_GAP = multiProperty("row-gap",
            LengthPercentage.ZERO, MultiPropertyTaffyUpdater.GAP);
    public static final Property<LengthPercentage> COLUMN_GAP = multiProperty("column-gap",
            LengthPercentage.ZERO, MultiPropertyTaffyUpdater.GAP);

    public static final Property<TextAlign> TEXT_ALIGN = inherited("text-align",
            (style, value, builder) -> builder.textAlign(value), TextAlign.AUTO);

    public static final Property<GridAutoFlow> GRID_AUTO_FLOW = new Property<>("grid-auto-flow",
            (style, value, builder) -> builder.gridAutoFlow(value), GridAutoFlow.ROW);
    public static final Property<GridPlacement> GRID_ROW_START = multiProperty("grid-row-start",
            GridPlacement.AUTO, MultiPropertyTaffyUpdater.GRID_ROW);
    public static final Property<GridPlacement> GRID_ROW_END = multiProperty("grid-row-end",
            GridPlacement.AUTO, MultiPropertyTaffyUpdater.GRID_ROW);
    public static final Property<GridPlacement> GRID_COLUMN_START = multiProperty("grid-column-start",
            GridPlacement.AUTO, MultiPropertyTaffyUpdater.GRID_COLUMN);
    public static final Property<GridPlacement> GRID_COLUMN_END = multiProperty("grid-column-end",
            GridPlacement.AUTO, MultiPropertyTaffyUpdater.GRID_COLUMN);
    public static final Property<List<GridTemplateComponent>> GRID_TEMPLATE_ROWS = new Property<>(
            "grid-template-rows",
            (style, value, builder) -> builder.gridTemplateRows(value), Collections.emptyList());
    public static final Property<List<GridTemplateComponent>> GRID_TEMPLATE_COLUMNS = new Property<>(
            "grid-template-columns",
            (style, value, builder) -> builder.gridTemplateColumns(value), Collections.emptyList());
    public static final Property<List<TrackSizingFunction>> GRID_AUTO_ROWS = new Property<>(
            "grid-auto-rows",
            (style, value, builder) -> builder.gridAutoRows(value), Collections.emptyList());
    public static final Property<List<TrackSizingFunction>> GRID_AUTO_COLUMNS = new Property<>(
            "grid-auto-columns", (style, value, builder) -> builder.gridAutoColumns(value),
            Collections.emptyList());
    public static final Property<List<List<String>>> GRID_TEMPLATE_ROW_NAMES = new Property<>(
            "grid-template-row-names",
            (style, value, builder) -> builder.gridTemplateRowNames(value), Collections.emptyList());
    public static final Property<List<List<String>>> GRID_TEMPLATE_COLUMN_NAMES = new Property<>(
            "grid-template-column-names",
            (style, value, builder) -> builder.gridTemplateColumnNames(value), Collections.emptyList());
    public static final Property<List<GridTemplateArea>> GRID_TEMPLATE_AREAS = new Property<>(
            "grid-template-areas",
            (style, value, builder) -> builder.gridTemplateAreas(toGridTemplateAreas(value)),
            Collections.emptyList());

    private static <T> Property<T> multiProperty(String name, T defaultValue,
            MultiPropertyTaffyUpdater multiPropertyUpdater) {
        return new Property<>(name, defaultValue, multiPropertyUpdater);
    }

    private static Optional<GridTemplateAreas> toGridTemplateAreas(List<GridTemplateArea> areas) {
        if (areas.isEmpty())
            return Optional.empty();
        return Optional.of(new GridTemplateAreas(areas, gridAreaRowCount(areas), gridAreaColumnCount(areas)));
    }

    private static int gridAreaRowCount(List<GridTemplateArea> areas) {
        int rows = 0;
        for (GridTemplateArea area : areas)
            rows = Math.max(rows, area.rowEnd - 1);
        return rows;
    }

    private static int gridAreaColumnCount(List<GridTemplateArea> areas) {
        int columns = 0;
        for (GridTemplateArea area : areas)
            columns = Math.max(columns, area.columnEnd - 1);
        return columns;
    }

    public static Font font(ElementNode node) {
        int flags = (BOLD.get(node) ? Font.BOLD : 0) | (ITALIC.get(node) ? Font.ITALIC : 0);
        return new Font(FONT_FAMILY.get(node, Font.DIALOG), flags, FONT_SIZE.get(node));
    }
}
