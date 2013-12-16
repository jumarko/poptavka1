/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.demand;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Represents full detail of demandType. Serves for creating new
 * demandType or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class DemandTypeDetail implements IsSerializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private Long id;
    private String value;
    private String description;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public DemandTypeDetail() {
        // for serialization
    }

    /**************************************************************************/
    /* Getters & Setters                                                      */
    /**************************************************************************/
    /*
     * Demand type description pair.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * Demand type id pair.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*
     * Demand type value pair.
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {

        return "\nGlobal DemandType Detail Info:"
                + "\n    demandTypeId="
                + id + "\n     value="
                + value + "\n    Description="
                + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DemandTypeDetail other = (DemandTypeDetail) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}