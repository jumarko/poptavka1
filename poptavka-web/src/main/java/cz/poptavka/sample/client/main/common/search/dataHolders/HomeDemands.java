
package cz.poptavka.sample.client.main.common.search.dataHolders;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import java.io.Serializable;
import java.util.Date;

/** HOMEDEMANDS **/
public class HomeDemands implements Serializable {

    private String demandTitle = null;
    private CategoryDetail demandCategory = null;
    private LocalityDetail demandLocality = null;
    private Integer priceFrom = null;
    private Integer priceTo = null;
    private String demandType = null;
    private Date endDate = null;
    //0 - today
    //1 - yesterday
    //2 - last week
    //3 - last month
    //4 - no limit
    private Integer creationDate = null;

    public Integer getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Integer creationDate) {
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

    public Integer getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Integer priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Integer getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Integer priceTo) {
        this.priceTo = priceTo;
    }
}