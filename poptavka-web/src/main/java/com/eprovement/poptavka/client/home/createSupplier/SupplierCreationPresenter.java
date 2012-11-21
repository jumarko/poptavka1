package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.home.createSupplier.widget.ServiceWidget;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierInfoPresenter.SupplierInfoInterface;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
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
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.logging.Logger;

@Presenter(view = SupplierCreationView.class)
public class SupplierCreationPresenter
        extends LazyPresenter<SupplierCreationPresenter.CreationViewInterface, SupplierCreationEventBus> {

    // Main Panel Tab Constants
    private static final int FIRST_TAB_BASIC = 0;
    private static final int SECOND_TAB_CATEGORY = 1;
    private static final int THIRD_TAB_LOCALITY = 2;
    private static final int FOURTH_TAB_SERVICE = 3;
    private final static Logger LOGGER = Logger.getLogger("SupplierCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private int maxSelectedTab = -1;

    public interface CreationViewInterface extends LazyView, IsWidget {

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        StatusIconLabel getStatusLabel(int order);

        /** Buttons. **/
        HasClickHandlers getRegisterButton();

        HasClickHandlers getNextButton1();

        HasClickHandlers getNextButton2();

        HasClickHandlers getNextButton3();

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
    }

    public void onForward() {
        LOGGER.info("SupplierCreationPresenter loaded");
        Storage.setCurrentlyLoadedView(Constants.HOME_CREATE_SUPPLIERS);
        maxSelectedTab = -1;
        eventBus.setUpSearchBar(null);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Used to navigate/invoke supplier creation module.
     */
    public void onGoToCreateSupplierModule() {
        eventBus.initSupplierForm(view.getHolderPanel(FIRST_TAB_BASIC));
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
        addNextButtonsHandlers();
    }

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
                            //Otherwise load widget
                            eventBus.initSupplierForm(view.getHolderPanel(FIRST_TAB_BASIC));
                        } else {
                            //If already loaded, just activate presenter for holding requests
                            eventBus.activateAddressWidgetPresenter();
                        }
                    case SECOND_TAB_CATEGORY:
                        LOGGER.info(" -> Category Widget");
                        if (maxSelectedTab < SECOND_TAB_CATEGORY) {
                            eventBus.initCategoryWidget(
                                    view.getHolderPanel(SECOND_TAB_CATEGORY),
                                    Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                                    CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS);
                        }
                        //Don't need to active CategorySelectorPresenter, because he is the only one
                        //action listener after data retrieving process.
                        //(In Localities, there are two listeners after data are retrieved)
                        break;
                    case THIRD_TAB_LOCALITY:
                        LOGGER.info(" -> Locality Widget");
                        if (maxSelectedTab < THIRD_TAB_LOCALITY) {
                            eventBus.initLocalityWidget(
                                    view.getHolderPanel(THIRD_TAB_LOCALITY),
                                    Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                                    CategoryCell.DISPLAY_COUNT_OF_SUPPLIERS);
                        } else {
                            eventBus.activateLocalityWidgetPresenter();
                        }
                        break;
                    case FOURTH_TAB_SERVICE:
                        LOGGER.info(" -> init Service Form supplierService");
                        if (maxSelectedTab < FOURTH_TAB_SERVICE) {
                            eventBus.initServiceForm(view.getHolderPanel(FOURTH_TAB_SERVICE));
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
        ServiceWidget service =
                (ServiceWidget) view.getHolderPanel(FOURTH_TAB_SERVICE).getWidget();

        BusinessUserDetail newSupplier = info.createSupplier();
        newSupplier.getSupplier().setLocalities(locs.getCellListDataProvider().getList());
        newSupplier.getSupplier().setCategories(cats.getCellListDataProvider().getList());
        newSupplier.getSupplier().addService(service.getSelectedService());

        eventBus.registerSupplier(newSupplier);
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterClient());
    }

    private boolean canContinue(int step) {
        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        LOGGER.fine(widget.getClass().getName());
        return widget.isValid();
    }
}
