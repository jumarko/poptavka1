package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.common.OrderType;
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

import org.springframework.security.access.annotation.Secured;

@RemoteServiceRelativePath(AdminRPCService.URL)
public interface AdminRPCService extends RemoteService {

    String URL = "service/admin";

    //---------------------- DEMAND ------------------------------------------------
    Long getAdminDemandsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<FullDemandDetail> getAdminDemands(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateDemand(FullDemandDetail detailObject) throws RPCException;

    //---------------------- CLIENT ------------------------------------------------
    @Secured("admin")
    Long getAdminClientsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<ClientDetail> getAdminClients(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateClient(ClientDetail detailObject) throws RPCException;

    //---------------------- SUPPLIER ------------------------------------------------
    @Secured("admin")
    Long getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<FullSupplierDetail> getAdminSuppliers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateSupplier(FullSupplierDetail detailObject) throws RPCException;

    //---------------------- OFFER ------------------------------------------------
    @Secured("admin")
    Long getAdminOffersCount(SearchModuleDataHolder searchDataHolder) throws RPCException;

    @Secured("admin")
    List<OfferDetail> getAdminOffers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateOffer(OfferDetail detailObject) throws RPCException;

    //---------------------- ACCESS ROLE ------------------------------------------------
    @Secured("admin")
    Long getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<AccessRoleDetail> getAdminAccessRoles(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateAccessRole(AccessRoleDetail detailObject) throws RPCException;

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    @Secured("admin")
    Long getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<ActivationEmailDetail> getAdminEmailsActivation(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateEmailActivation(ActivationEmailDetail detailObject) throws RPCException;

    //---------------------- INVOICE --------------------------------------------------
    @Secured("admin")
    Long getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<InvoiceDetail> getAdminInvoices(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateInvoice(InvoiceDetail detailObject) throws RPCException;

    //---------------------- MESSAGE ------------------------------------------------
    @Secured("admin")
    Long getAdminMessagesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<MessageDetail> getAdminMessages(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateMessage(MessageDetail detailObject) throws RPCException;

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    @Secured("admin")
    Long getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<PaymentDetail> getAdminOurPaymentDetails(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateOurPaymentDetail(PaymentDetail detailObject) throws RPCException;

    //---------------------- PAYMENT METHOD ------------------------------------------------
    @Secured("admin")
    Long getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<PaymentMethodDetail> getAdminPaymentMethods() throws RPCException;
    
    @Secured("admin")
    List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updatePaymentMethod(PaymentMethodDetail detailObject) throws RPCException;

    //---------------------- PERMISSION ------------------------------------------------
    @Secured("admin")
    Long getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<PermissionDetail> getAdminPermissions(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updatePermission(PermissionDetail detailObject) throws RPCException;

    //---------------------- PREFERENCES ------------------------------------------------
    @Secured("admin")
    Long getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<PreferenceDetail> getAdminPreferences(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updatePreference(PreferenceDetail detailObject) throws RPCException;

    //---------------------- PROBLEM ------------------------------------------------
    @Secured("admin")
    Long getAdminProblemsCount(SearchModuleDataHolder searchDataHolder) throws RPCException;
    
    @Secured("admin")
    List<ProblemDetail> getAdminProblems(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws RPCException;
    
    @Secured("admin")
    void updateProblem(ProblemDetail detailObject) throws RPCException;
}
