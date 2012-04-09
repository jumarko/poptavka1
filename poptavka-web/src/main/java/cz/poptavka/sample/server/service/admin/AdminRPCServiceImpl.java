/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.admin;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.client.service.demand.AdminRPCService;
import cz.poptavka.sample.domain.activation.EmailActivation;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.invoice.Invoice;
import cz.poptavka.sample.domain.invoice.OurPaymentDetails;
import cz.poptavka.sample.domain.invoice.PaymentMethod;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.settings.Preference;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Problem;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.rights.AccessRole;
import cz.poptavka.sample.domain.user.rights.Permission;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.demand.DemandService;
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
import cz.poptavka.sample.shared.domain.type.MessageType;
import cz.poptavka.sample.shared.exceptions.CommonException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/*
 * TODO Martin
 * Vsetky count zrobit inak, ked sa bude riesit tento modul.
 * Vsetky updaty dorobit.
 */
/**
 * @author Martin Slavkovsky
 */
public class AdminRPCServiceImpl extends AutoinjectingRemoteService implements AdminRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    //--------------------------------------------------- CONVERTERS ---------------------------------------------------
    //--------------------------------------------------- WILL BE MOVED to the standalone classes ----------------------
    private final Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter =
            new Converter<PaymentMethod, PaymentMethodDetail>() {

                @Override
                public PaymentMethodDetail convert(PaymentMethod paymentMethod) {
                    return PaymentMethodDetail.createPaymentMethodDetail(paymentMethod);
                }
            };
    private final Converter<Permission, PermissionDetail> permissionConverter =
            new Converter<Permission, PermissionDetail>() {

                @Override
                public PermissionDetail convert(Permission permission) {
                    return PermissionDetail.createPermissionsDetail(permission);
                }
            };
    private final Converter<Preference, PreferenceDetail> preferenceConverter =
            new Converter<Preference, PreferenceDetail>() {

                @Override
                public PreferenceDetail convert(Preference preference) {
                    return PreferenceDetail.createPreferenceDetail(preference);
                }
            };
    private final Converter<Problem, ProblemDetail> problemConverter = new Converter<Problem, ProblemDetail>() {

        @Override
        public ProblemDetail convert(Problem problem) {
            return ProblemDetail.createProblemDetail(problem);
        }
    };
    private final Converter<Demand, FullDemandDetail> demandConverter = new Converter<Demand, FullDemandDetail>() {

        @Override
        public FullDemandDetail convert(Demand demand) {
            return FullDemandDetail.createDemandDetail(demand);
        }
    };
    private final Converter<Client, ClientDetail> clientConverter = new Converter<Client, ClientDetail>() {

        @Override
        public ClientDetail convert(Client client) {
            return ClientDetail.createClientDetail(client);
        }
    };
    private final Converter<Supplier, FullSupplierDetail> supplierConverter =
            new Converter<Supplier, FullSupplierDetail>() {

                @Override
                public FullSupplierDetail convert(Supplier supplier) {
                    return FullSupplierDetail.createFullSupplierDetail(supplier);
                }
            };
    private final Converter<Offer, OfferDetail> offerConverter = new Converter<Offer, OfferDetail>() {

        @Override
        public OfferDetail convert(Offer offer) {
            return OfferDetail.createOfferDetail(offer);
        }
    };
    private final Converter<AccessRole, AccessRoleDetail> accessRoleConverter =
            new Converter<AccessRole, AccessRoleDetail>() {

                @Override
                public AccessRoleDetail convert(AccessRole role) {
                    return AccessRoleDetail.createAccessRoleDetail(role);
                }
            };
    private final Converter<Message, MessageDetail> messageConverter =
            new Converter<Message, MessageDetail>() {

                @Override
                public MessageDetail convert(Message message) {
                    return MessageDetail.createMessageDetail(message);
                }
            };
    private final Converter<OurPaymentDetails, PaymentDetail> paymentConverter =
            new Converter<OurPaymentDetails, PaymentDetail>() {

                @Override
                public PaymentDetail convert(OurPaymentDetails payment) {
                    return PaymentDetail.createOurPaymentDetailDetail(payment);
                }
            };
    private final Converter<Invoice, InvoiceDetail> invoiceConverter = new Converter<Invoice, InvoiceDetail>() {

        @Override
        public InvoiceDetail convert(Invoice invoice) {
            return InvoiceDetail.createInvoiceDetail(invoice);
        }
    };
    private final Converter<EmailActivation, EmailActivationDetail> emailActivationConverter =
            new Converter<EmailActivation, EmailActivationDetail>() {

                @Override
                public EmailActivationDetail convert(EmailActivation emailActivation) {
                    return EmailActivationDetail.createEmailActivationDetail(emailActivation);
                }
            };
    //--------------------------------------------------- END OF CONVERTERS --------------------------------------------
    private GeneralService generalService;
    private DemandService demandService;

    @Autowired
    public void setGeneralService(GeneralService generalService) throws CommonException {
        this.generalService = generalService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) throws CommonException {
        this.demandService = demandService;
    }

    /**********************************************************************************************
     ***********************  DEMAND SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminDemandsCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Demand.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Demand.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<FullDemandDetail> getAdminDemands(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Demand.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), demandConverter);
    }

    @Override
    public FullDemandDetail updateDemand(FullDemandDetail fullDemandDetail) throws CommonException {
        return generalService.merge(fullDemandDetail);
    }

    /**********************************************************************************************
     ***********************  CLIENT SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminClientsCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Client.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Client.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<ClientDetail> getAdminClients(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Client.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), clientConverter);
    }

    @Override
    public ClientDetail updateClient(ClientDetail accessRoleDetail) throws CommonException {
        return generalService.merge(accessRoleDetail);
    }

    /**********************************************************************************************
     ***********************  SUPPLIER SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminSuppliersCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Supplier.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Supplier.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<FullSupplierDetail> getAdminSuppliers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Supplier.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), supplierConverter);
    }

    @Override
    public FullSupplierDetail updateSupplier(FullSupplierDetail accessRoleDetail) throws CommonException {
        return generalService.merge(accessRoleDetail);
    }

    /**********************************************************************************************
     ***********************  OFFER SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminOffersCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Offer.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Offer.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<OfferDetail> getAdminOffers(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Offer.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), offerConverter);
    }

    @Override
    public OfferDetail updateOffer(OfferDetail accessRoleDetail) {
        return generalService.merge(accessRoleDetail);
    }

    /**********************************************************************************************
     ***********************  ACCESS ROLE SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminAccessRolesCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(AccessRole.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(AccessRole.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<AccessRoleDetail> getAdminAccessRoles(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(AccessRole.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), accessRoleConverter);
    }

//    @Override
    public AccessRoleDetail updateAccessRole(AccessRoleDetail accessRoleDetail) throws CommonException {
        return generalService.merge(accessRoleDetail);
    }

    /**********************************************************************************************
     ***********************  EMAIL ACTIVATION SECTION. *******************************************
     **********************************************************************************************/
    @Override
    public Long getAdminEmailsActivationCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(EmailActivation.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(EmailActivation.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<EmailActivationDetail> getAdminEmailsActivation(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(EmailActivation.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), emailActivationConverter);
    }

    @Override
    public EmailActivationDetail updateEmailActivation(EmailActivationDetail supplierDetail) {
        return generalService.merge(supplierDetail);
    }

    /**********************************************************************************************
     ***********************  INVOICE SECTION. ****************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminInvoicesCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Invoice.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Invoice.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<InvoiceDetail> getAdminInvoices(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Invoice.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), invoiceConverter);
    }

    @Override
    public InvoiceDetail updateInvoice(InvoiceDetail supplierDetail) throws CommonException {
        return generalService.merge(supplierDetail);
    }

    /**********************************************************************************************
     ***********************  MESSAGE SECTION. ****************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminMessagesCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Message.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Message.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<MessageDetail> getAdminMessages(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Message.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
//        search.setPage(count);
        return this.createDetailList(generalService.search(search), messageConverter);
    }

    @Override
    public MessageDetail updateMessage(MessageDetail supplierDetail) throws CommonException {
        return generalService.merge(supplierDetail);
    }

    /**********************************************************************************************
     ***********************  OUR PAYMENT DETAIL SECTION. *****************************************
     **********************************************************************************************/
    @Override
    public Long getAdminOurPaymentDetailsCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(OurPaymentDetails.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(OurPaymentDetails.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<PaymentDetail> getAdminOurPaymentDetails(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(OurPaymentDetails.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), paymentConverter);
    }

    @Override
    public PaymentDetail updateOurPaymentDetail(PaymentDetail supplierDetail) throws CommonException {
        return generalService.merge(supplierDetail);
    }

    /**********************************************************************************************
     ***********************  PAYMENT METHOD SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminPaymentMethodsCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(PaymentMethod.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(PaymentMethod.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<PaymentMethodDetail> getAdminPaymentMethods(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(PaymentMethod.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), paymentMethodConverter);
    }

    @Override
    public List<PaymentMethodDetail> getAdminPaymentMethods() {
        final Search search = new Search(PaymentMethod.class);
        search.addSort("id", false);
        return this.createDetailList(generalService.search(search), paymentMethodConverter);
    }

    @Override
    public PaymentMethodDetail updatePaymentMethod(PaymentMethodDetail accessRoleDetail) throws CommonException {
        return generalService.merge(accessRoleDetail);
    }

    /**********************************************************************************************
     ***********************  PERMISSION SECTION. *************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminPermissionsCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Permission.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Permission.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<PermissionDetail> getAdminPermissions(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Permission.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), permissionConverter);
    }

    @Override
    public PermissionDetail updatePermission(PermissionDetail supplierDetail) throws CommonException {
        return generalService.merge(supplierDetail);
    }

    /**********************************************************************************************
     ***********************  PREFERENCE SECTION. *************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminPreferencesCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Preference.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Preference.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<PreferenceDetail> getAdminPreferences(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Preference.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), preferenceConverter);
    }

    @Override
    public PreferenceDetail updatePreference(PreferenceDetail supplierDetail) throws CommonException {
        return generalService.merge(supplierDetail);
    }

    /**********************************************************************************************
     ***********************  PROBLEM SECTION. ************************************************
     **********************************************************************************************/
    @Override
    public Long getAdminProblemsCount(SearchModuleDataHolder searchDataHolder) throws CommonException {
        Search search = null;
        if (searchDataHolder == null) {
            search = new Search(Problem.class);
        } else {
            search = this.setFilters(searchDataHolder, new Search(Problem.class));
        }
        return (long) generalService.count(search);
    }

    @Override
    public List<ProblemDetail> getAdminProblems(int start, int count,
            SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns) throws CommonException {
        Search search = new Search(Problem.class);
        if (searchDataHolder != null) {
            search = this.setFilters(searchDataHolder, search);
        }
        search = this.setSortSearch(orderColumns, search);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createDetailList(generalService.search(search), problemConverter);
    }

    @Override
    public ProblemDetail updateProblem(ProblemDetail accessRoleDetail) throws CommonException {
        return generalService.merge(accessRoleDetail);
    }

    /**********************************************************************************************
     ***********************  COMMON METHODS. *************************************************
     **********************************************************************************************/
    private <Domain, Detail> List<Detail> createDetailList(Collection<Domain> domainObjects,
            Converter<Domain, Detail> convertCallback) {
        final List<Detail> detailObjects = new ArrayList<Detail>();
        for (Domain domainObject : domainObjects) {
            detailObjects.add(convertCallback.convert(domainObject));
        }
        return detailObjects;
    }

    private Search setSortSearch(Map<String, OrderType> orderColumns, Search search) {
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

    private Search filter(Search search, FilterItem item) {
        switch (item.getOperation()) {
            case FilterItem.OPERATION_EQUALS:
                search.addFilterEqual(item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_LIKE:
                search.addFilterLike(item.getItem(), "%" + item.getValue().toString() + "%");
                break;
            case FilterItem.OPERATION_IN:
                search.addFilterIn(item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_FROM:
                search.addFilterGreaterOrEqual(item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_TO:
                search.addFilterLessOrEqual(item.getItem(), item.getValue());
                break;
            default:
                break;
        }
        return search;
    }

    private Search setFilters(SearchModuleDataHolder searchDataHolder, Search search) {
        for (FilterItem item : searchDataHolder.getFilters()) {
            if (item.getItem().equals("type")) {
                search.addFilterEqual("type", demandService.getDemandType(item.getValue().toString()));
                //Da sa aj takto? AK ano, mozem odstanit demandService cim zmensim opat download fragment
//                search.addFilterEqual("type", generalService.find(DemandType.class, item.getValue().toString()));
            } else if (item.getItem().equals("companyName")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("personFirstName")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("personLastName")) {
                List<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("description")) {
                Collection<BusinessUserData> data = generalService.search(
                        this.filter(new Search(BusinessUserData.class), item));
                search.addFilterIn("businessUser.businessUserData", data);
            } else if (item.getItem().equals("state")) {
                //TODO skontrolovat, v message totiz list roli + ci je to rovnaky typ triedy
                search.addFilterIn("roles.MessageUserRole.MessageUserRoleType",
                        MessageType.valueOf(item.getValue().toString()));
            } else {
                this.filter(search, item);
            }
        }
        return search;
    }
}
