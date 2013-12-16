package com.eprovement.poptavka.rest.common.dto;

import java.util.Map;

/**
 * Basic dto for Category.
 * Either used as an input dto (POST demand, POST supplier), then at least {@code id} field should be filled.
 * Alternatively, {@code sicCode} can be used instead of id (id should be null in this case).
 */
public class CategoryDto {
    private Long id;
    private String sicCode;
    private String name;
    private String description;
    private Map<String, String> links;

    public Long getId() {
        return id;
    }

    public CategoryDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSicCode() {
        return sicCode;
    }

    public CategoryDto setSicCode(String sicCode) {
        this.sicCode = sicCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CategoryDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public CategoryDto setLinks(Map<String, String> links) {
        this.links = links;
        return this;
    }

    @Override
    public String toString() {
        return "CategoryDto{"
                + "id='" + id + '\''
                + "sicCode='" + sicCode + '\''
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", links=" + links
                + '}';
    }
}
