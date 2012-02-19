/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.locality;

import java.util.Map;

public class LocalityDto {
    private String code;
    private String name;
    private String type;
    private Map<String, String> links;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "CategoryDto{"
                + "code='" + code + '\''
                + ", name='" + name + '\''
                + ", type='" + type + '\''
                + ", links=" + links
                + '}';
    }
}
