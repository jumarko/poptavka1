package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
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
public final class SearchModuleDataHolder implements IsSerializable {

    //1. part of search bar ... store string for fulltext search
    private String searchText = "";
    //2. part of search bar ... store categories chosen by user
    private ArrayList<CategoryDetail> categories = new ArrayList<CategoryDetail>();
    //3. part of search bar ... store localities chosen by user
    private ArrayList<LocalityDetail> localities = new ArrayList<LocalityDetail>();
    //4. part of search bar ... store additional filters on certaing domain object attribute
    private ArrayList<FilterItem> attributes = new ArrayList<FilterItem>();

    public static SearchModuleDataHolder getSearchModuleDataHolder() {
        return new SearchModuleDataHolder();
    }
    private SearchModuleDataHolder() {
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
    public static final String VALUE_SEPARATOR = "=";
    public static final String ITEM_SEPARATOR = ";";
    public static final String LIST_BRACKET_LEFT = "[";
    public static final String LIST_BRACKET_RIGHT = "]";
    public static final String LIST_ITEM_SEPARATOR = ",";

    /**
     * String format: >> text=;cats=[..,..],locs=[..,..];attrs=[..,..] <<.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (!searchText.isEmpty()) {
            str.append("text");
            str.append(VALUE_SEPARATOR);
            str.append("\"");
            str.append(searchText);
            str.append("\"");
            str.append(ITEM_SEPARATOR);
        }
        if (!categories.isEmpty()) {
            str.append("cats");
            str.append(VALUE_SEPARATOR);
            str.append(categories.toString());
            str.append(ITEM_SEPARATOR);
        }
        if (!localities.isEmpty()) {
            str.append("locs");
            str.append(VALUE_SEPARATOR);
            str.append(localities.toString());
            str.append(ITEM_SEPARATOR);
        }
        if (!attributes.isEmpty()) {
            str.append("attrs");
            str.append(VALUE_SEPARATOR);
            str.append(toStringAttributes());
        }
        return str.toString();
    }

    private String toStringAttributes() {
        StringBuilder str = new StringBuilder("(");
        int group = -1;
        boolean first = true;
        for (FilterItem item : attributes) {
            if (group == item.getGroup()) {
                str.append(" | ");
            } else {
                if (!first) {
                    str.append(") & (");
                }
                first = false;
                group = item.getGroup();
            }
            str.append(item.toString());
        }
        str.append(")");
        return str.toString();
    }

    public String toStringWithIDs() {
        StringBuilder str = new StringBuilder();
        if (!searchText.isEmpty()) {
            str.append("text");
            str.append(VALUE_SEPARATOR);
            str.append("\"");
            str.append(searchText);
            str.append("\"");
            str.append(ITEM_SEPARATOR);
        }
        if (!categories.isEmpty()) {
            str.append("cats");
            str.append(VALUE_SEPARATOR);
            str.append(categoriesIDsToString());
            str.append(ITEM_SEPARATOR);
        }
        if (!localities.isEmpty()) {
            str.append("locs");
            str.append(VALUE_SEPARATOR);
            str.append(localitiesIDsToString());
            str.append(ITEM_SEPARATOR);
        }
        if (!attributes.isEmpty()) {
            str.append("attrs");
            str.append(VALUE_SEPARATOR);
            str.append(toStringAttributes());
        }
        return str.toString();
    }

    /**
     * Cannot use default toString because it returns category name, but to URL we
     * need to remember categories id.
     * @return
     */
    public String categoriesIDsToString() {
        StringBuilder str = new StringBuilder(LIST_BRACKET_LEFT);
        for (CategoryDetail cat : categories) {
            str.append(cat.getId());
            str.append(LIST_ITEM_SEPARATOR);
        }
        if (!categories.isEmpty()) {
            str.delete(str.length() - 1, str.length());
        }
        str.append(LIST_BRACKET_RIGHT);
        return str.toString();
    }

    /**
     * Cannot use default toString because it returns locality name, but to URL we
     * need to remember localities id.
     * @return
     */
    public String localitiesIDsToString() {
        StringBuilder str = new StringBuilder(LIST_BRACKET_LEFT);
        for (LocalityDetail loc : localities) {
            str.append(loc.getId());
            str.append(LIST_ITEM_SEPARATOR);
        }
        if (!localities.isEmpty()) {
            str.delete(str.length() - 1, str.length());
        }
        str.append(LIST_BRACKET_RIGHT);
        return str.toString();
    }

    public static SearchModuleDataHolder parseSearchModuleDataHolder(String urlToken) {
        if (urlToken == null) {
            return null;
        }
        urlToken = urlToken.replace(LIST_BRACKET_LEFT, "");
        urlToken = urlToken.replace(LIST_BRACKET_RIGHT, "");
        String[] items = urlToken.split(ITEM_SEPARATOR);

        SearchModuleDataHolder searchModuleDataHolder = new SearchModuleDataHolder();

        //text=textString
        searchModuleDataHolder.setSearchText(items[0].split(VALUE_SEPARATOR)[1].replaceAll("\"", ""));
        //cats=[..,..]
        ArrayList<CategoryDetail> categories = new ArrayList<CategoryDetail>();
        String[] cats = items[1].split(VALUE_SEPARATOR);
        if (cats.length > 1) {
            for (String catId : cats[1].split(LIST_ITEM_SEPARATOR)) {
                categories.add(new CategoryDetail(Long.valueOf(catId), ""));
            }
        }
        searchModuleDataHolder.setCategories(categories);
        //locs=[..,..]
        ArrayList<LocalityDetail> localities = new ArrayList<LocalityDetail>();
        String[] locs = items[2].split(VALUE_SEPARATOR);
        if (locs.length > 1) {
            for (String locId : locs[1].split(LIST_ITEM_SEPARATOR)) {
                localities.add(new LocalityDetail("", Long.parseLong(locId)));
            }
        }
        searchModuleDataHolder.setLocalities(localities);
        //attrs=[..,..]
        ArrayList<FilterItem> attributes = new ArrayList<FilterItem>();
        String[] attrs = items[3].split(VALUE_SEPARATOR);
        if (attrs.length > 1) {
            for (String attr : attrs[1].split(LIST_ITEM_SEPARATOR)) {
                attributes.add(FilterItem.parseFilterItem(attr));
            }
        }
        searchModuleDataHolder.setAttributes(attributes);
        return searchModuleDataHolder;
    }
}
