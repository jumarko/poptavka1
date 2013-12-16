package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter.ClientSettingsViewInterface;
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
@Presenter(view = ClientSettingsView.class, multiple = true)
public class ClientSettingsPresenter extends LazyPresenter<ClientSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface ClientSettingsViewInterface extends LazyView {

        void setClientSettings(SettingDetail detail);

        SettingDetail updateClientSettings(SettingDetail detail);

        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onSetClientSettings(SettingDetail detail) {
        view.setClientSettings(detail);
    }

    public SettingDetail updateClientSettings(SettingDetail detail) {
        return view.updateClientSettings(detail);
    }
}
