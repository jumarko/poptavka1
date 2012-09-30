package com.eprovement.poptavka.client.user.clientdemands;

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
 * History converter class. Handles history for ClientDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "clientDemands")
public class ClientDemandsModuleHistoryConverter implements HistoryConverter<ClientDemandsModuleEventBus> {

    @Inject
    protected UserRPCServiceAsync service = null;

    public String onGoToClientDemandsModule(SearchModuleDataHolder filterm, int loadWidget) {
        //TODO Ivan - userId vymazat z url, nemalo by sa uchovavat - temporary solution
        return "widget=" + loadWidget + ";user=" + Storage.getUser().getUserId();
    }

    @Override
    public void convertFromToken(String historyName, String param, final ClientDemandsModuleEventBus eventBus) {
        if (Storage.isRootStartMethodCalledFirst()) {
            final String[] params = param.split(";");
            //TODO Ivan - tu treba zrobit to prihlasenie ako si chcel, aby sa nasetoval user a business user do Storage,
            //az potom budu fungovat RPC servisy
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
                                    eventBus.userMenuStyleChange(Constants.USER_CLIENT_MODULE);
                                    eventBus.goToClientDemandsModule(null, Integer.valueOf(params[0].split("=")[1]));
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
