package com.eprovement.poptavka.client.homesuppliers;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.domain.CategoryDetail;

/**
 * History converter class. Handles history for HomeSuppliersModule.
 * This class works different than others. Token is not created for navigation method, because
 * whole widget works different.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "homeSuppliers")
public class HomeSuppliersHistoryConverter implements HistoryConverter<HomeSuppliersEventBus> {

    /**
     * Creates token(URL) for addToPath method.
     *
     * @param categoryDetail - processing category
     * @return token string like module/method?param, where param like: location=home;categoryId=220
     */
    public String onAddToPath(CategoryDetail categoryDetail) {
        StringBuilder str = new StringBuilder();
        /* Martin - Nemusi to byt, pretoze v convertFromToken to neberiem vobec do uvahy.
        Ale pre testovacie ucely ale vhodne. Potom moze odstranit */
        if (Storage.getUser() == null) {
            str.append("location=home");
        } else {
            str.append("location=user");
        }
        str.append(";categoryId=");
        str.append(Long.toString(categoryDetail.getId()));
        return str.toString();
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onAddToPath method in HomeSuppliersHistoryConverter class.
     * @param eventBus - HomeSuppliersEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeSuppliersEventBus eventBus) {
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.HOME_SUPPLIERS_MODULE);
        } else {
            eventBus.userMenuStyleChange(Constants.USER_DEMANDS_MODULE);
        }
        String[] params = param.split(";");
        CategoryDetail categoryDetail = new CategoryDetail(Long.valueOf(
                params[1].replace("categoryId=", "")), null);

        //ROOT
        SearchModuleDataHolder searchModuleDataHolder = null;
        if (categoryDetail.getId() != 0) {
            searchModuleDataHolder = new SearchModuleDataHolder();
            searchModuleDataHolder.getCategories().clear();
            searchModuleDataHolder.getCategories().add(categoryDetail);
        }
        eventBus.displayParentOrChild(searchModuleDataHolder);
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
