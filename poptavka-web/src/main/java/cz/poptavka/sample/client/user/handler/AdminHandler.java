package cz.poptavka.sample.client.user.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.OfferRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.SupplierRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import java.util.Map;
import java.util.logging.Logger;

@EventHandler
public class AdminHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private OfferRPCServiceAsync offerService = null;
    @Inject
    private SupplierRPCServiceAsync supplierService = null;
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
                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /***********************************************************************************************
     ***********************  SUPPLIER SECTION. ****************************************************
     **********************************************************************************************/
    public void onGetAdminTabSuppliersCount() {
        supplierService.getSuppliersCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                LOGGER.info("Found: " + result);
                eventBus.createAdminSuppliersAsyncDataProvider(result);
            }
        });
    }

    public void onGetAdminSuppliers(int start, int count) {
        supplierService.getSuppliers(start, count, new AsyncCallback<ArrayList<FullSupplierDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<FullSupplierDetail> result) {
                LOGGER.info("Found: " + result.size());
                eventBus.displayAdminTabSuppliers(result);
            }
        });
    }

    public void onGetSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns) {
        supplierService.getSortedSuppliers(start, count, orderColumns,
                new AsyncCallback<ArrayList<FullSupplierDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<FullSupplierDetail> result) {
                        eventBus.displayAdminTabSuppliers(result);
                    }
                });
    }

    public void onUpdateSupplier(FullSupplierDetail supplierDetail, String updateWhat) {
        supplierService.updateSupplier(supplierDetail, updateWhat, new AsyncCallback<FullSupplierDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(FullSupplierDetail result) {
                eventBus.refreshUpdatedSupplier(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OFFER SECTION. ******************************************************
     **********************************************************************************************/
    public void onUpdateOffer(FullOfferDetail offer) {
        offerService.updateOffer(offer, new AsyncCallback<FullOfferDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(FullOfferDetail result) {
                eventBus.refreshUpdatedOffer(result);
            }
        });
    }
}
