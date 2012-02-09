/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand.dto;

import java.util.List;

public abstract class AbstractDto {
    private List<String> links;

    protected AbstractDto() {
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }
}
