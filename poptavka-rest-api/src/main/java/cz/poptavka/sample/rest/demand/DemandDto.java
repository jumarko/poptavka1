/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class DemandDto {
    private String title;
    private String description;
    private BigDecimal price;
    private Date createdDate;
    private Date endDate;
    private Map<String, String> links;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }


    @Override
    public String toString() {
        return "DemandDto{"
                + "title='" + title + '\''
                + ", description='" + description + '\''
                + ", price=" + price
                + ", createdDate=" + createdDate
                + ", endDate=" + endDate
                + ", links=" + links
                + '}';
    }
}
