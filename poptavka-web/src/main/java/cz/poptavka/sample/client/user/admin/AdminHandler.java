package cz.poptavka.sample.client.user.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.GeneralRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.PaymentDetail;
import cz.poptavka.sample.shared.domain.PaymentMethodDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;
import cz.poptavka.sample.shared.domain.PreferenceDetail;
import cz.poptavka.sample.shared.domain.ProblemDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EventHandler
public class AdminHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private LocalityRPCServiceAsync localityService = null;
    @Inject
    private GeneralRPCServiceAsync generalService = null;

    /**********************************************************************************************
     ***********************  DEMAND SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminDemandsCount() {
        generalService.getAdminDemandsCount(new AsyncCallback<Long>() {
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
        generalService.getAdminDemands(start, count,
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

    public void onGetSortedDemands(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedDemands(start, count, orderColumns,
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
        generalService.updateDemand(demand, new AsyncCallback<FullDemandDetail>() {
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
        generalService.getAdminSuppliersCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminSuppliersAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminSuppliers(int start, int count) {
        generalService.getAdminSuppliers(start, count, new AsyncCallback<List<FullSupplierDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<FullSupplierDetail> result) {
                eventBus.displayAdminTabSuppliers(result);
            }
        });
    }

    public void onGetSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedSuppliers(start, count, orderColumns,
                new AsyncCallback<List<FullSupplierDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<FullSupplierDetail> result) {
                        eventBus.displayAdminTabSuppliers(result);
                    }
                });
    }

    public void onUpdateSupplier(FullSupplierDetail supplier) {
        generalService.updateSupplier(supplier, new AsyncCallback<FullSupplierDetail>() {
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
        generalService.getAdminOffersCount(new AsyncCallback<Long>() {
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

    public void onGetAdminOffers(int start, int count) {
        generalService.getAdminOffers(start, count, new AsyncCallback<List<OfferDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<OfferDetail> result) {
                eventBus.displayAdminTabOffers(result);
            }
        });
    }

    public void onGetSortedOffers(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedOffers(start, count, orderColumns,
                new AsyncCallback<List<OfferDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<OfferDetail> result) {
                        eventBus.displayAdminTabOffers(result);
                    }
                });
    }

    public void onUpdateOffer(OfferDetail offer) {
        generalService.updateOffer(offer, new AsyncCallback<OfferDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(OfferDetail result) {
//                eventBus.refreshUpdatedOffer(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  CLIENT SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminClientsCount() {
        generalService.getAdminClientsCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminClientsAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminClients(int start, int count) {
        generalService.getAdminClients(start, count, new AsyncCallback<List<ClientDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<ClientDetail> result) {
                eventBus.displayAdminTabClients(result);
            }
        });
    }

    public void onGetSortedClients(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedClients(start, count, orderColumns,
                new AsyncCallback<List<ClientDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<ClientDetail> result) {
                        eventBus.displayAdminTabClients(result);
                    }
                });
    }

    public void onUpdateClient(ClientDetail client) {
        generalService.updateClient(client, new AsyncCallback<ClientDetail>() {
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
        generalService.getAdminAccessRolesCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminAccessRoleAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminAccessRoles(int start, int count) {
        generalService.getAdminAccessRoles(start, count, new AsyncCallback<List<AccessRoleDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<AccessRoleDetail> result) {
                eventBus.displayAdminTabAccessRoles(result);
            }
        });
    }

    public void onGetSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedAccessRoles(start, count, orderColumns,
                new AsyncCallback<List<AccessRoleDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<AccessRoleDetail> result) {
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
        generalService.getAdminEmailsActivationCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminEmailsActivationAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminEmailsActivation(int start, int count) {
        generalService.getAdminEmailsActivation(start, count, new AsyncCallback<List<EmailActivationDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<EmailActivationDetail> result) {
                eventBus.displayAdminTabEmailsActivation(result);
            }
        });
    }

    public void onGetSortedEmailsActivation(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedEmailsActivation(start, count, orderColumns,
                new AsyncCallback<List<EmailActivationDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<EmailActivationDetail> result) {
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
        generalService.getAdminInvoicesCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminInvoicesAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminInvoices(int start, int count) {
        generalService.getAdminInvoices(start, count, new AsyncCallback<List<InvoiceDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<InvoiceDetail> result) {
                eventBus.displayAdminTabInvoices(result);
            }
        });
    }

    public void onGetSortedInvoices(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedInvoices(start, count, orderColumns,
                new AsyncCallback<List<InvoiceDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<InvoiceDetail> result) {
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
     ***********************  Message SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminMessagesCount() {
        generalService.getAdminMessagesCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminMessagesAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminMessages(int start, int count) {
        generalService.getAdminMessages(start, count, new AsyncCallback<List<MessageDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<MessageDetail> result) {
                eventBus.displayAdminTabMessages(result);
            }
        });
    }

    public void onGetSortedMessages(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedMessages(start, count, orderColumns,
                new AsyncCallback<List<MessageDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<MessageDetail> result) {
                        eventBus.displayAdminTabMessages(result);
                    }
                });
    }

    public void onUpdateMessage(MessageDetail client) {
        generalService.updateMessage(client, new AsyncCallback<MessageDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(MessageDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OUR PAYMENT DETAILS SECTION. *****************************************
     **********************************************************************************************/
    public void onGetAdminOurPaymentDetailsCount() {
        generalService.getAdminOurPaymentDetailsCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminOurPaymentDetailAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminOurPaymentDetails(int start, int count) {
        generalService.getAdminOurPaymentDetails(start, count, new AsyncCallback<List<PaymentDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<PaymentDetail> result) {
                eventBus.displayAdminTabOurPaymentDetails(result);
            }
        });
    }

    public void onGetSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedOurPaymentDetails(start, count, orderColumns,
                new AsyncCallback<List<PaymentDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<PaymentDetail> result) {
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
     ***********************  PAYMENT METHOD SECTION. *****************************************
     **********************************************************************************************/
    public void onGetAdminPaymentMethodsCount() {
        generalService.getAdminPaymentMethodsCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminPaymentMethodAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminPaymentMethods(int start, int count) {
        generalService.getAdminPaymentMethods(start, count, new AsyncCallback<List<PaymentMethodDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<PaymentMethodDetail> result) {
                eventBus.displayAdminTabPaymentMethods(result);
            }
        });
    }

    public void onGetSortedPaymentMethods(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedPaymentMethods(start, count, orderColumns,
                new AsyncCallback<List<PaymentMethodDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<PaymentMethodDetail> result) {
                        eventBus.displayAdminTabPaymentMethods(result);
                    }
                });
    }

    public void onUpdatePaymentMethod(PaymentMethodDetail paymentMethods) {
        generalService.updatePaymentMethod(paymentMethods, new AsyncCallback<PaymentMethodDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(PaymentMethodDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PERMISSIONS SECTION. *****************************************
     **********************************************************************************************/
    public void onGetAdminPermissionsCount() {
        generalService.getAdminPermissionsCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminPermissionAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminPermissions(int start, int count) {
        generalService.getAdminPermissions(start, count, new AsyncCallback<List<PermissionDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<PermissionDetail> result) {
                eventBus.displayAdminTabPermissions(result);
            }
        });
    }

    public void onGetSortedPermissions(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedPermissions(start, count, orderColumns,
                new AsyncCallback<List<PermissionDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<PermissionDetail> result) {
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
        generalService.getAdminPreferencesCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminPreferenceAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminPreferences(int start, int count) {
        generalService.getAdminPreferences(start, count, new AsyncCallback<List<PreferenceDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<PreferenceDetail> result) {
                eventBus.displayAdminTabPreferences(result);
            }
        });
    }

    public void onGetSortedPreferences(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedPreferences(start, count, orderColumns,
                new AsyncCallback<List<PreferenceDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<PreferenceDetail> result) {
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

    /**********************************************************************************************
     ***********************  PROBLEM SECTION. *****************************************
     **********************************************************************************************/
    public void onGetAdminProblemsCount() {
        generalService.getAdminProblemsCount(new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminProblemAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminProblems(int start, int count) {
        generalService.getAdminProblems(start, count, new AsyncCallback<List<ProblemDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(List<ProblemDetail> result) {
                eventBus.displayAdminTabProblems(result);
            }
        });
    }

    public void onGetSortedProblems(int start, int count, Map<String, OrderType> orderColumns) {
        generalService.getAdminSortedProblems(start, count, orderColumns,
                new AsyncCallback<List<ProblemDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    @Override
                    public void onSuccess(List<ProblemDetail> result) {
                        eventBus.displayAdminTabProblems(result);
                    }
                });
    }

    public void onUpdateProblem(ProblemDetail client) {
        generalService.updateProblem(client, new AsyncCallback<ProblemDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onSuccess(ProblemDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }
}
