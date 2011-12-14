package cz.poptavka.sample.client.main.common.search.dataHolders;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import java.io.Serializable;

/** ADMINSUPPLIERS **/
public class AdminSuppliers implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private String type = null;
    private Boolean certified = null;
    private String verified = null;
    private String supplierName = null;
    private CategoryDetail supplierCategory = null;
    private LocalityDetail supplierLocality = null;
    private Integer ratingFrom = null;
    private Integer ratingTo = null;
    private String supplierDescription = null;

    public Boolean getCertified() {
        return certified;
    }

    public void setCertified(Boolean certified) {
        this.certified = certified;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public Integer getRatingFrom() {
        return ratingFrom;
    }

    public void setRatingFrom(Integer ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public Integer getRatingTo() {
        return ratingTo;
    }

    public void setRatingTo(Integer ratingTo) {
        this.ratingTo = ratingTo;
    }

    public CategoryDetail getSupplierCategory() {
        return supplierCategory;
    }

    public void setSupplierCategory(CategoryDetail supplierCategory) {
        this.supplierCategory = supplierCategory;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
    }

    public LocalityDetail getSupplierLocality() {
        return supplierLocality;
    }

    public void setSupplierLocality(LocalityDetail supplierLocality) {
        this.supplierLocality = supplierLocality;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
