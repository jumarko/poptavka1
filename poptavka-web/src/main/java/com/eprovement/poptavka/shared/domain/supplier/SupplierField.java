package com.eprovement.poptavka.shared.domain.supplier;

import com.eprovement.poptavka.shared.search.ISortField;

/**
 *
 * @author Ivan Vlcek
 */
public enum SupplierField implements ISortField {

    OVERALL_RATING("overalRating");
    private String value;

    private SupplierField(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}