/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.SystemSettingsPresenter.SystemSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SystemSettingsView.class, multiple = true)
public class SystemSettingsPresenter extends LazyPresenter<SystemSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface SystemSettingsViewInterface extends LazyView {

        void setSystemSettings(SettingDetail detail);

        void setChangeHandler(ChangeHandler changeHandler);

        SettingDetail updateSystemSettings(SettingDetail detail);

        void commit();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //history of changes
    private ArrayList<ChangeDetail> updatedFields = new ArrayList<ChangeDetail>();

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.setChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                NotificationItemView source = (NotificationItemView) event.getSource();
                manageUpdatedField(source.getEnabledChangeDetail());
                manageUpdatedField(source.getPeriodChangeDetail());
                eventBus.updateSystemStatus(isSystemSettingsCHanged());
            }
        });
    }

    private void manageUpdatedField(ChangeDetail changeDetail) {
        if (!changeDetail.getOriginalValue().equals(changeDetail.getValue())) {
            //if contains already - remove before adding new
            if (updatedFields.contains(changeDetail)) {
                updatedFields.remove(changeDetail);
            }
            updatedFields.add(changeDetail);
        } else {
            updatedFields.remove(changeDetail);
        }
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initSystemSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onSetSystemSettings(SettingDetail detail) {
        view.setSystemSettings(detail);
    }

    public SettingDetail updateSystemSettings(SettingDetail detail) {
        return view.updateSystemSettings(detail);
    }

    public boolean isSystemSettingsCHanged() {
        return !updatedFields.isEmpty();
    }
}
