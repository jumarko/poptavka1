package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.AdminRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
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
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

@EventHandler
public class AdminHandler extends BaseEventHandler<AdminEventBus> {

    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private LocalityRPCServiceAsync localityService = null;
    @Inject
    private AdminRPCServiceAsync generalService = null;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.ADMIN_ACCESS_ROLE:
                getAdminAccessRolesCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_CLIENTS:
                getAdminClientsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_DEMANDS:
                getAdminDemandsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_EMAILS_ACTIVATION:
                getAdminEmailsActivationCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_INVOICES:
                getAdminInvoicesCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_MESSAGES:
                getAdminMessagesCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_OFFERS:
                getAdminOffersCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_OUR_PAYMENT_DETAILS:
                getAdminOurPaymentDetailsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_PAYMENT_METHODS:
                getAdminOurPaymentDetailsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_PERMISSIONS:
                getAdminPermissionsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_PREFERENCES:
                getAdminPreferencesCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_PROBLEMS:
                getAdminProblemsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_SUPPLIERS:
                getAdminSuppliersCount(grid, searchDefinition);
                break;
            default:
                break;
        }
    }

    public void onGetData(SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.ADMIN_ACCESS_ROLE:
                getAdminAccessRoles(searchDefinition);
                break;
            case Constants.ADMIN_CLIENTS:
                getAdminClients(searchDefinition);
                break;
            case Constants.ADMIN_DEMANDS:
                getAdminDemands(searchDefinition);
                break;
            case Constants.ADMIN_EMAILS_ACTIVATION:
                getAdminEmailsActivation(searchDefinition);
                break;
            case Constants.ADMIN_INVOICES:
                getAdminInvoices(searchDefinition);
                break;
            case Constants.ADMIN_MESSAGES:
                getAdminMessages(searchDefinition);
                break;
            case Constants.ADMIN_OFFERS:
                getAdminOffers(searchDefinition);
                break;
            case Constants.ADMIN_OUR_PAYMENT_DETAILS:
                getAdminOurPaymentDetails(searchDefinition);
                break;
            case Constants.ADMIN_PAYMENT_METHODS:
                getAdminOurPaymentDetails(searchDefinition);
                break;
            case Constants.ADMIN_PERMISSIONS:
                getAdminPermissions(searchDefinition);
                break;
            case Constants.ADMIN_PREFERENCES:
                getAdminPreferences(searchDefinition);
                break;
            case Constants.ADMIN_PROBLEMS:
                getAdminProblems(searchDefinition);
                break;
            case Constants.ADMIN_SUPPLIERS:
                getAdminSuppliers(searchDefinition);
                break;
            default:
                break;
        }
    }

    /**********************************************************************************************
     ***********************  DEMAND SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminDemandsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminDemandsCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
//                grid.getDataProvider().updateRowCount(result.intValue(), true);
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminDemands(SearchDefinition searchDefinition) {
        generalService.getAdminDemands(searchDefinition,
                new SecuredAsyncCallback<List<FullDemandDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<FullDemandDetail> result) {
                        eventBus.displayAdminTabDemands(result);
                    }
                });
    }

    public void onUpdateDemand(FullDemandDetail demand) {
        generalService.updateDemand(demand, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  SUPPLIER SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminSuppliersCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminSuppliersCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminSuppliers(SearchDefinition searchDefinition) {
        generalService.getAdminSuppliers(searchDefinition,
                new SecuredAsyncCallback<List<FullSupplierDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<FullSupplierDetail> result) {
                        eventBus.displayAdminTabSuppliers(result);
                    }
                });
    }

    public void onUpdateSupplier(FullSupplierDetail supplier) {
        generalService.updateSupplier(supplier, new SecuredAsyncCallback<FullSupplierDetail>(eventBus) {
            @Override
            public void onSuccess(FullSupplierDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OFFER SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminOffersCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminOffersCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminOffers(SearchDefinition searchDefinition) {
        generalService.getAdminOffers(searchDefinition,
                new SecuredAsyncCallback<List<OfferDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<OfferDetail> result) {
                        eventBus.displayAdminTabOffers(result);
                    }
                });
    }

    public void onUpdateOffer(OfferDetail offer) {
        generalService.updateOffer(offer, new SecuredAsyncCallback<OfferDetail>(eventBus) {
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
    public void getAdminClientsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminClientsCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminClients(SearchDefinition searchDefinition) {
        generalService.getAdminClients(searchDefinition,
                new SecuredAsyncCallback<List<ClientDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ClientDetail> result) {
                        eventBus.displayAdminTabClients(result);
                    }
                });
    }

    public void onUpdateClient(ClientDetail client) {
        generalService.updateClient(client, new SecuredAsyncCallback<ClientDetail>(eventBus) {
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
    public void getAdminAccessRolesCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminAccessRolesCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminAccessRoles(SearchDefinition searchDefinition) {
        generalService.getAdminAccessRoles(searchDefinition,
                new SecuredAsyncCallback<List<AccessRoleDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<AccessRoleDetail> result) {
                        eventBus.displayAdminTabAccessRoles(result);
                    }
                });
    }

    public void onUpdateAccessRole(AccessRoleDetail role) {
        generalService.updateAccessRole(role, new SecuredAsyncCallback<AccessRoleDetail>(eventBus) {
            @Override
            public void onSuccess(AccessRoleDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  EMAIL ACTIVATION SECTION.*********************************************
     **********************************************************************************************/
    public void getAdminEmailsActivationCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminEmailsActivationCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminEmailsActivation(SearchDefinition searchDefinition) {
        generalService.getAdminEmailsActivation(searchDefinition,
                new SecuredAsyncCallback<List<ActivationEmailDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ActivationEmailDetail> result) {
                        eventBus.displayAdminTabEmailsActivation(result);
                    }
                });
    }

    public void onUpdateEmailActivation(ActivationEmailDetail client) {
        generalService.updateEmailActivation(client, new SecuredAsyncCallback<ActivationEmailDetail>(eventBus) {
            @Override
            public void onSuccess(ActivationEmailDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  INVOICE SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminInvoicesCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminInvoicesCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminInvoices(SearchDefinition searchDefinition) {
        generalService.getAdminInvoices(searchDefinition,
                new SecuredAsyncCallback<List<InvoiceDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<InvoiceDetail> result) {
                        eventBus.displayAdminTabInvoices(result);
                    }
                });
    }

    public void onUpdateInvoice(InvoiceDetail client) {
        generalService.updateInvoice(client, new SecuredAsyncCallback<InvoiceDetail>(eventBus) {
            @Override
            public void onSuccess(InvoiceDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  Message SECTION. *****************************************************
     **********************************************************************************************/
    public void getAdminMessagesCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminMessagesCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminMessages(SearchDefinition searchDefinition) {
        generalService.getAdminMessages(searchDefinition,
                new SecuredAsyncCallback<List<MessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<MessageDetail> result) {
                        eventBus.displayAdminTabMessages(result);
                    }
                });
    }

    public void onUpdateMessage(MessageDetail client) {
        generalService.updateMessage(client, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  OUR PAYMENT DETAILS SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminOurPaymentDetailsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminOurPaymentDetailsCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminOurPaymentDetails(SearchDefinition searchDefinition) {
        generalService.getAdminOurPaymentDetails(searchDefinition,
                new SecuredAsyncCallback<List<PaymentDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<PaymentDetail> result) {
                        eventBus.displayAdminTabOurPaymentDetails(result);
                    }
                });
    }

    public void onUpdateOurPaymentDetail(PaymentDetail client) {
        generalService.updateOurPaymentDetail(client, new SecuredAsyncCallback<PaymentDetail>(eventBus) {
            @Override
            public void onSuccess(PaymentDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PAYMENT METHOD SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminPaymentMethodsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminPaymentMethodsCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminPaymentMethods(SearchDefinition searchDefinition) {
        generalService.getAdminPaymentMethods(searchDefinition,
                new SecuredAsyncCallback<List<PaymentMethodDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<PaymentMethodDetail> result) {
                        eventBus.displayAdminTabPaymentMethods(result);
                    }
                });
    }

    public void onUpdatePaymentMethod(PaymentMethodDetail paymentMethods) {
        generalService.updatePaymentMethod(paymentMethods, new SecuredAsyncCallback<PaymentMethodDetail>(eventBus) {
            @Override
            public void onSuccess(PaymentMethodDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PERMISSIONS SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminPermissionsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminPermissionsCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminPermissions(SearchDefinition searchDefinition) {
        generalService.getAdminPermissions(searchDefinition,
                new SecuredAsyncCallback<List<PermissionDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<PermissionDetail> result) {
                        eventBus.displayAdminTabPermissions(result);
                    }
                });
    }

    public void onUpdatePermission(PermissionDetail permissions) {
        generalService.updatePermission(permissions, new SecuredAsyncCallback<PermissionDetail>(eventBus) {
            @Override
            public void onSuccess(PermissionDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PREFERENCES SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminPreferencesCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminPreferencesCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminPreferences(SearchDefinition searchDefinition) {
        generalService.getAdminPreferences(searchDefinition,
                new SecuredAsyncCallback<List<PreferenceDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<PreferenceDetail> result) {
                        eventBus.displayAdminTabPreferences(result);
                    }
                });
    }

    public void onUpdatePreference(PreferenceDetail client) {
        generalService.updatePreference(client, new SecuredAsyncCallback<PreferenceDetail>(eventBus) {
            @Override
            public void onSuccess(PreferenceDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**********************************************************************************************
     ***********************  PROBLEM SECTION. *****************************************
     **********************************************************************************************/
    public void getAdminProblemsCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        generalService.getAdminProblemsCount(searchDefinition, new SecuredAsyncCallback<Long>(eventBus) {
            @Override
            public void onSuccess(Long result) {
                grid.getDataProvider().updateRowCount(result.intValue(), true);
            }
        });
    }

    public void getAdminProblems(SearchDefinition searchDefinition) {
        generalService.getAdminProblems(searchDefinition,
                new SecuredAsyncCallback<List<ProblemDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<ProblemDetail> result) {
                        eventBus.displayAdminTabProblems(result);
                    }
                });
    }

    public void onUpdateProblem(ProblemDetail client) {
        generalService.updateProblem(client, new SecuredAsyncCallback<ProblemDetail>(eventBus) {
            @Override
            public void onSuccess(ProblemDetail result) {
//                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    public void onUpdateUnreadMessagesCount() {
        generalService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                // empty i.e number of new messages could be retrieved
                GWT.log("UpdateUnreadMessagesCount retrieved, number=" + result.getUnreadMessagesCount());
                eventBus.setUpdatedUnreadMessagesCount(result.getUnreadMessagesCount());
            }
        });
    }
}
