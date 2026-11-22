package com.github.ruediste.jreact.components;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

import com.antepod.taffy.style.AlignContent;
import com.antepod.taffy.style.AlignItems;
import com.antepod.taffy.style.Dimension;
import com.antepod.taffy.style.Display;
import com.antepod.taffy.style.FlexDirection;
import com.antepod.taffy.style.Overflow;
import com.antepod.taffy.style.TextAlign;
import com.github.ruediste.jreact.OptionalN;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.primitives.Accordion;
import com.github.ruediste.jreact.style.StyleProperties.BorderStyle;
import com.github.ruediste.jreact.theme.ThemeElements;

public class Components {
    public ThemeElements e;

    public Components(ThemeElements elements) {
        this.e = elements;
    }

    public <T> Accordion.RootSingle<T> accordion(OptionalN<T> value) {
        return new Accordion.RootSingle<T>(ra -> {
            var root = e.div().apply(ra.config())
                    .className("accordion")
                    .display(Display.FLEX)
                    .width(Dimension.percent(1f))
                    .flexDirection(FlexDirection.COLUMN);
            root.children(() -> {
                ra.items().forEach(item -> {
                    new Accordion.Item<>(item, ia -> {
                        var itemNode = e.div().apply(ia.config())
                                .className("accordion-item")
                                .display(Display.FLEX)
                                .flexDirection(FlexDirection.COLUMN);
                        itemNode.notLast()
                                .borderBottomStyle(BorderStyle.SOLID)
                                .borderBottomWidth(1)
                                .borderBottomColor().border()
                                .inline();
                        itemNode.children(() -> {
                            Accordion.Header(ha -> {
                                e.div().apply(ha.config())
                                        .className("accordion-header")
                                        .display(Display.FLEX)
                                        .children(() -> {
                                            Accordion.Trigger(ta -> {
                                                var trigger = e.div().apply(ta.config())
                                                        .className("accordion-trigger")
                                                        .display(Display.FLEX)
                                                        .flex1()
                                                        .alignItems(AlignItems.START)
                                                        .justifyContent(AlignContent.SPACE_BETWEEN)
                                                        .borderRadius().lg()
                                                        .border()
                                                        .borderColor(JColor.TRANSPARENT)
                                                        .paddingTop(2.5f)
                                                        .paddingBottom(2.5f)
                                                        .textAlign(TextAlign.LEGACY_LEFT)
                                                        .textSm()
                                                        .fontBold()
                                                        .disabled(ta.ctx().isDisabled())
                                                        .focusable(!ta.ctx().isDisabled())
                                                        .accessibleName(ta.ctx().accessibleName());
                                                trigger.hover()
                                                        .backgroundColor().accent()
                                                        .underline()
                                                        .inline();
                                                trigger.focusVisible()
                                                        .ringWidth(3)
                                                        .ringColor().ring(0.5)
                                                        .inline();
                                                trigger.disabled()
                                                        .opacity(0.5)
                                                        .pointerEventsNone()
                                                        .inline();
                                                trigger.children(() -> {
                                                    item.header.run();
                                                    e.icon(new ChevronIcon(ta.ctx().isOpen()))
                                                            .className("accordion-trigger-icon")
                                                            .width(16)
                                                            .height(16)
                                                            .shrink0()
                                                            .mlAuto()
                                                            .color().mutedForeground();
                                                });
                                            });
                                        });
                            });
                            Accordion.Content(ca -> {
                                e.div().apply(ca.config())
                                        .className("accordion-content")
                                        .display(ca.ctx().isOpen() ? Display.FLEX : Display.NONE)
                                        .flexDirection(FlexDirection.COLUMN)
                                        .overflow(Overflow.HIDDEN)
                                        .paddingTop(0)
                                        .paddingBottom(2.5f)
                                        .textSm()
                                        .children(item.content);
                            });
                        });
                    });
                });
            });
        }).value(value);
    }

    private static final class ChevronIcon implements Icon {
        private final boolean open;

        ChevronIcon(boolean open) {
            this.open = open;
        }

        @Override
        public int getIconWidth() {
            return 16;
        }

        @Override
        public int getIconHeight() {
            return 16;
        }

        @Override
        public void paintIcon(java.awt.Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (open) {
                    g.drawLine(x + 4, y + 10, x + 8, y + 6);
                    g.drawLine(x + 8, y + 6, x + 12, y + 10);
                } else {
                    g.drawLine(x + 4, y + 6, x + 8, y + 10);
                    g.drawLine(x + 8, y + 10, x + 12, y + 6);
                }
            } finally {
                g.dispose();
            }
        }
    }
}
