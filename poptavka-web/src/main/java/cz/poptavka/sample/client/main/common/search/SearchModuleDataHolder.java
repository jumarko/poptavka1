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
public class SearchModuleDataHolder implements Serializable {

    /** COMMON - full text search **/
//    private String type = null;
    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
    /** HOMEDEMANDS **/
    private String demandTitle = "";
    private CategoryDetail demandCategory = null;
    private LocalityDetail demandLocality = null;
    private int priceFrom = -1;
    private int priceTo = -1;
    private String demandType = "";
    private Date endDate = null;
    //0 - today
    //1 - yesterday
    //2 - last week
    //3 - last month
    //4 - no limit
    private int creationDate = -1;

    public int getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(int creationDate) {
        this.creationDate = creationDate;
    }

    public CategoryDetail getDemandCategory() {
        return demandCategory;
    }

    public void setDemandCategory(CategoryDetail demandCategory) {
        this.demandCategory = demandCategory;
    }

    public LocalityDetail getDemandLocality() {
        return demandLocality;
    }

    public void setDemandLocality(LocalityDetail demandLocality) {
        this.demandLocality = demandLocality;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
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

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
    /** HOMESUPPLIERS **/
    private String supplierName = "";
    private CategoryDetail supplierCategory = null;
    private LocalityDetail supplierLocality = null;
    private int ratingFrom = -1;
    private int ratingTo = -1;
    private String supplierDescription = "";

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
    /** POTENTIALDEMANDMESSAGES **/
    /** MESSAGES - INBOX **/
    /** MESSAGES - SENT **/
    /** MESSAGES - DELETED **/
    /** ADMINACCESSROLE **/
    /** ADMINCLIENT **/
    /** ADMINDEMAND **/
    /** ADMINEMAILACTIVATION **/
    /** ADMININVOICE **/
    /** ADMINMESSAGES **/
    /** ADMINOFFER **/
    /** ADMINPAYMETNDETAIL **/
    /** ADMINPERMISSION **/
    /** ADMINPREFERENCES **/
    /** ADMINPROBLEMS **/
    /** ADMINSUPPLIERS **/
}
