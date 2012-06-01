package cz.poptavka.sample.client.service.demand;

import cz.poptavka.sample.shared.domain.adminModule.ActivationEmailDetail;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.adminModule.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.adminModule.ClientDetail;
import cz.poptavka.sample.shared.domain.adminModule.InvoiceDetail;
import cz.poptavka.sample.shared.domain.adminModule.OfferDetail;
import cz.poptavka.sample.shared.domain.adminModule.PaymentDetail;
import cz.poptavka.sample.shared.domain.adminModule.PaymentMethodDetail;
import cz.poptavka.sample.shared.domain.adminModule.PermissionDetail;
import cz.poptavka.sample.shared.domain.adminModule.PreferenceDetail;
import cz.poptavka.sample.shared.domain.adminModule.ProblemDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.Map;

public interface AdminRPCServiceAsync {

    //---------------------- DEMAND ------------------------------------------------
    void getAdminDemandsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminDemands(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<FullDemandDetail>> callback);

    void updateDemand(FullDemandDetail detailObject,
            AsyncCallback<FullDemandDetail> callback);

    //---------------------- CLIENT ------------------------------------------------
    void getAdminClientsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminClients(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<ClientDetail>> callback);

    void updateClient(ClientDetail detailObject, AsyncCallback<ClientDetail> callback);

    //---------------------- SUPPLIER ------------------------------------------------
    void getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminSuppliers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<FullSupplierDetail>> callback);

    void updateSupplier(FullSupplierDetail detailObject, AsyncCallback<FullSupplierDetail> callback);

    //---------------------- OFFER ------------------------------------------------
    void getAdminOffersCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminOffers(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<OfferDetail>> callback);

    void updateOffer(OfferDetail detailObject, AsyncCallback<OfferDetail> callback);

    //---------------------- ACCESS ROLE ------------------------------------------------
    void getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminAccessRoles(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<AccessRoleDetail>> callback);

    void updateAccessRole(AccessRoleDetail detailObject, AsyncCallback<AccessRoleDetail> callback);

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    void getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminEmailsActivation(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<ActivationEmailDetail>> callback);

    void updateEmailActivation(ActivationEmailDetail detailObject, AsyncCallback<ActivationEmailDetail> callback);

    //---------------------- INVOICE --------------------------------------------------
    void getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminInvoices(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<InvoiceDetail>> callback);

    void updateInvoice(InvoiceDetail detailObject, AsyncCallback<InvoiceDetail> callback);

    //---------------------- MESSAGE ------------------------------------------------
    void getAdminMessagesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminMessages(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<MessageDetail>> callback);

    void updateMessage(MessageDetail detailObject, AsyncCallback<MessageDetail> callback);

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    void getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminOurPaymentDetails(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PaymentDetail>> callback);

    void updateOurPaymentDetail(PaymentDetail detailObject, AsyncCallback<PaymentDetail> callback);

    //---------------------- PAYMENT METHOD ------------------------------------------------
    void getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPaymentMethods(AsyncCallback<List<PaymentMethodDetail>> callback);

    void getAdminPaymentMethods(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PaymentMethodDetail>> callback);

    void updatePaymentMethod(PaymentMethodDetail detailObject, AsyncCallback<PaymentMethodDetail> callback);

    //---------------------- PERMISSION ------------------------------------------------
    void getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPermissions(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PermissionDetail>> callback);

    void updatePermission(PermissionDetail detailObject, AsyncCallback<PermissionDetail> callback);

    //---------------------- PREFERENCES ------------------------------------------------
    void getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminPreferences(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<PreferenceDetail>> callback);

    void updatePreference(PreferenceDetail detailObject, AsyncCallback<PreferenceDetail> callback);

    //---------------------- PROBLEM ------------------------------------------------
    void getAdminProblemsCount(SearchModuleDataHolder searchDataHolder, AsyncCallback<Long> callback);

    void getAdminProblems(int start, int count, SearchModuleDataHolder searchDataHolder,
            Map<String, OrderType> orderColumns, AsyncCallback<List<ProblemDetail>> callback);

    void updateProblem(ProblemDetail detailObject, AsyncCallback<ProblemDetail> callback);
}
