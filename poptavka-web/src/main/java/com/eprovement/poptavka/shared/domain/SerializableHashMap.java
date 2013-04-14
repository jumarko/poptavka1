/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.HashMap;

/**
 *
 * @author Martin Slavkovsky
 */
public class SerializableHashMap<K, V> extends HashMap<K, V> implements IsSerializable {

    public SerializableHashMap() {
        //nothing by default
    }
}
