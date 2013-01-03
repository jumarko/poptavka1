/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import java.io.Serializable;
import java.util.Date;

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
    private DemandField exposerDemandFieldType;
    //Original values
    private Date originalExposeDateType;
    private Integer originalEposeIntegerType;
    private String originalExposeStringType;
    //Changed values
    private Date exposeDateType;
    private Integer exposeIntegerType;
    private String exposeStringType;


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
//        } else if (value instanceof ArrayList) {
//            this.exposeListType = (ArrayList<Serializable>) value;
        }
    }

    public Object getOriginalValue() {
        if (originalExposeStringType != null) {
            return originalExposeStringType;
        } else if (originalExposeDateType != null) {
            return originalExposeDateType;
        } else if (originalEposeIntegerType != null) {
            return originalEposeIntegerType;
//        } else if (exposeListType != null) {
//            return exposeListType;
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
//        } else if (value instanceof ArrayList) {
//            this.exposeListType = (ArrayList<Serializable>) value;
        }
    }

    public Object getValue() {
        if (exposeStringType != null) {
            return exposeStringType;
        } else if (exposeDateType != null) {
            return exposeDateType;
        } else if (exposeIntegerType != null) {
            return exposeIntegerType;
//        } else if (exposeListType != null) {
//            return exposeListType;
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
    }
}