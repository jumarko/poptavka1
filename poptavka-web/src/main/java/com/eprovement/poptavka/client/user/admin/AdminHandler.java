package com.eprovement.poptavka.client.user.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.AdminRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.domain.address.LocalityType;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PreferenceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;
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

    /**********************************************************************************************
     ***********************  DEMAND SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminDemandsCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminDemandsCount(searchDataHolder, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminDemandsAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminDemands(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminDemands(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<FullDemandDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminSuppliersCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminSuppliersCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminSuppliersAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminSuppliers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminSuppliers(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<FullSupplierDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminOffersCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminOffersCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminOffersAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminOffers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminOffers(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<OfferDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    RPCException commonException = (RPCException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
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
    public void onGetAdminClientsCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminClientsCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    RPCException commonException = (RPCException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminClientsAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminClients(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminClients(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<ClientDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            RPCException commonException = (RPCException) caught;
                            errorDialog = new ErrorDialogPopupView();
                            errorDialog.show(commonException.getSymbol());
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminAccessRolesCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    RPCException commonException = (RPCException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminAccessRoleAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminAccessRoles(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminAccessRoles(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<AccessRoleDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            RPCException commonException = (RPCException) caught;
                            errorDialog = new ErrorDialogPopupView();
                            errorDialog.show(commonException.getSymbol());
                        }
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
                if (caught instanceof RPCException) {
                    RPCException commonException = (RPCException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
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
    public void onGetAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminEmailsActivationCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    RPCException commonException = (RPCException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminEmailsActivationAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminEmailsActivation(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminEmailsActivation(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<ActivationEmailDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            RPCException commonException = (RPCException) caught;
                            errorDialog = new ErrorDialogPopupView();
                            errorDialog.show(commonException.getSymbol());
                        }
                    }
                    @Override
                    public void onSuccess(List<ActivationEmailDetail> result) {
                        eventBus.displayAdminTabEmailsActivation(result);
                    }
                });
    }

    public void onUpdateEmailActivation(ActivationEmailDetail client) {
        generalService.updateEmailActivation(client, new AsyncCallback<ActivationEmailDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    RPCException commonException = (RPCException) caught;
                    errorDialog = new ErrorDialogPopupView();
                    errorDialog.show(commonException.getSymbol());
                }
            }
            @Override
            public void onSuccess(ActivationEmailDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  INVOICE SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminInvoicesCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminInvoicesCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminInvoicesAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminInvoices(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminInvoices(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<InvoiceDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminMessagesCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminMessagesCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminMessagesAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminMessages(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminMessages(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<MessageDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminOurPaymentDetailsCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminOurPaymentDetailAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminOurPaymentDetails(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminOurPaymentDetails(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<PaymentDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }
                    @Override
                    public void onSuccess(List<PaymentDetail> result) {
                        eventBus.showAdminTabOurPaymentDetails(result);
                    }
                });
    }

    public void onUpdateOurPaymentDetail(PaymentDetail client) {
        generalService.updateOurPaymentDetail(client, new AsyncCallback<PaymentDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminPaymentMethodsCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminPaymentMethodAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminPaymentMethods(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminPaymentMethods(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<PaymentMethodDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminPermissionsCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminPermissionsCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminPermissionAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminPermissions(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminPermissions(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<PermissionDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminPreferencesCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminPreferencesCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminPreferenceAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminPreferences(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminPreferences(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<PreferenceDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
    public void onGetAdminProblemsCount(SearchModuleDataHolder searchDataHolder) {
        generalService.getAdminProblemsCount(searchDataHolder, new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminProblemAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminProblems(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns) {
        generalService.getAdminProblems(start, count, searchDataHolder, orderColumns,
                new AsyncCallback<List<ProblemDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }
            @Override
            public void onSuccess(ProblemDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }
}
