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
     * To convert token for initHomeSuppliersModule method
     * @param searchDataHolder
     * @param location
     * @return token string like module/method?param, where param = homeRoot or userRoot
     */
    public String convertToToken(String methodName, SearchModuleDataHolder searchDataHolder, String location) {
        if (methodName.equals("initHomeSuppliersModule")) {
            return location + "Root";
        }
        return "";
    }

    /**
     * To convert token for addToPath method
     * @param methodName
     * @param category
     * @param location
     * @return token string like categoryId=333
     */
    public String convertToToken(String methodName, CategoryDetail category) {
        if (methodName.equals("updatePath")) {
            return "categoryId=" + Long.toString(category.getId());
        }
        return "";
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
        if (methodName.equals("initHomeSuppliersModule")) {
            eventBus.initHomeSuppliersModule(null, param.replace("Root", ""));
        }
        if (methodName.equals("updatePath")) {
            eventBus.loadingShow(MSGS.loading());
            param = param.replace("categoryId=", "");
            eventBus.updatePath(new CategoryDetail(Long.valueOf(param), null));
            eventBus.getSubCategories(Long.valueOf(param));
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }
}
