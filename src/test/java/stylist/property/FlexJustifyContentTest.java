/*
 * Copyright (C) 2018 stylist Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stylist.property;

import org.junit.jupiter.api.Test;

import stylist.StyleTester;

/**
 * @version 2018/08/30 18:37:48
 */
public class FlexJustifyContentTest extends StyleTester {

    @Test
    public void start() throws Exception {
        ValidatableStyle style = writeStyle(() -> {
            display.flex().justifyContent.start();
        });

        assert style.property("justify-content", "flex-start");
        assert style.property("-webkit-justify-content", "flex-start");
    }

    @Test
    public void end() throws Exception {
        ValidatableStyle style = writeStyle(() -> {
            display.flex().justifyContent.end();
        });

        assert style.property("justify-content", "flex-end");
        assert style.property("-webkit-justify-content", "flex-end");
    }

    @Test
    public void center() throws Exception {
        ValidatableStyle style = writeStyle(() -> {
            display.flex().justifyContent.center();
        });

        assert style.property("justify-content", "center");
        assert style.property("-webkit-justify-content", "center");
    }

    @Test
    public void spaceAround() throws Exception {
        ValidatableStyle style = writeStyle(() -> {
            display.flex().justifyContent.spaceAround();
        });

        assert style.property("justify-content", "space-around");
        assert style.property("-webkit-justify-content", "space-around");
    }

    @Test
    public void spaceBetween() throws Exception {
        ValidatableStyle style = writeStyle(() -> {
            display.flex().justifyContent.spaceBetween();
        });

        assert style.property("justify-content", "space-between");
        assert style.property("-webkit-justify-content", "space-between");
    }
}
