/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.main.common.search.dataHolders;

/**
 * <B>Item</B> = string representing domain object attribute
 * <B>Operation</B> = see class constants
 * <B>Value</B> = item value
 *
 * Class was design to use directly with Search in RPC methods.
 * See <I>filter</I> and <I>setFilters</I> methods in <I>AdminRPCServiceImpl</I>.
 *
 * @author Martin Slavkovsky
 */
public class FilterItem {
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
    private Object value = null;

    public FilterItem(String item, int operation, Object value) {
        this.item = item;
        this.operation = operation;
        this.value = value;
    }

    public String getItem() {
        return item;
    }

    public int getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }
}
