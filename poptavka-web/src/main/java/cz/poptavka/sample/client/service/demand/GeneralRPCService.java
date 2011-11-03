package cz.poptavka.sample.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
    Long getAdminDemandsCount();

    List<FullDemandDetail> getAdminDemands(int start, int count);

    FullDemandDetail updateDemand(FullDemandDetail detailObject);

    List<FullDemandDetail> getAdminSortedDemands(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- CLIENT ------------------------------------------------
    Long getAdminClientsCount();

    List<ClientDetail> getAdminClients(int start, int count);

    ClientDetail updateClient(ClientDetail detailObject);

    List<ClientDetail> getAdminSortedClients(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- SUPPLIER ------------------------------------------------
    Long getAdminSuppliersCount();

    List<FullSupplierDetail> getAdminSuppliers(int start, int count);

    FullSupplierDetail updateSupplier(FullSupplierDetail detailObject);

    List<FullSupplierDetail> getAdminSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- OFFER ------------------------------------------------
    Long getAdminOffersCount();

    List<OfferDetail> getAdminOffers(int start, int count);

    OfferDetail updateOffer(OfferDetail detailObject);

    List<OfferDetail> getAdminSortedOffers(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- ACCESS ROLE ------------------------------------------------
    Long getAdminAccessRolesCount();

    List<AccessRoleDetail> getAdminAccessRoles(int start, int count);

    AccessRoleDetail updateAccessRole(AccessRoleDetail detailObject);

    List<AccessRoleDetail> getAdminSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    Long getAdminEmailsActivationCount();

    List<EmailActivationDetail> getAdminEmailsActivation(int start, int count);

    EmailActivationDetail updateEmailActivation(EmailActivationDetail detailObject);

    List<EmailActivationDetail> getAdminSortedEmailsActivation(
            int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- INVOICE --------------------------------------------------
    Long getAdminInvoicesCount();

    List<InvoiceDetail> getAdminInvoices(int start, int count);

    InvoiceDetail updateInvoice(InvoiceDetail detailObject);

    List<InvoiceDetail> getAdminSortedInvoices(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- MESSAGE ------------------------------------------------
    Long getAdminMessagesCount();

    List<MessageDetail> getAdminMessages(int start, int count);

    MessageDetail updateMessage(MessageDetail detailObject);

    List<MessageDetail> getAdminSortedMessages(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    Long getAdminOurPaymentDetailsCount();

    List<PaymentDetail> getAdminOurPaymentDetails(int start, int count);

    PaymentDetail updateOurPaymentDetail(PaymentDetail detailObject);

    List<PaymentDetail> getAdminSortedOurPaymentDetails(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PAYMENT METHOD ------------------------------------------------
    Long getAdminPaymentMethodsCount();

    List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count);

    PaymentMethodDetail updatePaymentMethod(PaymentMethodDetail detailObject);

    List<PaymentMethodDetail> getAdminSortedPaymentMethods(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PERMISSION ------------------------------------------------
    Long getAdminPermissionsCount();

    List<PermissionDetail> getAdminPermissions(int start, int count);

    PermissionDetail updatePermission(PermissionDetail detailObject);

    List<PermissionDetail> getAdminSortedPermissions(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PREFERENCES ------------------------------------------------
    Long getAdminPreferencesCount();

    List<PreferenceDetail> getAdminPreferences(int start, int count);

    PreferenceDetail updatePreference(PreferenceDetail detailObject);

    List<PreferenceDetail> getAdminSortedPreferences(int start, int count, Map<String, OrderType> orderColumns);

    //---------------------- PROBLEM ------------------------------------------------
    Long getAdminProblemsCount();

    List<ProblemDetail> getAdminProblems(int start, int count);

    ProblemDetail updateProblem(ProblemDetail detailObject);

    List<ProblemDetail> getAdminSortedProblems(int start, int count, Map<String, OrderType> orderColumns);
}
