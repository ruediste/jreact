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

/**
 * Typed declarations of the style properties supported by {@link Style}.
 *
 * <p>
 * Each property carries a stable name and stores its value in a {@link Style}'s
 * value map. {@link #properties} records every declared property in declaration
 * order.
 */
public abstract class StyleProperties {
    /** All declared properties, in declaration order. */
    public static final List<Property<?>> properties = new ArrayList<>();

    /** Updates a Taffy style builder for one explicitly declared property. */
    @FunctionalInterface
    public interface TaffyUpdater<T> {
        void update(Style style, T value, com.antepod.taffy.style.Style.Builder builder);
    }

    /** A single typed style property. */
    public static class Property<T> {
        public final String name;
        private final Optional<TaffyUpdater<T>> taffyUpdater;
        private final T defaultValue;

        Property(String name) {
            this(name, null, null);
        }

        Property(String name, TaffyUpdater<T> taffyUpdater) {
            this(name, taffyUpdater, null);
        }

        Property(String name, TaffyUpdater<T> taffyUpdater, T defaultValue) {
            this.name = name;
            this.taffyUpdater = Optional.ofNullable(taffyUpdater);
            this.defaultValue = defaultValue;
            properties.add(this);
        }

        public String formatValue(T value) {
            return value != null ? value.toString() : "";
        }

        public boolean isSet(Style style) {
            return style.values.containsKey(this);
        }

        @SuppressWarnings("unchecked")
        void applyTaffy(Style style, Object value, com.antepod.taffy.style.Style.Builder builder) {
            taffyUpdater.ifPresent(updater -> updater.update(style, (T) value, builder));
        }

        @SuppressWarnings("unchecked")
        public T get(Style style, T fallback) {
            if (style.values.containsKey(this))
                return (T) style.values.get(this);
            if (style.defaultValues != null && style.defaultValues.containsKey(this))
                return (T) style.defaultValues.get(this);
            return fallback;
        }

        public T get(Style style) {
            return get(style, defaultValue);
        }

        @SuppressWarnings("unchecked")
        public OptionalN<T> getIfPresent(Style style) {
            if (style.values.containsKey(this))
                return OptionalN.of((T) style.values.get(this));
            return OptionalN.empty();
        }

        public void set(Style style, T value) {
            style.values.put(this, Objects.requireNonNull(value));
        }

        public void setNullable(Style style, T value) {
            if (value == null) {
                style.values.remove(this);
            } else {
                style.values.put(this, value);
            }
        }
    }

    /** A property whose value is a double. */
    public static class DoubleProperty extends Property<java.lang.Double> {
        DoubleProperty(String name) {
            super(name);
        }

        DoubleProperty(String name, TaffyUpdater<java.lang.Double> updater) {
            super(name, updater);
        }

        DoubleProperty(String name, TaffyUpdater<java.lang.Double> updater, java.lang.Double defaultValue) {
            super(name, updater, defaultValue);
        }
    }

