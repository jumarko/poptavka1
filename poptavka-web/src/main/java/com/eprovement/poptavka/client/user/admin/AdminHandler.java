package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.common.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.AdminRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PreferenceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;
import java.util.Map;

@EventHandler
public class AdminHandler extends BaseEventHandler<AdminEventBus> {

    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private LocalityRPCServiceAsync localityService = null;
    @Inject
    private AdminRPCServiceAsync generalService = null;
    private ErrorDialogPopupView errorDialog;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.ADMIN_ACCESS_ROLE:
                getAdminAccessRolesCount(grid, detail);
                break;
            case Constants.ADMIN_CLIENTS:
                getAdminClientsCount(grid, detail);
                break;
            case Constants.ADMIN_DEMANDS:
                getAdminDemandsCount(grid, detail);
                break;
            case Constants.ADMIN_EMAILS_ACTIVATION:
                getAdminEmailsActivationCount(grid, detail);
                break;
            case Constants.ADMIN_INVOICES:
                getAdminInvoicesCount(grid, detail);
                break;
            case Constants.ADMIN_MESSAGES:
                getAdminMessagesCount(grid, detail);
                break;
            case Constants.ADMIN_OFFERS:
                getAdminOffersCount(grid, detail);
                break;
            case Constants.ADMIN_OUR_PAYMENT_DETAILS:
                getAdminOurPaymentDetailsCount(grid, detail);
                break;
            case Constants.ADMIN_PAYMENT_METHODS:
                getAdminOurPaymentDetailsCount(grid, detail);
                break;
            case Constants.ADMIN_PERMISSIONS:
                getAdminPermissionsCount(grid, detail);
                break;
            case Constants.ADMIN_PREFERENCES:
                getAdminPreferencesCount(grid, detail);
                break;
            case Constants.ADMIN_PROBLEMS:
                getAdminProblemsCount(grid, detail);
                break;
            case Constants.ADMIN_SUPPLIERS:
                getAdminSuppliersCount(grid, detail);
                break;
            default:
                break;
        }
    }

    public void onGetData(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.ADMIN_ACCESS_ROLE:
                getAdminAccessRoles(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_CLIENTS:
                getAdminClients(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_DEMANDS:
                getAdminDemands(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_EMAILS_ACTIVATION:
                getAdminEmailsActivation(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_INVOICES:
                getAdminInvoices(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_MESSAGES:
                getAdminMessages(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_OFFERS:
                getAdminOffers(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_OUR_PAYMENT_DETAILS:
                getAdminOurPaymentDetails(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_PAYMENT_METHODS:
                getAdminOurPaymentDetails(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_PERMISSIONS:
                getAdminPermissions(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_PREFERENCES:
                getAdminPreferences(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_PROBLEMS:
                getAdminProblems(start, maxResults, detail, orderColumns);
                break;
            case Constants.ADMIN_SUPPLIERS:
                getAdminSuppliers(start, maxResults, detail, orderColumns);
                break;
            default:
                break;
        }
    }

    /**********************************************************************************************
     ***********************  DEMAND SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminDemandsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminDemandsCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminDemands(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminDemands(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<FullDemandDetail>>() {
                    @Override
                    public void onSuccess(List<FullDemandDetail> result) {
                        eventBus.displayAdminTabDemands(result);
                    }
                });
    }

    public void onUpdateDemand(FullDemandDetail demand) {
        generalService.updateDemand(demand, new SecuredAsyncCallback<FullDemandDetail>() {
            @Override
            public void onSuccess(FullDemandDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    //------------------- DEMAND SECTION - CATEGORY SECTION. -------------------------------
    public void onGetAdminDemandRootCategories() {
        categoryService.getAllRootCategories(new SecuredAsyncCallback<List<CategoryDetail>>() {
            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.displayAdminDemandCategories(result);
            }
        });
    }

    public void onGetAdminDemandSubCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new SecuredAsyncCallback<List<CategoryDetail>>() {
            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.displayAdminDemandCategories(result);
            }
        });
    }

    public void onGetAdminDemandParentCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new SecuredAsyncCallback<List<CategoryDetail>>() {
            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.doBackDemandCategories(result);
            }
        });
    }

    //------------------- DEMAND SECTION - LOCALITY SECTION. -------------------------------
    public void onGetAdminDemandRootLocalities() {
        localityService.getLocalities(LocalityType.REGION, new SecuredAsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> result) {
                eventBus.displayAdminDemandLocalities(result);
            }
        });
    }

    public void onGetAdminDemandSubLocalities(String locCode) {
        localityService.getLocalities(locCode, new SecuredAsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> result) {
                eventBus.displayAdminDemandLocalities(result);
            }
        });
    }

    public void onGetAdminDemandParentLocalities(String locCode) {
        localityService.getLocalities(locCode, new SecuredAsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> result) {
                eventBus.doBackDemandLocalities(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  SUPPLIER SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminSuppliersCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminSuppliersCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminSuppliers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminSuppliers(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<FullSupplierDetail>>() {
                    @Override
                    public void onSuccess(List<FullSupplierDetail> result) {
                        eventBus.displayAdminTabSuppliers(result);
                    }
                });
    }

    public void onUpdateSupplier(FullSupplierDetail supplier) {
        generalService.updateSupplier(supplier, new SecuredAsyncCallback<FullSupplierDetail>() {
            @Override
            public void onSuccess(FullSupplierDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    //----------------------- SUPPLIER SECTION - CATEGORY SECTION. ---------------------------------
    public void onGetAdminSupplierRootCategories() {
        categoryService.getAllRootCategories(new SecuredAsyncCallback<List<CategoryDetail>>() {
            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.displayAdminSupplierCategories(result);
            }
        });
    }

    public void onGetAdminSupplierSubCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new SecuredAsyncCallback<List<CategoryDetail>>() {
            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.displayAdminSupplierCategories(result);
            }
        });
    }

    public void onGetAdminSupplierParentCategories(Long catId) {
        categoryService.getCategoryChildren(catId, new SecuredAsyncCallback<List<CategoryDetail>>() {
            @Override
            public void onSuccess(List<CategoryDetail> result) {
                eventBus.doBackSupplierCategories(result);
            }
        });
    }

    //-------------------------- SUPPLIER SECTION - LOCALITY SECTION. -----------------------------
    public void onGetAdminSupplierRootLocalities() {
        localityService.getLocalities(LocalityType.REGION, new SecuredAsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> result) {
                eventBus.displayAdminDemandLocalities(result);
            }
        });
    }

    public void onGetAdminSupplierSubLocalities(String locCode) {
        localityService.getLocalities(locCode, new SecuredAsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> result) {
                eventBus.displayAdminSupplierLocalities(result);
            }
        });
    }

    public void onGetAdminSupplierParentLocalities(String locCode) {
        localityService.getLocalities(locCode, new SecuredAsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> result) {
                eventBus.doBackSupplierLocalities(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OFFER SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminOffersCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminOffersCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminOffers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminOffers(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<OfferDetail>>() {
                    @Override
                    public void onSuccess(List<OfferDetail> result) {
                        eventBus.displayAdminTabOffers(result);
                    }
                });
    }

    public void onUpdateOffer(OfferDetail offer) {
        generalService.updateOffer(offer, new SecuredAsyncCallback<OfferDetail>() {
            @Override
            public void onSuccess(OfferDetail result) {
                // TODO: what's wrong with this ?
//                eventBus.refreshUpdatedOffer(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  CLIENT SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminClientsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminClientsCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminClients(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminClients(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<ClientDetail>>() {
                    @Override
                    public void onSuccess(List<ClientDetail> result) {
                        eventBus.displayAdminTabClients(result);
                    }
                });
    }

    public void onUpdateClient(ClientDetail client) {
        generalService.updateClient(client, new SecuredAsyncCallback<ClientDetail>() {
            @Override
            public void onSuccess(ClientDetail result) {
                // TODO: what's wrong with this ?
                //                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  ACCESS ROLE SECTION. *************************************************
     **********************************************************************************************/
    public void getAdminAccessRolesCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminAccessRolesCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminAccessRoles(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminAccessRoles(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<AccessRoleDetail>>() {
                    @Override
                    public void onSuccess(List<AccessRoleDetail> result) {
                        eventBus.displayAdminTabAccessRoles(result);
                    }
                });
    }

    public void onUpdateAccessRole(AccessRoleDetail role) {
        generalService.updateAccessRole(role, new SecuredAsyncCallback<AccessRoleDetail>() {
            @Override
            public void onSuccess(AccessRoleDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  EMAIL ACTIVATION SECTION.*********************************************
     **********************************************************************************************/
    public void getAdminEmailsActivationCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminEmailsActivationCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminEmailsActivation(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminEmailsActivation(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<ActivationEmailDetail>>() {
                    @Override
                    public void onSuccess(List<ActivationEmailDetail> result) {
                        eventBus.displayAdminTabEmailsActivation(result);
                    }
                });
    }

    public void onUpdateEmailActivation(ActivationEmailDetail client) {
        generalService.updateEmailActivation(client, new SecuredAsyncCallback<ActivationEmailDetail>() {
            @Override
            public void onSuccess(ActivationEmailDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  INVOICE SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminInvoicesCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminInvoicesCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminInvoices(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminInvoices(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<InvoiceDetail>>() {
                    @Override
                    public void onSuccess(List<InvoiceDetail> result) {
                        eventBus.displayAdminTabInvoices(result);
                    }
                });
    }

    public void onUpdateInvoice(InvoiceDetail client) {
        generalService.updateInvoice(client, new SecuredAsyncCallback<InvoiceDetail>() {
            @Override
            public void onSuccess(InvoiceDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  Message SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminMessagesCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminMessagesCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminMessages(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminMessages(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<MessageDetail>>() {
                    @Override
                    public void onSuccess(List<MessageDetail> result) {
                        eventBus.displayAdminTabMessages(result);
                    }
                });
    }

    public void onUpdateMessage(MessageDetail client) {
        generalService.updateMessage(client, new SecuredAsyncCallback<MessageDetail>() {
            @Override
            public void onSuccess(MessageDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OUR PAYMENT DETAILS SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminOurPaymentDetailsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminOurPaymentDetailsCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminOurPaymentDetails(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminOurPaymentDetails(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<PaymentDetail>>() {
                    @Override
                    public void onSuccess(List<PaymentDetail> result) {
                        eventBus.displayAdminTabOurPaymentDetails(result);
                    }
                });
    }

    public void onUpdateOurPaymentDetail(PaymentDetail client) {
        generalService.updateOurPaymentDetail(client, new SecuredAsyncCallback<PaymentDetail>() {
            @Override
            public void onSuccess(PaymentDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PAYMENT METHOD SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminPaymentMethodsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminPaymentMethodsCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminPaymentMethods(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminPaymentMethods(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<PaymentMethodDetail>>() {
                    @Override
                    public void onSuccess(List<PaymentMethodDetail> result) {
                        eventBus.displayAdminTabPaymentMethods(result);
                    }
                });
    }

    public void onUpdatePaymentMethod(PaymentMethodDetail paymentMethods) {
        generalService.updatePaymentMethod(paymentMethods, new SecuredAsyncCallback<PaymentMethodDetail>() {
            @Override
            public void onSuccess(PaymentMethodDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PERMISSIONS SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminPermissionsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminPermissionsCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminPermissions(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminPermissions(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<PermissionDetail>>() {
                    @Override
                    public void onSuccess(List<PermissionDetail> result) {
                        eventBus.displayAdminTabPermissions(result);
                    }
                });
    }

    public void onUpdatePermission(PermissionDetail permissions) {
        generalService.updatePermission(permissions, new SecuredAsyncCallback<PermissionDetail>() {
            @Override
            public void onSuccess(PermissionDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PREFERENCES SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminPreferencesCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminPreferencesCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminPreferences(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminPreferences(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<PreferenceDetail>>() {
                    @Override
                    public void onSuccess(List<PreferenceDetail> result) {
                        eventBus.displayAdminTabPreferences(result);
                    }
                });
    }

    public void onUpdatePreference(PreferenceDetail client) {
        generalService.updatePreference(client, new SecuredAsyncCallback<PreferenceDetail>() {
            @Override
            public void onSuccess(PreferenceDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PROBLEM SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminProblemsCount(final UniversalAsyncGrid grid, SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminProblemsCount(searchDataHolder, new SecuredAsyncCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                grid.createAsyncDataProvider(result.intValue());
            }
        });
    }

    public void getAdminProblems(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminProblems(start, count, searchDataHolder, orderColumns,
                new SecuredAsyncCallback<List<ProblemDetail>>() {
                    @Override
                    public void onSuccess(List<ProblemDetail> result) {
                        eventBus.displayAdminTabProblems(result);
                    }
                });
    }

    public void onUpdateProblem(ProblemDetail client) {
        generalService.updateProblem(client, new SecuredAsyncCallback<ProblemDetail>() {
            @Override
            public void onSuccess(ProblemDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }
}
