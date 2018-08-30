/*
 * Copyright (C) 2018 stylist Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stylist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @version 2018/08/30 18:17:46
 */
public class DualList<K, V> {

    /** The key list. */
    private final ArrayList<K> keys = new ArrayList();

    /** The value list. */
    private final ArrayList<V> values = new ArrayList();

    /**
     * <p>
     * Retrieve the first matched value by the specified key.
     * </p>
     * 
     * @param key A key.
     * @return A first matched value.
     */
    public Optional<V> get(K key) {
        int index = keys.indexOf(key);

        if (index == -1) {
            return Optional.empty();
        } else {
            return Optional.of(values.get(index));
        }
    }

    /**
     * <p>
     * Retrieve all matched values by the specified key.
     * </p>
     * 
     * @param key A key.
     * @return A list of all matched values.
     */
    public List<V> getAll(K key) {
        List<V> matched = new ArrayList();

        for (int i = 0, length = keys.size(); i < length; i++) {
            if (keys.get(i).equals(key)) {
                matched.add(values.get(i));
            }
        }
        return matched;
    }

    /**
     * <p>
     * Append the specified value by the specified key.
     * </p>
     * 
     * @param key A key.
     * @param value A value.
     * @return An updated {@link DualList}.
     */
    public DualList<K, V> add(K key, V value) {
        keys.add(key);
        values.add(value);

        return this;
    }

    /**
     * <p>
     * Update the specified value by the specified key.
     * </p>
     * 
     * @param key A key.
     * @param value A value to update.
     * @return An updated {@link DualList}.
     */
    public DualList<K, V> set(K key, V value) {
        int index = keys.indexOf(key);

        if (index == -1) {
            keys.add(key);
            values.add(value);
        } else {
            values.set(index, value);
        }
        return this;
    }

    /**
     * <p>
     * Remove the first matched item by the specified key.
     * </p>
     * 
     * @param key A key to remove.
     * @return An updated {@link DualList}.
     */
    public DualList<K, V> remove(K key) {
        int index = keys.indexOf(key);

        if (index != -1) {
            keys.remove(index);
            values.remove(index);
        }
        return this;
    }

    /**
     * <p>
     * Remove the first matched item by the specified key.
     * </p>
     * 
     * @param key A key to remove.
     * @return An updated {@link DualList}.
     */
    public DualList<K, V> removeAll(K key) {
        for (int i = keys.size() - 1; 0 <= i; i--) {
            if (keys.get(i).equals(key)) {
                keys.remove(i);
                values.remove(i);
            }
        }
        return this;
    }

    /**
     * <p>
     * Remove all items.
     * </p>
     */
    public void clear() {
        keys.clear();
        values.clear();
    }

    /**
     * <p>
     * Compute the current item size.
     * </p>
     * 
     * @return A current item size.
     */
    public int size() {
        return keys.size();
    }

    /**
     * <p>
     * Retrieve the key by the specified index.
     * </p>
     * 
     * @param index A index to find.
     * @return A indexed key.
     */
    public K key(int index) {
        return keys.get(index);
    }

    /**
     * <p>
     * Retrieve the index by the specified key.
     * </p>
     * 
     * @param key A key to find.
     * @return A index for the specified key.
     */
    public int key(K key) {
        return keys.indexOf(key);
    }

    /**
     * <p>
     * Retrieve key list.
     * </p>
     * 
     * @return
     */
    public List<K> keys() {
        return keys;
    }

    /**
     * <p>
     * Retrieve the value by the specified index.
     * </p>
     * 
     * @param index A index to find.
     * @return A indexed value.
     */
    public V value(int index) {
        return values.get(index);
    }

    /**
     * <p>
     * Retrieve the index by the specified value.
     * </p>
     * 
     * @param value A value to find.
     * @return A index for the specified value.
     */
    public int value(V value) {
        return values.indexOf(value);
    }

    /**
     * <p>
     * Retrieve value list.
     * </p>
     * 
     * @return
     */
    public List<V> values() {
        return values;
    }

    /**
     * <p>
     * Test whether the specified key and value are stored or not
     * </p>
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean contains(String key, String value) {
        int index = keys.indexOf(key);

        if (index == -1) {
            return false;
        }
        return values.get(index).equals(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("DualList[");

        for (int i = 0; i < keys.size(); i++) {
            builder.append(keys.get(i)).append("=").append(values.get(i));

            if (i != keys.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");

        return builder.toString();
    }
}
