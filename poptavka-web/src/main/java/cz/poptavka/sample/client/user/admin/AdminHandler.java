package cz.poptavka.sample.client.user.admin;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.ClientRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.GeneralRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.OfferRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.SupplierRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.PaymentDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;
import cz.poptavka.sample.shared.domain.PreferenceDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.Map;
import java.util.logging.Logger;

@EventHandler
public class AdminHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private SupplierRPCServiceAsync supplierService = null;
    @Inject
    private ClientRPCServiceAsync clientService = null;
    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private LocalityRPCServiceAsync localityService = null;
    @Inject
    private OfferRPCServiceAsync offerService = null;
    @Inject
    private GeneralRPCServiceAsync generalService = null;
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

    public void onUpdateDemand(FullDemandDetail demand) {
        demandService.updateDemand(demand, new AsyncCallback<FullDemandDetail>() {

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

    //------------------- DEMAND SECTION - CATEGORY SECTION. -------------------------------
    public void onGetAdminDemandRootCategories() {
        categoryService.getAllRootCategories(new AsyncCallback<List<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.displayAdminDemandCategories(result);
            }
        });
    }

    public void onGetAdminDemandSubCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                eventBus.displayAdminDemandCategories(result);
            }
        });
    }

    public void onGetAdminDemandParentCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                eventBus.doBackDemandCategories(result);
            }
        });
    }

    //------------------- DEMAND SECTION - LOCALITY SECTION. -------------------------------
    public void onGetAdminDemandRootLocalities() {
        localityService.getLocalities(LocalityType.REGION, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.displayAdminDemandLocalities(result);
            }
        });
    }

    public void onGetAdminDemandSubLocalities(String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.displayAdminDemandLocalities(result);
            }
        });
    }

    public void onGetAdminDemandParentLocalities(String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.doBackDemandLocalities(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  SUPPLIER SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminSuppliersCount() {
        supplierService.getSuppliersCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminSuppliersAsyncDataProvider(result.intValue());
            }
        });
    }

//    public void onGetAdminSuppliers(int start, int count) {
//        supplierService.getSuppliers(start, count, new AsyncCallback<ArrayList<FullSupplierDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<FullSupplierDetail> result) {
//                eventBus.displayAdminTabSuppliers(result);
//            }
//        });
//
//    }

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

    public void onUpdateSupplier(FullSupplierDetail supplier) {
        supplierService.updateSupplier(supplier, new AsyncCallback<FullSupplierDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(FullSupplierDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    //----------------------- SUPPLIER SECTION - CATEGORY SECTION. ---------------------------------
    public void onGetAdminSupplierRootCategories() {
        categoryService.getAllRootCategories(new AsyncCallback<List<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.displayAdminSupplierCategories(result);
            }
        });
    }

    public void onGetAdminSupplierSubCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                eventBus.displayAdminSupplierCategories(result);
            }
        });
    }

    public void onGetAdminSupplierParentCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> result) {
                eventBus.doBackSupplierCategories(result);
            }
        });
    }

    //-------------------------- SUPPLIER SECTION - LOCALITY SECTION. -----------------------------
    public void onGetAdminSupplierRootLocalities() {
        localityService.getLocalities(LocalityType.REGION, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.displayAdminDemandLocalities(result);
            }
        });
    }

    public void onGetAdminSupplierSubLocalities(String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.displayAdminSupplierLocalities(result);
            }
        });
    }

    public void onGetAdminSupplierParentLocalities(String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> result) {
                eventBus.doBackSupplierLocalities(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OFFER SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminOffersCount() {
        offerService.getAllOffersCount(new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminOffersAsyncDataProvider(result.intValue());
            }
        });
    }

//    public void onGetAdminOffers(int start, int count) {
//        offerService.getOffers(start, count, new AsyncCallback<List<FullOfferDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(List<FullOfferDetail> result) {
//                eventBus.displayAdminTabOffers(result);
//            }
//        });
//
//    }

    public void onGetSortedOffers(int start, int count, Map<String, OrderType> orderColumns) {
        offerService.getSortedOffers(start, count, orderColumns,
                new AsyncCallback<List<FullOfferDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
                        eventBus.displayAdminTabOffers(result);
                    }
                });
    }

    public void onUpdateOffer(FullOfferDetail offer) {
        offerService.updateOffer(offer, new AsyncCallback<FullOfferDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(FullOfferDetail result) {
//                eventBus.refreshUpdatedOffer(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  CLIENT SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminClientsCount() {
        clientService.getClientsCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminClientsAsyncDataProvider(result);
            }
        });
    }

//    public void onGetAdminClients(int start, int count) {
//        clientService.getClients(start, count, new AsyncCallback<ArrayList<ClientDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<ClientDetail> result) {
//                eventBus.displayAdminTabClients(result);
//            }
//        });
//
//    }

    public void onGetSortedClients(int start, int count, Map<String, OrderType> orderColumns) {
        clientService.getSortedClients(start, count, orderColumns,
                new AsyncCallback<ArrayList<ClientDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<ClientDetail> result) {
                        eventBus.displayAdminTabClients(result);
                    }
                });
    }

    public void onUpdateClient(ClientDetail client) {
        clientService.updateClient(client, new AsyncCallback<ClientDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ClientDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  ACCESS ROLE SECTION. *************************************************
     **********************************************************************************************/
    public void onGetAdminAccessRolesCount() {
        generalService.getAccessRolesCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminClientsAsyncDataProvider(result.intValue());
            }
        });
    }

