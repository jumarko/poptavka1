package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter;
import com.eprovement.poptavka.client.home.createDemand.widget.FormDemandBasicPresenter.FormDemandBasicInterface;
import com.eprovement.poptavka.client.home.createDemand.widget.FormUserRegistrationPresenter;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
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
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.logging.Logger;

@Presenter(view = DemandCreationView.class, multiple = true)
public class DemandCreationPresenter
        extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, DemandCreationEventBus> {

    private final static Logger LOGGER = Logger.getLogger("DemandCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    // Main Panel Tab Constants
    private static final int FIRST_TAB_LOGIN = 0;
    private static final int SECOND_TAB_FORM = 1;
    private static final int THIRD_TAB_CATEGORY = 2;
    private static final int FOURTH_TAB_LOCALITY = 3;
    private static final int FIFTH_TAB_ADVANCE = 4;
    // TODO praso - All this presenters should be moved into this particular
    // module otherwise they will fall in left-over fragment
    private FormDemandBasicPresenter demandBasicForm = null;
    private FormDemandAdvPresenter demandAdvForm = null;
    private FormUserRegistrationPresenter registrationForm = null;
    private int maxSelectedTab = -1;
    //flags
    private boolean blockFirstTab = false;

    public interface CreationViewInterface extends LazyView, IsWidget {

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        /** Labels. **/
        StatusIconLabel getStatusLabel(int order);

        /** Buttons. **/
        //Comment - what's the difference in compile report if using return values: HasClickHandlers vs Button
        Button getCreateDemandButton();

        Button getLoginBtn();

        Button getRegisterBtn();

        /** Headers. **/
        HTML getFirstTabHeader();

        /** Other. **/
//        void toggleLoginRegistration();
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
        addLoginButtonHandler();
        addRegisterButtonHandler();
        addCreateDemandButtonHandler();
    }

    // Bind helper methods
    //--------------------------------------------------------------------------
    private void addMainPanelBeforeSelectionHandler() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (eventItem == FIRST_TAB_LOGIN && blockFirstTab) {
                    event.cancel();
                }
                if (view.getMainPanel().getSelectedIndex() < eventItem) {
                    //if previous step is valid, continue
                    if (canContinue(eventItem - 1)) {
                        view.getStatusLabel(eventItem - 1).setPassedSmall(true);
                        view.getStatusLabel(eventItem - 1).setMessage(MSGS.ok());
                    } else {
                        view.getStatusLabel(eventItem - 1).setPassedSmall(false);
                        view.getStatusLabel(eventItem - 1).setMessage(getErrorInfoLabel(eventItem - 1));
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
                addMainPanelSelectionHandlerInner(event);
            }
        });
    }

    private void addMainPanelSelectionHandlerInner(SelectionEvent<Integer> event) {
        switch (event.getSelectedItem()) {
            case FIRST_TAB_LOGIN:
                eventBus.registerTabToken(FIRST_TAB_LOGIN);
                break;
            case SECOND_TAB_FORM:
                LOGGER.info(" -> Supplier Info Form");
                eventBus.registerTabToken(SECOND_TAB_FORM);
                if (maxSelectedTab < SECOND_TAB_FORM) {
                    if (view.getHolderPanel(SECOND_TAB_FORM).getWidget() == null) {
                        eventBus.initDemandBasicForm(view.getHolderPanel(SECOND_TAB_FORM));
                    }
                }
                break;
            case THIRD_TAB_CATEGORY:
                LOGGER.info(" -> Category Widget");
                eventBus.registerTabToken(THIRD_TAB_CATEGORY);
                if (maxSelectedTab < THIRD_TAB_CATEGORY) {
                    if (view.getHolderPanel(THIRD_TAB_CATEGORY).getWidget() == null) {
                        eventBus.initCategoryWidget(
                                view.getHolderPanel(THIRD_TAB_CATEGORY),
                                Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                                CategoryCell.DISPLAY_COUNT_OF_DEMANDS,
                                null);
                    }
                }
                break;
            case FOURTH_TAB_LOCALITY:
                LOGGER.info(" -> Locality Widget");
                eventBus.registerTabToken(FOURTH_TAB_LOCALITY);
                if (maxSelectedTab < FOURTH_TAB_LOCALITY) {
                    if (view.getHolderPanel(FOURTH_TAB_LOCALITY).getWidget() == null) {
                        eventBus.initLocalityWidget(
                                view.getHolderPanel(FOURTH_TAB_LOCALITY),
                                Constants.WITH_CHECK_BOXES,
                                CategoryCell.DISPLAY_COUNT_OF_DEMANDS,
                                null);
                    }
                }
                break;
            case FIFTH_TAB_ADVANCE:
                LOGGER.info(" -> init Demand Form supplierService");
                eventBus.registerTabToken(FIFTH_TAB_ADVANCE);
                if (maxSelectedTab < FIFTH_TAB_ADVANCE) {
                    if (view.getHolderPanel(FIFTH_TAB_ADVANCE).getWidget() == null) {
                        eventBus.initDemandAdvForm(view.getHolderPanel(FIFTH_TAB_ADVANCE));
                    }
                }
                break;
            default:
                break;
        }
        if (maxSelectedTab < event.getSelectedItem()) {
            maxSelectedTab = event.getSelectedItem();
        }
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
                view.getLoginBtn().setVisible(false);
                view.getRegisterBtn().setVisible(false);
                view.getStatusLabel(FIRST_TAB_LOGIN).setMessage(MSGS.registerInfoLabel());
//                eventBus.toggleLoginRegistration();
                eventBus.initRegistrationForm(view.getHolderPanel(FIRST_TAB_LOGIN));
            }
        });
    }

    private void addCreateDemandButtonHandler() {
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(FIFTH_TAB_ADVANCE)) {
                    createNewDemand(Storage.getBusinessUserDetail());
                }
            }
        });
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        eventBus.registerTabToken(FIRST_TAB_LOGIN);
    }

    public void onForward() {
        LOGGER.info("DemandCreationPresenter loaded");
        Storage.setCurrentlyLoadedView(Constants.CREATE_DEMAND);
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
        //remove widgets to force widget to init them again
        onRestoreDefaultFirstTab();
        view.getHolderPanel(SECOND_TAB_FORM).setWidget(null);
        view.getHolderPanel(THIRD_TAB_CATEGORY).setWidget(null);
        view.getHolderPanel(FOURTH_TAB_LOCALITY).setWidget(null);
        view.getHolderPanel(FIFTH_TAB_ADVANCE).setWidget(null);
        if (Storage.getUser() != null) {
            blockFirstTab = true;
            //TODO Jaro - add some style which look like disable
//            view.getFirstTabHeader().setStyleName("");
            view.getMainPanel().selectTab(SECOND_TAB_FORM);
        } else {
            blockFirstTab = false;
            //in case back functionality was performed, set selected tab to first
            view.getMainPanel().selectTab(FIRST_TAB_LOGIN);
            //and reset first tab header css
            //TODO Jaro - remove style which look like disable
//            view.getFirstTabHeader().removeStyleName("");
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

    public void onInitRegistrationForm(SimplePanel holderWidget) {
        if (registrationForm != null) {
            eventBus.removeHandler(registrationForm);
        }
        registrationForm = eventBus.addHandler(FormUserRegistrationPresenter.class);
        registrationForm.initRegistrationForm(holderWidget);
    }

    // Responses
    //--------------------------------------------------------------------------
    /** Done automatically in step five, when option: register new client is selected. **/
    // -- don't need anymore - after moving registration as first step
    //DO NOT EDIT
//    public void onToggleLoginRegistration() {
//        view.toggleLoginRegistration();
//    }
    /** showing error after login failure. **/
    public void onLoginError() {
        // TODO change to global status changer eventBus call
        view.getStatusLabel(FIRST_TAB_LOGIN).setStyleState(
                StyleResource.INSTANCE.common().errorMessage(), State.ERROR_16);
        view.getStatusLabel(FIRST_TAB_LOGIN).setTexts(MSGS.wrongLoginMessage(), MSGS.wrongLoginDescription());
    }

    public void onRestoreDefaultFirstTab() {
        view.getHolderPanel(FIRST_TAB_LOGIN).clear();
        view.getLoginBtn().setVisible(true);
        view.getRegisterBtn().setVisible(true);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
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
                (FormDemandBasicInterface) view.getHolderPanel(SECOND_TAB_FORM).getWidget();
        CategorySelectorInterface categoryValues =
                (CategorySelectorInterface) view.getHolderPanel(THIRD_TAB_CATEGORY).getWidget();
        LocalitySelectorInterface localityValues =
                (LocalitySelectorInterface) view.getHolderPanel(FOURTH_TAB_LOCALITY).getWidget();
        FormDemandAdvViewInterface advValues =
                (FormDemandAdvViewInterface) view.getHolderPanel(FIFTH_TAB_ADVANCE).getWidget();

        // Fill in the FullDemandDetail obejct from former holder panels.
        FullDemandDetail demand = new FullDemandDetail();
        demand.setBasicInfo(basicValues.getValues());

        //localities
        demand.setLocalities(new ArrayList<LocalityDetail>(localityValues.getCellListDataProvider().getList()));

        //categories
        demand.setCategories(new ArrayList<CategoryDetail>(categoryValues.getCellListDataProvider().getList()));

        demand.setAdvInfo(advValues.getValues());
        eventBus.createDemand(demand, client.getClientId());
    }

    private String getErrorInfoLabel(int step) {
        switch (step) {
            case SECOND_TAB_FORM:
                return MSGS.validationErrorInfoLabel();
            case THIRD_TAB_CATEGORY:
                return MSGS.categorySelectorInfoLabel();
            case FOURTH_TAB_LOCALITY:
                return MSGS.localitySelectorInfoLabel();
            case FIFTH_TAB_ADVANCE:
                return MSGS.validationErrorInfoLabel();
            default:
                return "";
        }
    }

    private boolean canContinue(int step) {
        if (step == FIRST_TAB_LOGIN) {
            return true;
        } else {
            ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
            LOGGER.fine(widget.getClass().getName());
            return widget.isValid();
        }
    }
}