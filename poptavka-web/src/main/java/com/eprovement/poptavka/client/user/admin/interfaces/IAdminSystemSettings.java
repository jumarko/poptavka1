/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.interfaces;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.admin.system.AdminSystemSettingsPresenter;
import com.eprovement.poptavka.domain.enums.LogType;
import com.eprovement.poptavka.shared.domain.PropertiesDetail;
import com.eprovement.poptavka.shared.domain.adminModule.LogDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;
import java.util.List;

/**
 * Interface for managing SystemSettings widget.
 *
 * <h4>Note</h4>
 * Manage Gateway and Presenter in 1:1 relation.
 * Any change to this interface will automatically recquire change in implementation
 * which is good. Easy usage search, which is also very usefull.
 * Also doctypes are available despite calling through eventbus.
 *
 * @author Martin Slavkovsky
 * @since 18.2.2014
 */
public interface IAdminSystemSettings {

    public interface Gateway {

        /**
         * Inits SystemSettings widget.
         * @param holder where widget's view will be set
         */
        @Event(handlers = AdminSystemSettingsPresenter.class)
        void initAdminSystemSettings();

        /**
         * Sets widget's profile data with given system's data.
         * @param detail object carrying system's profile data
         */
        @Event(handlers = AdminSystemSettingsPresenter.class)
        void setSystemSettings();

        /**
         * Fills given system's profile data with current widget's data.
         * @param detail to be updated
         * @return updated detail object
         */
        @Event(handlers = AdminSystemSettingsPresenter.class, passive = true)
        void fillSystemSettings(SettingDetail detail);
    }

    public interface Presenter {

        /**
         * @see Gateway#initSystemSettings(SimplePanel)
         */
        void onInitAdminSystemSettings();

        void onResponseSystemProperties(List<PropertiesDetail> properties);

        void onResponseJobProgress(LogType job, LogDetail result);
    }

    public interface View extends LazyView, ProvidesValidate, IsWidget {

        void setDemandCountsProgress(LogDetail detail);

        void setSupplierCountsProgress(LogDetail detail);

        FlowPanel getPropertiesPanel();

        Button getDemandCountsBtn();

        Button getSupplierCountsBtn();
    }
}
