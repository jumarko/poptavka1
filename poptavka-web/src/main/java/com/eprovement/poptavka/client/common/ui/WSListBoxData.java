/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides data for WSListBox.
 * Object holds data as <item key, item tex> pairs.
 *
 * @author Martin Slavkovsky
 */
public class WSListBoxData {

    /**
     * Represent pairs: Key - Item;
     */
    private Map<Integer, String> items;

    public WSListBoxData() {
        items = new HashMap<Integer, String>();
    }

    /**
     * Inserts item with its key.
     * @param item text
     * @param key - item's key
     */
    public void insertItem(String item, Integer key) {
        items.put(key, item);
    }

    /**
     * Get item by its index in map.
     * @param index of wanted item.
     * @return item under gived index
     */
    public String getItemByIndex(int index) {
        return (String) items.values().toArray()[index];
    }

    /**
     * Get item by its key representation in map.
     * @param key = key under item is stored
     * @return item under given key
     */
    public String getItemByKey(int key) {
        return items.get(key);
    }

    /**
     * Get all items.
     * @return map defining key-item pairs
     */
    public Map<Integer, String> getItems() {
        return items;
    }
}