    public static com.antepod.taffy.style.Style buildTaffyStyle(Style style) {
        var builder = com.antepod.taffy.style.Style.builder();
        for (var entry : style.values.entrySet())
            entry.getKey().applyTaffy(style, entry.getValue(), builder);
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

    /** Width and solid color of a single border edge. */
    public record Edge(double width, Color color) {
        public Edge {
            nonNegative(width);
            Objects.requireNonNull(color);
        }
    }

    /** Per-side solid borders whose widths contribute to content insets. */
    public record Border(Edge top, Edge right, Edge bottom, Edge left) {
        public Border {
            Objects.requireNonNull(top);
            Objects.requireNonNull(right);
            Objects.requireNonNull(bottom);
            Objects.requireNonNull(left);
        }
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

    public static final Property<Color> BACKGROUND = new Property<>("background");
    public static final Property<Color> COLOR = new Property<>("color", null,
            new Color(32, 36, 40));
    public static final Property<Border> BORDER_DECORATION = new Property<>("border-decoration");
    public static final DoubleProperty RADIUS = new DoubleProperty("radius", null, 0.);
    public static final Property<String> FONT_FAMILY = new Property<>("font-family", null,
            java.awt.Font.DIALOG);
    public static final Property<Integer> FONT_SIZE = new Property<>("font-size", null, 14);
    public static final Property<Boolean> BOLD = new Property<>("bold", null, false);
    public static final Property<Boolean> ITALIC = new Property<>("italic", null, false);
    public static final DoubleProperty LINE_HEIGHT = new DoubleProperty("line-height", null,
            Double.NaN);
    public static final Property<Shadow> BOX_SHADOW = new Property<>("box-shadow");
    public static final Property<Shadow> TEXT_SHADOW = new Property<>("text-shadow");
    public static final DoubleProperty OPACITY = new DoubleProperty("opacity", null, 1.0);

    // CSS layout properties used by the layout engine.
    public static final DoubleProperty GROW = new DoubleProperty("grow",
            (style, value, builder) -> builder.flexGrow(value.floatValue()), 0.0);
    public static final DoubleProperty SHRINK = new DoubleProperty("shrink",
            (style, value, builder) -> builder.flexShrink(value.floatValue()), 1.0);
    public static final Property<Display> DISPLAY = new Property<>("display",
            (style, value, builder) -> builder.display(value), Display.DEFAULT);
    public static final Property<Position> POSITION = new Property<>("position",
            (style, value, builder) -> builder.position(value), Position.RELATIVE);
    public static final Property<Direction> WRITING_MODE = new Property<>("writing-direction",
            (style, value, builder) -> builder.direction(value), Direction.LTR);
    public static final Property<Float> FLOAT_MODE = new Property<>("float",
            (style, value, builder) -> builder.floatValue(value), Float.NONE);
    public static final Property<Clear> CLEAR = new Property<>("clear",
            (style, value, builder) -> builder.clear(value), Clear.NONE);
    public static final Property<BoxSizing> BOX_SIZING = new Property<>("box-sizing",
            (style, value, builder) -> builder.boxSizing(value), BoxSizing.BORDER_BOX);
    public static final Property<TextAlign> TEXT_ALIGN = new Property<>("text-align",
            (style, value, builder) -> builder.textAlign(value), TextAlign.AUTO);
    public static final Property<AlignItems> ALIGN_ITEMS = new Property<>("align-items",
            (style, value, builder) -> builder.alignItems(Optional.of(value)));
    public static final Property<AlignItems> ALIGN_SELF = new Property<>("align-self",
            (style, value, builder) -> builder.alignSelf(Optional.of(value)));
    public static final Property<AlignContent> ALIGN_CONTENT = new Property<>("align-content",
            (style, value, builder) -> builder.alignContent(Optional.of(value)));
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
    public static final Property<Size<Dimension>> SIZE = new Property<>("size",
            (style, value, builder) -> builder.size(value), autoSize());
    public static final Property<Size<Dimension>> MIN_SIZE = new Property<>("min-size",
            (style, value, builder) -> builder.minSize(value), autoSize());
    public static final Property<Size<Dimension>> MAX_SIZE = new Property<>("max-size",
            (style, value, builder) -> builder.maxSize(value), autoSize());
    public static final Property<java.lang.Float> ASPECT_RATIO = new Property<>("aspect-ratio",
            (style, value, builder) -> builder.aspectRatio(Optional.of(value)));
    public static final Property<Size<LengthPercentage>> GAP = new Property<>("gap",
            (style, value, builder) -> builder.gap(value),
            new Size<>(LengthPercentage.ZERO, LengthPercentage.ZERO));
    public static final Property<Rect<LengthPercentageAuto>> MARGIN = new Property<>("margin",
            (style, value, builder) -> builder.margin(value), zeroAutoRect());
    public static final Property<Rect<LengthPercentage>> PADDING = new Property<>("padding",
            (style, value, builder) -> builder.padding(value), zeroLpRect());
    public static final Property<Rect<LengthPercentage>> BORDER = new Property<>("border",
            (style, value, builder) -> builder.border(value), zeroLpRect());
    public static final Property<Rect<LengthPercentageAuto>> INSET = new Property<>("inset",
            (style, value, builder) -> builder.inset(value), autoRect());
    public static final Property<Boolean> ITEM_IS_TABLE = new Property<>("item-is-table",
            (style, value, builder) -> builder.itemIsTable(value), false);
    public static final Property<Boolean> ITEM_IS_REPLACED = new Property<>("item-is-replaced",
            (style, value, builder) -> builder.itemIsReplaced(value), false);
    public static final Property<java.lang.Float> SCROLLBAR_WIDTH = new Property<>(
            "scrollbar-width",
            (style, value, builder) -> builder.scrollbarWidth(value), 0.0f);
    public static final Property<Point<Overflow>> OVERFLOW = new Property<>("overflow",
            (style, value, builder) -> builder.overflow(value),
            new Point<>(Overflow.VISIBLE, Overflow.VISIBLE));
    public static final Property<GridAutoFlow> GRID_AUTO_FLOW = new Property<>("grid-auto-flow",
            (style, value, builder) -> builder.gridAutoFlow(value), GridAutoFlow.ROW);
    public static final Property<Line<GridPlacement>> GRID_ROW = new Property<>("grid-row",
            (style, value, builder) -> builder.gridRow(value),
            new Line<>(GridPlacement.AUTO, GridPlacement.AUTO));
    public static final Property<Line<GridPlacement>> GRID_COLUMN = new Property<>("grid-column",
            (style, value, builder) -> builder.gridColumn(value),
            new Line<>(GridPlacement.AUTO, GridPlacement.AUTO));
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
            "grid-template-areas", (style, value, builder) -> builder.gridTemplateAreas(
                    new GridTemplateAreas(value, getGridTemplateAreaRowCount(style),
                            getGridTemplateAreaColumnCount(style))));
    public static final Property<Integer> GRID_TEMPLATE_AREA_ROW_COUNT = new Property<>(
            "grid-template-area-row-count",
            (style, value, builder) -> applyGridAreas(style, builder));
    public static final Property<Integer> GRID_TEMPLATE_AREA_COLUMN_COUNT = new Property<>(
            "grid-template-area-column-count",
            (style, value, builder) -> applyGridAreas(style, builder));

    private static Size<Dimension> autoSize() {
        return new Size<>(Dimension.AUTO, Dimension.AUTO);
    }

    private static Rect<LengthPercentage> zeroLpRect() {
        return new Rect<>(LengthPercentage.ZERO, LengthPercentage.ZERO,
                LengthPercentage.ZERO, LengthPercentage.ZERO);
    }

    private static Rect<LengthPercentageAuto> zeroAutoRect() {
        return new Rect<>(LengthPercentageAuto.ZERO, LengthPercentageAuto.ZERO,
                LengthPercentageAuto.ZERO, LengthPercentageAuto.ZERO);
    }

    private static Rect<LengthPercentageAuto> autoRect() {
        return new Rect<>(LengthPercentageAuto.AUTO, LengthPercentageAuto.AUTO,
                LengthPercentageAuto.AUTO, LengthPercentageAuto.AUTO);
    }

    private static void applyGridAreas(Style style, com.antepod.taffy.style.Style.Builder builder) {
        builder.gridTemplateAreas(new GridTemplateAreas(GRID_TEMPLATE_AREAS.get(style),
                getGridTemplateAreaRowCount(style),
                getGridTemplateAreaColumnCount(style)));
    }

    public static int getGridTemplateAreaRowCount(Style style) {
        int inferred = inferredAreaRows(style);
        Integer explicit = GRID_TEMPLATE_AREA_ROW_COUNT.getIfPresent(style);
        return explicit == null ? inferred : Math.max(explicit, inferred);
    }

    public static int getGridTemplateAreaColumnCount(Style style) {
        int inferred = inferredAreaColumns(style);
        Integer explicit = GRID_TEMPLATE_AREA_COLUMN_COUNT.getIfPresent(style);
        return explicit == null ? inferred : Math.max(explicit, inferred);
    }

    private static int inferredAreaRows(Style style) {
        int rows = 0;
        for (GridTemplateArea area : GRID_TEMPLATE_AREAS.get(style)) {
            rows = Math.max(rows, area.rowEnd - 1);
        }
        return rows;
    }

    private static int inferredAreaColumns(Style style) {
        int columns = 0;
        for (GridTemplateArea area : GRID_TEMPLATE_AREAS.get(style)) {
            columns = Math.max(columns, area.columnEnd - 1);
        }
        return columns;
    }

    public static Font font(Style style) {
        int flags = (BOLD.get(style) ? Font.BOLD : 0) | (ITALIC.get(style) ? Font.ITALIC : 0);
        return new Font(FONT_FAMILY.get(style, Font.DIALOG), flags, FONT_SIZE.get(style));
    }
}
