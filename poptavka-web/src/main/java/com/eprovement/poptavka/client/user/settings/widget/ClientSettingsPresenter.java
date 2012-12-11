/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.ClientSettingsPresenter.ClientSettingsViewInterface;
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

    public interface ClientSettingsViewInterface extends LazyView {
        Widget getWidgetView();
    }

    @Override
    public void bindView() {

    }

    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

}
