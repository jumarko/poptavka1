package cz.poptavka.sample.client.home.supplier;

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

import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter;
import cz.poptavka.sample.client.home.supplier.widget.SupplierInfoPresenter.SupplierInfoInterface;
import cz.poptavka.sample.client.home.supplier.widget.SupplierServicePresenter;
import cz.poptavka.sample.client.main.common.SimpleIconLabel;
import cz.poptavka.sample.client.main.common.StatusIconLabel;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import cz.poptavka.sample.client.main.common.creation.ProvidesValidate;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import cz.poptavka.sample.client.main.common.service.ServiceWidget;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.service.demand.SupplierRPCServiceAsync;
import cz.poptavka.sample.shared.domain.ServiceDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

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

    public void onStart() {
        // TODO praso
    }

    public void onForward() {
        // TODO praso - switch css to selected menu button.
        //eventBus.selectCompanyMenu();
    }

    public void onGoToCreateSupplier(String location) {
        this.onAtRegisterSupplier(location);
    }

    /**
     * Init method call.
     * TODO decide WHEN other parts should be built.
     */
    public void onAtRegisterSupplier(String location) {
        LOGGER.info("Initializing Supplier Registration Widget ... ");
//        eventBus.setBodyWidget(view.getWidgetView());
        //init parts
        LOGGER.info(" -> Supplier Info Form");
        eventBus.initSupplierForm(view.getSupplierInfoHolder());
        LOGGER.info(" -> Category Widget");
        eventBus.initCategoryWidget(view.getCategoryHolder());
        LOGGER.info(" -> Locality Widget");
        eventBus.initLocalityWidget(view.getLocalityHolder());
        LOGGER.info(" -> init Service Form supplierService");
        initServices();

        if (location.equals("home")) {
            eventBus.setHomeBodyHolderWidget(view.getWidgetView());
        } else if (location.equals("user")) {
            eventBus.setUserBodyHolderWidget(view.getWidgetView());
        }
    }

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
    @Inject
    private SupplierRPCServiceAsync supplierRpcService = null;

    public void setService(SupplierRPCServiceAsync service) {
        this.supplierRpcService = service;
    }

    private void initServices() {
        supplierRpcService.getSupplierServices(new AsyncCallback<ArrayList<ServiceDetail>>() {

            @Override
            public void onFailure(Throwable arg0) {
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
}
