package cz.poptavka.sample.client.homesuppliers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.shared.domain.CategoryDetail;

/**
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.SIMPLE, name = "homeSuppliers")
public class HomeSuppliersHistoryConverter implements HistoryConverter<HomeSuppliersEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    /**
     * To convert token for addToPath method.
     * @param searchDataHolder
     * @param location
     * @return token string like module/method?param
     */
    public String convertToString(String methodName, CategoryDetail categoryDetail, String location) {
        return "location=" + location + ";categoryId=" + Long.toString(categoryDetail.getId());
    }

    /**
     * Called when browser action <b>back</b> or <b>forward</b> is evocated.
     * Or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param). Url generates convertToToken method.
     * @param eventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeSuppliersEventBus eventBus) {
        String[] params = param.split(";");
        String location = params[0].replace("location=", "");
        CategoryDetail categoryDetail = new CategoryDetail(Long.valueOf(params[1].replace("categoryId=", "")), null);

        //ROOT
        if (categoryDetail.getId() == 0) {
            eventBus.initHomeSuppliersModule(null, location);
        } else {
            SearchModuleDataHolder searchModuleDataHolder = new SearchModuleDataHolder();
            searchModuleDataHolder.initHomeSuppliers();
            searchModuleDataHolder.getHomeSuppliers().setSupplierCategory(categoryDetail);
            eventBus.initHomeSuppliersModule(searchModuleDataHolder, location);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
