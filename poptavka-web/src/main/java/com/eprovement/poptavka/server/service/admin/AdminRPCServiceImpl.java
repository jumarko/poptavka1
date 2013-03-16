/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.admin;

import com.eprovement.poptavka.client.service.demand.AdminRPCService;
import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.invoice.Invoice;
import com.eprovement.poptavka.domain.invoice.OurPaymentDetails;
import com.eprovement.poptavka.domain.invoice.PaymentMethod;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.settings.Preference;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Problem;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
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
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Search;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

/*
 * TODO LATER Martin Vsetky count zrobit inak, ked sa bude riesit tento modul. Vsetky
 * updaty dorobit.
 */
/**
 * @author Martin Slavkovsky
 */
@Configurable
public class AdminRPCServiceImpl extends AutoinjectingRemoteService implements AdminRPCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRPCServiceImpl.class);
    private GeneralService generalService;
    private DemandService demandService;
    private MessageService messageService;
    private UserMessageService userMessageService;
    private PotentialDemandService potentialDemandService;
    private Converter<Demand, FullDemandDetail> fullDemandConverter;
    private Converter<Client, ClientDetail> clientConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Offer, OfferDetail> offerConverter;
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;
    private Converter<ActivationEmail, ActivationEmailDetail> activationEmailConverter;
    private Converter<Invoice, InvoiceDetail> invoiceConverter;
    private Converter<OurPaymentDetails, PaymentDetail> paymentConverter;
    private Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter;
    private Converter<Permission, PermissionDetail> permissionConverter;
    private Converter<Preference, PreferenceDetail> preferenceConverter;
    private Converter<Problem, ProblemDetail> problemConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<Locality, LocalityDetail> localityConverter;
    private Converter<Category, CategoryDetail> categoryConverter;
    private Converter<Search, SearchDefinition> searchConverter;
    private Converter<UserMessage, MessageDetail> userMessageConverter;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @Autowired
    public void setPotentialDemandService(PotentialDemandService potentialDemandService) {
        this.potentialDemandService = potentialDemandService;
    }

    @Autowired
    public void setFullDemandConverter(
            @Qualifier("fullDemandConverter") Converter<Demand, FullDemandDetail> fullDemandConverter) {
        this.fullDemandConverter = fullDemandConverter;
    }

    @Autowired
    public void setClientConverter(@Qualifier("clientConverter") Converter<Client, ClientDetail> clientConverter) {
        this.clientConverter = clientConverter;
    }

    @Autowired
    public void setSupplierConverter(
            @Qualifier("supplierConverter") Converter<Supplier, FullSupplierDetail> supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setOfferConverter(@Qualifier("offerConverter") Converter<Offer, OfferDetail> offerConverter) {
        this.offerConverter = offerConverter;
    }

    @Autowired
    public void setAccessRoleConverter(
            @Qualifier("accessRoleConverter") Converter<AccessRole, AccessRoleDetail> accessRoleConverter) {
        this.accessRoleConverter = accessRoleConverter;
    }

    @Autowired
    public void setActivationEmailConverter(
            @Qualifier("activationEmailConverter") Converter<
                    ActivationEmail, ActivationEmailDetail> activationEmailConverter) {
        this.activationEmailConverter = activationEmailConverter;
    }

    @Autowired
    public void setSearchConverter(
            @Qualifier("searchConverter") Converter<Search, SearchDefinition> searchConverter) {
        this.searchConverter = searchConverter;
    }

    @Autowired
    public void setInvoiceConverter(@Qualifier("invoiceConverter") Converter<Invoice, InvoiceDetail> invoiceConverter) {
        this.invoiceConverter = invoiceConverter;
    }

    @Autowired
    public void setPaymentConverter(
            @Qualifier("paymentConverter") Converter<OurPaymentDetails, PaymentDetail> paymentConverter) {
        this.paymentConverter = paymentConverter;
    }

    @Autowired
    public void setPaymentMethodConverter(
            @Qualifier("paymentMethodConverter") Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter) {
        this.paymentMethodConverter = paymentMethodConverter;
    }

    @Autowired
    public void setPermissionConverter(
            @Qualifier("permissionConverter") Converter<Permission, PermissionDetail> permissionConverter) {
        this.permissionConverter = permissionConverter;
    }

    @Autowired
    public void setPreferenceConverter(
            @Qualifier("preferenceConverter") Converter<Preference, PreferenceDetail> preferenceConverter) {
        this.preferenceConverter = preferenceConverter;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, LocalityDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setProblemConverter(@Qualifier("problemConverter") Converter<Problem, ProblemDetail> problemConverter) {
        this.problemConverter = problemConverter;
    }

    @Autowired
    public void setMessageConverter(@Qualifier("messageConverter") Converter<Message, MessageDetail> messageConverter) {
        this.messageConverter = messageConverter;
    }

    /**
     * ********************************************************************************************
     * ********************** DEMAND SECTION.
     * ************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminDemandsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        searchConverter.convertToSource(searchDefinition);
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Demand.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<FullDemandDetail> getAdminDemands(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Demand.class);
        return fullDemandConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Boolean updateDemands(HashMap<Long, ArrayList<ChangeDetail>> changes) throws
            RPCException, ApplicationSecurityException {
        Demand demand = null;
        for (Long demandId : changes.keySet()) {
            demand = demandService.getById(demandId);
            updateDemandFields(demand, changes.get(demandId));
//            generalService.merge(demand);
            demandService.update(demand);
        }

        return true;
    }

    private Demand updateDemandFields(Demand demand, ArrayList<ChangeDetail> changes) {
        for (ChangeDetail change : changes) {
            switch (DemandField.valueOf(change.getField())) {
                case TITLE:
                    demand.setTitle((String) change.getValue());
                    break;
                case DESCRIPTION:
                    demand.setDescription((String) change.getValue());
                    break;
                case PRICE:
                    demand.setPrice(BigDecimal.valueOf(Long.valueOf((String) change.getValue())));
                    break;
                case END_DATE:
                    demand.setEndDate((Date) change.getValue());
                    break;
                case VALID_TO_DATE:
                    demand.setValidTo((Date) change.getValue());
                    break;
                case MAX_OFFERS:
                    demand.setMaxSuppliers((Integer) change.getValue());
                    break;
                case MIN_RATING:
                    demand.setMinRating(Integer.parseInt((String) change.getValue()));
                    break;
                case DEMAND_STATUS:
                    demand.setStatus(DemandStatus.valueOf((String) change.getValue()));
                    break;
                case CREATED:
                    demand.setCreatedDate((Date) change.getValue());
                    break;
                case CATEGORIES:
                    demand.setCategories(categoryConverter.convertToSourceList(
                            (ArrayList<CategoryDetail>) change.getValue()));
                    break;
                case LOCALITIES:
                    demand.setLocalities(localityConverter.convertToSourceList(
                            (ArrayList<LocalityDetail>) change.getValue()));
                    break;
                case EXCLUDE_SUPPLIER:
                    demand.setExcludedSuppliers(supplierConverter.convertToSourceList(
                            (ArrayList<FullSupplierDetail>) change.getValue()));
                    break;
                default:
                    break;
            }
        }
        return demand;
    }

    /**
     * ********************************************************************************************
     * ********************** CLIENT SECTION.
     * ************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminClientsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Client.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ClientDetail> getAdminClients(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Client.class);
        return clientConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateClient(ClientDetail clientDetail) throws
            RPCException, ApplicationSecurityException {
        Client client = generalService.find(Client.class, clientDetail.getId());
        if (client.getOveralRating() != clientDetail.getOveralRating()) {
            client.setOveralRating(clientDetail.getOveralRating());
        }
        client.setVerification(Verification.valueOf(clientDetail.getVerification()));
        //TODO LATER Martin - how to update businessUserData, supplierBlackList, demandsIds???
        generalService.merge(client);
    }

    /**
     * ********************************************************************************************
     * ********************** SUPPLIER SECTION.
     * ************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminSuppliersCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Supplier.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<FullSupplierDetail> getAdminSuppliers(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Supplier.class);
        return supplierConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateSupplier(FullSupplierDetail supplierDetail) throws
            RPCException, ApplicationSecurityException {
        Supplier supplier = generalService.find(Supplier.class, supplierDetail.getSupplierId());
        generalService.merge(supplier);
    }

    /**
     * ********************************************************************************************
     * ********************** OFFER SECTION.*******************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminOffersCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Offer.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<OfferDetail> getAdminOffers(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Offer.class);
        return offerConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateOffer(OfferDetail offerDetail) throws RPCException, ApplicationSecurityException {
        Offer offer = generalService.find(Offer.class, offerDetail.getId());
        if (!offer.getPrice().equals(offerDetail.getPrice())) {
            offer.setPrice((BigDecimal) offerDetail.getPrice());
        }
        if (!offer.getCreated().equals(offerDetail.getCreatedDate())) {
            offer.setCreated(offerDetail.getCreatedDate());
        }
        if (!offer.getFinishDate().equals(offerDetail.getFinishDate())) {
            offer.setFinishDate(offerDetail.getFinishDate());
        }
        if (!offer.getSupplier().getBusinessUser().getBusinessUserData().getCompanyName().equals(
                offerDetail.getSupplierName())) {
            offer.getSupplier().getBusinessUser().getBusinessUserData().setCompanyName(offerDetail.getSupplierName());
        }
        //TODO Martin - how to update OfferState??
        if (!offer.getPrice().equals(offerDetail.getPrice())) {
            offer.setPrice((BigDecimal) offerDetail.getPrice());
        }
        generalService.merge(offer);
    }

    /**
     * ********************************************************************************************
     * ********************** ACCESS ROLE SECTION.
     * ************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminAccessRolesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(AccessRole.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<AccessRoleDetail> getAdminAccessRoles(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(AccessRole.class);
        return accessRoleConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateAccessRole(AccessRoleDetail accessRoleDetail) throws RPCException, ApplicationSecurityException {
        AccessRole accessRole = generalService.find(AccessRole.class, accessRoleDetail.getId());
        if (!accessRole.getName().equals(accessRoleDetail.getName())) {
            accessRole.setName(accessRoleDetail.getName());
        }
        if (!accessRole.getDescription().equals(accessRoleDetail.getDescription())) {
            accessRole.setDescription(accessRoleDetail.getDescription());
        }
        if (!accessRole.getCode().equalsIgnoreCase(accessRoleDetail.getCode())) {
            accessRole.setCode(accessRoleDetail.getCode());
        }
        //TODO Martin - update permissions
        generalService.merge(accessRole);
    }

    /**
     * ********************************************************************************************
     * ********************** EMAIL ACTIVATION SECTION.
     * *******************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminEmailsActivationCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(ActivationEmail.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ActivationEmailDetail> getAdminEmailsActivation(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(ActivationEmail.class);
        return activationEmailConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateEmailActivation(ActivationEmailDetail emailActivationDetail) throws
            RPCException, ApplicationSecurityException {
        ActivationEmail emailActivation = generalService.find(ActivationEmail.class, emailActivationDetail.getId());
        if (!emailActivation.getActivationCode().equals(emailActivationDetail.getActivationCode())) {
            emailActivation.setActivationCode(emailActivationDetail.getActivationCode());
        }
        if (!emailActivation.getValidTo().equals(emailActivationDetail.getTimeout())) {
            emailActivation.setValidTo(emailActivationDetail.getTimeout());
        }
        generalService.merge(emailActivation);
    }

    /**
     * ********************************************************************************************
     * ********************** INVOICE SECTION.
     * ****************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminInvoicesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Invoice.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<InvoiceDetail> getAdminInvoices(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Invoice.class);
        return invoiceConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateInvoice(InvoiceDetail invoiceDetail) throws RPCException, ApplicationSecurityException {
        Invoice invoice = generalService.find(Invoice.class, invoiceDetail.getId());
        if (!invoice.getInvoiceNumber().equals(invoiceDetail.getInvoiceNumber())) {
            invoice.setInvoiceNumber(invoiceDetail.getInvoiceNumber());
        }
        //------------------------------ Dates ---------------------------------
        if (!invoice.getIssueDate().equals(invoiceDetail.getIssueDate())) {
            invoice.setIssueDate(invoiceDetail.getIssueDate());
        }
        if (!invoice.getShipmentDate().equals(invoiceDetail.getShipmentDate())) {
            invoice.setShipmentDate(invoiceDetail.getShipmentDate());
        }
        if (!invoice.getDueDate().equals(invoiceDetail.getDueDate())) {
            invoice.setDueDate(invoiceDetail.getDueDate());
        }
        //------------------------ Bank information ----------------------------
        if (!invoice.getBankAccountNumber().equals(invoiceDetail.getBankAccountNumber())) {
            invoice.setBankAccountNumber(invoiceDetail.getBankAccountNumber());
        }
        if (!invoice.getBankCode().equals(invoiceDetail.getBankCode())) {
            invoice.setBankCode(invoiceDetail.getBankCode());
        }
        if (!invoice.getVariableSymbol().equals(invoiceDetail.getVariableSymbol())) {
            invoice.setVariableSymbol(invoiceDetail.getVariableSymbol());
        }
        if (!invoice.getConstSymbol().equals(invoiceDetail.getConstSymbol())) {
            invoice.setConstSymbol(invoiceDetail.getConstSymbol());
        }
        //------------------------------ Price ---------------------------------
        if (!invoice.getTaxBasis().equals(invoiceDetail.getTaxBasis())) {
            invoice.setTaxBasis(invoiceDetail.getTaxBasis());
        }
        if (invoice.getVatRate() != invoiceDetail.getVatRate()) {
            invoice.setVatRate(invoiceDetail.getVatRate());
        }
        if (!invoice.getVat().equals(invoiceDetail.getVat())) {
            invoice.setVat(invoiceDetail.getVat());
        }
        if (!invoice.getTotalPrice().equals(invoiceDetail.getTotalPrice())) {
            invoice.setTotalPrice(invoiceDetail.getTotalPrice());
        }
        if (!invoice.getConstSymbol().equals(invoiceDetail.getConstSymbol())) {
            invoice.setConstSymbol(invoiceDetail.getConstSymbol());
        }
        //TODO Martin - how to update userServices, paymentMethods
        generalService.merge(invoice);
    }

    /**
     * ********************************************************************************************
     * ********************** MESSAGE SECTION.
     * ****************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminMessagesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Message.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<MessageDetail> getAdminMessages(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Message.class);
        return messageConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateMessage(MessageDetail messageDetail) throws RPCException, ApplicationSecurityException {
        Message message = generalService.find(Message.class, messageDetail.getMessageId());
        //TODO LATER Martin - how to update missing ones
        if (!message.getSubject().equals(messageDetail.getSubject())) {
            message.setSubject(messageDetail.getSubject());
        }
        if (!message.getBody().equals(messageDetail.getBody())) {
            message.setBody(messageDetail.getBody());
        }
        if (!message.getCreated().equals(messageDetail.getCreated())) {
            message.setCreated(messageDetail.getCreated());
        }
        if (!message.getSent().equals(messageDetail.getSent())) {
            message.setSent(messageDetail.getSent());
        }
        if (!message.getMessageState().equals(MessageState.valueOf(messageDetail.getMessageState()))) {
            message.setMessageState(MessageState.valueOf(messageDetail.getMessageState()));
        }

        generalService.merge(message);
    }

    /**************************************************************************/
    /*  NEW DEMANDS SECTION.                                                  */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminNewDemandsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.addFilterEqual("status", DemandStatus.NEW);
        search.setSearchClass(Demand.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<FullDemandDetail> getAdminNewDemands(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.addFilterEqual("status", DemandStatus.NEW);
        search.setSearchClass(Demand.class);
        return fullDemandConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void approveDemands(Set<FullDemandDetail> demandsToApprove) throws
            RPCException, ApplicationSecurityException {
        LOGGER.info("action=approve_demands status=start");
        for (FullDemandDetail demandDetail : demandsToApprove) {
            try {
                final Demand demand = demandService.getById(demandDetail.getDemandId());
                demandService.activateDemand(demand);
                potentialDemandService.sendDemandToPotentialSuppliers(demand);
            } catch (Exception e) {
                LOGGER.warn("action=approve_demands status=error demand_id={}", demandDetail.getDemandId(), e);
            }

        }
        LOGGER.info("action=approve_demands status=start");
    }

    /**
     * ********************************************************************************************
     * ********************** OUR PAYMENT DETAIL SECTION.
     * *****************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminOurPaymentDetailsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(OurPaymentDetails.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentDetail> getAdminOurPaymentDetails(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(OurPaymentDetails.class);
        return paymentConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateOurPaymentDetail(PaymentDetail paymentDetail) throws RPCException, ApplicationSecurityException {
        OurPaymentDetails ourPaymentDetails = generalService.find(OurPaymentDetails.class, paymentDetail.getId());
        if (!ourPaymentDetails.getBankAccount().equals(paymentDetail.getBankAccount())) {
            ourPaymentDetails.setBankAccount(paymentDetail.getBankAccount());
        }
        if (!ourPaymentDetails.getBankCode().equals(paymentDetail.getBankCode())) {
            ourPaymentDetails.setBankCode(paymentDetail.getBankCode());
        }
        if (!ourPaymentDetails.getCity().equals(paymentDetail.getCity())) {
            ourPaymentDetails.setCity(paymentDetail.getCity());
        }
        if (!ourPaymentDetails.getCountryVat().equals(paymentDetail.getCountryVat())) {
            ourPaymentDetails.setCountryVat(paymentDetail.getCountryVat());
        }
        if (!ourPaymentDetails.getEmail().equals(paymentDetail.getEmail())) {
            ourPaymentDetails.setEmail(paymentDetail.getEmail());
        }
        if (!ourPaymentDetails.getIban().equals(paymentDetail.getIban())) {
            ourPaymentDetails.setIban(paymentDetail.getIban());
        }
        if (!ourPaymentDetails.getIdentificationNumber().equals(paymentDetail.getIdentificationNumber())) {
            ourPaymentDetails.setIdentificationNumber(paymentDetail.getIdentificationNumber());
        }
        if (!ourPaymentDetails.getPhone().equals(paymentDetail.getPhone())) {
            ourPaymentDetails.setPhone(paymentDetail.getPhone());
        }
        if (!ourPaymentDetails.getStreet().equals(paymentDetail.getStreet())) {
            ourPaymentDetails.setStreet(paymentDetail.getStreet());
        }
        if (!ourPaymentDetails.getSwiftCode().equals(paymentDetail.getSwiftCode())) {
            ourPaymentDetails.setSwiftCode(paymentDetail.getSwiftCode());
        }
        if (!ourPaymentDetails.getTaxId().equals(paymentDetail.getTaxId())) {
            ourPaymentDetails.setTaxId(paymentDetail.getTaxId());
        }
        if (!ourPaymentDetails.getTitle().equals(paymentDetail.getTitle())) {
            ourPaymentDetails.setTitle(paymentDetail.getTitle());
        }
        if (!ourPaymentDetails.getZipCode().equals(paymentDetail.getZipCode())) {
            ourPaymentDetails.setZipCode(paymentDetail.getZipCode());
        }
        generalService.merge(ourPaymentDetails);
    }

    /**
     * ********************************************************************************************
     * ********************** PAYMENT METHOD SECTION.
     * ************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPaymentMethodsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(PaymentMethod.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentMethodDetail> getAdminPaymentMethods(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(PaymentMethod.class);
        return paymentMethodConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentMethodDetail> getAdminPaymentMethods() throws
            RPCException, ApplicationSecurityException {
        final Search search = new Search(PaymentMethod.class);
        search.addSort("id", false);
        return paymentMethodConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePaymentMethod(PaymentMethodDetail paymentMethodDetail) throws
            RPCException, ApplicationSecurityException {
        PaymentMethod paymentMethod = generalService.find(PaymentMethod.class, paymentMethodDetail.getId());
        if (!paymentMethod.getName().equals(paymentMethodDetail.getName())) {
            paymentMethod.setName(paymentMethodDetail.getName());
        }
        if (!paymentMethod.getDescription().equals(paymentMethodDetail.getDescription())) {
            paymentMethod.setDescription(paymentMethodDetail.getDescription());
        }
        generalService.merge(paymentMethod);
    }

    /**
     * ********************************************************************************************
     * ********************** PERMISSION SECTION.
     * *************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPermissionsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Permission.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PermissionDetail> getAdminPermissions(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Permission.class);
        return permissionConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePermission(PermissionDetail permissionDetail) throws RPCException, ApplicationSecurityException {
        Permission permission = generalService.find(Permission.class, permissionDetail.getId());
        if (!permission.getName().equals(permissionDetail.getName())) {
            permission.setName(permissionDetail.getName());
        }
        if (!permission.getDescription().equals(permissionDetail.getDescription())) {
            permission.setDescription(permissionDetail.getDescription());
        }
        if (!permission.getCode().equals(permissionDetail.getCode())) {
            permission.setCode(permissionDetail.getCode());
        }
        generalService.merge(permission);
    }

    /**
     * ********************************************************************************************
     * ********************** PREFERENCE SECTION.
     * *************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPreferencesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Preference.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PreferenceDetail> getAdminPreferences(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Preference.class);
        return preferenceConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePreference(PreferenceDetail preferenceDetail) throws RPCException, ApplicationSecurityException {
        Preference preference = generalService.find(Preference.class, preferenceDetail.getId());
        if (!preference.getKey().equals(preferenceDetail.getKey())) {
            preference.setKey(preferenceDetail.getKey());
        }
        if (!preference.getValue().equals(preferenceDetail.getValue())) {
            preference.setValue(preferenceDetail.getValue());
        }
        if (!preference.getDescription().equals(preferenceDetail.getDescription())) {
            preference.setDescription(preferenceDetail.getDescription());
        }
        generalService.merge(preference);
    }

    /**
     * ********************************************************************************************
     * ********************** PROBLEM SECTION.
     * ************************************************
     * ********************************************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminProblemsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Problem.class);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ProblemDetail> getAdminProblems(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(searchDefinition);
        search.setSearchClass(Problem.class);
        return problemConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateProblem(ProblemDetail problemDetail) throws RPCException, ApplicationSecurityException {
        Problem problem = generalService.find(Problem.class, problemDetail.getId());
        if (!problem.getText().equals(problemDetail.getText())) {
            problem.setText(problemDetail.getText());
        }
        generalService.merge(problem);
    }

    /**
     * This method will update number of unread messages of logged user.
     * Since this RPC class requires access of authenticated user (see security-web.xml) this method will be called
     * only when PoptavkaUserAuthentication object exist in SecurityContextHolder and we can retrieve userId.
     *
     * TODO Vojto - call DB servise to retrieve the number of unread messages for given userId
     *
     * @return UnreadMessagesDetail with number of unread messages and other info to be displayed after users logs in
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException {
        Long userId = ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        // TODO Vojto - get number of unread messages. UserId is provided from Authentication obejct see above
        UnreadMessagesDetail unreadMessagesDetail = new UnreadMessagesDetail();
        unreadMessagesDetail.setUnreadMessagesCount(99);
        return unreadMessagesDetail;
    }

    /**
     * Retrieve conversation between Operator and <code>Client</code> regarding particular demand. If there are no
     * <oode>UserMessage</code>-s for Operator/Admin empty conversation i.e. empty list is returned. In this case
     * the Operator/Admin must invoke <code>createConversation</code> that will create a new <code>UserMessage</code>.
     *
     * @param demandId which conversation should be loaded
     * @param userAdminId id of Admin or Operator user
     * @return List of <code>MessageDetail</code> objects
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<MessageDetail> getConversation(long demandId, long userAdminId) throws RPCException,
        ApplicationSecurityException {
        Demand demand = demandService.getById(demandId);
        Message threadRootMessage = messageService.getThreadRootMessage(demand);
        final List<UserMessage> userMessages = getConversationUserMessages(threadRootMessage.getId(), userAdminId);
        // set all user messages as read
        for (UserMessage userMessage : userMessages) {
            userMessage.setRead(true);
            generalService.save(userMessage);
        }
        return userMessageConverter.convertToTargetList(userMessages);
    }

    /**
     * Loads all UserMessages for conversation panel and sets them as read.
     *
     * @param threadId if of thread root message for whole conversation
     * @param userId user which user messages will be retrieved
     * @return list of <code>UserMessage</code>-s
     */
    private List<UserMessage> getConversationUserMessages(long threadId, long userId) {
        Message threadRoot = messageService.getById(threadId);
        User user = this.generalService.find(User.class, userId);
        final Search searchDefinition = new Search(UserMessage.class);
        searchDefinition.addSort("message.created", true);
        return this.messageService
                .getConversationUserMessages(threadRoot, user, searchDefinition);
    }

    /**
     * Creates conversation between <code>Client</code> and Admin/Operator user. Conversation is created in such a
     * way that new <code>UserMessage</code> is created for every <code>User</code> who invokes this method. Thus
     * enabling the user to write a reply message to <code>Client</code> in order to update <code>Demand</code>
     * description or title before this demand is approved.
     *
     * @param demandId for which the conversation is created
     * @param userAdminId id of operator or admin user
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void createConversation(long demandId, long userAdminId) throws RPCException,
        ApplicationSecurityException {
        Demand demand = demandService.getById(demandId);
        Message threadRootMessage = messageService.getThreadRootMessage(demand);
        userMessageService.getAdminUserMessage(threadRootMessage, generalService.find(User.class, userAdminId));
    }
}
