/*
 * Copyright (C) 2017 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stylist.util;

import java.util.Map;
import java.util.function.Consumer;

import kiss.Variable;
import stylist.CSSValue;
import stylist.Vendor;
import stylist.value.Color;

/**
 * @version 2018/09/01 21:21:24
 */
public class JavaFXLizer implements Consumer<Properties> {

    /** The special formatter for JavaFX. */
    public static final Formatter pretty() {
        return Formatter.pretty().color(Color::toRGB).postProcessor(new JavaFXLizer());
    }

    /** The special formatter for JavaFX. */
    public static final Formatter compact() {
        return Formatter.compact().color(Color::toRGB).postProcessor(new JavaFXLizer());
    }

    /** The property name mapping. */
    private static final Map<String, String> propertyNames = Map
            .of("width", "pref-width", "height", "pref-height", "color", "text-fill", "stroke-dasharray", "stroke-dash-array");

    /** The property value mapping. */
    private static final Map<String, String> cursorProperties = Map.of("pointer", "hand");

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Properties properties) {
        properties.compactTo("padding", "0", sides("padding-*"));
        properties.compactTo("border-width", "0", sides("border-*-width"));
        properties.compactTo("border-style", "solid", sides("border-*-style"));
        properties.compactTo("border-color", Color.Transparent, sides("border-*-color"));
        properties.value("cursor", cursorProperties);

        alignment(properties);

        // assign prefix and map special name
        properties.keys(key -> Vendor.JavaFX + propertyNames.getOrDefault(key, key));
    }

    /**
     * Configure alignment.
     * 
     * @param properties
     */
    private void alignment(Properties properties) {
        Variable<CSSValue> horizontal = properties.remove("text-align");
        Variable<CSSValue> vertical = properties.remove("vertical-align");

        if (horizontal.isPresent() || vertical.isPresent()) {
            String value = "";
            String h = horizontal.or(CSSValue.of("left")).toString();
            String v = vertical.or(CSSValue.of("center")).toString();

            if (v.equals("middle")) {
                v = "center";
            }

            if (h.equals("center") && v.equals("center")) {
                value = "center";
            } else {
                value = v + "-" + h;
            }
            properties.add("alignment", CSSValue.of(value));
        }
    }

    /**
     * Helper method to build side names.
     * 
     * @param template
     * @return
     */
    private static String[] sides(String template) {
        String[] sides = {"top", "right", "bottom", "left"};

        for (int i = 0; i < sides.length; i++) {
            sides[i] = template.replace("*", sides[i]);
        }
        return sides;
    }
}
