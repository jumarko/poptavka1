package com.eprovement.poptavka.shared.domain.supplier;

import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayRating;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.user.client.rpc.IsSerializable;


import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class FullSupplierDetail implements IsSerializable, TableDisplayRating {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Enums. **/
    public enum SupplierField {

        OVERALL_RATING("overalRating");

        public static final String SEARCH_CLASS = "supplier";
        private String value;

        private SupplierField(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private long supplierId;
    /** BusinessUserDetail. **/
    private BusinessUserDetail userData;
    /** Class lists. **/
    @Valid
    @Size(min = 1)
    private ArrayList<LocalityDetail> localities;
    @Valid
    @Size(min = 1)
    private ArrayList<CategoryDetail> categories;
    @Valid
    @Size(min = 1)
    private ArrayList<ServiceDetail> services = new ArrayList<ServiceDetail>();
    /** Others. **/
    private boolean certified = false;
    @Min(value = 0, message = "{userMinRating}", groups = SearchGroup.class)
    @Max(value = 100, message = "{userMaxRating}", groups = SearchGroup.class)
    private int overalRating = -1;

    /**************************************************************************/
    /* Constuctors                                                            */
    /**************************************************************************/
    public FullSupplierDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters/Setters                                                        */
    /**************************************************************************/
    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(Collection<LocalityDetail> localities) {
        this.localities = new ArrayList<LocalityDetail>(localities);
    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public void setCategories(Collection<CategoryDetail> categories) {
        this.categories = new ArrayList<CategoryDetail>(categories);
    }

    public ArrayList<ServiceDetail> getServices() {
        return services;
    }

    public void setServices(Collection<ServiceDetail> services) {
        this.services = new ArrayList<ServiceDetail>(services);
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public BusinessUserDetail getUserData() {
        return userData;
    }

    public void setUserData(BusinessUserDetail userData) {
        this.userData = userData;
    }

    @Override
    public int getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(int overallRating) {
        this.overalRating = overallRating;
    }

    /**************************************************************************/
    /* Other                                                                  */
    /**************************************************************************/
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("FullSupplierDetail{");
        str.append("supplierId=" + supplierId);
        str.append(", localities=" + localities);
        str.append(", categories=" + categories);
        str.append(", services=" + services);
        str.append(", certified=" + certified);
        str.append(", certified=" + certified);
        str.append(", businessData=" + userData.toString());
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
        final FullSupplierDetail other = (FullSupplierDetail) obj;
        if (this.supplierId != other.supplierId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.supplierId ^ (this.supplierId >>> 32));
        return hash;
    }
}
