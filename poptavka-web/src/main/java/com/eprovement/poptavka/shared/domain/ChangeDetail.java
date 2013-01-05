/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class ChangeDetail implements Serializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final long serialVersionUID = -341982467222084345L;
    //Object field's enums
    private DemandField exposerDemandFieldType = null;
    //Original values
    private Date originalExposeDateType = null;
    private Integer originalEposeIntegerType = null;
    private String originalExposeStringType = null;
    private ArrayList<IListDetailObject> originalExposeListType = null;
    //Changed values
    private Date exposeDateType = null;
    private Integer exposeIntegerType = null;
    private String exposeStringType = null;
    private ArrayList<IListDetailObject> exposeListType = null;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public ChangeDetail() {
    }

    public ChangeDetail(Object field) {
        if (field instanceof DemandField) {
            this.exposerDemandFieldType = (DemandField) field;
        }
    }

    /**************************************************************************/
    /* Getter/Setter -- original values                                       */
    /**************************************************************************/
    public void setOriginalValue(Object value) {
        if (value instanceof String) {
            this.originalExposeStringType = (String) value;
        } else if (value instanceof Date) {
            this.originalExposeDateType = (Date) value;
        } else if (value instanceof Integer) {
            this.originalEposeIntegerType = (Integer) value;
        } else if (value instanceof List) {
            this.originalExposeListType = new ArrayList<IListDetailObject>(
                    (List<IListDetailObject>) value); //make a copy
        }
    }

    public Object getOriginalValue() {
        if (originalExposeStringType != null) {
            return originalExposeStringType;
        } else if (originalExposeDateType != null) {
            return originalExposeDateType;
        } else if (originalEposeIntegerType != null) {
            return originalEposeIntegerType;
        } else if (originalExposeListType != null) {
            return originalExposeListType;
        } else {
            return null;
        }
    }

    /**************************************************************************/
    /* Getter/Setter -- changed values                                       */
    /**************************************************************************/
    public void setValue(Object value) {
        if (value instanceof String) {
            this.exposeStringType = (String) value;
        } else if (value instanceof Date) {
            this.exposeDateType = (Date) value;
        } else if (value instanceof Integer) {
            this.exposeIntegerType = (Integer) value;
        } else if (value instanceof List) {
            this.exposeListType = new ArrayList<IListDetailObject>(
                    (List<IListDetailObject>) value); //make a copy
        }
    }

    public Object getValue() {
        if (exposeStringType != null) {
            return exposeStringType;
        } else if (exposeDateType != null) {
            return exposeDateType;
        } else if (exposeIntegerType != null) {
            return exposeIntegerType;
        } else if (exposeListType != null) {
            return exposeListType;
        } else {
            return null;
        }
    }

    /**************************************************************************/
    /* Getter/Setter -- field enums                                           */
    /**************************************************************************/
    public Object getField() {
        return exposerDemandFieldType;
    }

    /**************************************************************************/
    /* Other methods                                                          */
    /**************************************************************************/
    public void revert() {
        exposeDateType = originalExposeDateType;
        exposeIntegerType = originalEposeIntegerType;
        exposeStringType = originalExposeStringType;
        exposeListType = originalExposeListType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.exposerDemandFieldType != null ? this.exposerDemandFieldType.hashCode() : 0);
        hash = 97 * hash + (this.originalExposeDateType != null ? this.originalExposeDateType.hashCode() : 0);
        hash = 97 * hash + (this.originalEposeIntegerType != null ? this.originalEposeIntegerType.hashCode() : 0);
        hash = 97 * hash + (this.originalExposeStringType != null ? this.originalExposeStringType.hashCode() : 0);
        hash = 97 * hash + (this.originalExposeListType != null ? this.originalExposeListType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChangeDetail other = (ChangeDetail) obj;
        if (this.exposerDemandFieldType != null && !this.exposerDemandFieldType.equals(other.exposerDemandFieldType)) {
            return false;
        }
        return true;
    }
}
