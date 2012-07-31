package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
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
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.Map;


@RemoteServiceRelativePath(AdminRPCService.URL)
public interface AdminRPCService extends RemoteService {

    String URL = "service/admin";

    //---------------------- DEMAND ------------------------------------------------
    Long getAdminDemandsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;

    List<FullDemandDetail> getAdminDemands(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;

    void updateDemand(FullDemandDetail detailObject) throws RPCException;

    //---------------------- CLIENT ------------------------------------------------
    Long getAdminClientsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<ClientDetail> getAdminClients(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateClient(ClientDetail detailObject) throws RPCException;

    //---------------------- SUPPLIER ------------------------------------------------

    Long getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<FullSupplierDetail> getAdminSuppliers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateSupplier(FullSupplierDetail detailObject) throws RPCException;

    //---------------------- OFFER ------------------------------------------------

    Long getAdminOffersCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<OfferDetail> getAdminOffers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateOffer(OfferDetail detailObject) throws RPCException;

    //---------------------- ACCESS ROLE ------------------------------------------------

    Long getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<AccessRoleDetail> getAdminAccessRoles(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateAccessRole(AccessRoleDetail detailObject) throws RPCException;

    //---------------------- EMAIL ACTIVATION---------------------------------------------

    Long getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<ActivationEmailDetail> getAdminEmailsActivation(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateEmailActivation(ActivationEmailDetail detailObject) throws RPCException;

    //---------------------- INVOICE --------------------------------------------------

    Long getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<InvoiceDetail> getAdminInvoices(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateInvoice(InvoiceDetail detailObject) throws RPCException;

    //---------------------- MESSAGE ------------------------------------------------

    Long getAdminMessagesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<MessageDetail> getAdminMessages(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateMessage(MessageDetail detailObject) throws RPCException;

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------

    Long getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<PaymentDetail> getAdminOurPaymentDetails(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updateOurPaymentDetail(PaymentDetail detailObject) throws RPCException;

    //---------------------- PAYMENT METHOD ------------------------------------------------

    Long getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<PaymentMethodDetail> getAdminPaymentMethods() throws RPCException;


    List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updatePaymentMethod(PaymentMethodDetail detailObject) throws RPCException;

    //---------------------- PERMISSION ------------------------------------------------

    Long getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<PermissionDetail> getAdminPermissions(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updatePermission(PermissionDetail detailObject) throws RPCException;

    //---------------------- PREFERENCES ------------------------------------------------

    Long getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<PreferenceDetail> getAdminPreferences(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;


    void updatePreference(PreferenceDetail detailObject) throws RPCException;

    //---------------------- PROBLEM ------------------------------------------------

    Long getAdminProblemsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;


    List<ProblemDetail> getAdminProblems(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;

    void updateProblem(ProblemDetail detailObject) throws RPCException;
}
