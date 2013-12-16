/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class ChangeDetail implements IsSerializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //Object field's enums
    private String field;
    //Original values
    private Date dateValue;
    private Number integerValue;
    private String stringValue;
    private Boolean booleanValue;
    private ArrayList<ICatLocDetail> listValue;
    //Changed values
    private Date originalDateValue;
    private Number originalIntegerValue;
    private String originalStringValue;
    private Boolean originalBooleanValue;
    private ArrayList<ICatLocDetail> originalListValue;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public ChangeDetail() {
    }

    public ChangeDetail(String field) {
        this.field = field;
    }

    /**************************************************************************/
    /* Getter/Setter -- original values                                       */
    /**************************************************************************/
    public void setOriginalValue(Object value) {
        if (value instanceof String) {
            this.originalStringValue = (String) value;
        } else if (value instanceof Date) {
            this.originalDateValue = (Date) value;
        } else if (value instanceof Number) {
            this.originalIntegerValue = (Number) value;
        } else if (value instanceof Boolean) {
            this.originalBooleanValue = (Boolean) value;
        } else if (value instanceof List) {
            this.originalListValue = new ArrayList<ICatLocDetail>(
                    (List<ICatLocDetail>) value); //make a copy
        }
    }

    public Object getOriginalValue() {
        if (originalStringValue != null) {
            return originalStringValue;
        } else if (originalDateValue != null) {
            return originalDateValue;
        } else if (originalIntegerValue != null) {
            return originalIntegerValue;
        } else if (originalBooleanValue != null) {
            return originalBooleanValue;
        } else if (originalListValue != null) {
            return originalListValue;
        } else {
            return null;
        }
    }

    /**************************************************************************/
    /* Getter/Setter -- changed values                                       */
    /**************************************************************************/
    public void setValue(Object value) {
        if (value instanceof String) {
            this.stringValue = (String) value;
        } else if (value instanceof Date) {
            this.dateValue = (Date) value;
        } else if (value instanceof Number) {
            this.integerValue = (Number) value;
        } else if (value instanceof Boolean) {
            this.booleanValue = (Boolean) value;
        } else if (value instanceof List) {
            this.listValue = new ArrayList<ICatLocDetail>(
                    (List<ICatLocDetail>) value); //make a copy
        }
    }

    public Object getValue() {
        if (stringValue != null) {
            return stringValue;
        } else if (dateValue != null) {
            return dateValue;
        } else if (integerValue != null) {
            return integerValue;
        } else if (booleanValue != null) {
            return booleanValue;
        } else if (listValue != null) {
            return listValue;
        } else {
            return null;
        }
    }

    /**************************************************************************/
    /* Getter/Setter -- field enums                                           */
    /**************************************************************************/
    public String getField() {
        return field;
    }

    /**************************************************************************/
    /* Other methods                                                          */
    /**************************************************************************/
    public void commit() {
        originalDateValue = dateValue;
        originalIntegerValue = integerValue;
        originalStringValue = stringValue;
        originalBooleanValue = booleanValue;
        originalListValue = listValue;
    }

    public void revert() {
        dateValue = originalDateValue;
        integerValue = originalIntegerValue;
        stringValue = originalStringValue;
        booleanValue = originalBooleanValue;
        listValue = originalListValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.field != null ? this.field.hashCode() : 0);
        hash = 97 * hash + (this.originalDateValue != null ? this.originalDateValue.hashCode() : 0);
        hash = 97 * hash + (this.originalIntegerValue != null ? this.originalIntegerValue.hashCode() : 0);
        hash = 97 * hash + (this.originalStringValue != null ? this.originalStringValue.hashCode() : 0);
        hash = 97 * hash + (this.originalBooleanValue != null ? this.originalBooleanValue.hashCode() : 0);
        hash = 97 * hash + (this.originalListValue != null ? this.originalListValue.hashCode() : 0);
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
        if (this.field != null && !this.field.equals(other.field)) {
            return false;
        }
        return true;
    }
}
