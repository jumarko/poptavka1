package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.UserSettingsPresenter.UserSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = UserSettingsView.class, multiple = true)
public class UserSettingsPresenter extends LazyPresenter<UserSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface UserSettingsViewInterface extends LazyView {

        void setUserSettings(SettingDetail detail);

        SettingDetail updateUserSettings(SettingDetail detail);

        SimplePanel getAddressHolder();

        //Others
        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
        eventBus.initAddressSelector(view.getAddressHolder());
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onSetUserSettings(SettingDetail detail) {
        eventBus.setAddresses(detail.getUser().getAddresses());
        view.setUserSettings(detail);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void updateUserSettings(SettingDetail detail) {
        view.updateUserSettings(detail);
        eventBus.fillAddresses(detail.getUser().getAddresses());
    }
}