package cz.poptavka.sample.client.main.common.search;

import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds data of search module to filter data. Data are divided exactly according to
 * search bar: <I>attributes</I> filters, <I>categories</I> filters, <I>localities</I> filters
 *
 * @author Martin Slavkovsky
 */
public class SearchModuleDataHolder implements Serializable {

    private ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
    private ArrayList<CategoryDetail> categories = new ArrayList<CategoryDetail>();
    private ArrayList<LocalityDetail> localities = new ArrayList<LocalityDetail>();

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public ArrayList<FilterItem> getFilters() {
        return filters;
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }
}
