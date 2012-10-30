package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Holds data of search module to filter data. Data are divided exactly according to
 * search bar: <br>
 * 1. part of search bar ... store <I>text</I> string for fulltext search,<br>
 * 2. part of search bar ... store <I>categories</I> chosen by user, <br>
 * 3. part of search bar ... store <I>localities</I> chosen by user, <br>
 * 4. part of search bar ... store additional filters on certaing domain object <I>attributes</I>.
 *
 * @author Martin Slavkovsky
 */
public class SearchModuleDataHolder implements Serializable {

    //1. part of search bar ... store string for fulltext search
    private String searchText = "";
    //2. part of search bar ... store categories chosen by user
    private ArrayList<CategoryDetail> categories = new ArrayList<CategoryDetail>();
    //3. part of search bar ... store localities chosen by user
    private ArrayList<LocalityDetail> localities = new ArrayList<LocalityDetail>();
    //4. part of search bar ... store additional filters on certaing domain object attribute
    private ArrayList<FilterItem> attributes = new ArrayList<FilterItem>();

    public SearchModuleDataHolder() {
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setCategories(Collection<CategoryDetail> categories) {
        this.categories = new ArrayList<CategoryDetail>(categories);
    }

    public void setLocalities(Collection<LocalityDetail> localities) {
        this.localities = new ArrayList<LocalityDetail>(localities);
    }

    public void setAttributes(ArrayList<FilterItem> attributes) {
        this.attributes = attributes;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    public String getSearchText() {
        return searchText;
    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }

    public ArrayList<FilterItem> getAttributes() {
        return attributes;
    }

    /**************************************************************************/
    /* toString & parse                                                       */
    /**************************************************************************/
    /**
     * String format: >> text=;cats=[..,..],locs=[..,..];attrs=[..,..] <<.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("text=\"");
        str.append(searchText);
        str.append("\"");
        str.append(";cats=");
        str.append(categories.toString());
        str.append(";locs=");
        str.append(localities.toString());
        str.append(";attrs=");
        str.append(attributes.toString());
        return str.toString();
    }

    public String toStringWithIDs() {
        StringBuilder str = new StringBuilder();
        str.append("text=\"");
        str.append(searchText);
        str.append("\"");
        str.append(";cats=");
        str.append(categoriesIDsToString());
        str.append(";locs=");
        str.append(localitiesIDsToString());
        str.append(";attrs=");
        str.append(attributes.toString());
        return str.toString();
    }

    /**
     * Cannot use default toString because it returns category name, but to URL we
     * need to remember categories id.
     * @return
     */
    public String categoriesIDsToString() {
        StringBuilder str = new StringBuilder("[");
        for (CategoryDetail cat : categories) {
            str.append(cat.getId());
            str.append(",");
        }
        if (str.length() != 0) {
            str.delete(str.length() - 1, str.length());
        }
        str.append("]");
        return str.toString();
    }

    /**
     * Cannot use default toString because it returns locality name, but to URL we
     * need to remember localities id.
     * @return
     */
    public String localitiesIDsToString() {
        StringBuilder str = new StringBuilder("[");
        for (LocalityDetail loc : localities) {
            str.append(loc.getId());
            str.append(",");
        }
        if (str.length() != 0) {
            str.delete(str.length() - 1, str.length());
        }
        str.append("]");
        return str.toString();
    }

    public static SearchModuleDataHolder parseSearchModuleDataHolder(String urlToken) {
        urlToken = urlToken.replace("[", "");
        urlToken = urlToken.replace("]", "");
        String[] items = urlToken.split(";");

        SearchModuleDataHolder searchModuleDataHolder = new SearchModuleDataHolder();

        //text=textString
        searchModuleDataHolder.setSearchText(items[0].split("=")[1].replaceAll("\"", ""));
        //cats=[..,..]
        ArrayList<CategoryDetail> categories = new ArrayList<CategoryDetail>();
        String[] cats = items[1].split("=");
        if (cats.length > 1) {
            for (String catId : cats[1].split(",")) {
                categories.add(new CategoryDetail(Long.valueOf(catId), ""));
            }
        }
        searchModuleDataHolder.setCategories(categories);
        //locs=[..,..]
        ArrayList<LocalityDetail> localities = new ArrayList<LocalityDetail>();
        String[] locs = items[2].split("=");
        if (locs.length > 1) {
            for (String locCode : locs[1].split(",")) {
                localities.add(new LocalityDetail("", locCode));
            }
        }
        searchModuleDataHolder.setLocalities(localities);
        //attrs=[..,..]
        ArrayList<FilterItem> attributes = new ArrayList<FilterItem>();
        String[] attrs = items[3].split("=");
        if (attrs.length > 1) {
            for (String locCode : attrs[1].split(",")) {
                attributes.add(FilterItem.parseFilterItem(urlToken));
            }
        }
        searchModuleDataHolder.setAttributes(attributes);
        return searchModuleDataHolder;
    }
}
