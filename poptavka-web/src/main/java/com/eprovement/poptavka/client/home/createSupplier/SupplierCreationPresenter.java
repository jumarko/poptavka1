/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.logging.Logger;

/**
 * Supplier creation presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SupplierCreationView.class, multiple = true)
public class SupplierCreationPresenter
        extends LazyPresenter<SupplierCreationPresenter.CreationViewInterface, SupplierCreationEventBus>
        implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface CreationViewInterface extends LazyView, IsWidget, ProvidesValidate {

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        SimplePanel getFooterPanel();

        /** Buttons. **/
        Button getRegisterButton();

        /** Other. **/
        Tooltip getNextBtnTooltip(int order);

        CheckBox getAgreedCheck();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final int FIRST_TAB_USER_REGISTRATION = 0;
    private static final int SECOND_TAB_CATEGORY = 1;
    private static final int THIRD_TAB_LOCALITY = 2;
    private static final int FOURTH_TAB_SERVICES = 3;
    private final static Logger LOGGER = Logger.getLogger("SupplierCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private int maxSelectedTab = 1;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    /**
     * Sets body, toolbar, footer, search bar on each module forwad event.
     */
    public void onForward() {
        LOGGER.info("SupplierCreationPresenter loaded");
        Storage.setCurrentlyLoadedView(Constants.CREATE_SUPPLIER);
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Became Professional", null);
        eventBus.setFooter(view.getFooterPanel());
        eventBus.resetSearchBar(null);
        eventBus.menuStyleChange(Constants.CREATE_SUPPLIER);
        maxSelectedTab = 1;
        view.getAgreedCheck().setValue(false);
        view.getRegisterButton().setEnabled(true);
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing by default
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    @Override
    public void bindView() {
        addMainPanelBeforeSelectionHandler();
        addMainPanelSelectionHandler();
        addRegisterButtonHandler();
    }

    /**
     * Binds tab layout before selection handlers.
     * Check if current step is valid before continuing.
     */
    private void addMainPanelBeforeSelectionHandler() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                //If there is unvalidated tab between clicked tabs, cancel event.
                if (maxSelectedTab + 1 >= eventItem) {
                    //if selecting other tab, check if present tab is valid
                    if (view.getMainPanel().getSelectedIndex() < eventItem) {
                        //if present tab is valid, continue
                        if (!canContinue(view.getMainPanel().getSelectedIndex())) {
                            displayTooltip();
                            event.cancel();
                        }
                        //define how far am i allowed to click
                        //If there is unvalidated tab between clicked tabs, cancel event.
                        if (maxSelectedTab < eventItem) {
                            maxSelectedTab = eventItem;
                        }
                    }
                } else {
                    event.cancel();
                }
            }
        });
    }

    /**
     * Binds tab layout selection handler.
     * Inits particular step's widget.
     */
    private void addMainPanelSelectionHandler() {
        view.getMainPanel().addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                addMainPanelSelectionHandlerInner(event);
            }
        });
    }

    /**
     * Tab layout selection handler inner class.
     * Inits particular step's widget.
     */
    private void addMainPanelSelectionHandlerInner(SelectionEvent<Integer> event) {
        switch (event.getSelectedItem()) {
            case FIRST_TAB_USER_REGISTRATION:
                LOGGER.info(" -> Login Or Registration Selection Form");
                eventBus.checkCompanySelected();
                break;
            case SECOND_TAB_CATEGORY:
                LOGGER.info(" -> Category Widget");
                if (view.getHolderPanel(SECOND_TAB_CATEGORY).getWidget() == null) {
                    eventBus.initCatLocSelector(
                            view.getHolderPanel(SECOND_TAB_CATEGORY),
                            new CatLocSelectorBuilder.Builder(Constants.CREATE_SUPPLIER)
                                .initCategorySelector()
                                .initSelectorManager()
                                .withCheckboxesOnLeafsOnly()
                                .displayCountOfSuppliers()
                                .setSelectionRestriction(Constants.REGISTER_MAX_CATEGORIES)
                                .build());
                }
                setHeightSelector();
                break;
            case THIRD_TAB_LOCALITY:
                LOGGER.info(" -> Locality Widget");
                if (view.getHolderPanel(THIRD_TAB_LOCALITY).getWidget() == null) {
                    eventBus.initCatLocSelector(
                            view.getHolderPanel(THIRD_TAB_LOCALITY),
                            new CatLocSelectorBuilder.Builder(Constants.CREATE_SUPPLIER)
                                .initLocalitySelector()
                                .initSelectorManager()
                                .withCheckboxesOnLeafsOnly()
                                .displayCountOfSuppliers()
                                .setSelectionRestriction(Constants.REGISTER_MAX_LOCALITIES)
                                .build());
                }
                setHeightSelector();
                break;
            case FOURTH_TAB_SERVICES:
                LOGGER.info(" -> init Service Form supplierService");
                if (view.getHolderPanel(FOURTH_TAB_SERVICES).getWidget() == null) {
                    eventBus.initServicesWidget(ServiceType.SUPPLIER, view.getHolderPanel(FOURTH_TAB_SERVICES));
                }
                setHeightServices();
                break;
            default:
                break;
        }
    }

    /**
     * Binds register button hadnler.
     */
    private void addRegisterButtonHandler() {
        view.getRegisterButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(FOURTH_TAB_SERVICES)) {
                    LOGGER.fine("register him!");
                    view.getRegisterButton().setEnabled(false);
                    registerSupplier();
                } else {
                    LOGGER.fine("cannot continue");
                    displayTooltip();
                }
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Initializes SupplierCreation module.
     */
    public void onGoToCreateSupplierModule() {
        view.getMainPanel().selectTab(FIRST_TAB_USER_REGISTRATION);
        eventBus.initUserRegistration(view.getHolderPanel(FIRST_TAB_USER_REGISTRATION));
        setHeightRegistration();
        //remove widgets to force widget to init them again
        view.getHolderPanel(SECOND_TAB_CATEGORY).setWidget(null);
        view.getHolderPanel(THIRD_TAB_LOCALITY).setWidget(null);
        view.getHolderPanel(FOURTH_TAB_SERVICES).setWidget(null);
    }

    /**
     * Sets registration form height.
     * @param company - true if company form is visible
     */
    public void onSetUserRegistrationHeight(boolean company) {
        if (company) {
            setHeightRegistrationExtended();
        } else {
            setHeightRegistration();
        }
    }

    /**************************************************************************/
    /* Helsper methods                                                        */
    /**************************************************************************/
    /**
     * Register supplier.
     * Creates supplier detail and fill its attributes by calling fill events of
     * each step.
     */
    private void registerSupplier() {
        FullSupplierDetail newSupplier = new FullSupplierDetail();

        eventBus.fillBusinessUserDetail(newSupplier.getUserData());
        eventBus.fillCatLocs(newSupplier.getCategories(), Constants.CREATE_SUPPLIER);
        eventBus.fillCatLocs(newSupplier.getLocalities(), -Constants.CREATE_SUPPLIER);
        eventBus.fillServices(newSupplier.getServices());

        eventBus.registerSupplier(newSupplier);
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterSupplier());
    }

    /**
     * Check if current widget's components are valid before continuing.
     * @param step - current step
     * @return true if valid, false otherwise
     */
    private boolean canContinue(int step) {
        boolean valid = true;
        if (step == FOURTH_TAB_SERVICES) {
            valid = view.isValid();
        }

        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        return valid && widget.isValid();
    }

    /**
     * Displays tooltip on next button if something is missing.
     */
    private void displayTooltip() {
        view.getNextBtnTooltip(view.getMainPanel().getSelectedIndex()).show();
        Timer timer = new Timer() {

            @Override
            public void run() {
                view.getNextBtnTooltip(view.getMainPanel().getSelectedIndex()).hide();
            }
        };
        timer.schedule(Constants.VALIDATION_TOOLTIP_DISPLAY_TIME);
    }

    /**
     * Sets <b>services</b> tab layout height.
     */
    private void setHeightServices() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightBasic());
    }

    /**
     * Sets <b>selector</b> tab layout height.
     */
    private void setHeightSelector() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightSelector());
    }

    /**
     * Sets <b>registration</b> tab layout height.
     */
    private void setHeightRegistration() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistration());
    }

    /**
     * Sets <b>registration extended</b> tab layout height.
     */
    private void setHeightRegistrationExtended() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistrationExtended());
    }

    /**
     * Clear tab layout height.
     */
    private void clearHeight() {
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightBasic());
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightSelector());
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistration());
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistrationExtended());
    }
}
