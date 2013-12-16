/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.catLocSelector.others.CatLogSimpleCell;
import com.eprovement.poptavka.client.common.forms.RatingInfoForm;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import java.util.List;

/**
 * View consists of forms for editing categories, localities and services.
 * Also contains form for displaying supplier's rating.
 * <b><i>Note:</i></b>
 * For updating categories and localities a CatLocSelector widget is used.
 * The widget is displayed in SimpleConfirmPopup.
 *
 * @author Martin Slavkovsky
 */
public class SupplierSettingsView extends Composite
    implements SupplierSettingsPresenter.SupplierSettingsViewInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierSettingsViewUiBinder uiBinder = GWT.create(SupplierSettingsViewUiBinder.class);

    interface SupplierSettingsViewUiBinder extends UiBinder<Widget, SupplierSettingsView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
        StyleResource.INSTANCE.details().ensureInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) CellList categories, localities;
    @UiField RatingInfoForm ratingInfoForm;
    @UiField SimplePanel servicePanel;
    @UiField Button editCatBtn, editLocBtn;
    /** Class attributes. **/
    private ListDataProvider categoryProvider;
    private ListDataProvider localityProvider;
    private SimpleConfirmPopup selectorPopup;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates SupplierSettings view's compontents.
     */
    @Override
    public void createView() {
        categories = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        categoryProvider = new ListDataProvider(CatLocDetail.KEY_PROVIDER);
        categoryProvider.addDataDisplay(categories);

        localities = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        localityProvider = new ListDataProvider(CatLocDetail.KEY_PROVIDER);
        localityProvider.addDataDisplay(localities);

        selectorPopup = new SimpleConfirmPopup();

        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets SupplierSettings profile data.
     * @param detail carrying supplier's profile data
     */
    @Override
    public void setSupplierSettings(SettingDetail detail) {
        if (detail.getSupplier() != null
            && detail.getSupplier().getOveralRating() != null) {
            this.ratingInfoForm.setRating(detail.getSupplier().getOveralRating());
            this.categoryProvider.setList(detail.getSupplier().getCategories());
            this.localityProvider.setList(detail.getSupplier().getLocalities());
        }
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANELS. **/
    /**
     * @return the SimpleConfirmPopup
     */
    @Override
    public SimpleConfirmPopup getSelectorPopup() {
        return selectorPopup;
    }

    /**
     * @return the services container
     */
    @Override
    public SimplePanel getServicePanel() {
        return servicePanel;
    }

    /** BUTTONS. **/
    /**
     * @return the edit categories button
     */
    @Override
    public Button getEditCatBtn() {
        return editCatBtn;
    }

    /**
     * @return the edit localities button
     */
    @Override
    public Button getEditLocBtn() {
        return editLocBtn;
    }

    /** OTHERES. **/
    /**
     * @return list of updated categories
     */
    @Override
    public List<ICatLocDetail> getCategories() {
        return this.categoryProvider.getList();
    }

    /**
     * @return list of updated localities
     */
    @Override
    public List<ICatLocDetail> getLocalities() {
        return this.localityProvider.getList();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        return !categoryProvider.getList().isEmpty() && !localityProvider.getList().isEmpty();
    }

    /**
     * @return the widget view
     */
    @Override
    public SupplierSettingsView getWidgetView() {
        return this;
    }
}