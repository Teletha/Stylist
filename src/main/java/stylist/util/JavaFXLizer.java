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
import java.util.Optional;
import java.util.function.Consumer;

import stylist.CSSValue;
import stylist.Vendor;
import stylist.value.Color;

/**
 * @version 2018/09/05 13:36:50
 */
public class JavaFXLizer implements Consumer<Properties> {

    /** The special formatter for JavaFX. */
    public static final Formatter pretty() {
        return Formatter.pretty().color(Color::toRGB).postProcessor(new JavaFXLizer());
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
        properties.revalue("cursor", cursorProperties);

        alignment(properties);

        // assign prefix and map special name
        properties.rename(this::rename);
    }

    /**
     * Rename property names for JavaFX.
     * 
     * @param name
     * @return
     */
    private CSSValue rename(CSSValue propertyName) {
        String name = propertyName.toString();
        String renamed = propertyNames.getOrDefault(name, name);
        return CSSValue.of(Vendor.JavaFX + renamed);
    }

    /**
     * Configure alignment.
     * 
     * @param properties
     */
    private void alignment(Properties properties) {
        Optional<CSSValue> horizontal = properties.remove("text-align");
        Optional<CSSValue> vertical = properties.remove("vertical-align");

        if (horizontal.isPresent() || vertical.isPresent()) {
            String value = "";
            String h = horizontal.orElse(CSSValue.of("left")).toString();
            String v = vertical.orElse(CSSValue.of("center")).toString();

            if (v.equals("middle")) {
                v = "center";
            }

            if (h.equals("center") && v.equals("center")) {
                value = "center";
            } else {
                value = v + "-" + h;
            }
            properties.set("alignment", CSSValue.of(value));
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
