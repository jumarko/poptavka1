/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.general;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import cz.poptavka.sample.client.service.demand.GeneralRPCService;
import cz.poptavka.sample.domain.activation.EmailActivation;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.invoice.Invoice;
import cz.poptavka.sample.domain.invoice.OurPaymentDetails;
import cz.poptavka.sample.domain.invoice.PaymentMethod;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.settings.Preference;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Problem;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.rights.AccessRole;
import cz.poptavka.sample.domain.user.rights.Permission;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Martin Slavkovsky
 */
public class GeneralRPCServiceImpl extends AutoinjectingRemoteService implements GeneralRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    private GeneralService generalService;

    public GeneralService getAdminGeneralService() {
        return generalService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**********************************************************************************************
     ***********************  DEMAND SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminDemandsCount() {
        final Search search = new Search(Demand.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<FullDemandDetail> getAdminDemands(int start, int count) {
        final Search search = new Search(Demand.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDemandDetailList(generalService.search(search));
    }

    @Override
    public FullDemandDetail updateDemand(FullDemandDetail fullDemandDetail) {
        return generalService.merge(fullDemandDetail);
    }

    @Override
    public List<FullDemandDetail> getAdminSortedDemands(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createDemandDetailList(
                generalService.search(this.search(start, count, orderColumns, Demand.class)));
    }

    private List<FullDemandDetail> createDemandDetailList(Collection<Demand> demands) {
        List<FullDemandDetail> demandDetail = new ArrayList<FullDemandDetail>();
        for (Demand demand : demands) {
            demandDetail.add(FullDemandDetail.createDemandDetail(demand));
        }
        return demandDetail;
    }

    /**********************************************************************************************
     ***********************  CLIENT SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminClientsCount() {
        final Search search = new Search(Client.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<ClientDetail> getAdminClients(int start, int count) {
        final Search search = new Search(Client.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createClientDetailList(generalService.search(search));
    }

    @Override
    public ClientDetail updateClient(ClientDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    @Override
    public List<ClientDetail> getAdminSortedClients(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createClientDetailList(
                generalService.search(this.search(start, count, orderColumns, Client.class)));
    }

    private List<ClientDetail> createClientDetailList(Collection<Client> demands) {
        List<ClientDetail> accessRoles = new ArrayList<ClientDetail>();
        for (Client role : demands) {
            accessRoles.add(ClientDetail.createClientDetail(role));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  SUPPLIER SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminSuppliersCount() {
        final Search search = new Search(Supplier.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<FullSupplierDetail> getAdminSuppliers(int start, int count) {
        final Search search = new Search(Supplier.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createFullSupplierDetailList(generalService.search(search));
    }

    @Override
    public FullSupplierDetail updateSupplier(FullSupplierDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    @Override
    public List<FullSupplierDetail> getAdminSortedSuppliers(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createFullSupplierDetailList(
                generalService.search(this.search(start, count, orderColumns, Supplier.class)));
    }

    private List<FullSupplierDetail> createFullSupplierDetailList(Collection<Supplier> demands) {
        List<FullSupplierDetail> accessRoles = new ArrayList<FullSupplierDetail>();
        for (Supplier role : demands) {
            accessRoles.add(FullSupplierDetail.createFullSupplierDetail(role));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  OFFER SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminOffersCount() {
        final Search search = new Search(Offer.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<OfferDetail> getAdminOffers(int start, int count) {
        final Search search = new Search(Offer.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createOfferDetailList(generalService.search(search));
    }

    @Override
    public OfferDetail updateOffer(OfferDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    @Override
    public List<OfferDetail> getAdminSortedOffers(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createOfferDetailList(
                generalService.search(this.search(start, count, orderColumns, Offer.class)));
    }

    private List<OfferDetail> createOfferDetailList(Collection<Offer> demands) {
        List<OfferDetail> accessRoles = new ArrayList<OfferDetail>();
        for (Offer item : demands) {
            accessRoles.add(OfferDetail.createOfferDetail(item));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  ACCESS ROLE SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminAccessRolesCount() {
        final Search search = new Search(AccessRole.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<AccessRoleDetail> getAdminAccessRoles(int start, int count) {
        final Search search = new Search(AccessRole.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createAccessRoleDetailList(generalService.search(search));
    }

//    @Override
    public AccessRoleDetail updateAccessRole(AccessRoleDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    @Override
    public List<AccessRoleDetail> getAdminSortedAccessRoles(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createAccessRoleDetailList(
                generalService.search(this.search(start, count, orderColumns, AccessRole.class)));
    }

    private List<AccessRoleDetail> createAccessRoleDetailList(Collection<AccessRole> demands) {
        List<AccessRoleDetail> accessRoles = new ArrayList<AccessRoleDetail>();
        for (AccessRole role : demands) {
            accessRoles.add(AccessRoleDetail.createAccessRoleDetail(role));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  EMAIL ACTIVATION SECTION. *******************************************
     **********************************************************************************************/
    @Override
    public Long getAdminEmailsActivationCount() {
        final Search search = new Search(EmailActivation.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<EmailActivationDetail> getAdminEmailsActivation(int start, int count) {
        final Search search = new Search(EmailActivation.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createEmailActivationDetailList(generalService.search(search));
    }

    @Override
    public EmailActivationDetail updateEmailActivation(EmailActivationDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public List<EmailActivationDetail> getAdminSortedEmailsActivation(
            int start, int count, Map<String, OrderType> orderColumns) {
        return this.createEmailActivationDetailList(
                generalService.search(this.search(start, count, orderColumns, EmailActivation.class)));
    }

    private List<EmailActivationDetail> createEmailActivationDetailList(Collection<EmailActivation> emailsList) {
        List<EmailActivationDetail> emailsActivation = new ArrayList<EmailActivationDetail>();
        for (EmailActivation email : emailsList) {
            emailsActivation.add(EmailActivationDetail.createEmailActivationDetail(email));
        }
        return emailsActivation;
    }

    /**********************************************************************************************
     ***********************  INVOICE SECTION. ****************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminInvoicesCount() {
        final Search search = new Search(Invoice.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<InvoiceDetail> getAdminInvoices(int start, int count) {
        final Search search = new Search(Invoice.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createInvoiceDetailList(generalService.search(search));
    }

    @Override
    public InvoiceDetail updateInvoice(InvoiceDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public List<InvoiceDetail> getAdminSortedInvoices(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createInvoiceDetailList(
                generalService.search(this.search(start, count, orderColumns, Invoice.class)));
    }

    private List<InvoiceDetail> createInvoiceDetailList(Collection<Invoice> invoicesList) {
        List<InvoiceDetail> invoices = new ArrayList<InvoiceDetail>();
        for (Invoice invoice : invoicesList) {
            invoices.add(InvoiceDetail.createInvoiceDetail(invoice));
        }
        return invoices;
    }

    /**********************************************************************************************
     ***********************  MESSAGE SECTION. ****************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminMessagesCount() {
        final Search search = new Search(Message.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<MessageDetail> getAdminMessages(int start, int count) {
        final Search search = new Search(Message.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createMessageDetailList(generalService.search(search));
    }

    @Override
    public MessageDetail updateMessage(MessageDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public List<MessageDetail> getAdminSortedMessages(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createMessageDetailList(
                generalService.search(this.search(start, count, orderColumns, Message.class)));
    }

    private List<MessageDetail> createMessageDetailList(Collection<Message> messagesList) {
        List<MessageDetail> messages = new ArrayList<MessageDetail>();
        for (Message message : messagesList) {
            messages.add(MessageDetail.createMessageDetail(message));
        }
        return messages;
    }

    /**********************************************************************************************
     ***********************  OUR PAYMENT DETAIL SECTION. *****************************************
     **********************************************************************************************/
    @Override
    public Long getAdminOurPaymentDetailsCount() {
        final Search search = new Search(OurPaymentDetails.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<PaymentDetail> getAdminOurPaymentDetails(int start, int count) {
        final Search search = new Search(OurPaymentDetails.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createPaymentDetailList(generalService.search(search));
    }

    @Override
    public PaymentDetail updateOurPaymentDetail(PaymentDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public List<PaymentDetail> getAdminSortedOurPaymentDetails(
            int start, int count, Map<String, OrderType> orderColumns) {
        return this.createPaymentDetailList(
                generalService.search(this.search(start, count, orderColumns, OurPaymentDetails.class)));
    }

    private List<PaymentDetail> createPaymentDetailList(Collection<OurPaymentDetails> paymentsList) {
        List<PaymentDetail> payments = new ArrayList<PaymentDetail>();
        for (OurPaymentDetails payment : paymentsList) {
            payments.add(PaymentDetail.createOurPaymentDetailDetail(payment));
        }
        return payments;
    }

    /**********************************************************************************************
     ***********************  PAYMENT METHOD SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminPaymentMethodsCount() {
        final Search search = new Search(PaymentMethod.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count) {
        final Search search = new Search(PaymentMethod.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createPaymentMethodDetailList(generalService.search(search));
    }

    @Override
    public PaymentMethodDetail updatePaymentMethod(PaymentMethodDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    @Override
    public List<PaymentMethodDetail> getAdminSortedPaymentMethods(
            int start, int count, Map<String, OrderType> orderColumns) {
        return this.createPaymentMethodDetailList(
                generalService.search(this.search(start, count, orderColumns, PaymentMethod.class)));
    }

    private List<PaymentMethodDetail> createPaymentMethodDetailList(Collection<PaymentMethod> demands) {
        List<PaymentMethodDetail> accessRoles = new ArrayList<PaymentMethodDetail>();
        for (PaymentMethod item : demands) {
            accessRoles.add(PaymentMethodDetail.createPaymentMethodDetail(item));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  PERMISSION SECTION. *************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminPermissionsCount() {
        final Search search = new Search(Permission.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<PermissionDetail> getAdminPermissions(int start, int count) {
        final Search search = new Search(Permission.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createPermissionDetailList(generalService.search(search));
    }

    @Override
    public PermissionDetail updatePermission(PermissionDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public List<PermissionDetail> getAdminSortedPermissions(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createPermissionDetailList(
                generalService.search(this.search(start, count, orderColumns, Permission.class)));
    }

    private List<PermissionDetail> createPermissionDetailList(Collection<Permission> permissionList) {
        List<PermissionDetail> accessRoles = new ArrayList<PermissionDetail>();
        for (Permission permission : permissionList) {
            accessRoles.add(PermissionDetail.createPermissionsDetail(permission));
        }
        return accessRoles;
    }

    /**********************************************************************************************
     ***********************  PREFERENCE SECTION. *************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminPreferencesCount() {
        final Search search = new Search(Preference.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<PreferenceDetail> getAdminPreferences(int start, int count) {
        final Search search = new Search(Preference.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createPreferenceDetailList(generalService.search(search));
    }

    @Override
    public PreferenceDetail updatePreference(PreferenceDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    @Override
    public List<PreferenceDetail> getAdminSortedPreferences(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createPreferenceDetailList(
                generalService.search(this.search(start, count, orderColumns, Preference.class)));
    }

    private List<PreferenceDetail> createPreferenceDetailList(Collection<Preference> preference) {
        List<PreferenceDetail> preferences = new ArrayList<PreferenceDetail>();
        for (Preference role : preference) {
            preferences.add(PreferenceDetail.createPreferenceDetail(role));
        }
        return preferences;
    }

    /**********************************************************************************************
     ***********************  PROBLEM SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminProblemsCount() {
        final Search search = new Search(Problem.class);
        return (long) generalService.count(search);
    }

    @Override
    public List<ProblemDetail> getAdminProblems(int start, int count) {
        final Search search = new Search(Problem.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createProblemDetailList(generalService.search(search));
    }

    @Override
    public ProblemDetail updateProblem(ProblemDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    @Override
    public List<ProblemDetail> getAdminSortedProblems(int start, int count, Map<String, OrderType> orderColumns) {
        return this.createProblemDetailList(
                generalService.search(this.search(start, count, orderColumns, Problem.class)));
    }

    private List<ProblemDetail> createProblemDetailList(Collection<Problem> demands) {
        List<ProblemDetail> problems = new ArrayList<ProblemDetail>();
        for (Problem item : demands) {
            problems.add(ProblemDetail.createProblemDetail(item));
        }
        return problems;
    }

    /**********************************************************************************************
     ***********************  COMMON METHODS. *************************************************
     **********************************************************************************************/
    private Search search(int start, int count, Map<String, OrderType> orderColumns, Class<?> classs) {
        final Search search = new Search(classs);
        search.setFirstResult(start);
        search.setMaxResults(count);
        List<Sort> sorts = new ArrayList<Sort>();
        for (String str : orderColumns.keySet()) {
            if (orderColumns.get(str) == OrderType.ASC) {
                sorts.add(new Sort(str, false));
            } else {
                sorts.add(new Sort(str, true));
            }
        }
        return search.addSorts(sorts.toArray(new Sort[sorts.size()]));
    }
}
