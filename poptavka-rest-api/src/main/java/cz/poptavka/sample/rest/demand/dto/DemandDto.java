/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DemandDto extends AbstractDto {
    private String title;
    private String description;
    private BigDecimal price;
    private Date createdDate;
    private Date endDate;


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
}
