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

    private int start;
    private int maxResult;
    private SearchModuleDataHolder filter;
    private Map<String, OrderType> orderColumns;

    public SearchDefinition() {
    }

    public SearchDefinition(int start, int maxResult, SearchModuleDataHolder filter,
            Map<String, OrderType> orderColumns) {
        this.start = start;
        this.maxResult = maxResult;
        this.filter = filter;
        this.orderColumns = orderColumns;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
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
