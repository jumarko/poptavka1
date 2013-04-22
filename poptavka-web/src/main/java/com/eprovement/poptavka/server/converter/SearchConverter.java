/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SortPair;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
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
        final Search search = new Search();
        if (definition == null) {
            return search;
        }
        convertFirstResult(definition, search);
        convertMaxResult(definition, search);
        convertFilters(definition, search);
        //Temporary disabled - TODO LATER Martin - if all selects will use Search, can use automatic conversion.
        //Also usage of prefixes must be reconsidered to be used in converter.
//        convertOrderColumns(definition, search);

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

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    /**
     * Converts <b>MaxResult</b>, attribute of <b>SearchDefinition</b> to attribute of <b>Search</b>.
     * If SearchConverter is used in count methods of RPC services, maxResult is not set, because we
     * want to get all data. Therefore converter won't add maxResult attribute to Search object.
     *
     * @param definition
     * @param search
     */
    private void convertFirstResult(SearchDefinition definition, Search search) {
        //Ak chcem pouzit searchConverter aj v count metodach, udaj atribut
        //nie je vyplneny (== -1) preto ho nesmie konverter pridat do search objektu.
        if (definition.getFirstResult() != -1) {
            search.setFirstResult(definition.getFirstResult());
        }
    }

    /**
     * Converts <b>MaxResult</b>, attribute of <b>SearchDefinition</b> to attribute of <b>Search</b>.
     * If SearchConverter is used in count methods of RPC services, maxResult is not set, because we
     * want to get all data. Therefore converter won't add maxResult attribute to Search object.
     *
     * @param definition
     * @param search
     */
    private void convertMaxResult(SearchDefinition definition, Search search) {
        //Ak chcem pouzit searchConverter aj v count metodach, udaj atribut
        //nie je vyplneny (== -1) preto ho nesmie konverter pridat do search objektu.
        if (definition.getMaxResult() != -1) {
            search.setMaxResults(definition.getMaxResult());
        }
    }

    private void convertFilters(SearchDefinition definition, Search search) {
        if (definition != null && definition.getFilter() != null) {
            if (CollectionUtils.isNotEmpty(definition.getFilter().getAttributes())) {
                search.setFilters(filterConverter.convertToSourceList(definition.getFilter().getAttributes()));
            }
            if (CollectionUtils.isNotEmpty(definition.getFilter().getCategories())) {
                search.addFilterIn("categories.id", definition.getFilter().getCategories());
            }
            if (CollectionUtils.isNotEmpty(definition.getFilter().getLocalities())) {
                search.addFilterIn("localities.id", definition.getFilter().getLocalities());
            }
        }
    }

    private void convertOrderColumns(SearchDefinition definition, Search search) {
        if (definition.getSortOrder() != null) {
            for (SortPair column : definition.getSortOrder()) {
                if (column.getColumnOrderType() == OrderType.ASC) {
                    search.addSort(Sort.asc(column.getColumnName()));
                } else {
                    search.addSort(Sort.desc(column.getColumnName()));
                }
            }
        }
    }
}
