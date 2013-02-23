/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.util;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.List;

/**
 *
 * @author Vojtech Hubr
 */
public final class SearchUtils {
    private SearchUtils() {
        // utility class - DO NOT INSTANTIATE!
    }

    public static Search toBackendSearch(Class backendClass,
            SearchDefinition searchDefinition,
            List<String> searchAttributes, String categoryLocalityPrefix) {
        Search result = new Search(backendClass);
        result.setFirstResult(searchDefinition.getFirstResult());
        result.setMaxResults(searchDefinition.getMaxResult());
        if (searchDefinition.getFilter() != null) {
            if (searchDefinition.getFilter().getCategories() != null) {
                for (CategoryDetail categoryDetail : searchDefinition.getFilter()
                        .getCategories()) {
                    Category category = new Category();
                    category.setId(categoryDetail.getId());
                    result.addFilter(new Filter(categoryLocalityPrefix + "categories",
                            category, Filter.OP_IN));
                }
            }
            if (searchDefinition.getFilter().getLocalities() != null) {
                for (LocalityDetail localityDetail : searchDefinition.getFilter()
                        .getLocalities()) {
                    Locality locality = new Locality();
                    locality.setId(localityDetail.getId());
                    result.addFilter(new Filter(categoryLocalityPrefix + "localities",
                            locality, Filter.OP_IN));
                }
            }
            if (searchAttributes != null && searchDefinition.getFilter()
                            .getSearchText() != null) {
                Filter[] filters = new Filter[searchAttributes.size()];
                int index = 0;
                for (String attribute : searchAttributes) {
                    filters[index++] = (new Filter(attribute, "%" + searchDefinition.getFilter()
                            .getSearchText() + "%", Filter.OP_LIKE));
                }
                result.addFilterOr(filters);
            }
            System.out.println(result.getFilters());
        }
        return result;
    }

}
