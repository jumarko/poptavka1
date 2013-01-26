package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierInfoPresenter;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierInfoPresenter.SupplierInfoInterface;
import com.eprovement.poptavka.client.common.services.ServicesSelectorPresenter;
import com.eprovement.poptavka.client.common.services.ServicesSelectorView;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.logging.Logger;

@Presenter(view = SupplierCreationView.class, multiple = true)
public class SupplierCreationPresenter
        extends LazyPresenter<SupplierCreationPresenter.CreationViewInterface, SupplierCreationEventBus>
        implements NavigationConfirmationInterface {

    // Main Panel Tab Constants
    private static final int FIRST_TAB_BASIC = 0;
    private static final int SECOND_TAB_CATEGORY = 1;
    private static final int THIRD_TAB_LOCALITY = 2;
    private static final int FOURTH_TAB_SERVICE = 3;
    private final static Logger LOGGER = Logger.getLogger("SupplierCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private SupplierInfoPresenter supplierBasicForm = null;
    private ServicesSelectorPresenter supplierServiceForm = null;
    private int maxSelectedTab = -1;

    public interface CreationViewInterface extends LazyView, IsWidget {

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        Alert getAlert(int order);

        Icon getIcon(int order);

        /** Buttons. **/
        HasClickHandlers getRegisterButton();

        /** Other. **/
        Anchor getConditionLink();

        void showConditions();

        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        LOGGER.info("SupplierCreationPresenter loaded");
        eventBus.setBody(view.getWidgetView());
        Storage.setCurrentlyLoadedView(Constants.CREATE_SUPPLIER);
        maxSelectedTab = -1;
        eventBus.setUpSearchBar(null);
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Used to navigate/invoke supplier creation module.
     */
    public void onGoToCreateSupplierModule() {
        view.getMainPanel().selectTab(FIRST_TAB_BASIC);
        eventBus.registerTabToken(FIRST_TAB_BASIC);
        eventBus.initSupplierBasicForm(view.getHolderPanel(FIRST_TAB_BASIC));
        //remove widgets to force widget to init them again
        view.getHolderPanel(SECOND_TAB_CATEGORY).setWidget(null);
        view.getHolderPanel(THIRD_TAB_LOCALITY).setWidget(null);
        view.getHolderPanel(FOURTH_TAB_SERVICE).setWidget(null);
    }

    public void onGoToCreateSupplierModuleByHistory(int selectedTab) {
        eventBus.setHistoryStoredForNextOne(false);
        view.getMainPanel().selectTab(selectedTab);
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    @Override
    public void bindView() {
        addMainPanelBeforeSelectionHandler();
        addMainPanelSelectionHandler();
        addRegisterButtonHandler();
        addConditionLinkHandler();
    }

    private void addMainPanelBeforeSelectionHandler() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getSelectedIndex() < eventItem) {
                    //if previous step is valid, continue
                    if (canContinue(eventItem - 1)) {
                        view.getAlert(eventItem - 1).setType(AlertType.SUCCESS);
                        view.getAlert(eventItem - 1).setText(MSGS.commonOK());
                        view.getIcon(eventItem - 1).setType(IconType.OK_SIGN);
                    } else {
                        view.getAlert(eventItem - 1).setType(AlertType.ERROR);
                        view.getAlert(eventItem - 1).setText(getErrorInfoLabel(eventItem - 1));
                        view.getIcon(eventItem - 1).setType(IconType.REMOVE_SIGN);
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
            case FIRST_TAB_BASIC:
                eventBus.registerTabToken(FIRST_TAB_BASIC);
                break;
            case SECOND_TAB_CATEGORY:
                LOGGER.info(" -> Category Widget");
                eventBus.registerTabToken(SECOND_TAB_CATEGORY);
                if (maxSelectedTab < SECOND_TAB_CATEGORY) {
                    if (view.getHolderPanel(SECOND_TAB_CATEGORY).getWidget() == null) {
                        eventBus.initCategoryWidget(
                                view.getHolderPanel(SECOND_TAB_CATEGORY),
                                Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                                CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS,
                                null);
                    }
                }
                break;
            case THIRD_TAB_LOCALITY:
                LOGGER.info(" -> Locality Widget");
                eventBus.registerTabToken(THIRD_TAB_LOCALITY);
                if (maxSelectedTab < THIRD_TAB_LOCALITY) {
                    if (view.getHolderPanel(THIRD_TAB_LOCALITY).getWidget() == null) {
                        eventBus.initLocalityWidget(
                                view.getHolderPanel(THIRD_TAB_LOCALITY),
                                Constants.WITH_CHECK_BOXES,
                                CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS,
                                null);
                    }
                }
                break;
            case FOURTH_TAB_SERVICE:
                LOGGER.info(" -> init Service Form supplierService");
                eventBus.registerTabToken(FOURTH_TAB_SERVICE);
                if (maxSelectedTab < FOURTH_TAB_SERVICE) {
                    if (view.getHolderPanel(FOURTH_TAB_SERVICE).getWidget() == null) {
                        eventBus.initServicesWidget(view.getHolderPanel(FOURTH_TAB_SERVICE));
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

    private void addRegisterButtonHandler() {
        view.getRegisterButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(FOURTH_TAB_SERVICE)) {
                    LOGGER.fine("registxer him!");
                    registerSupplier();
                } else {
                    LOGGER.fine("cannot continue");
                }
            }
        });
    }

    private void addConditionLinkHandler() {
        view.getConditionLink().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.showConditions();
            }
        });

    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    // Inject widgets
    //--------------------------------------------------------------------------
    public void onInitSupplierBasicForm(SimplePanel holderWidget) {
        if (supplierBasicForm != null) {
            eventBus.removeHandler(supplierBasicForm);
        }
        supplierBasicForm = eventBus.addHandler(SupplierInfoPresenter.class);
        supplierBasicForm.initSupplierForm(holderWidget);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    private void registerSupplier() {
        SupplierInfoInterface info =
                (SupplierInfoInterface) view.getHolderPanel(FIRST_TAB_BASIC).getWidget();
        CategorySelectorInterface cats =
                (CategorySelectorInterface) view.getHolderPanel(SECOND_TAB_CATEGORY).getWidget();
        LocalitySelectorInterface locs =
                (LocalitySelectorInterface) view.getHolderPanel(THIRD_TAB_LOCALITY).getWidget();
        ServicesSelectorView service =
                (ServicesSelectorView) view.getHolderPanel(FOURTH_TAB_SERVICE).getWidget();

        FullSupplierDetail newSupplier = new FullSupplierDetail();
        newSupplier.setUserData(info.createBusinessUserDetail());
        newSupplier.setLocalities(locs.getCellListDataProvider().getList());
        newSupplier.setCategories(cats.getCellListDataProvider().getList());
        newSupplier.setServices(Arrays.asList(service.getSelectedService()));

        eventBus.registerSupplier(newSupplier);
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterSupplier());
    }

    private String getErrorInfoLabel(int step) {
        switch (step) {
            case SECOND_TAB_CATEGORY:
                return MSGS.supplierCreationSecondTabErrorInfo();
            case THIRD_TAB_LOCALITY:
                return MSGS.supplierCreationThirdTabErrorInfo();
            case FOURTH_TAB_SERVICE:
                return MSGS.supplierCreationFourthTabErrorInfo();
            default:
                return "";
        }
    }

    private boolean canContinue(int step) {
        if (step == FOURTH_TAB_SERVICE) {
            //is aggreement terms checked?
            return view.isValid();
        } else {
            ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
            LOGGER.fine(widget.getClass().getName());
            return widget.isValid();
        }
    }
}
