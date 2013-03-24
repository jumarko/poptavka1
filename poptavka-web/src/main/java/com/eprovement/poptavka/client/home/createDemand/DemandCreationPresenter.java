package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.userRegistration.UserRegistrationFormView;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter.FormDemandBasicInterface;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
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

@Presenter(view = DemandCreationView.class, multiple = true)
public class DemandCreationPresenter
        extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, DemandCreationEventBus>
        implements NavigationConfirmationInterface {

    private final static Logger LOGGER = Logger.getLogger("DemandCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    // Main Panel Tab Constants
    private static final int FIRST_TAB_LOGIN_REGISTER_FORM = 0;
    private static final int SECONT_TAB_DEMAND_BASIC_FORM = 1;
    private static final int THIRD_TAB_CATEGORY = 2;
    private static final int FOURTH_TAB_LOCALITY = 3;
    private static final int FIFTH_TAB_DEMAND_ADVANCE_FORM = 4;
    // TODO praso - All this presenters should be moved into this particular
    // module otherwise they will fall in left-over fragment
    private FormDemandBasicPresenter demandBasicForm = null;
    private FormDemandAdvPresenter demandAdvForm = null;
    private int maxSelectedTab = 1;

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
        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
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

    private void addMainPanelSelectionHandler() {
        view.getMainPanel().addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                addMainPanelSelectionHandlerInner(event);
            }
        });
    }

    private void addMainPanelSelectionHandlerInner(SelectionEvent<Integer> event) {
        switch (event.getSelectedItem()) {
            case FIRST_TAB_LOGIN_REGISTER_FORM:
                eventBus.registerTabToken(FIRST_TAB_LOGIN_REGISTER_FORM);
                break;
            case SECONT_TAB_DEMAND_BASIC_FORM:
                LOGGER.info(" -> Supplier Info Form");
                eventBus.registerTabToken(SECONT_TAB_DEMAND_BASIC_FORM);
                if (view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM).getWidget() == null) {
                    eventBus.initDemandBasicForm(view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM));
                }
                break;
            case THIRD_TAB_CATEGORY:
                LOGGER.info(" -> Category Widget");
                eventBus.registerTabToken(THIRD_TAB_CATEGORY);
                if (view.getHolderPanel(THIRD_TAB_CATEGORY).getWidget() == null) {
                    eventBus.initCategoryWidget(
                            view.getHolderPanel(THIRD_TAB_CATEGORY),
                            Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                            CategoryCell.DISPLAY_COUNT_OF_DEMANDS,
                            null);
                }
                break;
            case FOURTH_TAB_LOCALITY:
                LOGGER.info(" -> Locality Widget");
                eventBus.registerTabToken(FOURTH_TAB_LOCALITY);
                if (view.getHolderPanel(FOURTH_TAB_LOCALITY).getWidget() == null) {
                    eventBus.initLocalityWidget(
                            view.getHolderPanel(FOURTH_TAB_LOCALITY),
                            Constants.WITH_CHECK_BOXES,
                            CategoryCell.DISPLAY_COUNT_OF_DEMANDS,
                            null);
                }
                break;
            case FIFTH_TAB_DEMAND_ADVANCE_FORM:
                LOGGER.info(" -> init Demand Form supplierService");
                eventBus.registerTabToken(FIFTH_TAB_DEMAND_ADVANCE_FORM);
                if (view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM).getWidget() == null) {
                    eventBus.initDemandAdvForm(view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM));
                }
                break;
            default:
                break;
        }
    }

    private void addBackButtonHandler() {
        view.getBackButtonTab1().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.restoreDefaultFirstTab();
            }
        });
    }

    private void addLoginButtonHandler() {
        view.getLoginBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.login(Constants.CREATE_DEMAND);
            }
        });
    }

    private void addRegisterButtonHandler() {
        view.getRegisterBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.setRegisterLayout();
                eventBus.initUserRegistrationForm(view.getHolderPanel(FIRST_TAB_LOGIN_REGISTER_FORM));
            }
        });
    }

    private void addRegisterClientButtonHandler() {
        view.getNextButtonTab1().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (((UserRegistrationFormView) view.getHolderPanel(FIRST_TAB_LOGIN_REGISTER_FORM)
                        .getWidget()).isValid()) {
                    registerNewClient();
                }
            }
        });
    }

    private void addCreateDemandButtonHandler() {
        view.getNextButtonTab5().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(FIFTH_TAB_DEMAND_ADVANCE_FORM)) {
                    createNewDemand(Storage.getBusinessUserDetail());
                }
            }
        });
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        LOGGER.info("DemandCreationPresenter loaded");
        Storage.setCurrentlyLoadedView(Constants.CREATE_DEMAND);
        eventBus.setBody(view.getWidgetView());
        eventBus.setUpSearchBar(null);
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.CREATE_DEMAND);
        } else {
            eventBus.userMenuStyleChange(Constants.CREATE_DEMAND);
        }
        maxSelectedTab = 1;
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Used to navigate/invoke demand creation module.
     */
    public void onGoToCreateDemandModule() {
        //must be set to null to force beforeSelectionHandler to handle selection
        //and create new instance of widgets
        view.getHolderPanel(SECONT_TAB_DEMAND_BASIC_FORM).setWidget(null);
        view.getHolderPanel(THIRD_TAB_CATEGORY).setWidget(null);
        view.getHolderPanel(FOURTH_TAB_LOCALITY).setWidget(null);
        view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM).setWidget(null);
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

    public void onGoToCreateDemandModuleByHistory(int selectedTab) {
        eventBus.setHistoryStoredForNextOne(false);
        view.getMainPanel().selectTab(selectedTab);
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    // Inject widgets
    //--------------------------------------------------------------------------
    public void onInitDemandBasicForm(SimplePanel holderWidget) {
        if (demandBasicForm != null) {
            eventBus.removeHandler(demandBasicForm);
        }
        demandBasicForm = eventBus.addHandler(FormDemandBasicPresenter.class);
        demandBasicForm.initDemandBasicForm(holderWidget);
    }

    public void onInitDemandAdvForm(SimplePanel holderWidget) {
        if (demandAdvForm != null) {
            eventBus.removeHandler(demandAdvForm);
        }
        demandAdvForm = eventBus.addHandler(FormDemandAdvPresenter.class);
        demandAdvForm.initDemandAdvForm(holderWidget);
    }

    // Responses
    //--------------------------------------------------------------------------
    public void onRestoreDefaultFirstTab() {
        view.getMainPanel().selectTab(FIRST_TAB_LOGIN_REGISTER_FORM);
        eventBus.registerTabToken(FIRST_TAB_LOGIN_REGISTER_FORM);
        view.getHolderPanel(FIRST_TAB_LOGIN_REGISTER_FORM).clear();
        view.setLoginLayout();
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void registerNewClient() {
        eventBus.loadingShow(MSGS.progressRegisterClient());
        UserRegistrationFormView accountInfo =
                (UserRegistrationFormView) view.getHolderPanel(FIRST_TAB_LOGIN_REGISTER_FORM).getWidget();

        BusinessUserDetail user = accountInfo.createBusinessUserDetail();
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
        CategorySelectorInterface categoryValues =
                (CategorySelectorInterface) view.getHolderPanel(THIRD_TAB_CATEGORY).getWidget();
        LocalitySelectorInterface localityValues =
                (LocalitySelectorInterface) view.getHolderPanel(FOURTH_TAB_LOCALITY).getWidget();
        FormDemandAdvViewInterface advValues =
                (FormDemandAdvViewInterface) view.getHolderPanel(FIFTH_TAB_DEMAND_ADVANCE_FORM).getWidget();

        // Fill in the FullDemandDetail obejct from former holder panels.
        FullDemandDetail demand = new FullDemandDetail();
        demand = basicValues.updateBasicDemandInfo(demand);
        demand = advValues.updateAdvDemandInfo(demand);
        demand.setLocalities(localityValues.getCellListDataProvider().getList());
        demand.setCategories(categoryValues.getCellListDataProvider().getList());
        eventBus.createDemand(demand, client.getClientId());
    }

    private boolean canContinue(int step) {
        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        return widget.isValid();
    }
}