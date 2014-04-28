package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.DemandStatus;
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
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RemoteServiceRelativePath(AdminRPCService.URL)
public interface AdminRPCService extends RemoteService {

    String URL = "service/admin";

    //---------------------- DEMAND ------------------------------------------------
    Long getAdminDemandsCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<FullDemandDetail> getAdminDemands(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    Boolean updateDemands(HashMap<Long, ArrayList<ChangeDetail>> changes) throws
            RPCException, ApplicationSecurityException;

    //---------------------- CLIENT ------------------------------------------------
    Long getAdminClientsCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<ClientDetail> getAdminClients(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateClient(ClientDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- SUPPLIER ------------------------------------------------
    Long getAdminSuppliersCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<FullSupplierDetail> getAdminSuppliers(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateSupplier(FullSupplierDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- OFFER ------------------------------------------------
    Long getAdminOffersCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<OfferDetail> getAdminOffers(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateOffer(OfferDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- ACCESS ROLE ------------------------------------------------
    Long getAdminAccessRolesCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<AccessRoleDetail> getAdminAccessRoles(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateAccessRole(AccessRoleDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- EMAIL ACTIVATION---------------------------------------------
    Long getAdminEmailsActivationCount(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    List<ActivationEmailDetail> getAdminEmailsActivation(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateEmailActivation(ActivationEmailDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- INVOICE --------------------------------------------------
    Long getAdminInvoicesCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<InvoiceDetail> getAdminInvoices(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateInvoice(InvoiceDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- MESSAGE ------------------------------------------------
    Long getAdminMessagesCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<MessageDetail> getAdminMessages(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateMessage(MessageDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- NEW DEMANDS --------------------------------------------
    Long getAdminNewDemandsCount() throws RPCException, ApplicationSecurityException;

    List<AdminDemandDetail> getAdminNewDemands(SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    //---------------------- ASSIGNED DEMANDS --------------------------------------------
    Long getAdminAssignedDemandsCount(
            long userId, SearchDefinition searchDefinition, DemandStatus demandStatus) throws
            RPCException, ApplicationSecurityException;

    List<AdminDemandDetail> getAdminAssignedDemands(
            long userId, DemandStatus demandStatus, SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException;

    //---------------------- ACTIVE DEMANDS ---------------------------------------------
    Long getAdminActiveDemandsCount(SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    List<AdminDemandDetail> getAdminActiveDemands(SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException;

    List<MessageDetail> getConversation(long threadRootId, long loggedUserId, long counterPartyUserId)
        throws RPCException;

    Long createConversation(long demandId, long userAdminId) throws RPCException, ApplicationSecurityException;

    void approveDemands(Set<AdminDemandDetail> demandsToApprove) throws RPCException, ApplicationSecurityException;

    //---------------------- OUR PAYMENT DETAIL -----------------------------------------
    Long getAdminOurPaymentDetailsCount(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    List<PaymentDetail> getAdminOurPaymentDetails(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateOurPaymentDetail(PaymentDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- PAYMENT METHOD ------------------------------------------------
    Long getAdminPaymentMethodsCount(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    List<PaymentMethodDetail> getAdminPaymentMethods() throws RPCException, ApplicationSecurityException;

    List<PaymentMethodDetail> getAdminPaymentMethods(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updatePaymentMethod(PaymentMethodDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- PERMISSION ------------------------------------------------
    Long getAdminPermissionsCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<PermissionDetail> getAdminPermissions(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updatePermission(PermissionDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- PREFERENCES ------------------------------------------------
    Long getAdminPreferencesCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<PreferenceDetail> getAdminPreferences(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updatePreference(PreferenceDetail detailObject) throws RPCException, ApplicationSecurityException;

    //---------------------- PROBLEM ------------------------------------------------
    Long getAdminProblemsCount(SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException;

    List<ProblemDetail> getAdminProblems(SearchDefinition searchDefinition) throws RPCException,
            ApplicationSecurityException;

    void updateProblem(ProblemDetail detailObject) throws RPCException, ApplicationSecurityException;

    UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException;
}
