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
import stylist.value.Numeric;

/**
 * @version 2018/08/30 18:38:35
 */
public class MarginTest extends StyleTester {

    @Test
    public void each() {
        ValidatableStyle parsed = writeStyle(() -> {
            margin.top(10, em);
            margin.bottom(20, px);
            margin.left(30, percent);
            margin.right(new Numeric(10, ex));
        });
        assert parsed.property("margin-top", "10em");
        assert parsed.property("margin-bottom", "20px");
        assert parsed.property("margin-left", "30%");
        assert parsed.property("margin-right", "10ex");
    }

    @Test
    public void auto() {
        ValidatableStyle parsed = writeStyle(() -> {
            margin.auto();
        });
        assert parsed.property("margin-left", "auto");
        assert parsed.property("margin-right", "auto");
    }

    @Test
    public void shorthand() {
        ValidatableStyle parsed = writeStyle(() -> {
            margin.horizontal(1, em);
        });
        assert parsed.property("margin-left", "1em");
        assert parsed.property("margin-right", "1em");

        parsed = writeStyle(() -> {
            margin.vertical(1, em);
        });
        assert parsed.property("margin-top", "1em");
        assert parsed.property("margin-bottom", "1em");

        parsed = writeStyle(() -> {
            margin.size(1, em);
        });
        assert parsed.property("margin-left", "1em");
        assert parsed.property("margin-right", "1em");
        assert parsed.property("margin-top", "1em");
        assert parsed.property("margin-bottom", "1em");
    }

    @Test
    public void calc() {
        Numeric one = new Numeric(1, em);
        Numeric two = new Numeric(2, px);

        ValidatableStyle parsed = writeStyle(() -> {
            margin.size(one.add(two));
        });
        assert parsed.property("margin-left", "calc(1em + 2px)", "-webkit-calc(1em + 2px)");
        assert parsed.property("margin-right", "calc(1em + 2px)", "-webkit-calc(1em + 2px)");
        assert parsed.property("margin-top", "calc(1em + 2px)", "-webkit-calc(1em + 2px)");
        assert parsed.property("margin-bottom", "calc(1em + 2px)", "-webkit-calc(1em + 2px)");
    }
}
