package com.github.ruediste.jreact.storybook;

import static com.github.ruediste.jreact.CoreElements.div;
import static com.github.ruediste.jreact.CoreElements.input;
import static com.github.ruediste.jreact.CoreElements.select;
import static com.github.ruediste.jreact.CoreElements.span;
import static com.github.ruediste.jreact.CoreElements.text;

import java.util.List;

import com.antepod.taffy.style.Display;
import com.antepod.taffy.style.FlexDirection;
import com.github.ruediste.jreact.Hooks;
import com.github.ruediste.jreact.State;
import com.github.ruediste.jreact.color.JColor;

public final class StorybookStories {
    private StorybookStories() {
    }

    public static void story(String name) {
        switch (name) {
            case "Overview" -> Overview();
            case "Buttons" -> Buttons();
            case "Inputs" -> Inputs();
            case "Feedback" -> Feedback();
            case "Layout" -> FlexLayout();
            case "Dialog" -> CustomDialog();
            case "Task Board" -> TaskBoard();
            default -> throw new IllegalArgumentException("Unknown story: " + name);
        }
    }

    private static void Overview() {
        div().padding(20).borderRadius(12).backgroundColor(JColor.WHITE)
                .flexDirection(FlexDirection.COLUMN).children(() -> {
                    text("Primitives compose into a synchronous Java UI.");
                    div().height(12);
                    span(() -> {
                        text("Div, ");
                        span().bold(true).children(() -> text("Span"));
                        text(", and Text are the base building blocks.");
                    });
                });
    }

    private static void Buttons() {
        State<Integer> count = Hooks.useState(0);
        div().flexDirection(FlexDirection.ROW).children(() -> {
            div()
                    .padding(10)
                    .borderRadius(8)
                    .backgroundColor(JColor.rgb(59, 130, 246))
                    .onClick(e -> count.set(count.get() + 1))
                    .children(
                            () -> text("Clicked " + count.get() + " times").color(JColor.WHITE));
            div().width(12);
            div().padding(10).borderRadius(8).backgroundColor(JColor.rgb(229, 231, 235))
                    .children(() -> text("Secondary"));
        });
    }

    private static void Inputs() {
        State<String> name = Hooks.useState("JReact");
        State<String> variant = Hooks.useState("Primary");
        text("Controlled input").bold(true);
        input(name.get(), name::set).width(240).padding(8);
        text("Hello, " + name.get());
        div().height(16);
        text("Select").bold(true);
        select(variant.get(), List.of("Primary", "Secondary", "Danger"), variant::set).width(180);
        text("Variant: " + variant.get());
    }

    private static void Feedback() {
        div().flexDirection(FlexDirection.ROW).children(() -> {
            badge("Info", JColor.rgb(219, 234, 254), JColor.rgb(30, 64, 175));
            badge("Success", JColor.rgb(220, 252, 231), JColor.rgb(22, 101, 52));
            badge("Warning", JColor.rgb(254, 249, 195), JColor.rgb(133, 77, 14));
        });
    }

    private static void badge(String label, JColor background, JColor foreground) {
        div().padding(10).margin(0, 8, 0, 0).borderRadius(999).backgroundColor(background)
                .children(() -> text(label).color(foreground).bold(true));
    }

    private static void FlexLayout() {
        // --8<-- [start:flex-layout]
        div().display(Display.FLEX)
                .flexDirection(FlexDirection.ROW)
                .padding(16)
                .backgroundColor(JColor.WHITE).children(() -> {
                    div().flexGrow(1).padding(12).backgroundColor(JColor.rgb(219, 234, 254))
                            .children(() -> text("Flexible"));
                    div().width(160).padding(12).backgroundColor(JColor.rgb(220, 252, 231))
                            .children(() -> text("Fixed width"));
                });
        // --8<-- [end:flex-layout]
    }

    private static void CustomDialog() {
        // --8<-- [start:custom-dialog]
        div().padding(24).borderRadius(12).backgroundColor(JColor.WHITE);
        div().fontSize(20).bold(true);
        div().children(() -> text("Dialog title"));
        // --8<-- [end:custom-dialog]
    }

    private static void TaskBoard() {
        State<Integer> completed = Hooks.useState(0);
        String[] tasks = { "Design data model", "Wire refresh pipeline", "Paint the tree", "Write tests" };
        for (String task : tasks) {
            div().padding(12).margin(0, 0, 8, 0).borderRadius(8).backgroundColor(JColor.WHITE)
                    .onClick(e -> completed.update(x -> x + 1))
                    .children(() -> text(task));
        }
        text(completed.get() + " interactions").color(JColor.rgb(75, 85, 99));
    }
}
