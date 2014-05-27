/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

/**
 * Converts SearchDefinition to Search.
 * @author Juraj Martinka
 */
public final class SearchConverter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final FilterConverter filterConverter;
    private final SortConverter sortConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates SearchConverter.
     */
    private SearchConverter(FilterConverter filterConverter, SortConverter sortConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(filterConverter);
        Validate.notNull(sortConverter);
        this.filterConverter = filterConverter;
        this.sortConverter = sortConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * Construct search object for count methods. It involves:
     * <ul>
     *      <li>convertFilters(definition, search);</li>
     * </ul>
     * No sort, or paginating involved, because when Count is needed, setting
     * sorting is unnecessary. It only make sense when retrieving data that
     * we want to be sorted. The same with paginating. I need count of all items,
     * not only for a certain page. That would be silly.
     *
     * Therefore only filter are converted and set to search object together with
     * options (OP_COUNT, RESULT_SINGLE) for returning single result - count.
     *
     * Use when converting search definition object for count purposes. Use with
     * generalService.count(Search) method.
     *
     * @param searchClass
     * @param definition
     * @return search object
     */
    public Search convertToSourceForCount(Class<?> searchClass, SearchDefinition definition) {
        final Search search = new Search(searchClass);
        if (definition == null) {
            return search;
        }
        convertFilters(definition, search);

        return search;
    }

    /**
     * Construct search object for getting data. It involves:
     * <ul>
     *      <li>convertFirstResult(definition, search);</li>
     *      <li>convertMaxResult(definition, search);</li>
     *      <li>convertFilters(definition, search);</li>
     *      <li>convertOrderColumns(definition, search);</li>
     * </ul>
     *
     * Unlike convertToSourceForCount, converting involves also sorting and paginating
     * and returns list of searched items.
     *
     * @param searchClass
     * @param definition
     * @return search object
     */
    public Search convertToSource(Class<?> searchClass, SearchDefinition definition) {
        final Search search = new Search(searchClass);
        if (definition == null) {
            return search;
        }
        convertFirstResult(definition, search);
        convertMaxResult(definition, search);
        convertFilters(definition, search);
        convertOrderColumns(definition, search);

        return search;
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

    /**
     * Converts filters.
     * @param definition - search criteria
     * @param search the Search object
     */
    private void convertFilters(SearchDefinition definition, Search search) {
        if (definition != null && definition.getFilter() != null) {
            if (CollectionUtils.isNotEmpty(definition.getFilter().getAttributes())) {
                search.setFilters(filterConverter.convertToSourceList(
                    search.getSearchClass(), definition.getFilter().getAttributes()));
            }
            if (CollectionUtils.isNotEmpty(definition.getFilter().getCategories())) {
                search.addFilterIn("categories.id", definition.getFilter().getCategories());
            }
            if (CollectionUtils.isNotEmpty(definition.getFilter().getLocalities())) {
                search.addFilterIn("localities.id", definition.getFilter().getLocalities());
            }
        }
    }

    /**
     * Converts order columns.
     * @param definition - search criteria
     * @param search the Search object
     */
    private void convertOrderColumns(SearchDefinition definition, Search search) {
        search.addSorts(sortConverter.convertToSourceList(search.getSearchClass(), definition.getSortOrder()));
    }
}
