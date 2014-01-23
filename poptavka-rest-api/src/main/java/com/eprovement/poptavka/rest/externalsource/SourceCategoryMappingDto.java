package com.eprovement.poptavka.rest.externalsource;

import java.util.List;
import java.util.Map;

public class SourceCategoryMappingDto {
    private String source;
    private List<ExternalCategoryDto> mappings;
    private Map<String, String> links;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<ExternalCategoryDto> getMappings() {
        return mappings;
    }

    public void setMappings(List<ExternalCategoryDto> mappings) {
        this.mappings = mappings;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
