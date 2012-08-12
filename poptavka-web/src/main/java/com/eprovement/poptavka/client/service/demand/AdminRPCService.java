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
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;


@RemoteServiceRelativePath(AdminRPCService.URL)
public interface AdminRPCService extends RemoteService {

    String URL = "service/admin";

    //---------------------- DEMAND ------------------------------------------------
    Long getAdminDemandsCount(SearchDefinition searchDefinition) throws RPCException;

    List<FullDemandDetail> getAdminDemands(SearchDefinition searchDefinition) throws RPCException;

    void updateDemand(FullDemandDetail detailObject) throws RPCException;

    //---------------------- CLIENT ------------------------------------------------
    Long getAdminClientsCount(SearchDefinition searchDefinition) throws RPCException;


    List<ClientDetail> getAdminClients(SearchDefinition searchDefinition) throws RPCException;


    void updateClient(ClientDetail detailObject) throws RPCException;

    //---------------------- SUPPLIER ------------------------------------------------

    Long getAdminSuppliersCount(SearchDefinition searchDefinition) throws RPCException;


    List<FullSupplierDetail> getAdminSuppliers(SearchDefinition searchDefinition) throws RPCException;


    void updateSupplier(FullSupplierDetail detailObject) throws RPCException;

    //---------------------- OFFER ------------------------------------------------

    Long getAdminOffersCount(SearchDefinition searchDefinition) throws RPCException;


    List<OfferDetail> getAdminOffers(SearchDefinition searchDefinition) throws RPCException;


    void updateOffer(OfferDetail detailObject) throws RPCException;

    //---------------------- ACCESS ROLE ------------------------------------------------

    Long getAdminAccessRolesCount(SearchDefinition searchDefinition) throws RPCException;


    List<AccessRoleDetail> getAdminAccessRoles(SearchDefinition searchDefinition) throws RPCException;


    void updateAccessRole(AccessRoleDetail detailObject) throws RPCException;

    //---------------------- EMAIL ACTIVATION---------------------------------------------

    Long getAdminEmailsActivationCount(SearchDefinition searchDefinition) throws RPCException;


    List<ActivationEmailDetail> getAdminEmailsActivation(SearchDefinition searchDefinition) throws RPCException;


    void updateEmailActivation(ActivationEmailDetail detailObject) throws RPCException;

    //---------------------- INVOICE --------------------------------------------------

    Long getAdminInvoicesCount(SearchDefinition searchDefinition) throws RPCException;


    List<InvoiceDetail> getAdminInvoices(SearchDefinition searchDefinition) throws RPCException;


    void updateInvoice(InvoiceDetail detailObject) throws RPCException;

    //---------------------- MESSAGE ------------------------------------------------

    Long getAdminMessagesCount(SearchDefinition searchDefinition) throws RPCException;


    List<MessageDetail> getAdminMessages(SearchDefinition searchDefinition) throws RPCException;


    void updateMessage(MessageDetail detailObject) throws RPCException;

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------

    Long getAdminOurPaymentDetailsCount(SearchDefinition searchDefinition) throws RPCException;


    List<PaymentDetail> getAdminOurPaymentDetails(SearchDefinition searchDefinition) throws RPCException;


    void updateOurPaymentDetail(PaymentDetail detailObject) throws RPCException;

    //---------------------- PAYMENT METHOD ------------------------------------------------

    Long getAdminPaymentMethodsCount(SearchDefinition searchDefinition) throws RPCException;


    List<PaymentMethodDetail> getAdminPaymentMethods() throws RPCException;


    List<PaymentMethodDetail> getAdminPaymentMethods(SearchDefinition searchDefinition) throws RPCException;


    void updatePaymentMethod(PaymentMethodDetail detailObject) throws RPCException;

    //---------------------- PERMISSION ------------------------------------------------

    Long getAdminPermissionsCount(SearchDefinition searchDefinition) throws RPCException;


    List<PermissionDetail> getAdminPermissions(SearchDefinition searchDefinition) throws RPCException;


    void updatePermission(PermissionDetail detailObject) throws RPCException;

    //---------------------- PREFERENCES ------------------------------------------------

    Long getAdminPreferencesCount(SearchDefinition searchDefinition) throws RPCException;


    List<PreferenceDetail> getAdminPreferences(SearchDefinition searchDefinition) throws RPCException;


    void updatePreference(PreferenceDetail detailObject) throws RPCException;

    //---------------------- PROBLEM ------------------------------------------------

    Long getAdminProblemsCount(SearchDefinition searchDefinition) throws RPCException;


    List<ProblemDetail> getAdminProblems(SearchDefinition searchDefinition) throws RPCException;

    void updateProblem(ProblemDetail detailObject) throws RPCException;
}
