package com.eprovement.poptavka.client.service.demand;

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
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface AdminRPCServiceAsync {

    //---------------------- DEMAND ------------------------------------------------
    void getAdminDemandsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminDemands(SearchDefinition searchDefinition, AsyncCallback<List<FullDemandDetail>> callback);

    void updateDemand(FullDemandDetail detailObject,
            AsyncCallback<FullDemandDetail> callback);

    //---------------------- CLIENT ------------------------------------------------
    void getAdminClientsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminClients(SearchDefinition searchDefinition, AsyncCallback<List<ClientDetail>> callback);

    void updateClient(ClientDetail detailObject, AsyncCallback<ClientDetail> callback);

    //---------------------- SUPPLIER ------------------------------------------------
    void getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminSuppliers(SearchDefinition searchDefinition, AsyncCallback<List<FullSupplierDetail>> callback);

    void updateSupplier(FullSupplierDetail detailObject, AsyncCallback<FullSupplierDetail> callback);

    //---------------------- OFFER ------------------------------------------------
    void getAdminOffersCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminOffers(SearchDefinition searchDefinition, AsyncCallback<List<OfferDetail>> callback);

    void updateOffer(OfferDetail detailObject, AsyncCallback<OfferDetail> callback);

    //---------------------- ACCESS ROLE ------------------------------------------------
    void getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminAccessRoles(SearchDefinition searchDefinition, AsyncCallback<List<AccessRoleDetail>> callback);

    void updateAccessRole(AccessRoleDetail detailObject, AsyncCallback<AccessRoleDetail> callback);

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    void getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminEmailsActivation(
            SearchDefinition searchDefinition, AsyncCallback<List<ActivationEmailDetail>> callback);

    void updateEmailActivation(ActivationEmailDetail detailObject, AsyncCallback<ActivationEmailDetail> callback);

    //---------------------- INVOICE --------------------------------------------------
    void getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminInvoices(SearchDefinition searchDefinition, AsyncCallback<List<InvoiceDetail>> callback);

    void updateInvoice(InvoiceDetail detailObject, AsyncCallback<InvoiceDetail> callback);

    //---------------------- MESSAGE ------------------------------------------------
    void getAdminMessagesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminMessages(SearchDefinition searchDefinition, AsyncCallback<List<MessageDetail>> callback);

    void updateMessage(MessageDetail detailObject, AsyncCallback<MessageDetail> callback);

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    void getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminOurPaymentDetails(SearchDefinition searchDefinition, AsyncCallback<List<PaymentDetail>> callback);

    void updateOurPaymentDetail(PaymentDetail detailObject, AsyncCallback<PaymentDetail> callback);

    //---------------------- PAYMENT METHOD ------------------------------------------------
    void getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPaymentMethods(AsyncCallback<List<PaymentMethodDetail>> callback);

    void getAdminPaymentMethods(SearchDefinition searchDefinition, AsyncCallback<List<PaymentMethodDetail>> callback);

    void updatePaymentMethod(PaymentMethodDetail detailObject, AsyncCallback<PaymentMethodDetail> callback);

    //---------------------- PERMISSION ------------------------------------------------
    void getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPermissions(SearchDefinition searchDefinition, AsyncCallback<List<PermissionDetail>> callback);

    void updatePermission(PermissionDetail detailObject, AsyncCallback<PermissionDetail> callback);

    //---------------------- PREFERENCES ------------------------------------------------
    void getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPreferences(SearchDefinition searchDefinition, AsyncCallback<List<PreferenceDetail>> callback);

    void updatePreference(PreferenceDetail detailObject, AsyncCallback<PreferenceDetail> callback);

    //---------------------- PROBLEM ------------------------------------------------
    void getAdminProblemsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminProblems(SearchDefinition searchDefinition, AsyncCallback<List<ProblemDetail>> callback);

    void updateProblem(ProblemDetail detailObject, AsyncCallback<ProblemDetail> callback);
}
