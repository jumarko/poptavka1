package cz.poptavka.sample.client.home.demands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.CategoryDetail;
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
                    eventBus.getDemandsByLocalities(localities);
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
     *
     * @param code
     *            - String representing code
     */
    public void onGetCategory(long id) {
        LOGGER.info("FilterByCategory: " + id);
        categoryService.getCategory(id, new AsyncCallback<Category>() {

            @Override
            public void onSuccess(Category result) {
                LOGGER.info("category found: " + result);
                if (result != null) {
                    Category[] categories = {result};
                    eventBus.getDemandsByCategories(categories);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info("onFailureFilterbyCategory");

            }
        });
    }

    /**
     * Get all categories. Used for display in listBox categories.
     */
    public void onGetCategories() {
        categoryService
                .getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {

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
    /**
     * Get all demand from database.
     *
     * @param result
     */
    public void onGetDemands() {
        ResultCriteria resultCriteria = new ResultCriteria.Builder().build();
        eventBus.getResultsCriteria(resultCriteria);
        demandService.getDemands(resultCriteria,
                new AsyncCallback<List<Demand>>() {

                    @Override
                    public void onSuccess(List<Demand> result) {
                        LOGGER.info("demands found: " + result.size());
                        eventBus.displayDemands(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.info("onFailureGetDemands");
                    }
                });
    }

    public void onGetDemandsByCategories(Category[] categories) {
        LOGGER.info("Display demand by category: " + Arrays.toString(categories));
        ResultCriteria resultCriteria = new ResultCriteria.Builder().build();
        eventBus.getResultsCriteria(resultCriteria);
        demandService.getDemands(resultCriteria, categories,
                new AsyncCallback<Set<Demand>>() {

                    @Override
                    public void onSuccess(Set<Demand> result) {
                        LOGGER.info("onSuccessGetDemandsByCategory");
                        eventBus.displayDemands(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.info("onFailureGetDemandsByCategory");
                    }
                });
    }

    public void onGetDemandsByLocalities(Locality[] localities) {
        ResultCriteria resultCriteria = new ResultCriteria.Builder().build();
        eventBus.getResultsCriteria(resultCriteria);
        demandService.getDemands(resultCriteria, localities,
                new AsyncCallback<Set<Demand>>() {

                    @Override
                    public void onSuccess(Set<Demand> result) {
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
