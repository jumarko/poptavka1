package cz.poptavka.sample.client.common;

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

import java.util.ArrayList;
import java.util.logging.Logger;

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
                eventBus.setCategoryDisplayData(CategoryType.ROOT, list);
            }
        });
    }

    public void onGetChildListCategories(final int newListPosition, String categoryId) {
        LOGGER.info("starting category service call");
        if (categoryId.equals("ALL_CATEGORIES")) {
            LOGGER.info(" --> root categories");
            categoryService.getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {
                @Override
                public void onFailure(Throwable arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onSuccess(ArrayList<CategoryDetail> list) {
                    eventBus.setCategoryListData(newListPosition, list);
                }
            });
        } else {
            LOGGER.info(" --> child categories");
            categoryService.getCategoryChildren(categoryId, new AsyncCallback<ArrayList<CategoryDetail>>() {
                @Override
                public void onSuccess(ArrayList<CategoryDetail> list) {
                    eventBus.setCategoryListData(newListPosition, list);
                }

                @Override
                public void onFailure(Throwable arg0) {
                    // TODO Auto-generated method stub

                }
            });
        }
        LOGGER.info("ending category service call");
    }
    /**
     * Creates new demand.
     *
     * @param detail front-end entity of demand
     * @param clientId client id
     */
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

    /**
     * Verify identity of user, if exists in the system.
     * If so, new demand is created.
     *
     * @param client existing user detail
     */
    public void onVerifyExistingClient(ClientDetail client) {
        clientService.verifyClient(client, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onSuccess(Long clientId) {
                if (clientId != -1) {
                    eventBus.setClientId(clientId);
                    eventBus.getBasicInfoValues();
                }
            }
        });

    }

    /**
     * Method registers new client and afterwards creates new demand.
     *
     * @param client newly created client
     */
    public void onRegisterNewClient(ClientDetail client) {
        clientService.createNewClient(client, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onSuccess(Long clientId) {
                if (clientId != -1) {
                    eventBus.setClientId(clientId);
                    eventBus.getBasicInfoValues();
                }
            }
        });
    }
}
