package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents full detail of domain object <b>Client</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class FullClientDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long clientId;
    /** BusinessUserDetail. **/
    private BusinessUserDetail userData;
    /** Class lists. **/
    private ArrayList<InvoiceDetail> invoices;
    private ArrayList<Long> supplierBlackListIds;
    private ArrayList<Long> demandsIds;

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
