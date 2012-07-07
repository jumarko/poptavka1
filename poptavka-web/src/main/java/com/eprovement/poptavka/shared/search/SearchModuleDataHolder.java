package com.eprovement.poptavka.shared.search;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import java.io.Serializable;
import java.util.ArrayList;

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

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }

    public ArrayList<FilterItem> getAttibutes() {
        return attributes;
    }
}
