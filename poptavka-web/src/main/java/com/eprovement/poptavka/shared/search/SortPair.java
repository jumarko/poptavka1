/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.FullClientDetail.ClientField;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail.OfferField;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.message.MessageDetail.MessageField;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail.UserMessageField;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail.SupplierField;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents sort pair. Specify column name and ordering type - how it should be sorted - ASC/DESC.
 * Prefix representing path to attributes can be specified too.
 *
 * @author Martin Slavkovsky
 */
public class SortPair implements IsSerializable {

    private String searchClass;
    private String columnName;
    private OrderType columnOrderType;

    public SortPair() {
    }

    public SortPair(String columnName, OrderType columnOrderType, String searchClass) {
        this.columnName = columnName;
        this.columnOrderType = columnOrderType;
        this.searchClass = searchClass;
    }

    public SortPair(DemandField demandField) {
        this.columnName = demandField.getValue();
        this.columnOrderType = OrderType.DESC;
        this.searchClass = DemandField.SEARCH_CLASS;
    }

    public SortPair(UserField userField) {
        this.columnName = userField.getValue();
        this.columnOrderType = OrderType.DESC;
        this.searchClass = UserField.SEARCH_CLASS;
    }

    public SortPair(ClientField clientField) {
        this.columnName = clientField.getValue();
        this.columnOrderType = OrderType.DESC;
        this.searchClass = ClientField.SEARCH_CLASS;
    }

    public SortPair(SupplierField supplierField) {
        this.columnName = supplierField.getValue();
        this.columnOrderType = OrderType.DESC;
        this.searchClass = SupplierField.SEARCH_CLASS;
    }

    public SortPair(MessageField messageField) {
        this.columnName = messageField.getValue();
        this.columnOrderType = OrderType.DESC;
        this.searchClass = MessageField.SEARCH_CLASS;
    }

    public SortPair(UserMessageField userMessageField) {
        this.columnName = userMessageField.getValue();
        this.columnOrderType = OrderType.DESC;
        this.searchClass = UserMessageField.SEARCH_CLASS;
    }

    public SortPair(OfferField offerField) {
        this.columnName = offerField.getValue();
        this.columnOrderType = OrderType.DESC;
        this.searchClass = OfferField.SEARCH_CLASS;
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

    public String getSearchClass() {
        return searchClass;
    }
}
