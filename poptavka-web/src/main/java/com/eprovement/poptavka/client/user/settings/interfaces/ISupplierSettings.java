/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.interfaces;

import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;
import java.util.List;

/**
 * Interface for managing SupplierSettings widget.
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
public interface ISupplierSettings {

    public interface Gateway {

        /**
         * Inits SupplierSettings widget.
         * @param holder where widget's view will be set
         */
        @Event(handlers = SupplierSettingsPresenter.class)
        void initSupplierSettings(SimplePanel holder);

        /**
         * Sets widget's profile data with given supplier's data.
         * @param detail object carrying supplier's profile data
         */
        @Event(handlers = SupplierSettingsPresenter.class)
        void setSupplierSettings(SettingDetail detail);

        /**
         * Fills given supplier's profile data with current widget's data.
         * @param detail to be updated
         * @return updated detail object
         */
        @Event(handlers = SupplierSettingsPresenter.class, passive = true)
        void fillSupplierSettings(SettingDetail detail);
    }

    public interface Presenter {

        /**
         * @see Gateway#initSupplierSettings(SimplePanel)
         */
        void onInitSupplierSettings(SimplePanel holder);

        /**
         * @see Gateway#setSupplierSettings(SettingDetail)
         */
        void onSetSupplierSettings(SettingDetail detail);

        /**
         * @see Gateway#fillSupplierSettings(SettingDetail)
         */
        void onFillSupplierSettings(SettingDetail detail);
    }

    public interface View extends LazyView, ProvidesValidate, IsWidget {

        /**
         * @see Gateway#initSupplierSettings(SimplePanel)
         */
        void setSupplierSettings(SettingDetail detail);

        /**
         * @return the SimpleConfirmPopup
         */
        SimpleConfirmPopup getSelectorPopup();

        /**
         * @return the services container
         */
        SimplePanel getServicePanel();

        /**
         * @return list of updated categories
         */
        List<ICatLocDetail> getCategories();

        /**
         * @return list of updated localities
         */
        List<ICatLocDetail> getLocalities();

        CellList getCategoriesList();

        CellList getLocalitiesList();
        //Buttons
        /**
         * @return the edit categories button
         */
        Button getEditCatBtn();

        /**
         * @return the edit localities button
         */
        Button getEditLocBtn();
    }
}
