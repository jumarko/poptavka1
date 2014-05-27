/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.common.GATracker;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter.FormDemandBasicInterface;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
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
 * DemandCreation presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = DemandCreationView.class, multiple = true)
public class DemandCreationPresenter
    extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, DemandCreationEventBus>
    implements NavigationConfirmationInterface {

    /**************************************************************************/
    /* DemandCreation view interface                                          */
    /**************************************************************************/
    public interface CreationViewInterface extends LazyView, IsWidget {

        void setFirstTabVisibility(boolean visible);

        void setLoginLayout();

        void setRegisterLayout();

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        /** Buttons. **/
        //Comment - what's the difference in compile report if using return values: HasClickHandlers vs Button
        Button getLoginBtn();

        Button getRegisterBtn();

        //register client button
        Button getNextButtonTab1();

        //create demand button
        Button getNextButtonTab5();

        //restore first tab defaults
        Button getBackButtonTab1();

        /** Headers. **/
        HTML getHeaderLabelTab1();

        /** Other. **/
        Tooltip getNextBtnTooltip(int order);

        SimplePanel getFooterPanel();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final static Logger LOGGER = Logger.getLogger("DemandCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    // Main Panel Tab Constants
    private static final int FIRST_TAB_LOGIN_REGISTER_FORM = 0;
    private static final int SECONT_TAB_DEMAND_BASIC_FORM = 1;
    private static final int THIRD_TAB_CATEGORY = 2;
    private static final int FOURTH_TAB_LOCALITY = 3;
    private static final int FIFTH_TAB_DEMAND_ADVANCE_FORM = 4;
    private int maxSelectedTab = 1;
    private int instaceIdCategories;
    private int instaceIdLocalities;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    /**
     * Sets body, toolbar, footer, search bar on each module forward.
     */
    public void onForward() {
        LOGGER.info("DemandCreationPresenter loaded");
        GATracker.trackPageview(Window.Location.getHref());
        Storage.setCurrentlyLoadedView(Constants.CREATE_DEMAND);
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Post a Project", null);
        eventBus.setFooter(view.getFooterPanel());
        eventBus.resetSearchBar(null);
        eventBus.menuStyleChange(Constants.CREATE_DEMAND);
        maxSelectedTab = 1;
        view.getNextButtonTab1().setEnabled(true); //register client btn
        view.getNextButtonTab5().setEnabled(true); //create demand btn
        view.setLoginLayout();
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing by default
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    /**
     * Binds handlers.
     */
    @Override
    public void bindView() {
        //Main panel handlers
        addMainPanelBeforeSelectionHandler();
        addMainPanelSelectionHandler();
        //Buttons handlers
        addBackButtonHandler();
        addLoginButtonHandler();
        addRegisterButtonHandler();
        addRegisterClientButtonHandler();
        addCreateDemandButtonHandler();
    }

    // Bind helper methods
    //--------------------------------------------------------------------------
    /**
     * Binds TabLayout before selection handler.
     * Perform validation before continuing.
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
                        if (view.getMainPanel().getSelectedIndex() == FIRST_TAB_LOGIN_REGISTER_FORM) {
                            event.cancel();
                        } else {
                            if (!canContinue(view.getMainPanel().getSelectedIndex())) {
                                displayTooltip();
                                event.cancel();
                            }
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
     * Binds TabLayout selection handler.
     * Inits particular widgets.
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
     * TabLayout selection handler inner class.
     * Inits particular widgets.
     */
    private void addMainPanelSelectionHandlerInner(SelectionEvent<Integer> event) {
        switch (event.getSelectedItem()) {
            case FIRST_TAB_LOGIN_REGISTER_FORM:
                LOGGER.info(" -> Login Or Registration Selection Form");
                setHeightBasic();
                break;
            case SECONT_TAB_DEMAND_BASIC_FORM:
                LOGGER.info(" -> Demand Basic Info Form");
                if (view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM).getWidget() == null) {
                    eventBus.initDemandBasicForm(view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM));
                }
                setHeightBasic();
                break;
            case THIRD_TAB_CATEGORY:
                LOGGER.info(" -> Category Widget");
                if (view.getHolderPanel(THIRD_TAB_CATEGORY).getWidget() == null) {
                    CatLocSelectorBuilder builder = new CatLocSelectorBuilder.Builder(Constants.CREATE_DEMAND)
                            .initCategorySelector()
                            .initSelectorManager()
                            .withCheckboxes()
                            .displayCountOfDemands()
                            .setSelectionRestriction(Constants.REGISTER_MAX_CATEGORIES)
                            .build();
                    instaceIdCategories = builder.getInstanceId();
                    eventBus.initCatLocSelector(view.getHolderPanel(THIRD_TAB_CATEGORY), builder);
                }
                setHeightSelector();
                break;
            case FOURTH_TAB_LOCALITY:
                LOGGER.info(" -> Locality Widget");
                if (view.getHolderPanel(FOURTH_TAB_LOCALITY).getWidget() == null) {
                    CatLocSelectorBuilder builder = new CatLocSelectorBuilder.Builder(Constants.CREATE_DEMAND)
                            .initLocalitySelector()
                            .initSelectorManager()
                            .withCheckboxes()
                            .displayCountOfDemands()
                            .setSelectionRestriction(Constants.REGISTER_MAX_LOCALITIES)
                            .build();
                    instaceIdLocalities = builder.getInstanceId();
                    eventBus.initCatLocSelector(view.getHolderPanel(FOURTH_TAB_LOCALITY), builder);
                }
                setHeightSelector();
                break;
            case FIFTH_TAB_DEMAND_ADVANCE_FORM:
                LOGGER.info(" -> init Demand Form supplierService");
                if (view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM).getWidget() == null) {
                    eventBus.initDemandAdvForm(view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM));
                }
                setHeightAdvanced();
                break;
            default:
                break;
        }
    }

    /**
     * Binds back button hanlder.
     */
    private void addBackButtonHandler() {
        view.getBackButtonTab1().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.restoreDefaultFirstTab();
                setHeightBasic();
            }
        });
    }

    /**
     * Binds login button handler.
     */
    private void addLoginButtonHandler() {
        view.getLoginBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GATracker.trackEvent("DemandCreation", "Login");
                eventBus.login(Constants.CREATE_DEMAND);
            }
        });
    }

    /**
     * Binds registration button handler.
     */
    private void addRegisterButtonHandler() {
        view.getRegisterBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GATracker.trackEvent("DemandCreation", "Registration");
                view.setRegisterLayout();
                setHeightRegistration();
                eventBus.initUserRegistration(view.getHolderPanel(FIRST_TAB_LOGIN_REGISTER_FORM));
            }
        });
    }

    /**
     * Binds registration button handler.
     */
    private void addRegisterClientButtonHandler() {
        view.getNextButtonTab1().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (((ProvidesValidate) view.getHolderPanel(FIRST_TAB_LOGIN_REGISTER_FORM).getWidget()).isValid()) {
                    view.getNextButtonTab1().setEnabled(false);
                    registerNewClient();
                }
            }
        });
    }

    /**
     * Binds create demand button handler.
     */
    private void addCreateDemandButtonHandler() {
        view.getNextButtonTab5().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(FIFTH_TAB_DEMAND_ADVANCE_FORM)) {
                    view.getNextButtonTab5().setEnabled(false);
                    createNewDemand(Storage.getBusinessUserDetail());
                }
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Initialize DemandCreation module.
     */
    public void onGoToCreateDemandModule() {
        //must be set to null to force beforeSelectionHandler to handle selection
        //and create new instance of widgets
        view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM).setWidget(null);
        view.getHolderPanel(THIRD_TAB_CATEGORY).setWidget(null);
        view.getHolderPanel(FOURTH_TAB_LOCALITY).setWidget(null);
        view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM).setWidget(null);
        setHeightBasic();
        if (Storage.getUser() != null) {
            view.setFirstTabVisibility(false);
            view.getMainPanel().selectTab(SECONT_TAB_DEMAND_BASIC_FORM, false);
            view.getMainPanel().addStyleName(Storage.RSCS.createTabPanel().fourStepTabPanel());
            eventBus.initDemandBasicForm(view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM));
        } else {
            view.getMainPanel().removeStyleName(Storage.RSCS.createTabPanel().fourStepTabPanel());
            view.setFirstTabVisibility(true);
            //remove widgets to force widget to init them again
            onRestoreDefaultFirstTab();
        }
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

    /**
     * Restore first tab.
     */
    public void onRestoreDefaultFirstTab() {
        view.getMainPanel().selectTab(FIRST_TAB_LOGIN_REGISTER_FORM);
        view.getHolderPanel(FIRST_TAB_LOGIN_REGISTER_FORM).clear();
        view.setLoginLayout();
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Registers new client.
     */
    private void registerNewClient() {
        eventBus.loadingShow(MSGS.progressRegisterClient());

        BusinessUserDetail user = new BusinessUserDetail();
        eventBus.fillBusinessUserDetail(user);
        //register a shof popup na activation code
        eventBus.registerNewClient(user);
    }

    /**
     * Called from DemandCreationHandler after successful login or registration
     * of new client. As first a new client is created through DemandCreationHandler
     * and after successful registration (or login of existing client) this method
     * is initiated by DemandCreationHandler to create new Demand for this client.
     *
     * @param client detail object that was either created (if new client) or
     * retried from database (if existing client)
     */
    private void createNewDemand(BusinessUserDetail client) {
        eventBus.loadingShow(MSGS.progressCreatingDemand());

        FormDemandBasicInterface basicValues =
            (FormDemandBasicInterface) view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM).getWidget();
        FormDemandAdvViewInterface advValues =
            (FormDemandAdvViewInterface) view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM).getWidget();

        // Fill in the FullDemandDetail obejct from former holder panels.
        FullDemandDetail demand = new FullDemandDetail();
        demand = basicValues.updateBasicDemandInfo(demand);
        demand = advValues.updateAdvDemandInfo(demand);
        eventBus.fillCatLocs(demand.getCategories(), instaceIdCategories);
        eventBus.fillCatLocs(demand.getLocalities(), instaceIdLocalities);
        eventBus.createDemand(demand, client.getClientId());
    }

    /**
     * Check if can continue to next step.
     * @param step to be validated
     * @return true if current step is valid, false otherwise.
     */
    private boolean canContinue(int step) {
        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        return widget.isValid();
    }

    /**
     * Displays next button tooltip.
     * Notifies user if something is missing.
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
     * Sets <b>basic</b> tab layout height
     */
    private void setHeightBasic() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightBasic());
    }

    /**
     * Sets <b>advanced</b> tab layout height
     */
    private void setHeightAdvanced() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightAdvanced());
    }

    /**
     * Sets <b>selector</b> tab layout height
     */
    private void setHeightSelector() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightSelector());
    }

    /**
     * Sets <b>registration</b> tab layout height
     */
    private void setHeightRegistration() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistration());
    }

    /**
     * Sets <b>registration extended</b> tab layout height
     */
    private void setHeightRegistrationExtended() {
        clearHeight();
        view.getMainPanel().addStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistrationExtended());
    }

    /**
     * Clears tab layout height.
     */
    private void clearHeight() {
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightBasic());
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightAdvanced());
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightSelector());
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistration());
        view.getMainPanel().removeStyleName(StyleResource.INSTANCE.createTabPanel().heightRegistrationExtended());
    }
}