/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.myListBox;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
public class MyListBoxData {

    private Map<Object, String> values;

    public MyListBoxData() {
        values = new HashMap<Object, String>();
    }

    public void insertItem(String item, Object value) {
        values.put(value, item);
    }

    public String getValue(int index) {
        return (String) values.values().toArray()[index];
    }

    public String getValue(String itemValue) {
        return values.get(itemValue);
    }

    public Map<Object, String> getValues() {
        return values;
    }
}
