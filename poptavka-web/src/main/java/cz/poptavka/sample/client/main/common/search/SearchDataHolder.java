/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.main.common.search;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Mato
 */
public class SearchDataHolder implements Serializable {

    private Boolean additional = false;
    /** SIMPLE DATA **/
    private String text;
    //0 - in demnads
    //1 - in suppliers
    private int where;
    private CategoryDetail category = null;
    private LocalityDetail locality = null;
    /** ADDITIONAL DATA **/
    //Demand additional info
    private int priceFrom;
    private int priceTo;
    private String demandType;
    private Date endDate;
    //0 - today
    //1 - yesterday
    //2 - last week
    //3 - last month
    //4 - no limit
    private int creationDate;
    //Supplier additional info
    private int ratingFrom;
    private int ratingTo;
    private String supplierDescription;

    public SearchDataHolder() {
        text = "";
        where = -1;
        category = null;
        locality = null;
        /** ADDITIONAL DATA **/
        //Demand additional info
        priceFrom = -1;
        priceTo = -1;
        demandType = null;
        endDate = null;
        creationDate = -1;
        //Supplier additional info
        ratingFrom = -1;
        ratingTo = -1;
        supplierDescription = "";
    }

    public Boolean isAdditionalInfo() {
        return additional;
    }

    public void setAddition(Boolean advanced) {
        this.additional = advanced;
    }

    public CategoryDetail getCategory() {
        return category;
    }

    public void setCategory(CategoryDetail category) {
        this.category = category;
    }

    public LocalityDetail getLocality() {
        return locality;
    }

    public void setLocality(LocalityDetail locality) {
        this.locality = locality;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    public int getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(int creationDate) {
        this.creationDate = creationDate;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date finnishDate) {
        this.endDate = finnishDate;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }

    public int getRatingFrom() {
        return ratingFrom;
    }

    public void setRatingFrom(int ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public int getRatingTo() {
        return ratingTo;
    }

    public void setRatingTo(int ratingTo) {
        this.ratingTo = ratingTo;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
    }
}
