package cz.poptavka.sample.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;
import cz.poptavka.sample.shared.domain.EmailActivationDetail;
import cz.poptavka.sample.shared.domain.InvoiceDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.PaymentDetail;
import cz.poptavka.sample.shared.domain.PaymentMethodDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;
import cz.poptavka.sample.shared.domain.PreferenceDetail;
import cz.poptavka.sample.shared.domain.ProblemDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.Map;

@RemoteServiceRelativePath("service/general")
public interface GeneralRPCService extends RemoteService {

    //---------------------- DEMAND ------------------------------------------------
    Long getAdminDemandsCount(SearchModuleDataHolder searchDataHolder);

    List<FullDemandDetail> getAdminDemands(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    FullDemandDetail updateDemand(FullDemandDetail detailObject);

//    List<FullDemandDetail> getAdminSortedDemands(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- CLIENT ------------------------------------------------
    Long getAdminClientsCount(SearchModuleDataHolder searchDataHolder);

    List<ClientDetail> getAdminClients(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    ClientDetail updateClient(ClientDetail detailObject);

//    List<ClientDetail> getAdminSortedClients(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- SUPPLIER ------------------------------------------------
    Long getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder);

    List<FullSupplierDetail> getAdminSuppliers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    FullSupplierDetail updateSupplier(FullSupplierDetail detailObject);

//    List<FullSupplierDetail> getAdminSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- OFFER ------------------------------------------------
    Long getAdminOffersCount(SearchModuleDataHolder searchDataHolder);

    List<OfferDetail> getAdminOffers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    OfferDetail updateOffer(OfferDetail detailObject);

//    List<OfferDetail> getAdminSortedOffers(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- ACCESS ROLE ------------------------------------------------
    Long getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder);

    List<AccessRoleDetail> getAdminAccessRoles(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    AccessRoleDetail updateAccessRole(AccessRoleDetail detailObject);

//    List<AccessRoleDetail> getAdminSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- EMAIL ACTIVATION---------------------------------------------
    Long getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder);

    List<EmailActivationDetail> getAdminEmailsActivation(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    EmailActivationDetail updateEmailActivation(EmailActivationDetail detailObject);

//    List<EmailActivationDetail> getAdminSortedEmailsActivation(
//            int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- INVOICE --------------------------------------------------
    Long getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder);

    List<InvoiceDetail> getAdminInvoices(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    InvoiceDetail updateInvoice(InvoiceDetail detailObject);

//    List<InvoiceDetail> getAdminSortedInvoices(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- MESSAGE ------------------------------------------------
    Long getAdminMessagesCount(SearchModuleDataHolder searchDataHolder);

    List<MessageDetail> getAdminMessages(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    MessageDetail updateMessage(MessageDetail detailObject);

//    List<MessageDetail> getAdminSortedMessages(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    Long getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder);

    List<PaymentDetail> getAdminOurPaymentDetails(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    PaymentDetail updateOurPaymentDetail(PaymentDetail detailObject);

//    List<PaymentDetail> getAdminSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- PAYMENT METHOD ------------------------------------------------
    Long getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder);

    List<PaymentMethodDetail> getAdminPaymentMethods();

    List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    PaymentMethodDetail updatePaymentMethod(PaymentMethodDetail detailObject);

//    List<PaymentMethodDetail> getAdminSortedPaymentMetho/ds(int start, int count,
//    Map<String, OrderType> orderColumns);
    //---------------------- PERMISSION ------------------------------------------------
    Long getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder);

    List<PermissionDetail> getAdminPermissions(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    PermissionDetail updatePermission(PermissionDetail detailObject);

//    List<PermissionDetail> getAdminSortedPermissions(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- PREFERENCES ------------------------------------------------
    Long getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder);

    List<PreferenceDetail> getAdminPreferences(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    PreferenceDetail updatePreference(PreferenceDetail detailObject);

//    List<PreferenceDetail> getAdminSortedPreferences(int start, int count, Map<String, OrderType> orderColumns);
    //---------------------- PROBLEM ------------------------------------------------
    Long getAdminProblemsCount(SearchModuleDataHolder searchDataHolder);

    List<ProblemDetail> getAdminProblems(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);

    ProblemDetail updateProblem(ProblemDetail detailObject);
//    List<ProblemDetail> getAdminSortedProblems(int start, int count, Map<String, OrderType> orderColumns);
}
