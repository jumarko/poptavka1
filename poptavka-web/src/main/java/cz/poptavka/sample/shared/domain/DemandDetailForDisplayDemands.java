package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import java.math.BigDecimal;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class DemandDetailForDisplayDemands implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;

    public enum DemandField {

        TITLE, DESCRIPTION, PRICE, FINISH_DATE, VALID_TO_DATE, MAX_OFFERS, MIN_RATING, DEMAND_TYPE
    }
    private Long id;
    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private int minRating;
    private Date created;
    private Date endDate;
    private Date validTo;
    private String title;
    private String description;
    private String type;
    private String status;
    private BigDecimal price;

    public DemandDetailForDisplayDemands() {
    }

    public static DemandDetailForDisplayDemands createDetail(Date created, Demand demand) {
        DemandDetailForDisplayDemands detail = new DemandDetailForDisplayDemands();
        if (demand == null) {
            return detail;
        }
        demand.getId();
        detail.setCreated(created);
        detail.setEndDate(demand.getEndDate());
        detail.setPrice(demand.getPrice());
        detail.setTitle(demand.getTitle());
        detail.setValidTo(demand.getValidTo());
        detail.setDescription(demand.getDescription());
        ArrayList<String> categories = new ArrayList<String>();
        if (demand.getCategories() != null) {
            for (Category category : demand.getCategories()) {
                categories.add(category.getName());
            }
        }
        ArrayList<String> localities = new ArrayList<String>();
        if (demand.getLocalities() != null) {
            for (Locality locality : demand.getLocalities()) {
                localities.add(locality.getName());
            }
        }
        detail.setCategories(categories);
        detail.setCategories(localities);

        if (demand.getStatus() != null) {
            detail.setStatus(demand.getStatus().getValue());
        }
        if (demand.getType() != null) {
            detail.setType(demand.getType().toString());
        }
        return detail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public ArrayList<String> getLocalities() {
        return localities;
    }

    public void setLocalities(ArrayList<String> localities) {
        this.localities = localities;
    }

    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDemandType() {
        return type;
    }

    public void setDemandType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    @Override
    public String toString() {
        return "* FullDemandDetail:"
                + "\n     localities="
                + localities + "\n     categories="
                + categories + "\n     clientId="
                + minRating + "\n     demandType="
                + type + "\n     demandStatus="
                + status;
    }
}