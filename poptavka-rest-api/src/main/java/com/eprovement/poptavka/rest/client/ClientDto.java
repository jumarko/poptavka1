/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.client;

import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;

import java.util.Map;

public class ClientDto extends BusinessUserDto {
    private Integer overallRating;
    private Map<String, String> links;

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
