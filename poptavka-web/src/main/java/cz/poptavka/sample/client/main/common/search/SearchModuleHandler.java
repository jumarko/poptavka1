package cz.poptavka.sample.client.main.common.search;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.GeneralRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.adminModule.PaymentMethodDetail;
import java.util.List;

//@SuppressWarnings("deprecation")
@EventHandler
public class SearchModuleHandler extends BaseEventHandler<SearchModuleEventBus> {

    private LocalityRPCServiceAsync localityService = null;
    private CategoryRPCServiceAsync categoryService = null;
    private GeneralRPCServiceAsync generalService = null;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    @Inject
    public void setLocalityService(LocalityRPCServiceAsync service) {
        localityService = service;
    }

    @Inject
    public void setCategoryService(CategoryRPCServiceAsync service) {
        categoryService = service;
    }

    // *** GET LOCALITY
    // ***************************************************************************
    /**
     * Get all localities. Used for display in listBox localities.
     */
    public void onRequestLocalities() {
        localityService.getLocalities(LocalityType.REGION,
                new AsyncCallback<ArrayList<LocalityDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<LocalityDetail> list) {
                        eventBus.responseLocalities(list);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        throw new UnsupportedOperationException("onFailureGetLocalities.");
                    }
                });
    }

    // *** GET CATEGORIES
    // ***************************************************************************
    /**
     * Get all categories. Used for display in listBox categories.
     */
    public void onRequestCategories() {
        categoryService.getCategories(
                new AsyncCallback<ArrayList<CategoryDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<CategoryDetail> list) {
                        eventBus.responseCategories(list);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        throw new UnsupportedOperationException("onFailureCategory");
                    }
                });
    }

    public void onRequestPaymentMethods() {
        generalService.getAdminPaymentMethods(new AsyncCallback<List<PaymentMethodDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<PaymentMethodDetail> result) {
                eventBus.responsePaymentMethods(result);
            }
        });
    }
}
