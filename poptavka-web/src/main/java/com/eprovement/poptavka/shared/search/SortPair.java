/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
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

    private ISortField sortField;
    private String columnName;
    private OrderType columnOrderType;

    public SortPair() {
        //for serialization
    }

    public SortPair(ISortField sortField, String columnName, OrderType columnOrderType) {
        this.sortField = sortField;
        this.columnName = columnName;
        this.columnOrderType = columnOrderType;
    }

    public static SortPair asc(ISortField fieldEnum) {
        return new SortPair(fieldEnum, fieldEnum.getValue(), OrderType.ASC);
    }

    public static SortPair desc(ISortField fieldEnum) {
        return new SortPair(fieldEnum, fieldEnum.getValue(), OrderType.DESC);
    }

    public String getColumnName() {
        return columnName;
    }

    public OrderType getColumnOrderType() {
        return columnOrderType;
    }

    public void setColumnOrderType(OrderType columnOrderType) {
        this.columnOrderType = columnOrderType;
    }

    public Class getFieldClass() {
        return sortField.getClass();
    }
}
