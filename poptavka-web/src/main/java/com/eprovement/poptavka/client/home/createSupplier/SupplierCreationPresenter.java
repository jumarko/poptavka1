package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.services.ServicesSelectorPresenter;
import com.eprovement.poptavka.client.common.services.ServicesSelectorView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierAccountInfoPresenter;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierDetailInfoPresenter;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
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
import com.google.gwt.user.client.ui.Label;
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
    private static final int FIRST_SUPPLIER_ACCOUNT_TAB = 0;
    private static final int SECOND_SUPPLIER_DETAIL_TAB = 1;
    private static final int THIRD_TAB_CATEGORY = 2;
    private static final int FOURTH_TAB_LOCALITY = 3;
    private static final int FIFTH_TAB_SERVICE = 4;
    private final static Logger LOGGER = Logger.getLogger("SupplierCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private SupplierAccountInfoPresenter supplierAccountForm = null;
    private SupplierDetailInfoPresenter supplierDetailForm = null;
    private ServicesSelectorPresenter supplierServiceForm = null;
    private int maxSelectedTab = -1;

    public interface CreationViewInterface extends LazyView, IsWidget {

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        Alert getAlert(int order);

        Icon getIcon(int order);

        Label getLabel(int order);

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
        view.getMainPanel().selectTab(FIRST_SUPPLIER_ACCOUNT_TAB);
        eventBus.registerTabToken(FIRST_SUPPLIER_ACCOUNT_TAB);
        eventBus.initSupplierAccountInfoForm(view.getHolderPanel(FIRST_SUPPLIER_ACCOUNT_TAB));
        //remove widgets to force widget to init them again
        view.getHolderPanel(SECOND_SUPPLIER_DETAIL_TAB).setWidget(null);
        view.getHolderPanel(THIRD_TAB_CATEGORY).setWidget(null);
        view.getHolderPanel(FOURTH_TAB_LOCALITY).setWidget(null);
        view.getHolderPanel(FIFTH_TAB_SERVICE).setWidget(null);
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
                        view.getLabel(eventItem - 1).setText(MSGS.commonOK());
                        view.getIcon(eventItem - 1).setType(IconType.OK_SIGN);
                    } else {
                        view.getAlert(eventItem - 1).setType(AlertType.ERROR);
                        view.getLabel(eventItem - 1).setText(getErrorInfoLabel(eventItem - 1));
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
            case FIRST_SUPPLIER_ACCOUNT_TAB:
                eventBus.registerTabToken(FIRST_SUPPLIER_ACCOUNT_TAB);
                break;
            case SECOND_SUPPLIER_DETAIL_TAB:
                eventBus.registerTabToken(FIRST_SUPPLIER_ACCOUNT_TAB);
                if (maxSelectedTab < SECOND_SUPPLIER_DETAIL_TAB) {
                    if (view.getHolderPanel(SECOND_SUPPLIER_DETAIL_TAB).getWidget() == null) {
                        eventBus.initSupplierDetailInfoForm(view.getHolderPanel(SECOND_SUPPLIER_DETAIL_TAB));
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
                                CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS,
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
                                CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS,
                                null);
                    }
                }
                break;
            case FIFTH_TAB_SERVICE:
                LOGGER.info(" -> init Service Form supplierService");
                eventBus.registerTabToken(FIFTH_TAB_SERVICE);
                if (maxSelectedTab < FIFTH_TAB_SERVICE) {
                    if (view.getHolderPanel(FIFTH_TAB_SERVICE).getWidget() == null) {
                        eventBus.initServicesWidget(view.getHolderPanel(FIFTH_TAB_SERVICE));
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
                if (canContinue(FIFTH_TAB_SERVICE)) {
                    LOGGER.fine("register him!");
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
    public void onInitSupplierAccountInfoForm(SimplePanel holderWidget) {
        if (supplierAccountForm != null) {
            eventBus.removeHandler(supplierAccountForm);
        }
        supplierAccountForm = eventBus.addHandler(SupplierAccountInfoPresenter.class);
        supplierAccountForm.initSupplierForm(holderWidget);
    }

    public void onInitSupplierDetailInfoForm(SimplePanel holderWidget) {
        if (supplierDetailForm != null) {
            eventBus.removeHandler(supplierDetailForm);
        }
        supplierDetailForm = eventBus.addHandler(SupplierDetailInfoPresenter.class);
        supplierDetailForm.initSupplierForm(holderWidget);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    private void registerSupplier() {
        SupplierAccountInfoPresenter.SupplierAccountInfoInterface accountInfo =
                (SupplierAccountInfoPresenter.SupplierAccountInfoInterface) view.getHolderPanel(
                    FIRST_SUPPLIER_ACCOUNT_TAB).getWidget();
        SupplierDetailInfoPresenter.SupplierDetailInfoInterface detailinfo =
                (SupplierDetailInfoPresenter.SupplierDetailInfoInterface) view.getHolderPanel(
                    SECOND_SUPPLIER_DETAIL_TAB).getWidget();
        CategorySelectorInterface cats =
                (CategorySelectorInterface) view.getHolderPanel(THIRD_TAB_CATEGORY).getWidget();
        LocalitySelectorInterface locs =
                (LocalitySelectorInterface) view.getHolderPanel(FOURTH_TAB_LOCALITY).getWidget();
        ServicesSelectorView service =
                (ServicesSelectorView) view.getHolderPanel(FIFTH_TAB_SERVICE).getWidget();

        FullSupplierDetail newSupplier = new FullSupplierDetail();
        BusinessUserDetail user = new BusinessUserDetail();
        user = accountInfo.updateBusinessUserDetail(user);
        user = detailinfo.updateBusinessUserDetail(user);
        newSupplier.setUserData(user);
        newSupplier.setLocalities(locs.getCellListDataProvider().getList());
        newSupplier.setCategories(cats.getCellListDataProvider().getList());
        newSupplier.setServices(Arrays.asList(service.getSelectedService()));

        eventBus.registerSupplier(newSupplier);
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterSupplier());
    }

    private String getErrorInfoLabel(int step) {
        switch (step) {
            case SECOND_SUPPLIER_DETAIL_TAB:
                return MSGS.supplierCreationSecondTabErrorInfo();
            case THIRD_TAB_CATEGORY:
                return MSGS.supplierCreationThirdTabErrorInfo();
            case FOURTH_TAB_LOCALITY:
                return MSGS.supplierCreationFourthTabErrorInfo();
            case FIFTH_TAB_SERVICE:
                return MSGS.supplierCreationFifthTabErrorInfo();
            default:
                return "";
        }
    }

    private boolean canContinue(int step) {
        if (step == FIFTH_TAB_SERVICE) {
            //is aggreement terms checked?
            return view.isValid();
        } else {
            ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
            LOGGER.fine(widget.getClass().getName());
            return widget.isValid();
        }
    }
}
