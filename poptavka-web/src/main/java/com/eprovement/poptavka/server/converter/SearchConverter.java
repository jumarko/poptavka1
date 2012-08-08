/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.Validate;

public final class SearchConverter implements Converter<Search, SearchModuleDataHolder> {

    private final Converter<Filter, FilterItem> filterConverter;

    private SearchConverter(Converter<Filter, FilterItem> filterConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(filterConverter);
        this.filterConverter = filterConverter;
    }

    @Override
    public SearchModuleDataHolder convertToTarget(Search source) {
        throw new UnsupportedOperationException("Convertion Search to SearchModuleDataHolder failed!");
    }

    @Override
    public Search converToSource(SearchModuleDataHolder detail) {
        Search search = new Search();
        if (detail != null) {
            if (!detail.getAttibutes().isEmpty()) {
                search.setFilters(filterConverter.convertToSourceList(detail.getAttibutes()));
            }
            if (!detail.getCategories().isEmpty()) {
                search.addFilterIn("categories.id", detail.getCategories());
            }
            if (!detail.getLocalities().isEmpty()) {
                search.addFilterIn("localities.id", detail.getLocalities());
            }
        }
        return search;
    }

    @Override
    public ArrayList<SearchModuleDataHolder> convertToTargetList(Collection<Search> sourceObjects) {
        throw new UnsupportedOperationException("Convertion List<Search> to List<SearchModuleDataHolder> failed!");
    }

    @Override
    public ArrayList<Search> convertToSourceList(Collection<SearchModuleDataHolder> targetObjects) {
        throw new UnsupportedOperationException("Convertion List<Search> to List<SearchModuleDataHolder> failed!");
    }
}
