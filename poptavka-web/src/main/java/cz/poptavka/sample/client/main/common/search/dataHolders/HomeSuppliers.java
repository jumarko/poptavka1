package cz.poptavka.sample.client.main.common.search.dataHolders;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import java.io.Serializable;

/** HOMESUPPLIERS **/
public class HomeSuppliers implements Serializable {

    private String supplierName = null;
    private CategoryDetail supplierCategory = null;
    private LocalityDetail supplierLocality = null;
    private Integer ratingFrom = null;
    private Integer ratingTo = null;
    private String supplierDescription = null;

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