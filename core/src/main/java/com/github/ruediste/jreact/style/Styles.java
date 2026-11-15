package com.github.ruediste.jreact.style;

import java.awt.Color;
import java.util.List;

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
import com.antepod.taffy.style.GridTemplateComponent;
import com.antepod.taffy.style.LengthPercentage;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.antepod.taffy.style.Overflow;
import com.antepod.taffy.style.Position;
import com.antepod.taffy.style.TextAlign;
import com.antepod.taffy.style.TrackSizingFunction;
import com.github.ruediste.jreact.CoreComponents;
import com.github.ruediste.jreact.nodes.Node;
import com.github.ruediste.jreact.style.StyleProperties.Border;
import com.github.ruediste.jreact.style.StyleProperties.Property;
import com.github.ruediste.jreact.style.StyleProperties.Shadow;

/**
 * Declares component-scoped style rules and resolves the node-tree cascade.
 *
 * <p>
 * Declarations belong to the active {@link CoreComponents} component context.
 * Direct-child
 * scope applies a rule to every node rendered by the declaring component
 * itself,
 * including nested layout nodes, but not to nodes rendered by nested
 * components.
 * Transitive scope extends matching to the component's entire rendered subtree,
 * including nested components. Scope eligibility is separate from selector
 * matching and does not increase specificity.
 *
 * <p>
 * Resolution applies matching rules by specificity, then by nearest declaring
 * component, then by later rule order. A node's inline properties take final
 * precedence. Resolved styles are replaced on each pass, allowing interaction
 * changes to affect pseudo-class rules without mutating the original
 * declarations.
 */
public final class Styles {

    public static void resolve(Node node) {

        Style style = new Style();
        node.styleRules.stream()
                .filter(rule -> rule.selector().match(node).matches())
                .forEach(rule -> style.putAll(rule.style()));
        style.putAll(node.inline);
        node.style = style;
        node.applyLayoutDefaults();
        node.children().forEach(Styles::resolve);
    }

    public interface StyleApiHelper<Api> {
        <T> Api putStyle(Property<T> property, T value);
    }

    public interface LayoutStyleApi<Self, Helper extends StyleApiHelper<Self>> extends ApiHelperProvider<Helper> {

        default Self grow(double value) {
            return _helper().putStyle(StyleProperties.GROW, value);
        }

        default Self shrink(double value) {
            return _helper().putStyle(StyleProperties.SHRINK, value);
        }

        default Self display(Display value) {
            return _helper().putStyle(StyleProperties.DISPLAY, value);
        }

        default Self position(Position value) {
            return _helper().putStyle(StyleProperties.POSITION, value);
        }

        default Self backgroundColor(Color value) {
            return _helper().putStyle(StyleProperties.BACKGROUND, value);
        }

        default Self color(Color value) {
            return _helper().putStyle(StyleProperties.COLOR, value);
        }

        default Self borderDecoration(Border value) {
            return _helper().putStyle(StyleProperties.BORDER_DECORATION, value);
        }

        default Self radius(double value) {
            return _helper().putStyle(StyleProperties.RADIUS, value);
        }

        default Self fontFamily(String value) {
            return _helper().putStyle(StyleProperties.FONT_FAMILY, value);
        }

        default Self fontSize(int value) {
            return _helper().putStyle(StyleProperties.FONT_SIZE, value);
        }

        default Self bold(boolean value) {
            return _helper().putStyle(StyleProperties.BOLD, value);
        }

        default Self italic(boolean value) {
            return _helper().putStyle(StyleProperties.ITALIC, value);
        }

        default Self lineHeight(double value) {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, value);
        }

        default Self boxShadow(Shadow value) {
            return _helper().putStyle(StyleProperties.BOX_SHADOW, value);
        }

        default Self textShadow(Shadow value) {
            return _helper().putStyle(StyleProperties.TEXT_SHADOW, value);
        }

        default Self opacity(double value) {
            return _helper().putStyle(StyleProperties.OPACITY, value);
        }

        default Self writingDirection(Direction value) {
            return _helper().putStyle(StyleProperties.WRITING_MODE, value);
        }

        default Self floatMode(Float value) {
            return _helper().putStyle(StyleProperties.FLOAT_MODE, value);
        }

        default Self clear(Clear value) {
            return _helper().putStyle(StyleProperties.CLEAR, value);
        }

        default Self boxSizing(BoxSizing value) {
            return _helper().putStyle(StyleProperties.BOX_SIZING, value);
        }

        default Self textAlign(TextAlign value) {
            return _helper().putStyle(StyleProperties.TEXT_ALIGN, value);
        }

        default Self alignItems(AlignItems value) {
            return _helper().putStyle(StyleProperties.ALIGN_ITEMS, value);
        }

        default Self alignSelf(AlignItems value) {
            return _helper().putStyle(StyleProperties.ALIGN_SELF, value);
        }

