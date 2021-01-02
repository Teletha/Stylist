/*
 * Copyright (C) 2021 stylist Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stylist.property;

import stylist.PropertyDefinition;

/**
 * @version 2018/08/30 18:25:12
 */
public class Content extends PropertyDefinition<Content> {

    /**
     * <p>
     * Text content.
     * </p>
     * 
     * @param value
     * @return
     */
    public Content text(String value) {
        return value("'" + value + "'");
    }

    /**
     * <p>
     * The pseudo-element is not generated.
     * </p>
     * 
     * @return
     */
    public Content none() {
        return value("none");
    }

    /**
     * <p>
     * Returns the value of the element's attribute X as a string. If there is no attribute X, an
     * empty string is returned. The case-sensitivity of attribute names depends on the document
     * language.
     * </p>
     * 
     * @param name
     * @return
     */
    public Content attr(String name) {
        return value("attr(" + name + ")");
    }

    /**
     * <p>
     * These values are replaced by the appropriate string from the quotes property.
     * </p>
     * 
     * @return
     */
    public Content openQuote() {
        return value("open-quote");
    }

    /**
     * <p>
     * These values are replaced by the appropriate string from the quotes property.
     * </p>
     * 
     * @return
     */
    public Content closeQuote() {
        return value("close-quote");
    }
}