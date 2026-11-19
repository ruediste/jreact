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
import com.github.ruediste.jreact.CoreElements;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.style.StyleProperties.BorderStyle;
import com.github.ruediste.jreact.style.StyleProperties.Property;
import com.github.ruediste.jreact.style.StyleProperties.Shadow;

/**
 * Declares component-scoped style rules and resolves the node-tree cascade.
 *
 * <p>
 * Declarations belong to the active {@link CoreElements} component context.
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
public final class CoreStyleApis {

    public interface StyleApiHelper<Api> {
        <T> Api putStyle(Property<T> property, T value);
    }

    public interface BorderStyleApi<Self, Helper extends StyleApiHelper<Self>> extends ApiHelperProvider<Helper> {

        default Self borderTopColor(JColor value) {
            return _helper().putStyle(StyleProperties.BORDER_TOP_COLOR, value);
        }

        default Self borderRightColor(JColor value) {
            return _helper().putStyle(StyleProperties.BORDER_RIGHT_COLOR, value);
        }

        default Self borderBottomColor(JColor value) {
            return _helper().putStyle(StyleProperties.BORDER_BOTTOM_COLOR, value);
        }

        default Self borderLeftColor(JColor value) {
            return _helper().putStyle(StyleProperties.BORDER_LEFT_COLOR, value);
        }

        default Self borderColor(JColor value) {
            _helper().putStyle(StyleProperties.BORDER_TOP_COLOR, value);
            _helper().putStyle(StyleProperties.BORDER_RIGHT_COLOR, value);
            _helper().putStyle(StyleProperties.BORDER_BOTTOM_COLOR, value);
            return _helper().putStyle(StyleProperties.BORDER_LEFT_COLOR, value);
        }

        default Self borderTopStyle(BorderStyle value) {
            return _helper().putStyle(StyleProperties.BORDER_TOP_STYLE, value);
        }

        default Self borderRightStyle(BorderStyle value) {
            return _helper().putStyle(StyleProperties.BORDER_RIGHT_STYLE, value);
        }

        default Self borderBottomStyle(BorderStyle value) {
            return _helper().putStyle(StyleProperties.BORDER_BOTTOM_STYLE, value);
        }

        default Self borderLeftStyle(BorderStyle value) {
            return _helper().putStyle(StyleProperties.BORDER_LEFT_STYLE, value);
        }

        default Self borderStyle(BorderStyle value) {
            _helper().putStyle(StyleProperties.BORDER_TOP_STYLE, value);
            _helper().putStyle(StyleProperties.BORDER_RIGHT_STYLE, value);
            _helper().putStyle(StyleProperties.BORDER_BOTTOM_STYLE, value);
            return _helper().putStyle(StyleProperties.BORDER_LEFT_STYLE, value);
        }

        default Self borderTopLeftRadius(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.BORDER_TOP_LEFT_RADIUS, value);
        }

        default Self borderTopLeftRadius(double value) {
            return borderTopLeftRadius(LengthPercentage.length((float) value));
        }

        default Self borderTopRightRadius(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.BORDER_TOP_RIGHT_RADIUS, value);
        }

        default Self borderTopRightRadius(double value) {
            return borderTopRightRadius(LengthPercentage.length((float) value));
        }

        default Self borderBottomRightRadius(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.BORDER_BOTTOM_RIGHT_RADIUS, value);
        }

        default Self borderBottomRightRadius(double value) {
            return borderBottomRightRadius(LengthPercentage.length((float) value));
        }

        default Self borderBottomLeftRadius(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.BORDER_BOTTOM_LEFT_RADIUS, value);
        }

        default Self borderBottomLeftRadius(double value) {
            return borderBottomLeftRadius(LengthPercentage.length((float) value));
        }

        default Self borderRadius(LengthPercentage value) {
            _helper().putStyle(StyleProperties.BORDER_TOP_LEFT_RADIUS, value);
            _helper().putStyle(StyleProperties.BORDER_TOP_RIGHT_RADIUS, value);
            _helper().putStyle(StyleProperties.BORDER_BOTTOM_RIGHT_RADIUS, value);
            return _helper().putStyle(StyleProperties.BORDER_BOTTOM_LEFT_RADIUS, value);
        }

        default Self borderRadius(double value) {
            return borderRadius(LengthPercentage.length((float) value));
        }

        default Self borderTopWidth(double value) {
            return _helper().putStyle(StyleProperties.BORDER_TOP_WIDTH, value);
        }

        default Self borderRightWidth(double value) {
            return _helper().putStyle(StyleProperties.BORDER_RIGHT_WIDTH, value);
        }

        default Self borderBottomWidth(double value) {
            return _helper().putStyle(StyleProperties.BORDER_BOTTOM_WIDTH, value);
        }

        default Self borderLeftWidth(double value) {
            return _helper().putStyle(StyleProperties.BORDER_LEFT_WIDTH, value);
        }

        default Self borderWidth(double value) {
            _helper().putStyle(StyleProperties.BORDER_TOP_WIDTH, value);
            _helper().putStyle(StyleProperties.BORDER_RIGHT_WIDTH, value);
            _helper().putStyle(StyleProperties.BORDER_BOTTOM_WIDTH, value);
            return _helper().putStyle(StyleProperties.BORDER_LEFT_WIDTH, value);
        }

        default Self borderWidth(Rect<Double> value) {
            _helper().putStyle(StyleProperties.BORDER_TOP_WIDTH, value.top);
            _helper().putStyle(StyleProperties.BORDER_RIGHT_WIDTH, value.right);
            _helper().putStyle(StyleProperties.BORDER_BOTTOM_WIDTH, value.bottom);
            return _helper().putStyle(StyleProperties.BORDER_LEFT_WIDTH, value.left);
        }
    }

    public interface VisualStyleApi<Self, Helper extends StyleApiHelper<Self>> extends ApiHelperProvider<Helper> {

        default Self backgroundColor(Color value) {
            return _helper().putStyle(StyleProperties.BACKGROUND, value);
        }

        default Self color(Color value) {
            return _helper().putStyle(StyleProperties.COLOR, value);
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
    }

    public interface LayoutStyleApi<Self, Helper extends StyleApiHelper<Self>> extends ApiHelperProvider<Helper> {

        default Self flexGrow(double value) {
            return _helper().putStyle(StyleProperties.FLEX_GROW, value);
        }

        default Self flexShrink(double value) {
            return _helper().putStyle(StyleProperties.FLEX_SHRINK, value);
        }

        default Self display(Display value) {
            return _helper().putStyle(StyleProperties.DISPLAY, value);
        }

        default Self position(Position value) {
            return _helper().putStyle(StyleProperties.POSITION, value);
        }

        default Self direction(Direction value) {
            return _helper().putStyle(StyleProperties.DIRECTION, value);
        }

        default Self floatMode(Float value) {
            return _helper().putStyle(StyleProperties.FLOAT, value);
        }

        default Self clear(Clear value) {
            return _helper().putStyle(StyleProperties.CLEAR, value);
        }

        default Self boxSizing(BoxSizing value) {
            return _helper().putStyle(StyleProperties.BOX_SIZING, value);
        }

        default Self top(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.TOP, value);
        }

        default Self top(double value) {
            return top(LengthPercentageAuto.length((float) value));
        }

        default Self right(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.RIGHT, value);
        }

        default Self right(double value) {
            return right(LengthPercentageAuto.length((float) value));
        }

        default Self bottom(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.BOTTOM, value);
        }

        default Self bottom(double value) {
            return bottom(LengthPercentageAuto.length((float) value));
        }

        default Self left(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.LEFT, value);
        }

        default Self left(double value) {
            return left(LengthPercentageAuto.length((float) value));
        }

        default Self inset(LengthPercentageAuto value) {
            _helper().putStyle(StyleProperties.TOP, value);
            _helper().putStyle(StyleProperties.RIGHT, value);
            _helper().putStyle(StyleProperties.BOTTOM, value);
            return _helper().putStyle(StyleProperties.LEFT, value);
        }

        default Self inset(double value) {
            return inset(LengthPercentageAuto.length((float) value));
        }

        default Self inset(Rect<LengthPercentageAuto> value) {
            _helper().putStyle(StyleProperties.TOP, value.top);
            _helper().putStyle(StyleProperties.RIGHT, value.right);
            _helper().putStyle(StyleProperties.BOTTOM, value.bottom);
            return _helper().putStyle(StyleProperties.LEFT, value.left);
        }

        default Self width(Dimension value) {
            return _helper().putStyle(StyleProperties.WIDTH, value);
        }

        default Self width(double value) {
            return width(Dimension.length((float) value));
        }

        default Self height(Dimension value) {
            return _helper().putStyle(StyleProperties.HEIGHT, value);
        }

        default Self height(double value) {
            return height(Dimension.length((float) value));
        }

        default Self minWidth(Dimension value) {
            return _helper().putStyle(StyleProperties.MIN_WIDTH, value);
        }

        default Self minWidth(double value) {
            return minWidth(Dimension.length((float) value));
        }

        default Self minHeight(Dimension value) {
            return _helper().putStyle(StyleProperties.MIN_HEIGHT, value);
        }

        default Self minHeight(double value) {
            return minHeight(Dimension.length((float) value));
        }

        default Self maxWidth(Dimension value) {
            return _helper().putStyle(StyleProperties.MAX_WIDTH, value);
        }

        default Self maxWidth(double value) {
            return maxWidth(Dimension.length((float) value));
        }

        default Self maxHeight(Dimension value) {
            return _helper().putStyle(StyleProperties.MAX_HEIGHT, value);
        }

        default Self maxHeight(double value) {
            return maxHeight(Dimension.length((float) value));
        }

        default Self aspectRatio(float value) {
            return _helper().putStyle(StyleProperties.ASPECT_RATIO, value);
        }

        default Self marginTop(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.MARGIN_TOP, value);
        }

        default Self marginTop(double value) {
            return marginTop(LengthPercentageAuto.length((float) value));
        }

        default Self marginRight(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.MARGIN_RIGHT, value);
        }

        default Self marginRight(double value) {
            return marginRight(LengthPercentageAuto.length((float) value));
        }

        default Self marginBottom(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.MARGIN_BOTTOM, value);
        }

        default Self marginBottom(double value) {
            return marginBottom(LengthPercentageAuto.length((float) value));
        }

        default Self marginLeft(LengthPercentageAuto value) {
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, value);
        }

        default Self marginLeft(double value) {
            return marginLeft(LengthPercentageAuto.length((float) value));
        }

        default Self margin(LengthPercentageAuto value) {
            _helper().putStyle(StyleProperties.MARGIN_TOP, value);
            _helper().putStyle(StyleProperties.MARGIN_RIGHT, value);
            _helper().putStyle(StyleProperties.MARGIN_BOTTOM, value);
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, value);
        }

        default Self margin(double value) {
            return margin(LengthPercentageAuto.length((float) value));
        }

        default Self margin(double top, double right, double bottom, double left) {
            _helper().putStyle(StyleProperties.MARGIN_TOP, LengthPercentageAuto.length((float) top));
            _helper().putStyle(StyleProperties.MARGIN_RIGHT, LengthPercentageAuto.length((float) right));
            _helper().putStyle(StyleProperties.MARGIN_BOTTOM, LengthPercentageAuto.length((float) bottom));
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, LengthPercentageAuto.length((float) left));
        }

        default Self margin(Rect<LengthPercentageAuto> value) {
            _helper().putStyle(StyleProperties.MARGIN_TOP, value.top);
            _helper().putStyle(StyleProperties.MARGIN_RIGHT, value.right);
            _helper().putStyle(StyleProperties.MARGIN_BOTTOM, value.bottom);
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, value.left);
        }

        default Self paddingTop(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.PADDING_TOP, value);
        }

        default Self paddingTop(double value) {
            return paddingTop(LengthPercentage.length((float) value));
        }

        default Self paddingRight(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.PADDING_RIGHT, value);
        }

        default Self paddingRight(double value) {
            return paddingRight(LengthPercentage.length((float) value));
        }

        default Self paddingBottom(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.PADDING_BOTTOM, value);
        }

        default Self paddingBottom(double value) {
            return paddingBottom(LengthPercentage.length((float) value));
        }

        default Self paddingLeft(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.PADDING_LEFT, value);
        }

        default Self paddingLeft(double value) {
            return paddingLeft(LengthPercentage.length((float) value));
        }

        default Self padding(LengthPercentage value) {
            _helper().putStyle(StyleProperties.PADDING_TOP, value);
            _helper().putStyle(StyleProperties.PADDING_RIGHT, value);
            _helper().putStyle(StyleProperties.PADDING_BOTTOM, value);
            return _helper().putStyle(StyleProperties.PADDING_LEFT, value);
        }

        default Self padding(double value) {
            return padding(LengthPercentage.length((float) value));
        }

        default Self padding(double top, double right, double bottom, double left) {
            _helper().putStyle(StyleProperties.PADDING_TOP, LengthPercentage.length((float) top));
            _helper().putStyle(StyleProperties.PADDING_RIGHT, LengthPercentage.length((float) right));
            _helper().putStyle(StyleProperties.PADDING_BOTTOM, LengthPercentage.length((float) bottom));
            return _helper().putStyle(StyleProperties.PADDING_LEFT, LengthPercentage.length((float) left));
        }

        default Self padding(Rect<LengthPercentage> value) {
            _helper().putStyle(StyleProperties.PADDING_TOP, value.top);
            _helper().putStyle(StyleProperties.PADDING_RIGHT, value.right);
            _helper().putStyle(StyleProperties.PADDING_BOTTOM, value.bottom);
            return _helper().putStyle(StyleProperties.PADDING_LEFT, value.left);
        }

        default Self overflowX(Overflow value) {
            return _helper().putStyle(StyleProperties.OVERFLOW_X, value);
        }

        default Self overflowY(Overflow value) {
            return _helper().putStyle(StyleProperties.OVERFLOW_Y, value);
        }

        default Self overflow(Overflow value) {
            _helper().putStyle(StyleProperties.OVERFLOW_X, value);
            return _helper().putStyle(StyleProperties.OVERFLOW_Y, value);
        }

        default Self overflow(Point<Overflow> value) {
            _helper().putStyle(StyleProperties.OVERFLOW_X, value.x);
            return _helper().putStyle(StyleProperties.OVERFLOW_Y, value.y);
        }

        default Self scrollbarWidth(float value) {
            return _helper().putStyle(StyleProperties.SCROLLBAR_WIDTH, value);
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

        default Self rowGap(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.ROW_GAP, value);
        }

        default Self rowGap(double value) {
            return rowGap(LengthPercentage.length((float) value));
        }

        default Self columnGap(LengthPercentage value) {
            return _helper().putStyle(StyleProperties.COLUMN_GAP, value);
        }

        default Self columnGap(double value) {
            return columnGap(LengthPercentage.length((float) value));
        }

        default Self gap(LengthPercentage value) {
            _helper().putStyle(StyleProperties.ROW_GAP, value);
            return _helper().putStyle(StyleProperties.COLUMN_GAP, value);
        }

        default Self gap(double value) {
            return gap(LengthPercentage.length((float) value));
        }

        default Self gap(Size<LengthPercentage> value) {
            _helper().putStyle(StyleProperties.ROW_GAP, value.height);
            return _helper().putStyle(StyleProperties.COLUMN_GAP, value.width);
        }

        default Self textAlign(TextAlign value) {
            return _helper().putStyle(StyleProperties.TEXT_ALIGN, value);
        }

        default Self gridAutoFlow(GridAutoFlow value) {
            return _helper().putStyle(StyleProperties.GRID_AUTO_FLOW, value);
        }

        default Self gridRowStart(GridPlacement value) {
            return _helper().putStyle(StyleProperties.GRID_ROW_START, value);
        }

        default Self gridRowEnd(GridPlacement value) {
            return _helper().putStyle(StyleProperties.GRID_ROW_END, value);
        }

        default Self gridColumnStart(GridPlacement value) {
            return _helper().putStyle(StyleProperties.GRID_COLUMN_START, value);
        }

        default Self gridColumnEnd(GridPlacement value) {
            return _helper().putStyle(StyleProperties.GRID_COLUMN_END, value);
        }

        default Self gridRow(Line<GridPlacement> value) {
            _helper().putStyle(StyleProperties.GRID_ROW_START, value.start);
            return _helper().putStyle(StyleProperties.GRID_ROW_END, value.end);
        }

        default Self gridColumn(Line<GridPlacement> value) {
            _helper().putStyle(StyleProperties.GRID_COLUMN_START, value.start);
            return _helper().putStyle(StyleProperties.GRID_COLUMN_END, value.end);
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
    }
}
