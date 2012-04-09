package cz.poptavka.sample.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.adminModule.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.adminModule.ClientDetail;
import cz.poptavka.sample.shared.domain.adminModule.EmailActivationDetail;
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
import cz.poptavka.sample.shared.exceptions.CommonException;

import java.util.Map;

@RemoteServiceRelativePath("service/admin")
public interface AdminRPCService extends RemoteService {

    //---------------------- DEMAND ------------------------------------------------
    Long getAdminDemandsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<FullDemandDetail> getAdminDemands(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    FullDemandDetail updateDemand(FullDemandDetail detailObject) throws CommonException;

//    List<FullDemandDetail> getAdminSortedDemands(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- CLIENT ------------------------------------------------
    Long getAdminClientsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<ClientDetail> getAdminClients(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    ClientDetail updateClient(ClientDetail detailObject) throws CommonException;

//    List<ClientDetail> getAdminSortedClients(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- SUPPLIER ------------------------------------------------
    Long getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<FullSupplierDetail> getAdminSuppliers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    FullSupplierDetail updateSupplier(FullSupplierDetail detailObject) throws CommonException;

//    List<FullSupplierDetail> getAdminSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- OFFER ------------------------------------------------
    Long getAdminOffersCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<OfferDetail> getAdminOffers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    OfferDetail updateOffer(OfferDetail detailObject) throws CommonException;

//    List<OfferDetail> getAdminSortedOffers(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- ACCESS ROLE ------------------------------------------------
    Long getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<AccessRoleDetail> getAdminAccessRoles(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    AccessRoleDetail updateAccessRole(AccessRoleDetail detailObject) throws CommonException;

//    List<AccessRoleDetail> getAdminSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- EMAIL ACTIVATION---------------------------------------------
    Long getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<EmailActivationDetail> getAdminEmailsActivation(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    EmailActivationDetail updateEmailActivation(EmailActivationDetail detailObject) throws CommonException;

//    List<EmailActivationDetail> getAdminSortedEmailsActivation(
//            int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- INVOICE --------------------------------------------------
    Long getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<InvoiceDetail> getAdminInvoices(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    InvoiceDetail updateInvoice(InvoiceDetail detailObject) throws CommonException;

//    List<InvoiceDetail> getAdminSortedInvoices(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- MESSAGE ------------------------------------------------
    Long getAdminMessagesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<MessageDetail> getAdminMessages(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    MessageDetail updateMessage(MessageDetail detailObject) throws CommonException;

//    List<MessageDetail> getAdminSortedMessages(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    Long getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PaymentDetail> getAdminOurPaymentDetails(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    PaymentDetail updateOurPaymentDetail(PaymentDetail detailObject) throws CommonException;

//    List<PaymentDetail> getAdminSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- PAYMENT METHOD ------------------------------------------------
    Long getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PaymentMethodDetail> getAdminPaymentMethods() throws CommonException;

    List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    PaymentMethodDetail updatePaymentMethod(PaymentMethodDetail detailObject) throws CommonException;

//    List<PaymentMethodDetail> getAdminSortedPaymentMetho/ds(int start, int count,
//    Map<String, OrderType> orderColumns);
    //---------------------- PERMISSION ------------------------------------------------
    Long getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PermissionDetail> getAdminPermissions(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    PermissionDetail updatePermission(PermissionDetail detailObject) throws CommonException;

//    List<PermissionDetail> getAdminSortedPermissions(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- PREFERENCES ------------------------------------------------
    Long getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PreferenceDetail> getAdminPreferences(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    PreferenceDetail updatePreference(PreferenceDetail detailObject) throws CommonException;

//    List<PreferenceDetail> getAdminSortedPreferences(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- PROBLEM ------------------------------------------------
    Long getAdminProblemsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<ProblemDetail> getAdminProblems(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    ProblemDetail updateProblem(ProblemDetail detailObject) throws CommonException;
//    List<ProblemDetail> getAdminSortedProblems(int start, int count, Map<String, OrderType> orderColumns);
}
