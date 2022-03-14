package org.test;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private VerticalLayout firstLayout = new VerticalLayout(new Label("Visible by default"));
    private VerticalLayout secondLayout = new VerticalLayout(new Label("Hidden by default"));

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        secondLayout.setVisible(false);
        layout.addComponents(firstLayout, secondLayout);

        Label text = new Label("Open two browser tabs, then click 'Switch content' on any");

        Button switchLayouts = new Button("Switch content on other tabs", e -> {
            for (UI ui : UI.getCurrent().getSession().getUIs()) {
                if (ui != this) {
                    // Switch content on other tabs
                    MyUI myUi = (MyUI) ui;
                    myUi.access(() -> {
                        myUi.firstLayout.setVisible(false);
                        myUi.secondLayout.setVisible(true);
                    });
                    Notification.show("Observe AssertionError in the server console");
                }
            }
        });

        Button makeRoundTrip = new Button("Make round-trip", e -> {});

        layout.addComponents(text, switchLayouts, makeRoundTrip);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
