/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.demand;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.rest.client.ClientDto;
import com.eprovement.poptavka.rest.common.dto.CategoryDto;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import org.apache.commons.lang.Validate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DemandDto {

    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private Date createdDate;
    private Date endDate;
    private Map<String, String> links;
    private List<LocalityDto> localities = Collections.emptyList();
    private List<CategoryDto> categories = Collections.emptyList();
    private ClientDto client;
    private DemandStatus status;
    private String origin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<LocalityDto> getLocalities() {
        return localities;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setLocalities(List<LocalityDto> localities) {
        this.localities = Collections.unmodifiableList(localities);
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = Collections.unmodifiableList(categories);
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public void validate() {
        Validate.notEmpty(title, "demand's title cannot be empty");
        Validate.notEmpty(description, "demand's description cannot be empty");
        Validate.notNull(price, "demand's price cannot be empty");
        Validate.notEmpty(categories, "at least one demand category has to be specified");
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

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public DemandStatus getStatus() {
        return status;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
