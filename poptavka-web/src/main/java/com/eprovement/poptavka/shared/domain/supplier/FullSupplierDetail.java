package com.eprovement.poptavka.shared.domain.supplier;

import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.client.user.widget.grid.columns.AddressColumn.TableDisplayAddress;
import com.eprovement.poptavka.client.user.widget.grid.columns.LogoColumn.TableDisplayLogo;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;


import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class FullSupplierDetail implements IsSerializable, TableDisplayRating,
    TableDisplayAddress, TableDisplayLogo {

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
    private BusinessUserDetail userData = new BusinessUserDetail();
    /** Class lists. **/
    @Valid
    @Size(min = 1)
    private ArrayList<ICatLocDetail> localities = new ArrayList<ICatLocDetail>();
    @Valid
    @Size(min = 1)
    private ArrayList<ICatLocDetail> categories = new ArrayList<ICatLocDetail>();
    @Valid
    @Size(min = 1)
    private ArrayList<ServiceDetail> services = new ArrayList<ServiceDetail>();
    /** Others. **/
    private boolean certified = false;
    @Min(value = 0, message = "{ratingMin}", groups = SearchGroup.class)
    @Max(value = 100, message = "{ratingMax}", groups = SearchGroup.class)
    private Integer overalRating;
    /** Key provider **/
    public static final ProvidesKey<FullSupplierDetail> KEY_PROVIDER = new ProvidesKey<FullSupplierDetail>() {
        @Override
        public Object getKey(FullSupplierDetail item) {
            return item == null ? null : item.getSupplierId();
        }
    };

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

    public ArrayList<ICatLocDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(Collection<ICatLocDetail> localities) {
        this.localities = new ArrayList<ICatLocDetail>(localities);
    }

    public ArrayList<ICatLocDetail> getCategories() {
        return categories;
    }

    public void setCategories(Collection<ICatLocDetail> categories) {
        this.categories = new ArrayList<ICatLocDetail>(categories);
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
    public ArrayList<AddressDetail> getAddresses() {
        return userData.getAddresses();
    }

    @Override
    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(Integer overalRating) {
        this.overalRating = overalRating;
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
