package com.eprovement.poptavka.client.user.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.SettingsRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@EventHandler
public class SettingsHandler extends BaseEventHandler<SettingsEventBus> {

    @Inject
    SettingsRPCServiceAsync settingsService;
    private ErrorDialogPopupView errorDialog;

    public void onGetLoggedUser(long userId) {
        GWT.log("HomeSettingsHandler handling user" + userId);
        settingsService.getUserSettings(userId,
                new AsyncCallback<SettingsDetail>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                        GWT.log("neuspesny settingsDetail:"
                                + caught.getMessage());

                    }

                    @Override
                    public void onSuccess(SettingsDetail result) {
                        GWT.log("uspesny settingsDetail");
                        eventBus.setSettings(result);

                    }
                });
    }
}
