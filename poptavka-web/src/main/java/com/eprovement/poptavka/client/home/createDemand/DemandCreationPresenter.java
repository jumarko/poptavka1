package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createDemand.FormUserRegistrationPresenter.FormRegistrationInterface;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter.FormDemandBasicInterface;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.logging.Logger;

@Presenter(view = DemandCreationView.class)
public class DemandCreationPresenter
        extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, DemandCreationEventBus> {

    private final static Logger LOGGER = Logger.getLogger("DemandCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    // Main Panel Tab Constants
    private static final int FIRST_TAB_BASIC = 0;
    private static final int SECOND_TAB_CATEGORY = 1;
    private static final int THIRD_TAB_LOCALITY = 2;
    private static final int FOURTH_TAB_ADVANCE = 3;
    private static final int FIFTH_TAB_LOGIN = 4;
    // TODO praso - All this presenters should be moved into this particular
    // module otherwise they will fall in left-over fragment
    private FormDemandBasicPresenter demandBasicForm = null;
    private FormDemandAdvPresenter demandAdvForm = null;
    private int maxSelectedTab = -1;

    public interface CreationViewInterface extends LazyView, IsWidget {

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        StatusIconLabel getStatusLabel(int order);

        SimplePanel getHolderPanel(int order);

        /** Buttons. **/
        HasClickHandlers getCreateDemandButton();

        HasClickHandlers getNextButton1();

        HasClickHandlers getNextButton2();

        HasClickHandlers getNextButton3();

        HasClickHandlers getNextButton4();

        /** Other. **/
        void toggleLoginRegistration();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    @Override
    public void bindView() {
        addMainPanelBeforeSelectionHandler();
        addMainPanelSelectionHandler();
        addCreateDemandButtonHandler();
        addNextButtonsHandlers();
    }

    // Bind helper methods
    //--------------------------------------------------------------------------
    private void addMainPanelBeforeSelectionHandler() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getSelectedIndex() < eventItem) {
                    //if previous step is valid, continue
                    if (canContinue(eventItem - 1)) {
                        view.getStatusLabel(eventItem - 1).setPassedSmall(true);
                    } else {
                        view.getStatusLabel(eventItem - 1).setPassedSmall(false);
                        event.cancel();
                    }
                }
            }
        });
    }

    private void addMainPanelSelectionHandler() {
        view.getMainPanel().addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                switch (event.getSelectedItem()) {
                    case FIRST_TAB_BASIC:
                        LOGGER.info(" -> Supplier Info Form");
                        if (maxSelectedTab < FIRST_TAB_BASIC) {
                            eventBus.initDemandBasicForm(view.getHolderPanel(FIRST_TAB_BASIC));
                        }
                    case SECOND_TAB_CATEGORY:
                        LOGGER.info(" -> Category Widget");
                        if (maxSelectedTab < SECOND_TAB_CATEGORY) {
                            eventBus.initCategoryWidget(
                                    view.getHolderPanel(SECOND_TAB_CATEGORY),
                                    Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                                    CategoryCell.DISPLAY_COUNT_OF_DEMANDS);
                        }
                        break;
                    case THIRD_TAB_LOCALITY:
                        LOGGER.info(" -> Locality Widget");
                        if (maxSelectedTab < THIRD_TAB_LOCALITY) {
                            eventBus.initLocalityWidget(
                                    view.getHolderPanel(THIRD_TAB_LOCALITY),
                                    Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                                    CategoryCell.DISPLAY_COUNT_OF_DEMANDS);
                        }
                        break;
                    case FOURTH_TAB_ADVANCE:
                        LOGGER.info(" -> init Demand Form supplierService");
                        if (maxSelectedTab < FOURTH_TAB_ADVANCE) {
                            eventBus.initDemandAdvForm(view.getHolderPanel(FOURTH_TAB_ADVANCE));
                        }
                        break;
                    case FIFTH_TAB_LOGIN:
                        LOGGER.info(" -> init Login Form supplierService");
                        if (maxSelectedTab < FIFTH_TAB_LOGIN) {
                            eventBus.initLoginForm(view.getHolderPanel(FIFTH_TAB_LOGIN));
                        }
                        break;
                    default:
                        break;
                }
                if (maxSelectedTab < event.getSelectedItem()) {
                    maxSelectedTab = event.getSelectedItem();
                }
            }
        });
    }

    private void addCreateDemandButtonHandler() {
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(FIFTH_TAB_LOGIN)) {
                    registerNewCient();
                }
            }
        });
    }

    private void addNextButtonsHandlers() {
        ClickHandler clickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getMainPanel().selectTab(view.getMainPanel().getSelectedIndex() + 1, true);
            }
        };
        view.getNextButton1().addClickHandler(clickHandler);
        view.getNextButton2().addClickHandler(clickHandler);
        view.getNextButton3().addClickHandler(clickHandler);
        view.getNextButton4().addClickHandler(clickHandler);
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        eventBus.initDemandBasicForm(view.getHolderPanel(FIRST_TAB_BASIC));
    }

    public void onForward() {
        LOGGER.info("DemandCreationPresenter loaded");
        Storage.setCurrentlyLoadedView(Constants.HOME_CREATE_DEMAND);
        maxSelectedTab = -1;
        eventBus.setUpSearchBar(null);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Used to navigate/invoke demand creation module.
     */
    public void onGoToCreateDemandModule() {
        //nothing
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
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

    /**
     * Called from DemandCreationHandler after successful login or registration
     * of new client. As first a new client is created through DemandCreationHandler
     * and after successful registration (or login of existing client) this method
     * is initiated by DemandCreationHandler to create new Demand for this client.
     *
     * @param client detail object that was either created (if new client) or
     * retried from database (if existing client)
     */
    public void onPrepareNewDemandForNewClient(BusinessUserDetail client) {
        eventBus.loadingShow(MSGS.progressGettingDemandData());

        FormDemandBasicInterface basicValues =
                (FormDemandBasicInterface) view.getHolderPanel(FIRST_TAB_BASIC).getWidget();
        CategorySelectorInterface categoryValues =
                (CategorySelectorInterface) view.getHolderPanel(SECOND_TAB_CATEGORY).getWidget();
        LocalitySelectorInterface localityValues =
                (LocalitySelectorInterface) view.getHolderPanel(THIRD_TAB_LOCALITY).getWidget();
        FormDemandAdvViewInterface advValues =
                (FormDemandAdvViewInterface) view.getHolderPanel(FOURTH_TAB_ADVANCE).getWidget();

        // Fill in the FullDemandDetail obejct from former holder panels.
        FullDemandDetail demand = new FullDemandDetail();
        demand.setBasicInfo(basicValues.getValues());

        //localities
        demand.setLocalities(new ArrayList<LocalityDetail>(localityValues.getCellListDataProvider().getList()));

        //categories
        demand.setCategories(new ArrayList<CategoryDetail>(categoryValues.getCellListDataProvider().getList()));

        demand.setAdvInfo(advValues.getValues());
        eventBus.createDemand(demand, client.getClientId());
        // TODO Praso - improve loaging method. We can for example show some message to client while waiting
        eventBus.loadingShow(MSGS.progressCreatingDemand());

    }

    /** Done automatically in step five, when option: register new client is selected. **/
    //DO NOT EDIT
    public void onToggleLoginRegistration() {
        view.toggleLoginRegistration();
    }

    /** showing error after login failure. **/
    public void onLoginError() {
        // TODO change to global status changer eventBus call
        view.getStatusLabel(FIFTH_TAB_LOGIN).setStyleState(
                StyleResource.INSTANCE.common().errorMessage(), State.ERROR_16);
        view.getStatusLabel(FIFTH_TAB_LOGIN).setTexts(MSGS.wrongLoginMessage(), MSGS.wrongLoginDescription());
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**
     * In the process of creating a new demand by brand new client the method
     * registerNewClient() is called as first. When the registration of new client
     * is successful the demand will be created in the next step. See method
     * onPrepareNewDemandForNewClient().
     */
    private void registerNewCient() {
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterClient());
        // ClientDetail instance
        FormRegistrationInterface registerWidget =
                (FormRegistrationInterface) view.getHolderPanel(FIFTH_TAB_LOGIN).getWidget();
        BusinessUserDetail newClient = registerWidget.getNewClient();
        eventBus.registerNewClient(newClient);

    }

    private boolean canContinue(int step) {
        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        LOGGER.fine(widget.getClass().getName());
        return widget.isValid();
    }
}
