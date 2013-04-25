/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.domain.enums.OrderType;
import java.util.ArrayList;
import java.util.List;

/**
 * Enable setting default or custom sort order.
 * Default order is usually used if table is loaded for the first time.
 * Custom order is used if user chooses some column to be ordered.
 *
 * @author Martin Slavkovsky
 */
public class SortDataHolder {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**
     * Used when loading table for the first time.
     */
    private List<SortPair> defaultSortOrder;
    /**
     * Used when user manually specify column to be ordered.
     */
    private List<SortPair> customSortOrder;
    /**
     * Represents all table columns. Even those that don't support sorting.
     * Those values can be represented by empty string, but column order must be correct.
     */
    private List<SortPair> sortColumns;
    private boolean useDefaultSortOrder;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public SortDataHolder(List<SortPair> defaultSortOrder, List<SortPair> sortColumns) {
        this.sortColumns = sortColumns;
        this.defaultSortOrder = defaultSortOrder;
        this.customSortOrder = new ArrayList<SortPair>();
        this.useDefaultSortOrder = true;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setCustomSortOrder(List<SortPair> customSortOrder) {
        this.customSortOrder = customSortOrder;
    }

    public void addCustomSortOrder(int idx, OrderType sortOrder) {
        SortPair custom = this.sortColumns.get(idx);
        custom.setColumnOrderType(sortOrder);
        this.customSortOrder.add(custom);
    }

    public void useDefaultSortOrder() {
        this.useDefaultSortOrder = true;
    }

    public void useCustomSortOrder() {
        this.useDefaultSortOrder = false;
    }

    /**
     * Clear custom sort order attribute.
     */
    public void clear() {
        this.customSortOrder.clear();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public ArrayList<SortPair> getSortOrder() {
        if (useDefaultSortOrder) {
            return new ArrayList<SortPair>(this.defaultSortOrder);
        } else {
            return new ArrayList<SortPair>(this.customSortOrder);
        }
    }
}