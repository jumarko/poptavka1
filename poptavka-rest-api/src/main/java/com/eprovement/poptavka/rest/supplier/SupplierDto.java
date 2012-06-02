/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.supplier;

import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import java.util.Map;

public class SupplierDto extends BusinessUserDto {
    private Integer overalRating;
    private Map<String, String> links;

    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(Integer overalRating) {
        this.overalRating = overalRating;
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
                + ", links=" + links
                + '}';
    }
}
