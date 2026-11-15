package com.github.ruediste.jreact.storybook;

import static com.github.ruediste.jreact.CoreComponents.div;
import static com.github.ruediste.jreact.CoreComponents.input;
import static com.github.ruediste.jreact.CoreComponents.select;
import static com.github.ruediste.jreact.CoreComponents.span;
import static com.github.ruediste.jreact.CoreComponents.text;

import java.awt.Color;
import java.util.List;

import com.antepod.taffy.style.Display;
import com.antepod.taffy.style.FlexDirection;
import com.github.ruediste.jreact.Hooks;
import com.github.ruediste.jreact.State;

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
        div().padding(20).radius(12).backgroundColor(Color.WHITE)
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
                    .radius(8)
                    .backgroundColor(new Color(59, 130, 246)).children(
                            () -> text("Clicked " + count.get() + " times").color(Color.WHITE));
            div().width(12);
            div().padding(10).radius(8).backgroundColor(new Color(229, 231, 235))
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
            badge("Info", new Color(219, 234, 254), new Color(30, 64, 175));
            badge("Success", new Color(220, 252, 231), new Color(22, 101, 52));
            badge("Warning", new Color(254, 249, 195), new Color(133, 77, 14));
        });
    }

    private static void badge(String label, Color background, Color foreground) {
        div().padding(10).margin(0, 8, 0, 0).radius(999).backgroundColor(background)
                .children(() -> text(label).color(foreground).bold(true));
    }

    private static void FlexLayout() {
        // --8<-- [start:flex-layout]
        div().display(Display.FLEX)
                .flexDirection(FlexDirection.ROW)
                .padding(16)
                .backgroundColor(new Color(255, 255, 255)).children(() -> {
                    div().flexGrow(1).padding(12).backgroundColor(new Color(219, 234, 254))
                            .children(() -> text("Flexible"));
                    div().width(160).padding(12).backgroundColor(new Color(220, 252, 231))
                            .children(() -> text("Fixed width"));
                });
        // --8<-- [end:flex-layout]
    }

    private static void CustomDialog() {
        // --8<-- [start:custom-dialog]
        div().padding(24).radius(12).backgroundColor(Color.WHITE);
        div().fontSize(20).bold(true);
        div().children(() -> text("Dialog title"));
        // --8<-- [end:custom-dialog]
    }

    private static void TaskBoard() {
        State<Integer> completed = Hooks.useState(0);
        String[] tasks = { "Design data model", "Wire refresh pipeline", "Paint the tree", "Write tests" };
        for (String task : tasks) {
            div().padding(12).margin(0, 0, 8, 0).radius(8).backgroundColor(Color.WHITE)
                    .children(() -> text(task));
        }
        text(completed.get() + " interactions").color(new Color(75, 85, 99));
    }
}