//    public void onGetAdminAccessRoles(int start, int count) {
//        generalService.getAccessRoles(start, count, new AsyncCallback<ArrayList<AccessRoleDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<AccessRoleDetail> result) {
//                eventBus.displayAdminTabAccessRoles(result);
//            }
//        });
//
//    }

    public void onGetSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getSortedAccessRoles(start, count, orderColumns,
                new AsyncCallback<ArrayList<AccessRoleDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<AccessRoleDetail> result) {
                        eventBus.displayAdminTabAccessRoles(result);
                    }
                });
    }

    public void onUpdateAccessRole(AccessRoleDetail role) {
        generalService.updateAccessRole(role, new AsyncCallback<AccessRoleDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(AccessRoleDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  EMAIL ACTIVATION SECTION.*********************************************
     **********************************************************************************************/
    public void onGetAdminEmailsActivationCount() {
        generalService.getEmailsActivationCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminClientsAsyncDataProvider(result);
            }
        });
    }

//    public void onGetAdminEmailsActivation(int start, int count) {
//        generalService.getEmailsActivation(start, count, new AsyncCallback<ArrayList<EmailActivationDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<EmailActivationDetail> result) {
//                eventBus.displayAdminTabEmailsActivation(result);
//            }
//        });
//
//    }

    public void onGetSortedEmailsActivation(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getSortedEmailsActivation(start, count, orderColumns,
                new AsyncCallback<ArrayList<EmailActivationDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<EmailActivationDetail> result) {
                        eventBus.displayAdminTabEmailsActivation(result);
                    }
                });
    }

    public void onUpdateEmailActivation(EmailActivationDetail client) {
        generalService.updateEmailActivation(client, new AsyncCallback<EmailActivationDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(EmailActivationDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  INVOICE SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminInvoicesCount() {
        generalService.getInvoicesCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminClientsAsyncDataProvider(result);
            }
        });
    }

//    public void onGetAdminInvoices(int start, int count) {
//        generalService.getInvoices(start, count, new AsyncCallback<ArrayList<InvoiceDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<InvoiceDetail> result) {
//                eventBus.displayAdminTabInvoices(result);
//            }
//        });
//
//    }

    public void onGetSortedInvoices(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getSortedInvoices(start, count, orderColumns,
                new AsyncCallback<ArrayList<InvoiceDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<InvoiceDetail> result) {
                        eventBus.displayAdminTabInvoices(result);
                    }
                });
    }

    public void onUpdateInvoice(InvoiceDetail client) {
        generalService.updateInvoice(client, new AsyncCallback<InvoiceDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(InvoiceDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OUR PAYMENT DETAILS SECTION. *****************************************
     **********************************************************************************************/
    public void onGetAdminOurPaymentDetailsCount() {
        generalService.getOurPaymentDetailsCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminOurPaymentDetailAsyncDataProvider(result);
            }
        });
    }

//    public void onGetAdminOurPaymentDetails(int start, int count) {
//        generalService.getOurPaymentDetails(start, count, new AsyncCallback<ArrayList<PaymentDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<PaymentDetail> result) {
//                eventBus.displayAdminTabOurPaymentDetails(result);
//            }
//        });
//
//    }

    public void onGetSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getSortedOurPaymentDetails(start, count, orderColumns,
                new AsyncCallback<ArrayList<PaymentDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<PaymentDetail> result) {
                        eventBus.displayAdminTabOurPaymentDetails(result);
                    }
                });
    }

    public void onUpdateOurPaymentDetail(PaymentDetail client) {
        generalService.updateOurPaymentDetail(client, new AsyncCallback<PaymentDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(PaymentDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PERMISSIONS SECTION. *****************************************
     **********************************************************************************************/
    public void onGetAdminPermissionsCount() {
        generalService.getPermissionsCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminPermissionAsyncDataProvider(result);
            }
        });
    }

//    public void onGetAdminPermissions(int start, int count) {
//        generalService.getPermissions(start, count, new AsyncCallback<ArrayList<PermissionDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<PermissionDetail> result) {
//                eventBus.displayAdminTabPermissions(result);
//            }
//        });
//
//    }

    public void onGetSortedPermissions(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getSortedPermissions(start, count, orderColumns,
                new AsyncCallback<ArrayList<PermissionDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<PermissionDetail> result) {
                        eventBus.displayAdminTabPermissions(result);
                    }
                });
    }

    public void onUpdatePermission(PermissionDetail permissions) {
        generalService.updatePermission(permissions, new AsyncCallback<PermissionDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(PermissionDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PREFERENCES SECTION. *****************************************
     **********************************************************************************************/
    public void onGetAdminPreferencesCount() {
        generalService.getPreferencesCount(new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Integer result) {
                eventBus.createAdminPreferenceAsyncDataProvider(result);
            }
        });
    }

//    public void onGetAdminPreferences(int start, int count) {
//        generalService.getPreferences(start, count, new AsyncCallback<ArrayList<PreferenceDetail>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void onSuccess(ArrayList<PreferenceDetail> result) {
//                eventBus.displayAdminTabPreferences(result);
//            }
//        });
//
//    }

    public void onGetSortedPreferences(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getSortedPreferences(start, count, orderColumns,
                new AsyncCallback<ArrayList<PreferenceDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(ArrayList<PreferenceDetail> result) {
                        eventBus.displayAdminTabPreferences(result);
                    }
                });
    }

    public void onUpdatePreference(PreferenceDetail client) {
        generalService.updatePreference(client, new AsyncCallback<PreferenceDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(PreferenceDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }
}
