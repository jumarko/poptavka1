package cz.poptavka.sample.client.homedemands;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.List;
import java.util.Map;

//@SuppressWarnings("deprecation")
@EventHandler
public class HomeDemandsHandler extends BaseEventHandler<HomeDemandsEventBus> {

//    private LocalityRPCServiceAsync localityService = null;
//    private CategoryRPCServiceAsync categoryService = null;
    private DemandRPCServiceAsync demandService = null;
//    private static final Logger LOGGER = Logger.getLogger("MainHandler");
//
//    @Inject
//    public void setLocalityService(LocalityRPCServiceAsync service) {
//        localityService = service;
//    }
//
//    @Inject
//    public void setCategoryService(CategoryRPCServiceAsync service) {
//        categoryService = service;
//    }

    @Inject
    void setDemandService(DemandRPCServiceAsync service) {
        demandService = service;
    }

    // *** GET DEMANDS
    // ***************************************************************************
//    public void onGetDemand(FullDemandDetail demandDetail) {
//        demandService.getWholeDemand(demandDetail.getDemandId(), new AsyncCallback<Demand>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                Window.alert("Failed to retrieve whole demand. We are sorry :(. Try later.");
//            }
//
//            @Override
//            public void onSuccess(Demand result) {
////                eventBus.setDemand(result);
//            }
//        });
//
//    }
//
//    public void onGetSortedDemands(int start, int count, Map<String, OrderType> orderColumns) {
//        demandService.getSortedDemands(start, count, orderColumns, new AsyncCallback<List<FullDemandDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(List<FullDemandDetail> result) {
//                eventBus.displayDemands(result);
//            }
//        });
//    }
    //*************** GET DEMANDS COUNT *********************
//    public void onGetAllDemandsCount() {
//        demandService.getAllDemandsCount(new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.setResultSource("all");
////                eventBus.setResultCount(result);
//                eventBus.createAsyncDataProvider(result.intValue());
//            }
//        });
//    }
//
//    public void onGetSortedDemandsCount(Map<String, OrderType> orderColumns) {
//        demandService.getSortedDemandsCount(orderColumns, new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.setResultSource("allSorted");
////                eventBus.setResultCount(result);
//                eventBus.createAsyncDataProvider(result.intValue());
//            }
//        });
//    }
//
//    public void onGetDemandsCountCategory(long id) {
//        demandService.getDemandsCountByCategory(id, new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("onGetDemandsCountCategory failed.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.setResultSource("category");
////                eventBus.setResultCount(result);
//                eventBus.createAsyncDataProvider(result.intValue());
//            }
//        });
//    }
//
//    public void onGetDemandsCountLocality(String code) {
//        demandService.getDemandsCountByLocality(code, new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("onGetDemandsCountLocality failed.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.setResultSource("locality");
////                eventBus.setResultCount(result);
//                eventBus.createAsyncDataProvider(result.intValue());
//            }
//        });
//    }
//
//    public void onGetDemandsCountCategoryLocality(long id, String code) {
//        demandService.getDemandsCountByCategoryLocality(id, code, new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("onGetDemandsCountLocality failed.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.setResultSource("categoryLocality");
////                eventBus.setResultCount(result);
//                eventBus.createAsyncDataProvider(result.intValue());
//            }
//        });
//    }
    //*************** GET DEMANDS DATA *********************
    /**
     * Get all demand from database.
    //     */
//    public void onGetDemands(int fromResult, int toResult) {
//        demandService.getDemands(fromResult, toResult, new AsyncCallback<List<FullDemandDetail>>() {
//
//            @Override
//            public void onSuccess(List<FullDemandDetail> result) {
//                eventBus.displayDemands(result);
//            }
//
//            @Override
//            public void onFailure(Throwable caught) {
//                LOGGER.info("onFailureGetDemands");
//            }
//        });
//    }
//
//    public void onGetDemandsByCategories(int fromResult, int toResult, long id) {
//        demandService.getDemandsByCategory(fromResult, toResult, id,
//                new AsyncCallback<List<FullDemandDetail>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        throw new UnsupportedOperationException("Not supported yet.");
//                    }
//
//                    @Override
//                    public void onSuccess(List<FullDemandDetail> result) {
//                        eventBus.displayDemands(result);
//                    }
//                });
//    }
//
//    public void onGetDemandsByLocalities(int fromResult, int toResult, String id) {
//        demandService.getDemandsByLocality(fromResult, toResult, id,
//                new AsyncCallback<List<FullDemandDetail>>() {
//
//                    @Override
//                    public void onSuccess(List<FullDemandDetail> result) {
//                        LOGGER.info("onSuccessGetDemandsByLocality");
//                        eventBus.displayDemands(result);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        LOGGER.info("onFailureGetDemandsByLocality");
//                    }
//                });
//    }
//
//    public void onGetDemandsByCategoriesLocalities(int fromResult, int toResult, long id, String code) {
//        demandService.getDemandsByCategoryLocality(fromResult, toResult, id, code,
//                new AsyncCallback<List<FullDemandDetail>>() {
//
//                    @Override
//                    public void onSuccess(List<FullDemandDetail> result) {
//                        LOGGER.info("onSuccessGetDemandsByLocality");
//                        eventBus.displayDemands(result);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        LOGGER.info("onFailureGetDemandsByLocality");
//                    }
//                });
//    }
    //*************** FIND DEMANDS DATA *********************
    public void onFilterDemandsCount(SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        demandService.filterDemandsCount(detail, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("onFilterDemands (HomeDemandsHandler) - not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
//                eventBus.setResultSource("filter");
//                eventBus.setResultCount(result);
                eventBus.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onFilterDemands(int start, int count, SearchModuleDataHolder detail,
            Map<String, OrderType> orderColumns) {
        demandService.filterDemands(start, count, detail, orderColumns, new AsyncCallback<List<FullDemandDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("onFilterDemands (HomeDemandsHandler) - not supported yet.");
            }

            @Override
            public void onSuccess(List<FullDemandDetail> result) {
                eventBus.displayDemands(result);
            }
        });
    }
}
