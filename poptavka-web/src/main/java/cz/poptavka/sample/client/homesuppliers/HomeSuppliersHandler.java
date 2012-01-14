package cz.poptavka.sample.client.homesuppliers;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.SupplierRPCServiceAsync;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.List;
import java.util.Map;

//@SuppressWarnings("deprecation")
@EventHandler
public class HomeSuppliersHandler extends BaseEventHandler<HomeSuppliersEventBus> {

    private LocalityRPCServiceAsync localityService = null;
    private CategoryRPCServiceAsync categoryService = null;
    private DemandRPCServiceAsync demandService = null;
    private SupplierRPCServiceAsync supplierService = null;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

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
    public void setSupplierService(SupplierRPCServiceAsync service) {
        supplierService = service;
    }

    // *** GET LOCALITY
    // ***************************************************************************
    /**
     * Get all localities. Used for display in listBox localities.
     */
//    public void onGetLocalities() {
//        localityService.getLocalities(LocalityType.REGION,
//                new AsyncCallback<ArrayList<LocalityDetail>>() {
//
//                    @Override
//                    public void onSuccess(ArrayList<LocalityDetail> list) {
//                        eventBus.setLocalityData(list);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable arg0) {
//                        LOGGER.info("onFailureGetLocalities");
//                    }
//                });
//    }
    // *** GET CATEGORIES
    // ***************************************************************************
    /**
     * Get all categories. Used for display in listBox categories.
     */
    public void onGetCategories() {
        categoryService.getCategories(
                new AsyncCallback<ArrayList<CategoryDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<CategoryDetail> list) {
                        eventBus.displayRootcategories(list);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        LOGGER.info("onFailureCategory");
                    }
                });
    }

    public void onGetSubCategories(final Long category) {
        if (category == null) {
            categoryService.getCategories(
                    new AsyncCallback<ArrayList<CategoryDetail>>() {

                        @Override
                        public void onFailure(Throwable arg0) {
                            LOGGER.info("onFailureCategory");
                        }

                        @Override
                        public void onSuccess(ArrayList<CategoryDetail> list) {
                            eventBus.displaySubCategories(list, category);
                        }
                    });
        } else {
            categoryService.getCategoryChildren(category, new AsyncCallback<ArrayList<CategoryDetail>>() {

                @Override
                public void onFailure(Throwable caught) {
                    throw new UnsupportedOperationException("Get children categories failed.");
                }

                @Override
                public void onSuccess(ArrayList<CategoryDetail> result) {
                    LOGGER.info("Found subcategories: " + result.size());
                    eventBus.displaySubCategories(result, category);
                }
            });
        }
    }

    // *** GET SUPPLIERS COUNTS
    // ***************************************************************************
//    public void onGetSuppliersCountByCategoryLocality(Long category, String locality) {
//        supplierService.getSuppliersCount(category, locality, new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.createAsyncDataProviderSupplier(result);
//                eventBus.resetDisplaySuppliersPager(result.intValue());
//            }
//        });
//    }
//
//    public void onGetSuppliersCountByCategory(Long category) {
//        supplierService.getSuppliersCount(category, new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.createAsyncDataProviderSupplier(result);
//                eventBus.resetDisplaySuppliersPager(result.intValue());
//            }
//        });
//    }
//    public void onGetSuppliersCount(SearchDataHolder searchDataHolder) {
//        supplierService.getFilterSuppliersCount(searchDataHolder, new AsyncCallback<Long>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(Long result) {
////                eventBus.setResultCount(result);
////                eventBus.createAsyncDataProviderSupplier(result);
//                eventBus.resetDisplaySuppliersPager(result.intValue());
//            }
//        });
//    }
//
//    public void onGetSuppliersByCategoryLocality(int start, int count, Long category, String locality) {
//        supplierService.getSuppliers(start, count, category, locality,
//                new AsyncCallback<ArrayList<FullSupplierDetail>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        throw new UnsupportedOperationException("Not supported yet.");
//                    }
//
//                    @Override
//                    public void onSuccess(ArrayList<FullSupplierDetail> result) {
//                        eventBus.displaySuppliers(result);
//                    }
//                });
//    }
//
//    public void onGetSuppliersByCategory(int start, int count, Long category) {
//        supplierService.getSuppliers(start, count, category, new AsyncCallback<ArrayList<FullSupplierDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<FullSupplierDetail> result) {
//                eventBus.displaySuppliers(result);
//            }
//        });
//    }
    //*************** GET SUPPLIERS DATA *********************
    public void onGetSuppliersCount(SearchModuleDataHolder detail) {
        supplierService.filterSuppliersCount(detail, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("onFilterSuppliers "
                        + "(HomeSuppliersHandler) - not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
//                eventBus.resetDisplaySuppliersPager(result.intValue());
                eventBus.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetSuppliers(int start, int count, SearchModuleDataHolder search,
            Map<String, OrderType> orderColumns) {
        supplierService.filterSuppliers(start, count, search, orderColumns,
                new AsyncCallback<List<FullSupplierDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException(
                                "onFilterSuppliers (HomeSupliersHandler) - not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<FullSupplierDetail> result) {
                        eventBus.displaySuppliers(result);
                    }
                });
    }
}