        default Self alignContent(AlignContent value) {
            return _helper().putStyle(StyleProperties.ALIGN_CONTENT, value);
        }

        default Self justifyContent(AlignContent value) {
            return _helper().putStyle(StyleProperties.JUSTIFY_CONTENT, value);
        }

        default Self justifyItems(AlignItems value) {
            return _helper().putStyle(StyleProperties.JUSTIFY_ITEMS, value);
        }

        default Self justifySelf(AlignItems value) {
            return _helper().putStyle(StyleProperties.JUSTIFY_SELF, value);
        }

        default Self flexDirection(FlexDirection value) {
            return _helper().putStyle(StyleProperties.FLEX_DIRECTION, value);
        }

        default Self flexWrap(FlexWrap value) {
            return _helper().putStyle(StyleProperties.FLEX_WRAP, value);
        }

        default Self flexBasis(Dimension value) {
            return _helper().putStyle(StyleProperties.FLEX_BASIS, value);
        }

        default Self size(Size<Dimension> value) {
            return _helper().putStyle(StyleProperties.SIZE, value);
        }

        default Self minSize(Size<Dimension> value) {
            return _helper().putStyle(StyleProperties.MIN_SIZE, value);
        }

        default Self maxSize(Size<Dimension> value) {
            return _helper().putStyle(StyleProperties.MAX_SIZE, value);
        }

        default Self aspectRatio(float value) {
            return _helper().putStyle(StyleProperties.ASPECT_RATIO, value);
        }

        default Self gap(Size<LengthPercentage> value) {
            return _helper().putStyle(StyleProperties.GAP, value);
        }

        default Self margin(Rect<LengthPercentageAuto> value) {
            return _helper().putStyle(StyleProperties.MARGIN, value);
        }

        default Self padding(Rect<LengthPercentage> value) {
            return _helper().putStyle(StyleProperties.PADDING, value);
        }

        default Self border(Rect<LengthPercentage> value) {
            return _helper().putStyle(StyleProperties.BORDER, value);
        }

        default Self inset(Rect<com.antepod.taffy.style.LengthPercentageAuto> value) {
            return _helper().putStyle(StyleProperties.INSET, value);
        }

        default Self itemIsTable(boolean value) {
            return _helper().putStyle(StyleProperties.ITEM_IS_TABLE, value);
        }

        default Self itemIsReplaced(boolean value) {
            return _helper().putStyle(StyleProperties.ITEM_IS_REPLACED, value);
        }

        default Self scrollbarWidth(float value) {
            return _helper().putStyle(StyleProperties.SCROLLBAR_WIDTH, value);
        }

        default Self overflow(Point<Overflow> value) {
            return _helper().putStyle(StyleProperties.OVERFLOW, value);
        }

        default Self gridAutoFlow(GridAutoFlow value) {
            return _helper().putStyle(StyleProperties.GRID_AUTO_FLOW, value);
        }

        default Self gridRow(Line<GridPlacement> value) {
            return _helper().putStyle(StyleProperties.GRID_ROW, value);
        }

        default Self gridColumn(Line<GridPlacement> value) {
            return _helper().putStyle(StyleProperties.GRID_COLUMN, value);
        }

        default Self gridTemplateRows(List<GridTemplateComponent> value) {
            return _helper().putStyle(StyleProperties.GRID_TEMPLATE_ROWS, value);
        }

        default Self gridTemplateColumns(List<GridTemplateComponent> value) {
            return _helper().putStyle(StyleProperties.GRID_TEMPLATE_COLUMNS, value);
        }

        default Self gridAutoRows(List<TrackSizingFunction> value) {
            return _helper().putStyle(StyleProperties.GRID_AUTO_ROWS, value);
        }

        default Self gridAutoColumns(List<TrackSizingFunction> value) {
            return _helper().putStyle(StyleProperties.GRID_AUTO_COLUMNS, value);
        }

        default Self gridTemplateRowNames(List<List<String>> value) {
            return _helper().putStyle(StyleProperties.GRID_TEMPLATE_ROW_NAMES, value);
        }

        default Self gridTemplateColumnNames(List<List<String>> value) {
            return _helper().putStyle(StyleProperties.GRID_TEMPLATE_COLUMN_NAMES, value);
        }

        default Self gridTemplateAreas(List<GridTemplateArea> value) {
            return _helper().putStyle(StyleProperties.GRID_TEMPLATE_AREAS, value);
        }

        default Self gridTemplateAreaRowCount(int value) {
            return _helper().putStyle(StyleProperties.GRID_TEMPLATE_AREA_ROW_COUNT, value);
        }

        default Self gridTemplateAreaColumnCount(int value) {
            return _helper().putStyle(StyleProperties.GRID_TEMPLATE_AREA_COLUMN_COUNT, value);
        }
    }
}
