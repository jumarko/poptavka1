package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for SupplierDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "supplierDemands")
public class SupplierDemandsModuleHistoryConverter implements HistoryConverter<SupplierDemandsModuleEventBus> {

    @Inject
    protected UserRPCServiceAsync service = null;

    public String onGoToSupplierDemandsModule(SearchModuleDataHolder filterm, int loadWidget) {
        return "widget=" + loadWidget + ";user=" + Storage.getUser().getUserId();
    }

    @Override
    public void convertFromToken(String historyName, String param, final SupplierDemandsModuleEventBus eventBus) {
        if (Storage.isRootStartMethodCalledFirst()) {
            final String[] params = param.split(";");
            service.getUserById(Long.valueOf(params[1].split("=")[1]), new SecuredAsyncCallback<UserDetail>(eventBus) {
                @Override
                public void onSuccess(UserDetail result) {
                    Storage.setUserDetail(result);
                    service.getBusinessUserById(Long.valueOf(params[1].split("=")[1]),
                            new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                                @Override
                                public void onSuccess(BusinessUserDetail result) {
                                    Storage.setBusinessUserDetail(result);
                                    eventBus.atAccount();
                                    eventBus.userMenuStyleChange(Constants.USER_SUPPLIER_MODULE);
                                    eventBus.goToSupplierDemandsModule(null, Integer.valueOf(params[0].split("=")[1]));
                                }
                            });
                }
            });
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
