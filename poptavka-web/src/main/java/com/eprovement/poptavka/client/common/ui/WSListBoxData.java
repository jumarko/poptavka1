package com.eprovement.poptavka.client.common.ui;

import java.util.HashMap;
import java.util.Map;

/**
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

    public void insertItem(String item, Integer value) {
        items.put(value, item);
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
