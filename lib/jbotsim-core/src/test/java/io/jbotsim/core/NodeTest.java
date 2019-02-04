package io.jbotsim.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    public static final int DEFAULT_ICON_SIZE = 8;

    private Node createDefaultNode() {
        return new Node();
    }

    // region iconSize
    @Test
    void iconSize_defaultValue_ok() {
        Node node = createDefaultNode();
        assertEquals(DEFAULT_ICON_SIZE, node.getIconSize());
    }

    @Test
    void iconSize_setZero_ok() {
        testIconSizeGetterSetter(0);
    }

    @Test
    void iconSize_setNegative_ok() {
        testIconSizeGetterSetter(-15);
    }

    @Test
    void iconSize_setPositive_ok() {
        testIconSizeGetterSetter(15);
    }

    private void testIconSizeGetterSetter(int testedSize) {
        Node node = createDefaultNode();
        node.setIconSize(testedSize);
        assertEquals(testedSize, node.getIconSize());
    }

    // endregion
}