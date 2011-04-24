package cz.poptavka.sample.client.common;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;
import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.ClientRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

/**
 * Handler for common used RPC calls for localities and categories and other
 * common components.
 *
 * @author Beho
 *
 */
@SuppressWarnings("deprecation")
@EventHandler
public class CommonHandler extends BaseEventHandler<CommonEventBus> {

    private LocalityRPCServiceAsync localityService = null;
    private CategoryRPCServiceAsync categoryService = null;
    private DemandRPCServiceAsync demandService = null;
    private ClientRPCServiceAsync clientService = null;

    private static final Logger LOGGER = Logger.getLogger("CommonHandler");

    @Inject
    public void setLocalityService(LocalityRPCServiceAsync service) {
        localityService = service;
    }

    @Inject
    public void setCategoryService(CategoryRPCServiceAsync service) {
        categoryService = service;
    }

    @Inject
    void setDemandService(DemandRPCServiceAsync service) {
        demandService = service;
    }

    @Inject
    void setClientRPCServiceAsync(ClientRPCServiceAsync service) {
        clientService = service;
    }

    public void onGetLocalities() {
        localityService.getLocalities(LocalityType.REGION, new AsyncCallback<ArrayList<LocalityDetail>>() {
            @Override
            public void onSuccess(ArrayList<LocalityDetail> list) {
                eventBus.setLocalityData(LocalityType.REGION, list);
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO empty
            }
        });
    }

    public void onGetChildLocalities(final LocalityType type, String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO empty
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> list) {
                eventBus.setLocalityData(type, list);
            }
        });
    }

    public void onGetRootCategories() {
        categoryService.getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {
            @Override
            public void onFailure(Throwable arg0) {
                // empty
            }
            @Override
            public void onSuccess(ArrayList<CategoryDetail> list) {
                eventBus.setCategoryData(CategoryType.ROOT, list);
                eventBus.setCategoryDisplayData(CategoryType.ROOT, list);
            }
        });
    }

    public void onGetChildCategories(final CategoryType type, String categoryId) {
        LOGGER.info("starting category service call");
        categoryService.getCategoryChildren(categoryId, new AsyncCallback<ArrayList<CategoryDetail>>() {
            @Override
            public void onSuccess(ArrayList<CategoryDetail> list) {
                eventBus.setCategoryData(type, list);
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub

            }
        });
        LOGGER.info("ending category service call");
    }

    public void onCreateDemand(DemandDetail detail, Long clientId) {
        demandService.createNewDemand(detail, clientId, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(String result) {
                Window.alert(result);
            }
        });
        LOGGER.info("submitting new demand");
    }

    public void onVerifyExistingClient(ClientDetail client) {
//        clientService.verifyClient(client, new AsyncCallback<Long>() {
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onSuccess(Long clientId) {
//                eventBus.setClientId(clientId);
////                eventBus.getBasicInfoValues();
//            }
//        });

        //bypassing not working service
        eventBus.setClientId(1);
        eventBus.getBasicInfoValues();
    }

    public void onRegisterNewClient(ClientDetail client) {

    }
}
