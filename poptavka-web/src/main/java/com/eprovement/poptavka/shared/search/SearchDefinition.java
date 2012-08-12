package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.domain.enums.OrderType;
import java.io.Serializable;
import java.util.Map;

/**
 * Represent search definition sends to RPC services.
 *
 * @author Martin Slavkovsky
 */
public class SearchDefinition implements Serializable {

    private int firstResult = -1;
    private int maxResult = -1;
    private SearchModuleDataHolder filter = null;
    private Map<String, OrderType> orderColumns = null;

    public SearchDefinition() {
    }

    public SearchDefinition(SearchModuleDataHolder filter) {
        this.filter = filter;
    }

    public SearchDefinition(int start, int maxResult, SearchModuleDataHolder filter,
            Map<String, OrderType> orderColumns) {
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

    public Map<String, OrderType> getOrderColumns() {
        return orderColumns;
    }

    public void setOrderColumns(Map<String, OrderType> orderColumns) {
        this.orderColumns = orderColumns;
    }
}
