package io.jbotsim.ui.painting;

public class UIComponent {
    private Object component;

    public UIComponent(Object component) {
        this.component = component;
    }

    public Object getComponent()
    {
        return component;
    }
}
