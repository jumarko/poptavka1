/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents sort pair. Specify column name and ordering type - how it should be sorted - ASC/DESC.
 * Prefix representing path to attributes can be specified too.
 *
 * @author Martin Slavkovsky
 */
public class SortPair implements IsSerializable {

    private String pathToAttributes;
    private String columnName;
    private OrderType columnOrderType;

    public SortPair() {
    }

    public SortPair(String columnName, OrderType columnOrderType) {
        this.columnName = columnName;
        this.columnOrderType = columnOrderType;
    }

    public String getColumnName() {
        return columnName;
    }

    public OrderType getColumnOrderType() {
        return columnOrderType;
    }

    public String getPathToAttributes() {
        return pathToAttributes;
    }

    public void setPathToAttributes(String pathToAttributes) {
        this.pathToAttributes = pathToAttributes;
    }
}
