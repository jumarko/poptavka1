/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.search;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 * <B>Item</B> = string representing domain object attribute <B>Operation</B> =
 * see class constants <B>Value</B> = item value
 *
 * Class was design to use directly with Search in RPC methods. See
 * <I>filter</I> and <I>setFilters</I> methods in <I>AdminRPCServiceImpl</I>.
 *
 * FilterItems are grouped by <B>group<B> attribute represented by integer value.
 * So one FilterItem is a group of one element(filter).
 * If Group contains more elements(filters), operation OR is used among them.
 * If we have more groups, operation AND is used among them.
 * Summarize: Within a group is used OR operation, outside the group is used AND operation.
 *
 * @author Martin Slavkovsky
 */
public class FilterItem implements IsSerializable {

    /**************************************************************************/
    /* Enum - attribute's supported operations                                */
    /**************************************************************************/
    public enum Operation {

        OPERATION_FROM("=>"),
        OPERATION_TO("<="),
        OPERATION_EQUALS("="),
        OPERATION_IN(" IN "),
        OPERATION_LIKE("~");

        private String value;

        private Operation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Class attributes. **/
    private int group = 0;
    private ISortField item;
    private Operation operation;
    private String string;
    private Number number;
    private Date date;
    private Boolean bool;
    /** Constants. **/
    private static final String GROUP_INDICATOR = "^";

    /**************************************************************************/
    /* Initialziation                                                         */
    /**************************************************************************/
    public FilterItem() {
    }

    public FilterItem(ISortField item, Operation operation, Object value, int group) {
        this.item = item;
        this.operation = operation;
        if (value != null) {
            if (value instanceof String) {
                this.string = (String) value;
            }
            if (value instanceof Number) {
                this.number = (Number) value;
            }
            if (value instanceof Date) {
                this.date = (Date) value;
            }
            if (value instanceof Boolean) {
                this.bool = (Boolean) value;
            }
        }
        this.group = group;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public String getItem() {
        return item.getValue();
    }

    public String getFieldClass() {
        return item.getFieldClass();
    }

    public Operation getOperation() {
        return operation;
    }

    public Object getValue() {
        if (string != null) {
            return string;
        } else if (number != null) {
            return number;
        } else if (date != null) {
            return date;
        } else if (bool != null) {
            return bool;
        } else {
            return "";
        }
    }

    public int getGroup() {
        return group;
    }

    /**************************************************************************/
    /* toString & parse                                                       */
    /**************************************************************************/
    @Override
    public String toString() {
        StringBuilder infoStr = new StringBuilder();
        infoStr.append(toStringItem(item.getValue()));
        switch (operation) {
            case OPERATION_EQUALS:
                infoStr.append(Operation.OPERATION_EQUALS.getValue());
                break;
            case OPERATION_LIKE:
                infoStr.append(Operation.OPERATION_LIKE.getValue());
                break;
            case OPERATION_FROM:
                infoStr.append(Operation.OPERATION_FROM.getValue());
                break;
            case OPERATION_TO:
                infoStr.append(Operation.OPERATION_TO.getValue());
                break;
            case OPERATION_IN:
                infoStr.append(Operation.OPERATION_IN.getValue());
                break;
            default:
                break;
        }
        infoStr.append(getValue().toString());
        return infoStr.toString();
    }

    private String toStringItem(String item) {
        if (item.lastIndexOf(".") != -1) {
            return item.substring(item.lastIndexOf(".") + 1, item.length());
        }
        return item;
    }

    //TODO LATER Martin - history support for searching
//    public static FilterItem parseFilterItem(String filterItemString) {
//        int idx = -1;
//        int groupIdx = -1;
//        for (Operation operation : Operation.values()) {
//            idx = filterItemString.indexOf(operation.getValue());
//            groupIdx = filterItemString.indexOf(GROUP_INDICATOR);
//            return new FilterItem(
//                    filterItemString.substring(0, idx - 1),
//                    parseOperation(filterItemString.substring(idx, idx + 1)),
//                    parseValue(filterItemString.substring(idx + 2, groupIdx)),
//                    Integer.parseInt(filterItemString.substring(groupIdx + 1, filterItemString.length())));
//        }
//        return null;
//    }

    private static Operation parseOperation(String operationString) {
        return Operation.valueOf(operationString);
    }

    private static Object parseValue(String valueString) {
        if (valueString.matches("[0-9]+")) {
            return Integer.parseInt(valueString);
        } else if (valueString.matches("[a-zA-Z]+")) {
            return valueString;
        } else if (valueString.equals(Boolean.TRUE.toString())) {
            return Boolean.TRUE;
        } else if (valueString.equals(Boolean.FALSE.toString())) {
            return Boolean.FALSE;
        } else {
            return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).parse(valueString);
        }
    }
}
