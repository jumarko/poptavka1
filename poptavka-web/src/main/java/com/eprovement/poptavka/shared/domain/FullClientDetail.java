package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import com.eprovement.poptavka.shared.search.ISortField;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Represents full detail of domain object <b>Client</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class FullClientDetail implements IsSerializable, TableDisplayRating {

    /** Enums. **/
    public enum ClientField implements ISortField {

        ID("id"),
        OVERALL_RATING("overalRating");

        private String value;

        private ClientField(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private Long clientId;
    /** BusinessUserDetail. **/
    private BusinessUserDetail userData;
    /** Class lists. **/
    private ArrayList<InvoiceDetail> invoices;
    private ArrayList<Long> supplierBlackListIds;
    private ArrayList<Long> demandsIds;
    /** Others. **/
    @Min(value = 0, message = "{userMinRating}", groups = SearchGroup.class)
    @Max(value = 100, message = "{userMaxRating}", groups = SearchGroup.class)
    private Integer overalRating;

    /**************************************************************************/
    /* Constuctors                                                            */
    /**************************************************************************/
    public FullClientDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters/Setters                                                        */
    /**************************************************************************/
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public ArrayList<InvoiceDetail> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<InvoiceDetail> invoices) {
        this.invoices = new ArrayList<InvoiceDetail>(invoices);
    }

    public ArrayList<Long> getSupplierBlackListIds() {
        return supplierBlackListIds;
    }

    public void setSupplierBlackListIds(Collection<Long> supplierBlackListIds) {
        this.supplierBlackListIds = new ArrayList<Long>(supplierBlackListIds);
    }

    public ArrayList<Long> getDemandsIds() {
        return demandsIds;
    }

    public void setDemandsIds(Collection<Long> demandsIds) {
        this.demandsIds = new ArrayList<Long>(demandsIds);
    }

    public BusinessUserDetail getUserData() {
        return userData;
    }

    public void setUserData(BusinessUserDetail userData) {
        this.userData = userData;
    }

    @Override
    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(Integer overalRating) {
        this.overalRating = overalRating;
    }

    /**************************************************************************/
    /* Others                                                                 */
    /**************************************************************************/
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("FullClientDetail{");
        str.append("clientId=" + clientId);
        str.append(", invoices=" + invoices);
        str.append(", supplierBlackListIds=" + supplierBlackListIds);
        str.append(", demandsIds=" + demandsIds);
        str.append(", businessUserData=" + userData.toString());
        str.append('}');
        return str.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FullClientDetail other = (FullClientDetail) obj;
        if (this.clientId != other.getClientId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.clientId ^ (this.clientId >>> 32));
        return hash;
    }
}
