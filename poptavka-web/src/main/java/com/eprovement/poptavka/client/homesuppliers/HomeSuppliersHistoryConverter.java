/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.LesserSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * Manages history for HomeSuppliers module.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = "suppliers")
public class HomeSuppliersHistoryConverter implements HistoryConverter<HomeSuppliersEventBus> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final String LOCATION_HOME = "home";
    private static final String LOCATION_USER = "user";
    private static final String KEY_CATEGORY = "catId";
    private static final String KEY_PAGE = "page";
    private static final String KEY_SUPPLIER = "supId";
    private static final String VALUE_SEPARATOR = "=";
    private static final String ITEM_SEPARATOR = ";";
    //please notice, if choosing "" as NO_VALUE, parse function must be updated
    //due to possible NullPointerException. See convertFromToken in this class
    //and setModuleByHistory in Handler class.
    private static final String NO_VALUE = "null";

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /**
     * Creates token for history from search filters, selected category, page, and supplier.
     * @param searchDataHolder - search filters
     * @param category - selected category
     * @param page - selected page
     * @param supplierDetail - selected supplier
     * @return created token as string
     */
    public String onCreateTokenForHistory(SearchModuleDataHolder searchDataHolder,
        ICatLocDetail category, int page, LesserSupplierDetail supplierDetail) {
        StringBuilder token = new StringBuilder();
        //Location
        token.append(Storage.getUser() == null ? LOCATION_HOME : LOCATION_USER);
        token.append(ITEM_SEPARATOR);
        //Category
        token.append(KEY_CATEGORY);
        token.append(VALUE_SEPARATOR);
        token.append(category == null ? NO_VALUE : category.getId());
        //Page
        token.append(ITEM_SEPARATOR);
        token.append(KEY_PAGE);
        token.append(VALUE_SEPARATOR);
        token.append(page);
        //Supplier
        token.append(ITEM_SEPARATOR);
        token.append(KEY_SUPPLIER);
        token.append(VALUE_SEPARATOR);
        token.append(supplierDetail == null ? NO_VALUE : supplierDetail.getSupplierId());
        return token.toString();
    }

    /**************************************************************************/
    /* ConvertFromToken events                                                */
    /**************************************************************************/
    /**
     * Convert history token to action.
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL is created by createTokenForHistory method in HomeSuppliersHistoryConverter class.
     * @param eventBus - HomeSuppliersEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeSuppliersEventBus eventBus) {
        if (param.startsWith(LOCATION_USER)) {
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(Constants.SKIP);
            param = param.substring(LOCATION_USER.length() + ITEM_SEPARATOR.length(), param.length());
        } else {
            param = param.substring(LOCATION_HOME.length() + ITEM_SEPARATOR.length(), param.length());
        }
        Storage.setCalledDueToHistory(true);
        SearchModuleDataHolder searchDataHolder = null;
        //remove home/user part (i.e.: "home;catId=16;page=0;supId=134")
        param = param.substring(LOCATION_HOME.length() + ITEM_SEPARATOR.length(), param.length());
        //When back & forward events -> don't need to call goToHomeSupplierModule
        // - it would create new universalAsyncTable, ...
        // - just use what is already created - events will fire appropiate actions
        //parse param (i.e.: "catId=16;page=0;supId=134")
        String[] params = param.split(ITEM_SEPARATOR);
        //params == (i.e.: ["catId=16","page=0","supId=134"]
        eventBus.setModuleByHistory(searchDataHolder,
            params[0].split(VALUE_SEPARATOR)[1],
            params[1].split(VALUE_SEPARATOR)[1],
            params[2].split(VALUE_SEPARATOR)[1]);
    }

    /**************************************************************************/
    /*                                                                        */
    /**************************************************************************/
    @Override
    public boolean isCrawlable() {
        return false;
    }
}