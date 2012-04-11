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

    void updateDemand(FullDemandDetail detailObject) throws CommonException;

    //---------------------- CLIENT ------------------------------------------------
    Long getAdminClientsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<ClientDetail> getAdminClients(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateClient(ClientDetail detailObject) throws CommonException;

    //---------------------- SUPPLIER ------------------------------------------------
    Long getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<FullSupplierDetail> getAdminSuppliers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateSupplier(FullSupplierDetail detailObject) throws CommonException;

    //---------------------- OFFER ------------------------------------------------
    Long getAdminOffersCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<OfferDetail> getAdminOffers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateOffer(OfferDetail detailObject) throws CommonException;

    //---------------------- ACCESS ROLE ------------------------------------------------
    Long getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<AccessRoleDetail> getAdminAccessRoles(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateAccessRole(AccessRoleDetail detailObject) throws CommonException;

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    Long getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<EmailActivationDetail> getAdminEmailsActivation(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateEmailActivation(EmailActivationDetail detailObject) throws CommonException;

    //---------------------- INVOICE --------------------------------------------------
    Long getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<InvoiceDetail> getAdminInvoices(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateInvoice(InvoiceDetail detailObject) throws CommonException;

    //---------------------- MESSAGE ------------------------------------------------
    Long getAdminMessagesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<MessageDetail> getAdminMessages(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateMessage(MessageDetail detailObject) throws CommonException;

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    Long getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PaymentDetail> getAdminOurPaymentDetails(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateOurPaymentDetail(PaymentDetail detailObject) throws CommonException;

    //---------------------- PAYMENT METHOD ------------------------------------------------
    Long getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PaymentMethodDetail> getAdminPaymentMethods() throws CommonException;

    List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updatePaymentMethod(PaymentMethodDetail detailObject) throws CommonException;

    //---------------------- PERMISSION ------------------------------------------------
    Long getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PermissionDetail> getAdminPermissions(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updatePermission(PermissionDetail detailObject) throws CommonException;

    //---------------------- PREFERENCES ------------------------------------------------
    Long getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<PreferenceDetail> getAdminPreferences(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updatePreference(PreferenceDetail detailObject) throws CommonException;

    //---------------------- PROBLEM ------------------------------------------------
    Long getAdminProblemsCount(SearchModuleDataHolder searchDataHolder) throws CommonException;

    List<ProblemDetail> getAdminProblems(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException;

    void updateProblem(ProblemDetail detailObject) throws CommonException;
}
