package com.eprovement.poptavka.shared.domain.supplier;

import com.eprovement.poptavka.client.user.widget.grid.columns.AddressColumn.TableDisplayAddress;
import com.eprovement.poptavka.client.user.widget.grid.columns.LogoColumn.TableDisplayLogo;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.LesserBusinessUserDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;


import java.util.ArrayList;
import java.util.Collection;

public class LesserSupplierDetail implements IsSerializable, TableDisplayRating,
    TableDisplayAddress, TableDisplayLogo {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/

    private long supplierId;
    private LesserBusinessUserDetail userData = new LesserBusinessUserDetail();
    private ArrayList<ICatLocDetail> categories = new ArrayList<ICatLocDetail>();
    private boolean certified = false;
    private Integer overalRating;

    /** Key provider **/
    public static final ProvidesKey<LesserSupplierDetail> KEY_PROVIDER = new ProvidesKey<LesserSupplierDetail>() {
        @Override
        public Object getKey(LesserSupplierDetail item) {
            return item == null ? null : item.getSupplierId();
        }
    };

    /**************************************************************************/
    /* Constuctors                                                            */
    /**************************************************************************/
    public LesserSupplierDetail() {
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

    public ArrayList<ICatLocDetail> getCategories() {
        return categories;
    }

    public void setCategories(Collection<ICatLocDetail> categories) {
        this.categories = new ArrayList<ICatLocDetail>(categories);
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public LesserBusinessUserDetail getUserData() {
        return userData;
    }

    public void setUserData(LesserBusinessUserDetail userData) {
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
        StringBuilder str = new StringBuilder("LesserSupplierDetail{");
        str.append("supplierId=" + supplierId);
        str.append(", categories=" + categories);
        str.append(", certified=" + certified);
        str.append(", overalRating=" + overalRating);
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
        final LesserSupplierDetail other = (LesserSupplierDetail) obj;
        if (this.supplierId != other.supplierId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 13 * hash + (int) (this.supplierId ^ (this.supplierId >>> 32));
        return hash;
    }
}
