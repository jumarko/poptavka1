/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.demand;

import com.eprovement.poptavka.shared.search.ISortField;

/**
 *
 * @author Ivan Vlcek
 */
public enum DemandField implements ISortField {

    TITLE("title"),
    DESCRIPTION("description"),
    PRICE("price"),
    END_DATE("endDate"),
    VALID_TO("validTo"),
    MAX_OFFERS("maxSuppliers"),
    MIN_RATING("minRating"),
    DEMAND_TYPE("type.description"),
    CATEGORIES("categories"),
    LOCALITIES("localities"),
    DEMAND_STATUS("status"),
    CREATED("createdDate"),
    EXCLUDE_SUPPLIER("excludedSuppliers");
    private String value;

    private DemandField(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static DemandField toDemandField(String value) {
        for (DemandField field : DemandField.values()) {
            if (field.getValue().equals(value)) {
                return field;
            }
        }
        return null;
    }
}
