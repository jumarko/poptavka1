/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createDemand.widget.ButtonsPanel;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Tooltip;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

/**
 * Supplier creation view.
 *
 * @author Beho, Martin Slavkovsky
 */
public class SupplierCreationView extends OverflowComposite
        implements SupplierCreationPresenter.CreationViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);

    interface CreationViewUiBinder extends UiBinder<Widget, SupplierCreationView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
        CssInjector.INSTANCE.ensureCreateTabPanelStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel contentHolder1, contentHolder2, contentHolder3, contentHolder4;
    @UiField SimplePanel footerPanel;
    @UiField TabLayoutPanel mainPanel;
    @UiField ButtonsPanel buttonsPanel1, buttonsPanel2, buttonsPanel3, buttonsPanel4;
    @UiField CheckBox conditionCheck;
    @UiField Anchor conditionLink;
    @UiField Icon conditionValidationImage;
    /** Class attributes. **/
    private List<SimplePanel> holderPanels;
    private List<Tooltip> tooltips;
    private ClickHandler nextClickHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            selectNextTab();
        }
    };
    private ClickHandler backClickHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            selectPreviousTab();
        }
    };


    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates supplier creation view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        buttonsPanel1.getBackBtn().setVisible(false);
        bindHandlers();

        /** filling panels list **/
        holderPanels = Arrays.asList(contentHolder1, contentHolder2, contentHolder3, contentHolder4);
        tooltips = Arrays.asList(buttonsPanel1.getNextBtnTooltip(), buttonsPanel2.getNextBtnTooltip(),
            buttonsPanel3.getNextBtnTooltip(), buttonsPanel4.getNextBtnTooltip());

        for (SimplePanel panel : holderPanels) {
            setParentOverflow(panel, Overflow.AUTO);
        }
    }

    /**************************************************************************/
    /* UiHandler                                                              */
    /**************************************************************************/
    /**
     * Binds agreed check handler.
     */
    @UiHandler("conditionCheck")
    public void conditionCheckClickHandler(ClickEvent event) {
        isConditionChecked();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** PANELS. **/
    /**
     * @return the tab layout panel
     */
    @Override
    public TabLayoutPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * @param order of holder panel
     * @return the particular holder panel
     */
    @Override
    public SimplePanel getHolderPanel(int order) {
        return holderPanels.get(order);
    }

    /** BUTTONS. **/
    /**
     * @return the registration button
     */
    @Override
    public Button getRegisterButton() {
        return buttonsPanel4.getNextBtn();
    }

    @Override
    public Anchor getTermsAndConditionsButton() {
        return conditionLink;
    }

    /** OTHERS. **/
    /**
     * @param order of the next tooltip
     * @return the particular next tooltip
     */
    @Override
    public Tooltip getNextBtnTooltip(int order) {
        return tooltips.get(order);
    }

    /**
     * @return agreed checkbox
     */
    @Override
    public CheckBox getAgreedCheck() {
        return conditionCheck;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        for (SimplePanel holder : holderPanels) {
            if (holder.getWidget() != null) {
                ((ProvidesValidate) holder.getWidget()).reset();
            }
        }
    }

    /**
     * @return true if agreement checkbox is checked, false otherwise.
     */
    @Override
    public boolean isValid() {
        return isConditionChecked();
    }

    /**
     * @return the footer container
     */
    @Override
    public SimplePanel getFooterPanel() {
        return footerPanel;
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
     * Check if Terms & Conditions are checked.
     * @return true if checked, false otherwise
     */
    private boolean isConditionChecked() {
        conditionValidationImage.setVisible(!conditionCheck.getValue());
        if (conditionCheck.getValue()) {
            conditionLink.removeStyleName("color-red");
        } else {
            conditionLink.addStyleName("color-red");
        }
        return conditionCheck.getValue();
    }
    /**
     * Binds back & next buttons handlers.
     */
    private void bindHandlers() {
        //back click handler
        buttonsPanel1.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel2.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel3.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel4.getBackBtn().addClickHandler(backClickHandler);
        //next click handler
        buttonsPanel1.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel2.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel3.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel4.getNextBtn().addClickHandler(nextClickHandler);
    }

    /**
     * Selects text tab.
     */
    private void selectNextTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() + 1, true);
    }

    /**
     * Selects previous tab.
     */
    private void selectPreviousTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() - 1, true);
    }
}