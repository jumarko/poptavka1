package cz.poptavka.sample.client.home.demands;

import com.google.gwt.user.client.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

//@SuppressWarnings("deprecation")
@EventHandler
public class DemandsHandler extends BaseEventHandler<DemandsEventBus> {

    private LocalityRPCServiceAsync localityService = null;
    private CategoryRPCServiceAsync categoryService = null;
    private DemandRPCServiceAsync demandService = null;
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

    // *** GET LOCALITY
    // ***************************************************************************
    public void onGetLocality(long id) {
        LOGGER.info("FilterByLocality: " + id);
        localityService.getLocality(id, new AsyncCallback<Locality>() {

            @Override
            public void onSuccess(Locality result) {
                LOGGER.info("category found: " + result);
                if (result != null) {
                    Locality[] localities = {result};
                    eventBus.getDemandsByLocalities(ResultCriteria.EMPTY_CRITERIA, localities);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info("onFailureFilterbyLocality");
            }
        });
    }

    /**
     * Get all localities. Used for display in listBox localities.
     */
    public void onGetLocalities() {
        localityService.getLocalities(LocalityType.REGION,
                new AsyncCallback<ArrayList<LocalityDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<LocalityDetail> list) {
                        eventBus.setLocalityData(list);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        LOGGER.info("onFailureGetLocalities");
                    }
                });
    }

    // *** GET CATEGORIES
    // ***************************************************************************
    /**
     * Get category by its code.
     */
//    public void onGetCategory(long id) {
//        LOGGER.info("FilterByCategory: " + id);
//        categoryService.getCategory(id, new AsyncCallback<Category>() {
//
//            @Override
//            public void onSuccess(Category result) {
//                LOGGER.info("category found: " + result);
//                if (result != null) {
//                    Category[] categories = {result};
//                    eventBus.getDemandsByCategories(ResultCriteria.EMPTY_CRITERIA,categories);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable caught) {
//                LOGGER.info("onFailureFilterbyCategory");
//
//            }
//        });
    //    }
    /**
     * Get all categories. Used for display in listBox categories.
     */
    public void onGetCategories() {
        categoryService.getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onSuccess(ArrayList<CategoryDetail> list) {
                LOGGER.info("categories found: " + list.size());
                eventBus.setCategoryData(list);
            }

            @Override
            public void onFailure(Throwable arg0) {
                LOGGER.info("onFailureCategory");
            }
        });
    }

    // *** GET DEMANDS
    // ***************************************************************************
    public void onGetDemand(DemandDetail demandDetail) {
        LOGGER.info("Getting whole demand by id: " + demandDetail.getId());
        demandService.getWholeDemand(demandDetail.getId(), new AsyncCallback<Demand>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failed to retrieve whole demand. We are sorry :(. Try later.");
            }

            @Override
            public void onSuccess(Demand result) {
                eventBus.setDemand(result);
            }
        });
    }

    /**
     * Get all demand from database.
     */
    public void onGetDemands(ResultCriteria resultCriteria) {
        LOGGER.info("Get demands: " + resultCriteria.getFirstResult());
        demandService.getDemands(resultCriteria,
                new AsyncCallback<List<DemandDetail>>() {

                    @Override
                    public void onSuccess(List<DemandDetail> result) {
                        LOGGER.info("Demands found: " + result.size());

                        eventBus.displayDemands(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.info("onFailureGetDemands");
                    }
                });
    }

    public void onGetDemands2(int fromResult, int toResult) {
//        LOGGER.info("Get demands: " + resultCriteria.getFirstResult());
        demandService.getDemands(fromResult, toResult,
                new AsyncCallback<List<DemandDetail>>() {

                    @Override
                    public void onSuccess(List<DemandDetail> result) {
                        LOGGER.info("Demands found: " + result.size());

                        eventBus.displayDemands(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.info("onFailureGetDemands");
                    }
                });
    }

    public void onGetDemandsByCategories(ResultCriteria resultCriteria, Category[] categories) {
        demandService.getDemands(resultCriteria, categories,
                new AsyncCallback<List<DemandDetail>>() {

                    @Override
                    public void onSuccess(List<DemandDetail> result) {
                        LOGGER.info("onSuccessGetDemandsByCategory");
                        eventBus.displayDemands(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.info("onFailureGetDemandsByCategory");
                    }
                });
    }

    public void onGetDemandsByLocalities(ResultCriteria resultCriteria, Locality[] localities) {
        demandService.getDemands(resultCriteria, localities,
                new AsyncCallback<List<DemandDetail>>() {

                    @Override
                    public void onSuccess(List<DemandDetail> result) {
                        LOGGER.info("onSuccessGetDemandsByLocality");
                        eventBus.displayDemands(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.info("onFailureGetDemandsByLocality");
                    }
                });
    }
}
