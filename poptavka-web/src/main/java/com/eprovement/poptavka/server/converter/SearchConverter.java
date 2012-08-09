/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.Validate;

public final class SearchConverter implements Converter<Search, SearchDefinition> {

    private final Converter<Filter, FilterItem> filterConverter;

    private SearchConverter(Converter<Filter, FilterItem> filterConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(filterConverter);
        this.filterConverter = filterConverter;
    }

    @Override
    public SearchDefinition convertToTarget(Search source) {
        throw new UnsupportedOperationException("Convertion Search to SearchDefinition failed!");
    }

    @Override
    public Search convertToSource(SearchDefinition definition) {
        Search search = new Search();
        if (definition != null) {
            if (!definition.getFilter().getAttibutes().isEmpty()) {
                search.setFilters(filterConverter.convertToSourceList(definition.getFilter().getAttibutes()));
            }
            if (!definition.getFilter().getCategories().isEmpty()) {
                search.addFilterIn("categories.id", definition.getFilter().getCategories());
            }
            if (!definition.getFilter().getLocalities().isEmpty()) {
                search.addFilterIn("localities.id", definition.getFilter().getLocalities());
            }
        }
        search.setFirstResult(definition.getStart());
        search.setMaxResults(definition.getMaxResult());
        for (String column : definition.getOrderColumns().keySet()) {
            if (definition.getOrderColumns().get(column) == OrderType.ASC) {
                search.addSort(Sort.asc(column));
            } else {
                search.addSort(Sort.desc(column));
            }
        }
        return search;
    }

    @Override
    public ArrayList<SearchDefinition> convertToTargetList(Collection<Search> sourceObjects) {
        throw new UnsupportedOperationException("Convertion List<Search> to List<SearchDefinition> failed!");
    }

    @Override
    public ArrayList<Search> convertToSourceList(Collection<SearchDefinition> targetObjects) {
        throw new UnsupportedOperationException("Convertion List<Search> to List<SearchDefinition> failed!");
    }
}
