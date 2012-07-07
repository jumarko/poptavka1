/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.search;

import java.io.Serializable;
import java.util.Date;

/**
 * <B>Item</B> = string representing domain object attribute <B>Operation</B> =
 * see class constants <B>Value</B> = item value
 *
 * Class was design to use directly with Search in RPC methods. See
 * <I>filter</I> and <I>setFilters</I> methods in <I>AdminRPCServiceImpl</I>.
 *
 * @author Martin Slavkovsky
 */
public class FilterItem implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 800019522231189460L;
    /*
     * OPERATIONS
     */
    public static final int OPERATION_FROM = 0;
    public static final int OPERATION_TO = 1;
    public static final int OPERATION_EQUALS = 2;
    public static final int OPERATION_IN = 3;
    public static final int OPERATION_LIKE = 4;
    //
    private String item = null;
    private int operation = -1;
    //
    private String string;
    private Integer integer;
    private Date date;
    private Boolean bool;

    public FilterItem() {
    }

    public FilterItem(String item, int operation, Object value) {
        this.item = item;
        this.operation = operation;
        if (value != null) {
            if (value instanceof String) {
                this.string = (String) value;
            }
            if (value instanceof Integer) {
                this.integer = (Integer) value;
            }
            if (value instanceof Date) {
                this.date = (Date) value;
            }
            if (value instanceof Boolean) {
                this.bool = (Boolean) value;
            }
        }
    }

    public String getItem() {
        return item;
    }

    public int getOperation() {
        return operation;
    }

    public Object getValue() {
        if (string != null) {
            return string;
        } else if (integer != null) {
            return integer;
        } else if (date != null) {
            return date;
        } else if (bool != null) {
            return bool;
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder infoStr = new StringBuilder();
        infoStr.append(item);
        switch (operation) {
            case OPERATION_EQUALS:
                infoStr.append("=");
                break;
            case OPERATION_LIKE:
                infoStr.append("~");
                break;
            case OPERATION_FROM:
                infoStr.append(">");
                break;
            case OPERATION_TO:
                infoStr.append("<");
                break;
            case OPERATION_IN:
                infoStr.append(" in ");
                break;
            default:
                break;
        }
        infoStr.append(getValue().toString());
        return infoStr.toString();
    }
}
