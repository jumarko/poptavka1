package cz.poptavka.sample.client.main;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.ClientRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 * Handler for common used RPC calls for localities and categories and other
 * common components.
 *
 * @author Beho
 *
 */
@EventHandler
public class MainHandler extends BaseEventHandler<MainEventBus> {

    @Inject
    private LocalityRPCServiceAsync localityService = null;
    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private ClientRPCServiceAsync clientService = null;

    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    public void onGetRootLocalities() {
        localityService.getLocalities(LocalityType.REGION,
                new AsyncCallback<ArrayList<LocalityDetail>>() {
                    @Override
                    public void onSuccess(ArrayList<LocalityDetail> list) {
                        eventBus.setLocalityData(LocalityDetail.REGION, list);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        // TODO empty
                    }
                });
    }

    public void onGetChildLocalities(final int localityType, String locCode) {
        localityService.getLocalities(locCode,
                new AsyncCallback<ArrayList<LocalityDetail>>() {
                    @Override
                    public void onFailure(Throwable arg0) {
                        // TODO empty
                    }

                    @Override
                    public void onSuccess(ArrayList<LocalityDetail> list) {
                        eventBus.setLocalityData(localityType, list);
                    }
                });
    }

    public void onGetRootCategories() {
        categoryService
                .getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {
                    @Override
                    public void onFailure(Throwable arg0) {
                        // empty
                    }

                    @Override
                    public void onSuccess(ArrayList<CategoryDetail> list) {
                        // eventBus.setCategoryDisplayData(CategoryType.ROOT,
                        // list);
                        Window.alert("fix this method return value"
                                + "onGetRootCategories() - MainHandler.class");
                    }
                });
    }

    public void onGetChildListCategories(final int newListPosition,
            String categoryId) {
        LOGGER.info("starting category service call");
        if (categoryId.equals("ALL_CATEGORIES")) {
            LOGGER.info(" --> root categories");
            categoryService
                    .getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {
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
            categoryService.getCategoryChildren(Long.valueOf(categoryId),
                    new AsyncCallback<ArrayList<CategoryDetail>>() {
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

    // TODO remove these methods.
//    /**
//     * Creates new demand.
//     *
//     * @param detail
//     *            front-end entity of demand
//     * @param clientId
//     *            client id
//     */
    public void onCreateDemand(FullDemandDetail detail, Long clientId) {
//        GWT.log("Am I here?");
//        demandService.createNewDemand(detail, clientId,
//                new AsyncCallback<FullDemandDetail>() {
//                    @Override
//                    public void onFailure(Throwable arg0) {
//                        eventBus.loadingHide();
//                        Window.alert(arg0.getMessage());
//                    }
//
//                    @Override
//                    public void onSuccess(FullDemandDetail result) {
//                        // signal event
//                        eventBus.loadingHide();
//                        // TODO forward to user/atAccount
////                        eventBus.addNewDemand(result);
//                    }
//                });
//        LOGGER.info("submitting new demand");
    }

//    public void onCheckFreeEmail(String email) {
//        clientService.checkFreeEmail(email, new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable arg0) {
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                LOGGER.fine("result of compare " + result);
//                eventBus.checkFreeEmailResponse(result);
//                // eventBus.checkFreeEmailResponse();
//            }
//        });
//    }

}
