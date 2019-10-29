/*
 * Copyright (C) 2019 stylist Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stylist;

import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import kiss.I;
import stylist.value.Color;

/**
 * @version 2018/12/08 11:36:03
 */
public final class Stylist {

    /** The format style. */
    private String beforeSelector = "";

    /** The format style. */
    private String afterSelector = "";

    /** The format style. */
    private String afterStartBrace = "";

    /** The format style. */
    private String beforeEndBrace = "";

    /** The format style. */
    private String afterEndBrace = "";

    /** The format style. */
    private String beforePropertyName = "";

    /** The format style. */
    private String afterPropertyName = "";

    /** The format style. */
    private String beforePropertyValue = "";

    /** The format style. */
    private String afterPropertyValue = "";

    /** The format style. */
    private String afterPropertyLine = "";

    /** The format style. */
    private Function<Color, String> color = Color::toHSL;

    /** The format style. */
    private boolean comment = false;

    /** The format style. */
    private boolean showEmptyStyle = false;

    /** The manager of post processors. */
    private final List<Consumer<Properties>> posts = new ArrayList();

    /**
     * Hide constructor.
     */
    private Stylist() {
    }

    /**
     * Decorate selector.
     * 
     * @param before A before decoration.
     * @param after An after decoration.
     * @return Chainable API.
     */
    public Stylist selector(String before, String after) {
        if (before != null) {
            this.beforeSelector = before;
        }

        if (after != null) {
            this.afterSelector = after;
        }
        return this;
    }

    /**
     * Decorate start brace.
     * 
     * @param after An after decoration.
     * @return Chainable API.
     */
    public Stylist startBrace(String after) {
        if (after != null) {
            this.afterStartBrace = after;
        }
        return this;
    }

    /**
     * Decorate end brace.
     * 
     * @param before A before decoration.
     * @param after An after decoration.
     * @return Chainable API.
     */
    public Stylist endBrace(String before, String after) {
        if (before != null) {
            this.beforeEndBrace = before;
        }

        if (after != null) {
            this.afterEndBrace = after;
        }
        return this;
    }

    /**
     * Decorate property name.
     * 
     * @param before A before decoration.
     * @param after An after decoration.
     * @return Chainable API.
     */
    public Stylist propertyName(String before, String after) {
        if (before != null) {
            this.beforePropertyName = before;
        }

        if (after != null) {
            this.afterPropertyName = after;
        }
        return this;
    }

    /**
     * Decorate property value.
     * 
     * @param before A before decoration.
     * @param after An after decoration.
     * @return Chainable API.
     */
    public Stylist propertyValue(String before, String after) {
        if (before != null) {
            this.beforePropertyValue = before;
        }

        if (after != null) {
            this.afterPropertyValue = after;
        }
        return this;
    }

    /**
     * Decorate start brace.
     * 
     * @param after An after decoration.
     * @return Chainable API.
     */
    public Stylist propertyLine(String after) {
        if (after != null) {
            this.afterPropertyLine = after;
        }
        return this;
    }

    /**
     * Format {@link CSSValue}.
     * 
     * @param value
     * @return
     */
    public Function<Color, String> color() {
        return color;
    }

    /**
     * Add the color writer.
     * 
     * @param writer
     * @return
     */
    public Stylist color(Function<Color, String> writer) {
        if (writer != null) {
            this.color = writer;
        }
        return this;
    }

