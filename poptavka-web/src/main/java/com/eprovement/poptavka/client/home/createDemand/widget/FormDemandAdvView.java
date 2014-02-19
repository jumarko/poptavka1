/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.UrgencySelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * From demand advanced view.
 * @author Beho, Martin Slavkovsky
 */
public class FormDemandAdvView extends Composite
        implements FormDemandAdvPresenter.FormDemandAdvViewInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static FormDemandAdvViewUiBinder uiBinder = GWT.create(FormDemandAdvViewUiBinder.class);

    interface FormDemandAdvViewUiBinder extends UiBinder<Widget, FormDemandAdvView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField HTMLPanel demandTypeChoicePanel;
    @UiField IntegerBox maxOffersBox;
    @UiField Button normalBtn, attractiveBtn;
    @UiField UrgencySelectorView urgencySelector;
    /** Class attributes. **/
    private boolean attractiveSelected;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates demand advanced form view's components.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /**
     * Binds handler for normal demand type choice.
     */
    @UiHandler("normalBtn")
    public void normalBtnClickHandler(ClickEvent e) {
        attractiveSelected = false;
        setDemandTypeChoicePanelStyles(false);
    }

    /**
     * Binds handler for atractive demand type choice.
     */
    @UiHandler("attractiveBtn")
    public void attractiveBtnClickHandler(ClickEvent e) {
        attractiveSelected = true;
        setDemandTypeChoicePanelStyles(true);
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Toggle company info.
     * @param attractiveSelected
     */
    public void setDemandTypeChoicePanelStyles(boolean attractiveSelected) {
        if (attractiveSelected) {
            demandTypeChoicePanel.removeStyleName(Storage.RSCS.common().switchLeft());
            demandTypeChoicePanel.addStyleName(Storage.RSCS.common().switchRight());
        } else {
            demandTypeChoicePanel.removeStyleName(Storage.RSCS.common().switchRight());
            demandTypeChoicePanel.addStyleName(Storage.RSCS.common().switchLeft());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        //nothing by default
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * Validates view components.
     * @return true if view components are valid, false otherwise
     */
    @Override
    public boolean isValid() {
        if (maxOffersBox.getValue() == null) {
            maxOffersBox.setStyleName(StyleResource.INSTANCE.common().errorField());
            return false;
        } else {
            maxOffersBox.removeStyleName(StyleResource.INSTANCE.common().errorField());
            return true;
        }
    }

    /**
     * Fills appropriate attributes to given demand detail.
     * @param demandToUpdate - demand detail that is to be updated
     * @return updated demand detail
     */
    @Override
    public FullDemandDetail updateAdvDemandInfo(FullDemandDetail demandToUpdate) {
        demandToUpdate.setMaxSuppliers(maxOffersBox.getValue());
        demandToUpdate.setMinRating(0);
        demandToUpdate.setDemandType(getDemandType());
        demandToUpdate.setValidTo(urgencySelector.getValidTo());
        return demandToUpdate;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Get selected demand type.
     * @return Selected demand type as string.
     */
    private String getDemandType() {
        if (attractiveSelected) {
            return FullDemandDetail.DemandType.ATTRACTIVE.getValue();
        } else {
            return FullDemandDetail.DemandType.NORMAL.getValue();
        }
    }
}
