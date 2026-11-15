package com.github.ruediste.jreact.storybook;

import static com.github.ruediste.jreact.CoreComponents.div;
import static com.github.ruediste.jreact.CoreComponents.text;
import static com.github.ruediste.jreact.CoreComponents.viewportHeight;
import static com.github.ruediste.jreact.CoreComponents.viewportWidth;

import java.awt.Color;
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

public final class Storybook {
    public static final List<String> STORIES = List.of("Overview", "Buttons", "Inputs", "Feedback", "Layout", "Dialog",
            "Task Board");

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
                .backgroundColor(new Color(244, 246, 250)).children(() -> {
                    sidebar(selected);
                    content(selected.get());
                });
    }

    private static void sidebar(State<String> selected) {
        div().width(220).height(viewportHeight())
                .padding(18)
                .flexDirection(FlexDirection.COLUMN)
                .backgroundColor(new Color(31, 41, 55)).children(() -> {
                    text("JReact").fontSize(24).bold(true).color(Color.WHITE);
                    text("Component Storybook").fontSize(12).color(new Color(156, 163, 175));
                    div().height(20);
                    for (String story : STORIES) {
                        boolean active = story.equals(selected.get());
                        div().padding(10)
                                .radius(8)
                                .margin(0, 0, 6, 0)
                                .backgroundColor(active ? new Color(59, 130, 246) : new Color(31, 41, 55)).children(
                                        () -> text(story)
                                                .color(active ? Color.WHITE : new Color(209, 213, 219)));
                    }
                });
    }

    private static void content(String story) {
        div().flexGrow(1).height(viewportHeight()).padding(32)
                .flexDirection(FlexDirection.COLUMN).children(() -> {
                    text(story).fontSize(28).bold(true).color(new Color(17, 24, 39));
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
