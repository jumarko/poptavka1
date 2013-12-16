package com.eprovement.poptavka.rest.supplier;

import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import com.eprovement.poptavka.rest.common.dto.CategoryDto;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SupplierDto extends BusinessUserDto {
    private Integer overalRating;
    private Boolean certified;
    private List<LocalityDto> localities = Collections.emptyList();
    private List<CategoryDto> categories = Collections.emptyList();
    private Map<String, String> links;

    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(Integer overalRating) {
        this.overalRating = overalRating;
    }

    public Boolean isCertified() {
        return certified;
    }

    public void setCertified(Boolean certified) {
        this.certified = certified;
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

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }


    @Override
    public String toString() {
        return "SupplierDto{"
                + "overalRating=" + overalRating
                + "certified=" + certified
                + ", links=" + links
                + '}';
    }
}
