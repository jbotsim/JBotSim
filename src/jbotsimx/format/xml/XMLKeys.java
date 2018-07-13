package jbotsimx.format.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.NoSuchElementException;
import java.util.function.Function;

public enum XMLKeys {
    JBOTSIM("jbotsim"),
    TOPOLOGY("topology"),
    CLASSES("classes"),
    NODECLASS("node-class"),
    NODE("node"),
    LINK("link"),
    GRAPH("graph"),
    LINK_RESOLVER("link-resolver"),
    MESSAGE_ENGINE("message-engine"),
    SCHEDULER("scheduler"),
    CLOCKCLASS("clock-class"),

    GENERATOR("generator"),
    LINE("line"),
    GRID("grid"),
    KN("KN"),
    TORUS("torus"),
    RING("ring"),
    RANDOM_LOCATIONS("random-locations"),

    VERSION_ATTR("version"),
    IDENTIFIER_ATTR("id"),
    DIRECTED_ATTR("directed"),
    CLASS_ATTR("class"),
    SENSING_RANGE_ATTR("sensing-range"),
    COMMUNICATION_RANGE_ATTR("communication-range"),
    WIRELESS_ENABLED_ATTR("wireless-enabled"),
    SOURCE_ATTR("src"),
    DESTINATION_ATTR("dst"),
    DIRECTION_ATTR("direction"),
    CLOCK_SPEED_ATTR("speed"),

    ORDER_ATTR("order"),
    X_ORDER_ATTR("x-order"),
    Y_ORDER_ATTR("y-order"),
    ABSOLUTE_COORDS_ATTR("absolute-coords"),
    WIRED_ATTR("wired"),
    HORIZONTAL_ATTR("horizontal"),
    NODECLASS_ATTR("node-class"),

    WIDTH_ATTR("width"),
    HEIGHT_ATTR("height"),
    COLOR_ATTR("color"),
    SIZE_ATTR("size"),
    LOCATION_X_ATTR("x"),
    LOCATION_Y_ATTR("y"),
    LOCATION_Z_ATTR("z"),

    TRACE("trace"),
    ADD_NODE("add-node"),
    DELETE_NODE("delete-node"),
    SELECT_NODE("select-node"),
    MOVE_NODE("move-node"),
    START_TOPOLOGY("start-topology"),
    TIME_ATTR("time")
    ;

    private final String key;

    XMLKeys(String value) {
        this.key = value;
    }

    public boolean equals(String val) {
        return key.equals(val);
    }

    public Element createElement(Document doc) {
        return doc.createElement(key);
    }

    public Element createElement(Document doc, Element parent) {
        Element result = doc.createElement(key);
        parent.appendChild(result);
        return result;
    }

    public boolean labelsElement (Element e) {
        return key.equals(e.getNodeName());
    }

    public void setAttribute(Element el, String value) {
        el.setAttribute(key, value);
    }

    public <T> void setAttribute(Element el, T value) {
        setAttribute(el, String.valueOf(value));
    }

    public <T> void setNotDefaultAttribute(Element el, T value, T default_value) {
        if (value != default_value && !value.equals(default_value)) {
            setAttribute(el, value);
        }
    }

    public String getValueFor(Element el) {
        return el.getAttribute(key);
    }

    public boolean isAttributeOf(Element el) {
        return el.hasAttribute(key);
    }

    public String getValueFor(Element el, String default_value) {
        if (isAttributeOf(el))
            return getValueFor(el);
        return default_value;
    }

    public Integer getValueFor(Element el, Integer default_value) {
        return getValueFor(el, Integer::valueOf, default_value);
    }

    public Integer getIntegerValueFor(Element el) {
        return getValueFor(el, Integer::valueOf);
    }

    public Double getValueFor(Element el, Double default_value) {
        return getValueFor(el, Double::valueOf, default_value);
    }

    public Double getDoubleValueFor(Element el) {
        return getValueFor(el, Double::valueOf);
    }

    public Boolean getValueFor(Element el, Boolean default_value) {
        return getValueFor(el, Boolean::valueOf, default_value);
    }

    public <R> R getValueFor(Element el, Function<String, R> translate, R default_value) {
        if (isAttributeOf(el))
            return getValueFor(el, translate);
        return default_value;
    }

    public <R> R getValueFor(Element el, Function<String, R> translate) {
        if (!isAttributeOf(el))
            throw new NoSuchElementException(key);
        return translate.apply(el.getAttribute(key));
    }
}
