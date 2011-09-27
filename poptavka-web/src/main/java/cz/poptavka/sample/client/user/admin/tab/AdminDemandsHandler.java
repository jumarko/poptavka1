package cz.poptavka.sample.client.user.admin.tab;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.OfferRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.SupplierRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.Map;
import java.util.logging.Logger;

@EventHandler
public class AdminDemandsHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private OfferRPCServiceAsync offerService = null;
    @Inject
    private SupplierRPCServiceAsync supplierService = null;
    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private LocalityRPCServiceAsync localityService = null;

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private final static Logger LOGGER = Logger.getLogger("    UserHandler");

    /**********************************************************************************************
     ***********************  DEMAND SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminDemandsCount() {
        demandService.getAllDemandsCount(new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminDemandsAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminDemands(int start, int count) {
        demandService.getDemands(start, count, new AsyncCallback<List<FullDemandDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<FullDemandDetail> result) {
                eventBus.displayAdminTabDemands(result);
            }
        });

    }

    public void onGetSortedDemands(int start, int count, Map<String, OrderType> orderColumns) {
        demandService.getSortedDemands(start, count, orderColumns,
                new AsyncCallback<List<FullDemandDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<FullDemandDetail> result) {
                        eventBus.displayAdminTabDemands(result);
                    }
                });
    }

    public void onUpdateDemand(FullDemandDetail demand, String updateWhat) {
        demandService.updateDemand(demand, updateWhat, new AsyncCallback<FullDemandDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(FullDemandDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  CATEGORY SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminRootCategories() {
        categoryService.getAllRootCategories(new AsyncCallback<List<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.displayAdminCategories(1, result);
            }
        });
    }

    public void onGetAdminSubCategories(final int list, Long catId) {
        categoryService.getCategoryChildren(catId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                eventBus.displayAdminCategories(list, result);
            }
        });
    }
    public void onGetAdminParentCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                eventBus.doBackCategories(result);
            }
        });
    }
    /**********************************************************************************************
     ***********************  CATEGORY SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminRootLocalities() {
        localityService.getAllRootLocalities(new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.displayAdminLocalities(1, result);
            }
        });
    }

    public void onGetAdminSubLocalities(final int list, String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.displayAdminLocalities(list, result);
            }
        });
    }
    public void onGetAdminParentLocalities(String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.doBackLocalities(result);
            }
        });
    }
}
