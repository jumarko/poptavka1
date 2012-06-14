package com.eprovement.poptavka.client.home.createSupplier;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierInfoPresenter;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierInfoPresenter.SupplierInfoInterface;
import com.eprovement.poptavka.client.home.createSupplier.widget.SupplierServicePresenter;
import com.eprovement.poptavka.client.main.common.SimpleIconLabel;
import com.eprovement.poptavka.client.main.common.StatusIconLabel;
import com.eprovement.poptavka.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import com.eprovement.poptavka.client.main.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.main.common.service.ServiceWidget;
import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.service.demand.SupplierCreationRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@Presenter(view = SupplierCreationView.class)
public class SupplierCreationPresenter
        extends LazyPresenter<SupplierCreationPresenter.CreationViewInterface, SupplierCreationEventBus> {

    private final static Logger LOGGER = Logger.getLogger("SupplierCreationPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface CreationViewInterface extends LazyView {

        StackLayoutPanel getMainPanel();

        SimplePanel getSupplierInfoHolder();

        SimplePanel getLocalityHolder();

        SimplePanel getCategoryHolder();

        SimplePanel getServiceHolder();

        HasClickHandlers getRegisterButton();

        Anchor getConditionLink();

        Widget getWidgetView();

        StatusIconLabel getStatusLabel(int order);

        void showConditions();

        boolean isValid();

    }
    private SupplierInfoPresenter presenter = null;

    @Override
    public void bindView() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {

            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getVisibleIndex() < eventItem) {
                    boolean result = canContinue(eventItem);
                    view.getStatusLabel(eventItem).setPassedSmall(result);
                    if (!canContinue(eventItem)) {
                        event.cancel();
                    }
                }
            }

        });
        view.getRegisterButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(SERVICE)) {
                    LOGGER.fine("register him!");
                    registerSupplier();
                } else {
                    LOGGER.fine("cannot continue");
                }
            }

        });
        view.getConditionLink().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                view.showConditions();
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
        eventBus.setUpSearchBar(null, false, false, false);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onGoToCreateSupplierModule() {
        LOGGER.info("SupplierCreationPresenter loaded");
        //init parts
        LOGGER.info(" -> Supplier Info Form");
        eventBus.initSupplierForm(view.getSupplierInfoHolder());
        LOGGER.info(" -> Category Widget");
        eventBus.initCategoryWidget(view.getCategoryHolder());
        LOGGER.info(" -> Locality Widget");
        eventBus.initLocalityWidget(view.getLocalityHolder());
        LOGGER.info(" -> init Service Form supplierService");
        initServices();
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    private SupplierServicePresenter supplierService = null;
    private SupplierInfoPresenter supplierInfo = null;

    public void onInitServiceForm(SimplePanel serviceHolder) {
        if (supplierService != null) {
            eventBus.removeHandler(supplierService);
        }
        supplierService = eventBus.addHandler(SupplierServicePresenter.class);
        supplierService.initServiceForm(serviceHolder);
    }

    public void onInitSupplierForm(SimplePanel supplierInfoHolder) {
        if (supplierInfo != null) {
            eventBus.removeHandler(supplierInfo);
        }
        supplierInfo = eventBus.addHandler(SupplierInfoPresenter.class);
        supplierInfo.onInitSupplierForm(supplierInfoHolder);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    private void registerSupplier() {
        SupplierInfoInterface info = (SupplierInfoInterface) view.getSupplierInfoHolder().getWidget();
        LocalitySelectorInterface locs = (LocalitySelectorInterface) view.getLocalityHolder().getWidget();
        CategorySelectorInterface cats = (CategorySelectorInterface) view.getCategoryHolder().getWidget();
        ServiceWidget service = (ServiceWidget) view.getServiceHolder().getWidget();

        UserDetail newSupplier = info.createSupplier();
        newSupplier.getSupplier().setLocalities(locs.getSelectedLocalityCodes());
        newSupplier.getSupplier().setCategories(cats.getSelectedCategoryCodes());
        newSupplier.getSupplier().addService(service.getSelectedService());

        eventBus.registerSupplier(newSupplier);
        //signal event
        eventBus.loadingShow(MSGS.progressRegisterClient());
    }

    private static final int INFO = 1;
    private static final int CATEGORY = 2;
    private static final int LOCALITY = 3;
    private static final int SERVICE = 4;

    private boolean canContinue(int step) {
        if (step == INFO) {
            ProvidesValidate widget =
                    (ProvidesValidate) view.getSupplierInfoHolder().getWidget();
            return widget.isValid();
        }
        if (step == CATEGORY) {
            ProvidesValidate widget =
                    (ProvidesValidate) view.getCategoryHolder().getWidget();
            return widget.isValid();
        }
        if (step == LOCALITY) {
            ProvidesValidate widget =
                    (ProvidesValidate) view.getLocalityHolder().getWidget();
            return widget.isValid();
        }
        if (step == SERVICE) {
            return view.isValid();
        }
        //can't reach
        return false;
    }

    // TODO preco mame v presenteri SupplierRPCServiceAsync objekt??? Tymto uplne porusujeme
    // MVP model. Rozhranim ma byt predsa eventbus. OPRAVIT !!! a nerobit taketo chyby !!!
    @Inject
    private SupplierCreationRPCServiceAsync supplierCreationRpcService = null;
    protected ErrorDialogPopupView errorDialog;

    public void setService(SupplierCreationRPCServiceAsync service) {
        this.supplierCreationRpcService = service;
    }

    private void initServices() {
        supplierCreationRpcService.getSupplierServices(new AsyncCallback<ArrayList<ServiceDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                // TODO create some good explanation with contact formular
                SimpleIconLabel errorMsg =
                        new SimpleIconLabel("Unexpected Error occurred", "Something terrible happened during "
                        + "supplierService table initialization");
                errorMsg.setImageResource(StyleResource.INSTANCE.images().errorIcon24());
                view.getServiceHolder().setWidget(errorMsg);
            }

            @Override
            public void onSuccess(ArrayList<ServiceDetail> data) {
                LOGGER.info(" -> Service Form init & fill data");
                ServiceWidget serviceWidget = new ServiceWidget();
                view.getServiceHolder().setWidget(serviceWidget);
                serviceWidget.setData(data);
            }

        });
    }

}
