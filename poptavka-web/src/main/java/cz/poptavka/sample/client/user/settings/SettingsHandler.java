package cz.poptavka.sample.client.user.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.main.errorDialog.ErrorDialogPopupView;
import cz.poptavka.sample.client.service.demand.SettingsRPCServiceAsync;
import cz.poptavka.sample.shared.domain.settings.SettingsDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

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
                        if (caught instanceof CommonException) {
                            CommonException commonException = (CommonException) caught;
                            errorDialog = new ErrorDialogPopupView();
                            errorDialog.show(commonException.getSymbol());
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
