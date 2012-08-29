package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for HomeSuppliersModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.NONE, name = "homeSuppliers")
public class HomeSuppliersHistoryConverter implements HistoryConverter<HomeSuppliersEventBus> {

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
            eventBus.userMenuStyleChange(Constants.USER_SUPPLIER_MODULE);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
