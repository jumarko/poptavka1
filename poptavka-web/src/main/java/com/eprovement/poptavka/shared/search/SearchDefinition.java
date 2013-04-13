package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.SerializableHashMap;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.HashMap;

/**
 * Represent search definition sends to RPC services.
 *
 * @author Martin Slavkovsky
 */
public class SearchDefinition implements IsSerializable {

    private int firstResult = -1;
    private int maxResult = -1;
    private SearchModuleDataHolder filter = null;
    private SerializableHashMap<String, OrderType> orderColumns = null;

    public SearchDefinition() {
    }

    public SearchDefinition(SearchModuleDataHolder filter) {
        this.filter = filter;
    }

    public SearchDefinition(int start, int maxResult, SearchModuleDataHolder filter,
            SerializableHashMap<String, OrderType> orderColumns) {
        this.firstResult = start;
        this.maxResult = maxResult;
        this.filter = filter;
        this.orderColumns = orderColumns;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int start) {
        this.firstResult = start;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public SearchModuleDataHolder getFilter() {
        return filter;
    }

    public void setFilter(SearchModuleDataHolder filter) {
        this.filter = filter;
    }

    public HashMap<String, OrderType> getOrderColumns() {
        return orderColumns;
    }

    public void setOrderColumns(SerializableHashMap<String, OrderType> orderColumns) {
        this.orderColumns = orderColumns;
    }
}
