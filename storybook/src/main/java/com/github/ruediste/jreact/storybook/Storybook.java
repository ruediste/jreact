package com.github.ruediste.jreact.storybook;

import static com.github.ruediste.jreact.CoreElements.div;
import static com.github.ruediste.jreact.CoreElements.text;
import static com.github.ruediste.jreact.CoreElements.viewportHeight;
import static com.github.ruediste.jreact.CoreElements.viewportWidth;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.antepod.taffy.style.FlexDirection;
import com.github.ruediste.jreact.Hooks;
import com.github.ruediste.jreact.JReactAgent;
import com.github.ruediste.jreact.JReactPanel;
import com.github.ruediste.jreact.State;
import com.github.ruediste.jreact.color.JColor;

public final class Storybook {
    public static final List<String> STORIES = List.of("Overview", "Buttons", "Inputs", "Accordion", "Feedback",
            "Layout", "Dialog", "Task Board");

    private Storybook() {
    }

    public static JReactPanel panel() {
        return panel("Overview");
    }

    public static JReactPanel panel(String initialStory) {
        if (!STORIES.contains(initialStory))
            throw new IllegalArgumentException("Unknown story: " + initialStory);
        return new JReactPanel(() -> app(initialStory));
    }

    private static void app(String initialStory) {
        State<String> selected = Hooks.useState(initialStory);
        div().flexDirection(FlexDirection.ROW)
                .width(viewportWidth()).height(viewportHeight())
                .backgroundColor(JColor.rgb(244, 246, 250)).children(() -> {
                    sidebar(selected);
                    content(selected.get());
                });
    }

    private static void sidebar(State<String> selected) {
        div().width(220).height(viewportHeight())
                .padding(18)
                .flexDirection(FlexDirection.COLUMN)
                .backgroundColor(JColor.rgb(31, 41, 55)).children(() -> {
                    text("JReact").fontSize(24).bold(true).color(JColor.rgb(255, 255, 255));
                    text("Component Storybook").fontSize(12).color(JColor.rgb(156, 163, 175));
                    div().height(20);
                    for (String story : STORIES) {
                        boolean active = story.equals(selected.get());
                        div().padding(10)
                                .borderRadius(8)
                                .margin(0, 0, 6, 0)
                                .backgroundColor(active ? JColor.rgb(59, 130, 246) : JColor.rgb(31, 41, 55))
                                .onClick(e -> selected.set(story))
                                .children(
                                        () -> text(story)
                                                .color(active ? JColor.rgb(255, 255, 255) : JColor.rgb(209, 213, 219)));
                    }
                });
    }

    private static void content(String story) {
        div().flexGrow(1).height(viewportHeight()).padding(32)
                .flexDirection(FlexDirection.COLUMN).children(() -> {
                    text(story).fontSize(28).bold(true).color(JColor.rgb(17, 24, 39));
                    div().height(16);
                    StorybookStories.story(story);
                });
    }

    public static void main(String[] args) {
        JReactAgent.install();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JReact | Component Storybook");
            JReactPanel panel = panel();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent event) {
                    panel.close();
                }
            });
            frame.setContentPane(panel);
            frame.setSize(960, 800);
            frame.setLocationByPlatform(true);
            var icon = UIManager.getIcon("FileView.computerIcon");
            if (icon instanceof javax.swing.ImageIcon image)
                frame.setIconImage(image.getImage());
            frame.setVisible(true);
        });
    }
}
