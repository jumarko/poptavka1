package com.eprovement.poptavka.shared.search;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;

/**
 * Represent search definition sends to RPC services.
 *
 * @author Martin Slavkovsky
 */
public class SearchDefinition implements IsSerializable {

    private int firstResult = -1;
    private int maxResult = -1;
    private SearchModuleDataHolder filter;
    private ArrayList<SortPair> sortPairs;

    public SearchDefinition() {
    }

    public SearchDefinition(SearchModuleDataHolder filter) {
        this.filter = filter;
    }

    public SearchDefinition(int start, int maxResult,
            SearchModuleDataHolder filter, ArrayList<SortPair> sortPairs) {
        this.firstResult = start;
        this.maxResult = maxResult;
        this.filter = filter;
        this.sortPairs = sortPairs;
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

    public ArrayList<SortPair> getSortOrder() {
        return sortPairs;
    }

    public void setSortOrder(ArrayList<SortPair> sort) {
        this.sortPairs = sort;
    }
}