    /**
     * Accept comment.
     * 
     * @param comment
     * @return
     */
    public Stylist comment(boolean comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Show empty style.
     * 
     * @param showEmptyStyle
     * @return
     */
    public Stylist showEmptyStyle(boolean showEmptyStyle) {
        this.showEmptyStyle = showEmptyStyle;
        return this;
    }

    /**
     * Add the post-processor.
     * 
     * @param processor
     * @return
     */
    public Stylist postProcessor(Consumer<Properties> processor) {
        if (processor != null) {
            posts.add(processor);
        }
        return this;
    }

    /**
     * Format the specified {@link StyleRule}.
     * 
     * @param rule A target to format.
     * @return A formatted text.
     */
    final String format(StyleRule rule) {
        StringBuilder builder = new StringBuilder();
        format(rule, builder);
        return builder.toString();
    }

    /**
     * Format the specified {@link StyleRule}.
     * 
     * @param rule A target to format.
     * @param appendable An output for the formatted text.
     */
    final void format(StyleRule rule, Appendable appendable) {
        if (showEmptyStyle == false && rule.properties.size() == 0) {
            for (StyleRule child : rule.children) {
                format(child, appendable);
            }
            return;
        }

        try {
            for (Consumer<Properties> processor : posts) {
                processor.accept(rule.properties);
            }

            appendable.append(beforeSelector)
                    .append(comment(rule.description))
                    .append(rule.selector.toString())
                    .append(afterSelector)
                    .append('{')
                    .append(afterStartBrace);

            for (int i = 0, size = rule.properties.size(); i < size; i++) {
                appendable.append(beforePropertyName)
                        .append(rule.properties.name(i).toString())
                        .append(afterPropertyName)
                        .append(':')
                        .append(beforePropertyValue)
                        .append(rule.properties.value(i).format(this))
                        .append(afterPropertyValue)
                        .append(';')
                        .append(afterPropertyLine);
            }
            appendable.append(beforeEndBrace).append('}').append(afterEndBrace);

            for (StyleRule child : rule.children) {
                format(child, appendable);
            }
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    /**
     * Write comment.
     * 
     * @param message
     */
    private String comment(String message) {
        if (comment && message != null && message.length() != 0) {
            return "/* " + message + " */";
        } else {
            return "";
        }
    }

    /**
     * Write out all styles in the specified style definition.
     * 
     * @param definitions The style definitions.
     * @return A stylesheet representation.
     */
    public final String format(Class... definitions) {
        return format(I.signal(definitions).flatIterable(Stylist::styles).toList());
    }

    /**
     * Write out the specified {@link Style}.
     * 
     * @param styles The style definitions.
     * @return A stylesheet representation.
     */
    public final String format(Style... styles) {
        return format(List.of(styles));
    }

    /**
     * Write out the specified {@link Style}.
     * 
     * @param styles The style definitions.
     * @return A stylesheet representation.
     */
    private String format(Iterable<Style> styles) {
        StringBuilder builder = new StringBuilder();

        I.signal(styles).map(StyleRule::create).sort(Comparator.naturalOrder()).to(e -> {
            format(e, builder);
        });

        return builder.toString();
    }

    /**
     * Write out all managed styles.
     * 
     * @return
     */
    public final String format() {
        return format(I.signal(id.keySet()).as(Style.class).toList());
    }

    /**
     * Write out all managed styles.
     * 
     * @param output A style output buffer.
     * @return
     */
    public final Path formatTo(String output) {
        return formatTo(Path.of(output));
    }

    /**
     * Write out all managed styles.
     * 
     * @param output A style output buffer.
     * @return
     */
    public final Path formatTo(Path output) {
        try {
            if (Files.notExists(output)) {
                Files.createDirectories(output.getParent());
            }

            formatTo(Files.newBufferedWriter(output));
        } catch (IOException e) {
            throw I.quiet(e);
        }
        return output;
    }

    /**
     * Write out all managed styles.
     * 
     * @param output A style output buffer.
     * @return
     */
    public final void formatTo(Appendable output) {
        try {
            output.append(format());
        } catch (IOException e) {
            throw I.quiet(e);
        } finally {
            I.quiet(output);
        }
    }

    /**
     * Default human-readable formatter.
     * 
     * @return
     */
    public static final Stylist pretty() {
        return new Stylist().comment(true)
                .selector("", " ")
                .startBrace("\r\n")
                .propertyName("\t", "")
                .propertyValue(" ", "")
                .propertyLine("\r\n")
                .endBrace("", "\r\n\r\n");
    }

    /**
     * Default computer-readable formatter.
     * 
     * @return
     */
    public static final Stylist compact() {
        return new Stylist();
    }

    /** 1byte charset. */
    private static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /** The start index. */
    private static final int base = chars.length;

    /** The managed locations. */
    private static final Map<Location, String> id = new HashMap();

    /** The id manager. */
    private static final AtomicInteger counter = new AtomicInteger();

    static {
        for (Class domain : I.findAs(StyleDSL.class)) {
            styles(domain).forEach(Style::name);
        }
    }

    /**
     * Compute identifier for the specified {@link Location}.
     * 
     * @param location A target location.
     * @return An identifier.
     */
    static String id(Location location) {
        return id.computeIfAbsent(location, key -> {
            int id = counter.getAndIncrement();

            if (id == 0) {
                return String.valueOf(chars[0]);
            }

            StringBuilder builder = new StringBuilder();

            while (id != 0) {
                builder.append(chars[id % base]);
                id /= base;
            }
            return builder.toString();
        });
    }

    /**
     * Collect all styles in the specified style definitions.
     * 
     * @param definition The style definitions
     * @return
     */
    private static List<Style> styles(Class definition) {
        List<Style> styles = new ArrayList();

        for (Field field : definition.getDeclaredFields()) {
            try {
                if (Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);

                    if (Style.class.isAssignableFrom(field.getType())) {
                        Style located = (Style) field.get(null);

                        if (located != null) {
                            styles.add(located);
                        }
                    } else if (ValueStyle.class.isAssignableFrom(field.getType())) {
                        Type type = field.getGenericType();

                        if (type instanceof ParameterizedType) {
                            ParameterizedType parameterized = (ParameterizedType) type;
                            Type[] args = parameterized.getActualTypeArguments();

                            if (args.length == 1 && args[0] instanceof Class) {
                                Class param = (Class) args[0];

                                if (param.isEnum()) {
                                    ValueStyle style = (ValueStyle) field.get(null);

                                    for (Object constant : param.getEnumConstants()) {
                                        styles.add(style.of(constant));
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                throw I.quiet(e);
            }
        }
        return styles;
    }
}
